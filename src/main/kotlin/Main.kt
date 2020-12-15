package hazae41.emeralds

import hazae41.chestui.*
import net.milkbowl.vault.economy.Economy
import org.bukkit.Material.*
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.ServicePriority.Normal
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import java.io.File

lateinit var plugin: Main

class Main : JavaPlugin() {
  val dbfile = File(dataFolder, "data.db")

  val db = Database.connect(
    "jdbc:sqlite:${dbfile.path}",
    "org.sqlite.JDBC"
  )

  lateinit var token: String
  lateinit var currency: Currency

  override fun onEnable() {
    super.onEnable()
    plugin = this

    saveDefaultConfig()

    val ecoClz = Economy::class.java

    token = config.getString("name")!!
    currency = Currency(this, token)

    server.servicesManager
      .register(ecoClz, currency, this, Normal)

    getCommand("$")!!.apply {
      setExecutor { sender, _, _, args ->
        if (sender is Player)
          oncommand(sender, args)
        else true
      }
    }
  }

  fun oncommand(player: Player, args: Array<String>): Boolean {
    player.open(player.Banks())
    return true
  }

  fun Player.emeralds() = currency.getBalance(this).toInt()

  fun createBanks(balance: Int): List<Inventory> {
    val quotient = if (balance > 0) balance / 9 else 0
    val remainder = if (balance > 0) balance % 9 else 0

    var blocks: ItemStack? =
      ItemStack(EMERALD_BLOCK, quotient)
    var items: ItemStack? =
      ItemStack(EMERALD, remainder)

    val inventories = mutableListOf<Inventory>()

    while (blocks != null || items != null) {
      val title = "#${inventories.size + 1}"

      val inventory = server
        .createInventory(null, 6 * 9, title)

      if (blocks != null)
        blocks = inventory.addItem(blocks)[0]
      if (items != null)
        items = inventory.addItem(items)[0]

      inventories.add(inventory)
    }

    return inventories
  }

  fun Player.Banks(): GUI {
    val balance = emeralds()
    val banks = createBanks(balance)

    return gui("$balance $token", 6) {
      for (i in 0 until 6 * 9) {
        val inventory = banks
          .getOrNull(i) ?: break

        slot(i) {
          item = item(CHEST) {
            name = "#${i + 1}"
            val emeralds = inventory.emeralds()
            lore = wrap("§a$emeralds $token")
          }

          onclick = { p ->
            Bank(this@Banks, inventory)
            p.openInventory(inventory)
          }
        }
      }
    }
  }
}