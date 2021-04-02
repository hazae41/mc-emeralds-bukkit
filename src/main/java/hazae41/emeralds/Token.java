package hazae41.emeralds;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static java.math.BigDecimal.ROUND_DOWN;
import static net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE;

@SuppressWarnings("deprecation")
public abstract class Token implements Economy {
  private final String name;
  private final int digits;

  public Token(String name, int digits) {
    this.name = name;
    this.digits = digits;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int fractionalDigits() {
    return this.digits;
  }

  @Override
  public String format(double amount) {
    String value = new BigDecimal(amount)
            .setScale(this.digits, ROUND_DOWN)
            .toString();
    if (amount >= 2) return value + " " + currencyNamePlural();
    else return value + " " + currencyNameSingular();
  }

  @Override
  public String currencyNamePlural() {
    return this.name + "s";
  }

  @Override
  public String currencyNameSingular() {
    return this.name;
  }

  @Override
  public boolean hasBankSupport() {
    return false;
  }

  @Override
  public List<String> getBanks() {
    return Collections.emptyList();
  }

  @Override
  public EconomyResponse deleteBank(String name) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse bankBalance(String name) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse bankHas(String name, double amount) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse bankWithdraw(String name, double amount) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse bankDeposit(String name, double amount) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse createBank(String name, OfflinePlayer player) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public EconomyResponse isBankMember(String name, OfflinePlayer player) {
    return new EconomyResponse(0, 0, FAILURE, "Not supported");
  }

  @Override
  public boolean has(OfflinePlayer player, double amount) {
    double balance = getBalance(player);
    return balance > amount;
  }

  @Override
  public boolean has(OfflinePlayer player, String worldName, double amount) {
    return has(player, amount);
  }

  @Override
  public boolean hasAccount(OfflinePlayer player) {
    return true;
  }

  @Override
  public boolean hasAccount(OfflinePlayer player, String worldName) {
    return hasAccount(player);
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer player) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
    return createPlayerAccount(player);
  }

  @Override
  public double getBalance(OfflinePlayer player, String world) {
    return getBalance(player);
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
    return withdrawPlayer(player, amount);
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
    return depositPlayer(player, amount);
  }

  @Override
  public boolean hasAccount(String playerName) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public boolean hasAccount(String playerName, String worldName) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public double getBalance(String playerName) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public double getBalance(String playerName, String world) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public boolean has(String playerName, double amount) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public boolean has(String playerName, String worldName, double amount) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse withdrawPlayer(String playerName, double amount) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse depositPlayer(String playerName, double amount) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse createBank(String name, String player) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse isBankOwner(String name, String playerName) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public EconomyResponse isBankMember(String name, String playerName) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public boolean createPlayerAccount(String playerName) {
    throw new RuntimeException("Player names are not supported");
  }

  @Override
  public boolean createPlayerAccount(String playerName, String worldName) {
    throw new RuntimeException("Player names are not supported");
  }
}
