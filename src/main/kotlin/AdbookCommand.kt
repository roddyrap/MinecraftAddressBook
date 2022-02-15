import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AdbookCommand : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, args: Array<String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage(ChatColor.RED.toString() + "You must be a player!")
            return false
        }
        return AddressBook.useAddressBook(commandSender, commandSender.uniqueId, args)
    }
}