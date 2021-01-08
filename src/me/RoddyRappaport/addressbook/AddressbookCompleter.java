package me.RoddyRappaport.addressbook;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class AddressbookCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        UUID addressesID = new UUID(0, 0);
        if (command.getName().equals("address"))
        {
            addressesID = ((Player) sender).getUniqueId();
        }
        List<String> options = new ArrayList<String>();
        int argsLength = args.length;
        // Autocompleting!
        if (argsLength == 1) {
            options.add("show");
            options.add("add");
            options.add("remove");
            options.add("tp");
            return options;
        }
        // Getting looked at block if player
        String[] blockCoords = new String[]{"~", "~~", "~~~"};
        if (sender instanceof Player){
            Block lookedAtBlock = ((Player) sender).getTargetBlock((Set<Material>) null, 5);
            blockCoords[0] = String.valueOf(lookedAtBlock.getX());
            blockCoords[1] = String.valueOf(lookedAtBlock.getY());
            blockCoords[2] = String.valueOf(lookedAtBlock.getZ());
        }
        switch (args[0].toLowerCase()) {
            case "show":
                if (argsLength == 2) {
                    options.addAll(AddressBook.getAddressesNames(addressesID));
                    options.add("*");
                }
                return options;
            case "add":
                if (argsLength == 2) {
                    options.add("<Name>");
                }
                else
                {
                    switch (argsLength)
                    {
                        case 3:
                            options.add(blockCoords[0]);
                            options.add(blockCoords[0] + " " + blockCoords[1]);
                            options.add(blockCoords[0] + " " + blockCoords[1] + " " + blockCoords[2]);
                            break;
                        case 4:
                            options.add(blockCoords[1]);
                            options.add(blockCoords[1] + " " + blockCoords[2]);
                            break;
                        case 5:
                            options.add(blockCoords[2]);
                            break;
                    }
                    return options;
                }
                return options;
            case "remove":
                if (argsLength < 3)
                {
                    options.addAll(AddressBook.getAddressesNames(addressesID));
                }
                return options;
            case "tp":
                if (argsLength == 2) {
                    options.addAll(AddressBook.getAddressesNames(addressesID));
                }
                return options;
        }
        return options;
    }
}
