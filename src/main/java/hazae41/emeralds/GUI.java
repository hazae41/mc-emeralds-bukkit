package hazae41.emeralds;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI implements Listener {
  public final Player player;
  public final Inventory inventory;
  private final Plugin plugin;

  int balance;

  public GUI(Plugin plugin, Player player) {
    this.player = player;
    this.plugin = plugin;

    int realBalance = plugin.emeralds.getOrZero(player);

    String title = plugin.emeralds.format(realBalance);
    this.inventory = plugin.getServer().createInventory(null, 54, title);

    for (int i = 0; i < realBalance / 9; i++)
      inventory.addItem(new ItemStack(Material.EMERALD_BLOCK));
    for (int i = 0; i < realBalance % 9; i++)
      inventory.addItem(new ItemStack(Material.EMERALD));

    recalculate();

    EconomyResponse tx = plugin.emeralds.withdrawPlayer(player, this.balance);
    if (!tx.transactionSuccess()) throw new RuntimeException(tx.errorMessage);

    plugin.tempConfig.config.set(player.getUniqueId().toString(), balance);
    plugin.tempConfig.save();

    player.openInventory(inventory);

    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  public void recalculate() {
    int balance = 0;

    for (ItemStack item : inventory) {
      if (item == null) continue;
      if (item.getType() == Material.EMERALD)
        balance += item.getAmount();
      if (item.getType() == Material.EMERALD_BLOCK)
        balance += item.getAmount() * 9;
    }

    this.balance = balance;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onClick(InventoryClickEvent e) {
    if (e.getInventory() != this.inventory) return;
    if (e.getResult() == Event.Result.DENY) return;

    plugin.getServer().getScheduler().runTask(plugin, task -> {
      recalculate();

      plugin.tempConfig.config.set(player.getUniqueId().toString(), balance);
      plugin.tempConfig.save();
    });
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onClose(InventoryCloseEvent e) {
    if (e.getInventory() != this.inventory) return;
    HandlerList.unregisterAll(this);

    recalculate();

    EconomyResponse tx = plugin.emeralds.depositPlayer(player, balance);
    if (!tx.transactionSuccess()) throw new RuntimeException(tx.errorMessage);

    plugin.tempConfig.config.set(player.getUniqueId().toString(), null);
    plugin.tempConfig.save();

    inventory.clear();
  }
}