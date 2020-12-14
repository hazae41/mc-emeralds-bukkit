package hazae41.currencies

import net.milkbowl.vault.economy.Economy
import org.bukkit.OfflinePlayer

abstract class BasicEconomy(
    val currency: String,
    val digits: Int = 0
) : Economy {
    override fun isEnabled() = true

    override fun hasBankSupport() = false

    override fun getName() = currency

    override fun currencyNameSingular() = currency

    override fun currencyNamePlural() = currency + "s"

    override fun fractionalDigits() = digits

    override fun format(amount: Double) = "$amount $currency"

    override fun createPlayerAccount(player: OfflinePlayer?) = false

    override fun createPlayerAccount(player: OfflinePlayer?, world: String?) = false

    override fun hasAccount(player: OfflinePlayer?) = true

    override fun hasAccount(player: OfflinePlayer?, world: String?) = true

    override fun getBanks() =
        throw UnsupportedOperationException("Banks are not supported")

    override fun bankBalance(name: String?) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun bankDeposit(name: String?, amount: Double) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun bankHas(name: String?, amount: Double) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun bankWithdraw(name: String?, amount: Double) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun createBank(name: String?, player: OfflinePlayer?) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun deleteBank(name: String?) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun isBankMember(name: String?, player: OfflinePlayer?) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun isBankOwner(name: String?, player: OfflinePlayer?) =
        throw UnsupportedOperationException("Banks are not supported")

    override fun hasAccount(p0: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun hasAccount(p0: String?, p1: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun getBalance(p0: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun getBalance(p0: String?, p1: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun has(p0: String?, p1: Double) =
        throw UnsupportedOperationException("Deprecated")

    override fun has(p0: String?, p1: String?, p2: Double) =
        throw UnsupportedOperationException("Deprecated")

    override fun withdrawPlayer(p0: String?, p1: Double) =
        throw UnsupportedOperationException("Deprecated")

    override fun withdrawPlayer(p0: String?, p1: String?, p2: Double) =
        throw UnsupportedOperationException("Deprecated")

    override fun depositPlayer(p0: String?, p1: Double) =
        throw UnsupportedOperationException("Deprecated")

    override fun depositPlayer(p0: String?, p1: String?, p2: Double) =
        throw UnsupportedOperationException("Deprecated")

    override fun createBank(p0: String?, p1: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun isBankOwner(p0: String?, p1: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun isBankMember(p0: String?, p1: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun createPlayerAccount(p0: String?) =
        throw UnsupportedOperationException("Deprecated")

    override fun createPlayerAccount(p0: String?, p1: String?) =
        throw UnsupportedOperationException("Deprecated")
}