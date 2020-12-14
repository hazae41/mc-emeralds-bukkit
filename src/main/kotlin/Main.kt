package hazae41.currencies

import net.milkbowl.vault.economy.Economy
import org.bukkit.Material.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.ServicePriority.Normal
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import java.io.File

class Main : JavaPlugin() {
  val dbfile = File(dataFolder, "data.db")

  val db = Database.connect(
    "jdbc:sqlite:${dbfile.path}",
    "org.sqlite.JDBC"
  )

  lateinit var currency: Currency

  override fun onEnable() {
    super.onEnable()

    saveDefaultConfig()

    val ecoClz = Economy::class.java
    val token = config.getString("name")!!
    currency = Currency(this, token)

    server.servicesManager
      .register(ecoClz, currency, this, Normal)

    getCommand("$")!!.apply {
      setTabCompleter { sender, _, _, args ->
        if (sender is Player)
          sender.ontabcomplete(args)
        else emptyList()
      }
      setExecutor { sender, _, _, args ->
        if (sender is Player)
          sender.oncommand(args)
        else true
      }
    }


  }

  
  fun Player.openBank() {
    server.createInventory(this, 6) {

    }
  }

  fun Player.ontabcomplete(args: Array<String>): List<String> {
    val operator = args.getOrNull(0)
      ?: return listOf("add", "get")

    if (operator == "add")
      return emptyList()

    if (operator == "get")
      return listOf("1", "10", "100", "1000", "10000")

    return emptyList()
  }

  fun Player.oncommand(args: Array<String>): Boolean {
    val operator = args.getOrNull(0)

    if (operator == null) {
      val balance = currency.getBalance(this)
      sendMessage("$balance ${currency.name}")
      return true
    }

    if (operator in listOf("+", "add")) {
      val item = inventory.itemInMainHand
      val damount = item.amount.toDouble()

      when (item.type) {
        EMERALD_BLOCK -> currency.depositPlayer(this, damount * 9)
        EMERALD -> currency.depositPlayer(this, damount)
        else -> {
          sendMessage("Invalid type")
          return true
        }
      }

      inventory.setItemInMainHand(ItemStack(AIR))

      return true
    }

    if (operator in listOf("-", "get")) {
      val balance = currency.getBalance(this).toInt()

      var amount = args.getOrNull(1)
        ?.toIntOrNull() ?: return false

      amount = amount.coerceAtMost(balance)

      val damount = amount.toDouble()

      val transaction = currency.withdrawPlayer(this, damount)
      if (!transaction.transactionSuccess()) return true

      val quotient = amount / 9
      val remainder = amount % 9

      if (quotient > 0) {
        val blocks = ItemStack(EMERALD_BLOCK, quotient)
        world.dropItem(location, blocks)
      }

      if (remainder > 0) {
        val items = ItemStack(EMERALD, remainder)
        world.dropItem(location, items)
      }

      return true
    }

    return false
  }
}