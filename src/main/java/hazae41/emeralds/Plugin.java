package hazae41.emeralds;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class Plugin extends JavaPlugin {
  public Economy economy;
  public Config tempConfig;

  public Commands commands = new Commands(this);

  @Override
  public void onEnable() {
    saveDefaultConfig();

    this.economy = new Emeralds(this, getConfig().getString("name"));
    this.tempConfig = new Config(new File(getDataFolder(), "temp.yml"));

    getServer().getServicesManager().register(Economy.class, this.economy, this, ServicePriority.Highest);

    for (String key : tempConfig.config.getKeys(false)) {
      OfflinePlayer player = getServer().getOfflinePlayer(UUID.fromString(key));
      economy.depositPlayer(player, tempConfig.config.getInt(key));
      tempConfig.config.set(key, null);
    }

    tempConfig.save();
  }

  @Override
  public void onDisable() {
    Emeralds emeralds = (Emeralds) economy;

    getServer().getServicesManager().unregister(Economy.class, emeralds);

    emeralds.close();
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    try {
      if (command.getName().equals("money"))
        return commands.onMoney(sender, args);
      if (command.getName().equals("balance"))
        return commands.onBalance(sender, args);
      if (command.getName().equals("givemoney"))
        return commands.onGive(sender, args);
      if (command.getName().equals("takemoney"))
        return commands.onTake(sender, args);
      return false;
    } catch (Message msg) {
      sender.sendMessage(ChatColor.RED + msg.getMessage());
      return true;
    }
  }
}
