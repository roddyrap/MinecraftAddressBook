Address Book
=============

| This is a Minecraft bukkit plugin that creates a server-specific address book. The address book lets players save addresses and see them later, as well as removing those addresses and teleporting to them.
| NOTE: this plugin works only for Java Edition servers.

Commands
--------

| address - let's each player access their own section of the address book
|
| gaddress - let's each player access the public section of the address book.
| only ops can add and remove addresses from the public section of the address book.

Syntax
------

| Both of those commands' syntax is the same:
| To add an address to the address book:
| ``/(g)address add <Name> x y z``

- \<Name> - The name of the address

| To remove an address from the address book:
| ``/(g)address remove <Name>``

- \<Name> - The name of the address
- (Note: the auto-completer will show all available options to remove)

| To see addresses from the address book:
| ``/(g)address show <Name>``

- \<Name> - an optional parameter of the address to be shown. If empty or "*", all addresses will be shown.

| To teleport to an address from the address book:
| ``/(g)address tp <Name>`` 

- \<Name> - The address to tp to.

The auto-completer will show all available addresses when needed.

Use
---

To use the plugin download the latest jar from `the releases view in Github <https://github.com/Nimi142/MinecraftAddressBook/releases>`_
Add it to the plugin folder in the server's folder, and reload it. Done!
