package me.RoddyRappaport.addressbook;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class adbookCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "You must be a player!");
            return false;
        }
        return AddressBook.useAddressBook(commandSender, ((Player) commandSender).getUniqueId(), command, s, args);
    }

}
