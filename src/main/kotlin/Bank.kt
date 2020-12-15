package hazae41.emeralds

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

fun Inventory.emeralds(): Int {
  var count = 0

  for (item in contents) {
    if (item == null) continue
    if (item.type == Material.EMERALD_BLOCK)
      count += item.amount * 9
    if (item.type == Material.EMERALD)
      count += item.amount
  }

  return count
}

class Bank(
  val player: Player,
  val inventory: Inventory
) : Listener {
  init {
    plugin.server.pluginManager
      .registerEvents(this, plugin)
  }

  var balance = inventory.emeralds()

  val task = plugin.server.scheduler
    .runTaskTimer(plugin, { -> save() }, 20L, 20L)

  @EventHandler(priority = EventPriority.MONITOR)
  fun onclose(e: InventoryCloseEvent) {
    if (e.inventory != this.inventory) return
    HandlerList.unregisterAll(this)
    task.cancel()
    save()
  }

  fun save() {
    val count = inventory.emeralds()

    val deposit = (count - balance).toDouble()
    plugin.currency.depositPlayer(player, deposit)

    balance = count
  }
}