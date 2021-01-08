package me.RoddyRappaport.addressbook;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class adbookAllCommand  implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!args[0].equalsIgnoreCase("show"))
        {
            if (!commandSender.isOp())
            {
                commandSender.sendMessage(ChatColor.RED + "ERROR: You don't have sufficient privilege to execute command");
                return false;
            }
        }
        return AddressBook.useAddressBook(commandSender, new UUID(0, 0), command, s, args);
    }
}
