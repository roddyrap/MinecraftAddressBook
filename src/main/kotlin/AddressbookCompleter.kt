import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class AddressbookCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        var addressesID = UUID(0, 0)
        if (command.name == "address") {
            addressesID = (sender as Player).uniqueId
        }
        val options: MutableList<String> = mutableListOf()
        val argsLength = args.size
        // Autocompleting!
        if (argsLength == 1) {
            options.add("show")
            options.add("add")
            options.add("remove")
            options.add("tp")
            return options
        }
        // Getting looked at block if player
        val blockCoords = arrayOf("~", "~~", "~~~")
        if (sender is Player) {
            // Arbitrary max distance of 5 blocks
            val lookedAtBlock = sender.getTargetBlock(null, 5)
            blockCoords[0] = lookedAtBlock.x.toString()
            blockCoords[1] = lookedAtBlock.y.toString()
            blockCoords[2] = lookedAtBlock.z.toString()
        }
        when (args[0].lowercase(Locale.getDefault())) {
            "show" -> {
                if (argsLength == 2) {
                    options.addAll(AddressBook.getAddressesNames(addressesID))
                    options.add("*")
                }
                return options
            }
            "add" -> {
                if (argsLength == 2) {
                    options.add("<Name>")
                } else {
                    when (argsLength) {
                        3 -> {
                            options.add(blockCoords[0])
                            options.add(blockCoords[0] + " " + blockCoords[1])
                            options.add(blockCoords[0] + " " + blockCoords[1] + " " + blockCoords[2])
                        }
                        4 -> {
                            options.add(blockCoords[1])
                            options.add(blockCoords[1] + " " + blockCoords[2])
                        }
                        5 -> options.add(blockCoords[2])
                    }
                    return options
                }
                return options
            }
            "remove" -> {
                if (argsLength < 3) {
                    options.addAll(AddressBook.getAddressesNames(addressesID))
                }
                return options
            }
            "tp" -> {
                if (argsLength == 2) {
                    options.addAll(AddressBook.getAddressesNames(addressesID))
                }
                return options
            }
        }
        return options
    }
}