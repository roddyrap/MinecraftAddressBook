import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.*
import java.util.*

class AddressBook : JavaPlugin() {
    override fun onEnable() {
        val addressCompleter: TabCompleter = AddressbookCompleter()
        val addressbook = getCommand("address")
        if (addressbook != null) {
            addressbook.setExecutor(AdbookCommand())
            addressbook.tabCompleter = addressCompleter
        }
        val addressbookAll = getCommand("gaddress")
        if (addressbookAll != null) {
            addressbookAll.setExecutor(AdbookAllCommand())
            addressbookAll.tabCompleter = addressCompleter
        }
        val addressesFile = File(dataFolder, "addresses.ser")
        if (!addressesFile.exists()) {
            logger.info("No previous use of AddressBook found, hope you'll find it useful!")
            return
        }
        // deserializing book
        val fileIn: FileInputStream
        try {
            fileIn = FileInputStream(addressesFile)
            val objectIn = ObjectInputStream(fileIn)
            addressBook = objectIn.readObject() as HashMap<UUID, HashMap<String?, String?>>
            objectIn.close()
            fileIn.close()
            logger.info("Addresses read.")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onDisable() {

        // Serializing book
        val dataFolder = dataFolder
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                logger.severe("Couldn't create data folder, quitting without saving.")
                return
            }
        }
        val addressesFile = File(getDataFolder(), "addresses.ser")
        if (!addressesFile.exists()) {
            try {
                addressesFile.createNewFile()
            } catch (e: IOException) {
                logger.severe("Couldn't create data file, quitting without saving.")
            }
        }
        try {
            val fileOut = FileOutputStream(addressesFile)
            val out = ObjectOutputStream(fileOut)
            out.writeObject(addressBook)
            out.close()
            fileOut.close()
            logger.info("Addresses saved")
        } catch (e: IOException) {
            logger.severe("Couldn't update data file, quitting without saving.")
        }
    }

    companion object {
        var addressBook = HashMap<UUID, HashMap<String?, String?>>()
        fun useAddressBook(commandSender: CommandSender, addressesID: UUID, args: Array<String>): Boolean {
            val player = commandSender as Player
            val playerMap: HashMap<String?, String?>?
            if (args.isEmpty()) {
                commandSender.sendMessage(ChatColor.RED.toString() + "Error: command unclear")
                return true
            }
            when (args[0]) {
                "show" -> {
                    playerMap = addressBook[addressesID]
                    if (playerMap == null) {
                        commandSender.sendMessage(ChatColor.RED.toString() + "No Addresses to show!")
                        return true
                    }
                    if (args.size > 1 && playerMap[args[1]] != null && playerMap[args[1]] != "*") {
                        commandSender.sendMessage(args[1] + ": " + playerMap[args[1]])
                        return true
                    }
                    commandSender.sendMessage("Addresses:\n${playerAddressesShow(addressesID)}")
                }
                "add" -> {
                    if (args.size != 5) {
                        commandSender.sendMessage(ChatColor.RED.toString() + "Invalid amount of parameters")
                        return false
                    }
                    playerMap = addressBook.computeIfAbsent(
                        addressesID
                    ) { HashMap() }
                    playerMap[args[1]] = "${args[2]} ${args[3]} ${args[4]}"

                    commandSender.sendMessage("Added address ${args[1]} at location: ${args[2]} ${args[3]} ${args[4]}")
                }
                "remove" -> {
                    playerMap = addressBook[addressesID]
                    if (playerMap == null || playerMap.isEmpty()) {
                        commandSender.sendMessage(ChatColor.RED.toString() + "Couldn't remove address, does this address exist?")
                        return false
                    }
                    playerMap.remove(args[1])
                    commandSender.sendMessage("Removed key: " + args[1])
                }
                "tp" -> {
                    playerMap = addressBook[addressesID]
                    if (playerMap == null || playerMap[args[1]] == null) {
                        commandSender.sendMessage(ChatColor.RED.toString() + "Error: Can't find address")
                        return true
                    }
                    val addressString = playerMap[args[1]]
                    Bukkit.getServer().dispatchCommand(player, "tp $addressString")
                }
                else -> commandSender.sendMessage(ChatColor.RED.toString() + "Error: command unclear")
            }
            return true
        }

        fun playerAddressesShow(addressesID: UUID): String {
            val playerAddresses = addressBook[addressesID]
                ?: return "There are no addresses to be presented."
            val returnString = StringBuilder()
            for (key in playerAddresses.keys) {
                returnString.append("   ").append(key).append(": ").append(playerAddresses[key]).append("\n")
            }
            return returnString.toString()
        }

        fun getAddressesNames(addressesID: UUID): List<String> {
            return addressBook[addressesID]?.keys?.filterNotNull() ?: listOf()
        }
    }
}