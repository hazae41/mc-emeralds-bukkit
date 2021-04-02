package hazae41.emeralds;


import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE;
import static net.milkbowl.vault.economy.EconomyResponse.ResponseType.SUCCESS;

public class Emeralds extends Token {
  public final Plugin plugin;
  public Connection conn = null;

  public Emeralds(Plugin plugin, String name) {
    super(name, 0);
    this.plugin = plugin;

    try {
      SQLiteConfig config = new SQLiteConfig();
      File file = new File(plugin.getDataFolder(), "players.db");
      String url = "jdbc:sqlite:" + file.getAbsolutePath();
      this.conn = config.createConnection(url);

      String query = "CREATE TABLE IF NOT EXISTS players (id UUID NOT NULL PRIMARY KEY, amount INT DEFAULT 0 NOT NULL);";
      this.conn.createStatement().executeUpdate(query);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public void close() {
    try {
      if (conn != null) conn.close();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public Integer get(OfflinePlayer player) {
    try {
      if (!plugin.isEnabled())
        throw new RuntimeException("Plugin disabled");
      if (conn == null)
        throw new RuntimeException("Not connected");

      String query = "SELECT amount FROM players WHERE id = ?;";
      PreparedStatement statement = conn.prepareStatement(query);
      statement.setString(1, player.getUniqueId().toString());
      ResultSet result = statement.executeQuery();

      boolean exists = result.next();
      if (exists) return result.getInt("amount");

      return null;
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public void set(OfflinePlayer player, int amount) {
    try {
      if (!plugin.isEnabled())
        throw new RuntimeException("Plugin disabled");
      if (conn == null)
        throw new RuntimeException("Not connected");

      String query = "REPLACE INTO players (id, amount) VALUES(?, ?);";
      PreparedStatement statement = conn.prepareStatement(query);
      statement.setString(1, player.getUniqueId().toString());
      statement.setInt(2, amount);
      statement.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public int getOrZero(OfflinePlayer player) {
    Integer balance = get(player);
    if (balance == null) return 0;
    return balance;
  }

  @Override
  public double getBalance(OfflinePlayer player) {
    return getOrZero(player);
  }

  @Override
  public boolean hasAccount(OfflinePlayer player) {
    return get(player) != null;
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, double _amount) {
    int amount = (int) _amount;
    int balance = getOrZero(player);

    if (amount > balance)
      return new EconomyResponse(0, 0, FAILURE, "Insufficient funds");

    set(player, balance - amount);

    return new EconomyResponse(amount, balance - amount, SUCCESS, null);
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, double _amount) {
    int amount = (int) _amount;
    int balance = getOrZero(player);
    set(player, amount + balance);

    return new EconomyResponse(amount, balance + amount, SUCCESS, null);
  }

  public EconomyResponse setPlayer(OfflinePlayer player, double _amount) {
    int amount = (int) _amount;
    set(player, amount);

    return new EconomyResponse(amount, amount, SUCCESS, null);
  }


}
