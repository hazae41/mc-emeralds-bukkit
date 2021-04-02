package hazae41.emeralds;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class Plugin extends JavaPlugin {
  public Emeralds emeralds = null;
  public Config tempConfig = null;

  public final Commands commands = new Commands(this);

  @Override
  public void onEnable() {
    saveDefaultConfig();

    this.emeralds = new Emeralds(this, getConfig().getString("name"));
    this.tempConfig = new Config(new File(getDataFolder(), "temp.yml"));

    getServer().getServicesManager().register(Economy.class, this.emeralds, this, ServicePriority.Normal);

    PluginCommand money = Objects.requireNonNull(getCommand("money"));
    money.setExecutor((sender, command, label, args) -> commands.onMoney(sender, args));

    PluginCommand balance = Objects.requireNonNull(getCommand("balance"));
    balance.setExecutor((sender, command, label, args) -> commands.onBalance(sender, args));

    PluginCommand giveMoney = Objects.requireNonNull(getCommand("givemoney"));
    giveMoney.setExecutor((sender, command, label, args) -> commands.onGive(sender, args));

    PluginCommand takeMoney = Objects.requireNonNull(getCommand("takemoney"));
    takeMoney.setExecutor((sender, command, label, args) -> commands.onTake(sender, args));

    for (String key : tempConfig.config.getKeys(false)) {
      OfflinePlayer player = getServer().getOfflinePlayer(UUID.fromString(key));
      emeralds.depositPlayer(player, tempConfig.config.getInt(key));
      tempConfig.config.set(key, null);
    }

    tempConfig.save();
  }


  @Override
  public void onDisable() {
    emeralds.close();

    getServer().getServicesManager().unregister(Economy.class, this);
  }

}
