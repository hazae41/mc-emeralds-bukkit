package hazae41.emeralds;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

class Message extends Throwable {
  public Message(String message) {
    super(message);
  }
}

class NotPlayerMessage extends Message {
  public NotPlayerMessage() {
    super("You are not a player");
  }
}

public class Commands {
  private final Plugin plugin;

  public Commands(Plugin plugin) {
    this.plugin = plugin;
  }

  boolean onMoney(CommandSender sender, String[] args) throws Message {
    if (!(sender instanceof Player))
      throw new NotPlayerMessage();
    Player player = (Player) sender;
    new GUI(plugin, player);

    return true;
  }

  boolean onBalance(CommandSender sender, String[] args) throws Message {
    String name = Utils.get(args, 0).orElse(null);

    if (name == null) {
      if (!(sender instanceof Player))
        throw new NotPlayerMessage();
      Player player = (Player) sender;

      int balance = (int) plugin.economy.getBalance(player);
      String formatted = plugin.economy.format(balance);

      sender.sendMessage(ChatColor.GREEN + "You have " + formatted);

      return true;
    } else {
      List<Player> matches = plugin.getServer().matchPlayer(name);
      Player player = Utils.get(matches, 0).orElse(null);
      if (player == null) throw new Message("Could not find \"" + name + "\"");

      int balance = (int) plugin.economy.getBalance(player);
      String formatted = plugin.economy.format(balance);

      sender.sendMessage(player.getDisplayName()
              + ChatColor.GREEN + " has " + formatted);

      return true;
    }
  }

  boolean onGive(CommandSender sender, String[] args) throws Message {
    String name = Utils.get(args, 0).orElse(null);
    if (name == null) return false;

    List<Player> matches = plugin.getServer().matchPlayer(name);
    Player player = Utils.get(matches, 0).orElse(null);
    if (player == null) throw new Message("Could not find \"" + name + "\"");

    String _amount = Utils.get(args, 1).orElse(null);
    if (_amount == null) return false;

    Integer amount = Utils.parse(_amount).orElse(null);
    if (amount == null) return false;

    EconomyResponse tx = plugin.economy.depositPlayer(player, amount);
    if (!tx.transactionSuccess()) throw new Message(tx.errorMessage);

    String formatted = plugin.economy.format(amount);

    sender.sendMessage(ChatColor.GREEN
            + "Given " + formatted + " to "
            + ChatColor.RESET + player.getDisplayName());

    return true;

  }

  boolean onTake(CommandSender sender, String[] args) throws Message {
    String name = Utils.get(args, 0).orElse(null);
    if (name == null) return false;

    List<Player> matches = plugin.getServer().matchPlayer(name);
    Player player = Utils.get(matches, 0).orElse(null);
    if (player == null) throw new Message("Could not find \"" + name + "\"");

    String _amount = Utils.get(args, 1).orElse(null);
    if (_amount == null) return false;

    Integer amount = Utils.parse(_amount).orElse(null);
    if (amount == null) return false;

    EconomyResponse tx = plugin.economy.withdrawPlayer(player, amount);
    if (!tx.transactionSuccess()) throw new Message(tx.errorMessage);

    String formatted = plugin.economy.format(amount);

    sender.sendMessage(ChatColor.GREEN
            + "Took " + formatted + " from "
            + ChatColor.RESET + player.getDisplayName());

    return true;
  }
}
