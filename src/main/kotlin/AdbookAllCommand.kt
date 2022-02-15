import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

class AdbookAllCommand : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, args: Array<String>): Boolean {
        if (!args[0].equals("show", ignoreCase = true)) {
            if (!commandSender.isOp) {
                commandSender.sendMessage(ChatColor.RED.toString() + "ERROR: You don't have sufficient privilege to execute command")
                return false
            }
        }
        return AddressBook.useAddressBook(commandSender, UUID(0, 0), args)
    }
}