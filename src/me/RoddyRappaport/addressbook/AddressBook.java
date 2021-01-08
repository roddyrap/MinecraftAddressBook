package me.RoddyRappaport.addressbook;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

// TODO: give raspberry pi a static ip and port forward it.
// TODO: finish tab auto completer
public class AddressBook extends JavaPlugin {
    static HashMap<UUID, HashMap<String, String>> addressBook = new HashMap<UUID, HashMap<String, String>>();
    
    public static boolean useAddressBook(CommandSender commandSender, UUID addressesID, Command command, String s, String[] args)
    {
        Player player = (Player) commandSender;
        HashMap<String, String> playerMap;
        if (args.length == 0 || args[0] == null)
        {
            commandSender.sendMessage(ChatColor.RED + "Error: command unclear");
            return true;
        }
        switch (args[0])
        {
            case "show":
                playerMap = addressBook.get(addressesID);
                if(playerMap == null)
                {
                    commandSender.sendMessage(ChatColor.RED + "No Addresses to show!");
                    return true;
                }
                if (args.length > 1 &&  playerMap.get(args[1]) != null && !playerMap.get(args[1]).equals("*"))
                {
                    commandSender.sendMessage(args[1] + ": " + playerMap.get(args[1]));
                    return true;
                }
                commandSender.sendMessage("Addresses:\n" + playerAddressesShow(addressesID));
                break;
            case "add":
                if (args.length != 5)
                {
                    commandSender.sendMessage(ChatColor.RED + "Invalid amount of parameters");
                    return false;
                }
                playerMap = addressBook.computeIfAbsent(addressesID, k -> new HashMap<String, String>());
                playerMap.put(args[1], args[2] + " " + args[3] + " " + args[4]);
                commandSender.sendMessage("Added address " + args[1] + " at location: " + args[2] + " " + args[3] + " " + args[4]);
                break;
            case "remove":
                playerMap = addressBook.get(addressesID);
                if (playerMap == null || playerMap.keySet().toArray().length == 0)
                {
                    commandSender.sendMessage(ChatColor.RED + "Couldn't remove address, does this address exist?");
                    return false;
                }
                playerMap.remove(args[1]);
                commandSender.sendMessage("Removed key: " + args[1]);
                break;
            case "tp":
                playerMap = AddressBook.addressBook.get(addressesID);
                if (playerMap == null || playerMap.get(args[1]) == null)
                {
                    commandSender.sendMessage(ChatColor.RED + "ERROR: Can't find address");
                    return true;
                }
                String addressString = playerMap.get(args[1]);
                Bukkit.getServer().dispatchCommand(player,"tp " + addressString);
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Error: command unclear");
                break;
        }
        return true;
    }
    

    static public String playerAddressesShow(UUID addressesID)
    {
        HashMap<String, String> playerAddresses = addressBook.get(addressesID);
        if (playerAddresses == null)
        {
            return "There are no addresses to be presented.";
        }
        StringBuilder returnString = new StringBuilder();
        String[] keys = playerAddresses.keySet().toArray(new String[0]);
        for (String key : keys) {
            returnString.append("   ").append(key).append(": ").append(playerAddresses.get(key)).append("\n");
        }
        System.out.println(returnString.toString());
        return returnString.toString();
    }

    static public List<String> getAddressesNames(UUID addressesID)
    {
        HashMap<String, String> playerAddresses = addressBook.get(addressesID);
        if (playerAddresses == null)
        {
            return new ArrayList<String>();
        }
        return Arrays.asList(playerAddresses.keySet().toArray(new String[0]));
    }

    public void onEnable() {
        TabCompleter addressCompleter = new AddressbookCompleter();
        PluginCommand addressbook = this.getCommand("address");
        addressbook.setExecutor(new adbookCommand());
        addressbook.setTabCompleter(addressCompleter);
        PluginCommand addressbookAll = this.getCommand("gaddress");
        addressbookAll.setExecutor(new adbookAllCommand());
        addressbookAll.setTabCompleter(addressCompleter);
        File addressesFile = new File(getDataFolder(), "addresses.ser");
        if (!addressesFile.exists())
        {
            System.out.println("No previous use of AddressBook found, hope you'll find it useful!");
            return;
        };
        // deserializing book
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(addressesFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            addressBook = (HashMap<UUID, HashMap<String, String>>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Read addresses.");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

    public void onDisable() {

        // Serializing book
        File dataFolder = getDataFolder();
        if(!dataFolder.exists())
        {
            dataFolder.mkdir();
        }
        File addressesFile = new File(getDataFolder(), "addresses.ser");
        if (!addressesFile.exists()) {
            try {
                addressesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(addressesFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(addressBook);
            out.close();
            fileOut.close();
            System.out.println("Addresses saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
