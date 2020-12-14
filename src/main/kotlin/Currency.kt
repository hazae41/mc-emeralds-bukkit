package hazae41.currencies

import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE
import net.milkbowl.vault.economy.EconomyResponse.ResponseType.SUCCESS
import org.bukkit.OfflinePlayer
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object PlayerTable : UUIDTable() {
  val amount = double("amount").default(0.0)
}

class PlayerRow(id: EntityID<UUID>) : UUIDEntity(id) {
  companion object : UUIDEntityClass<PlayerRow>(PlayerTable) {
    fun getOrCreate(id: UUID) =
      findById(id) ?: new(id) {}
  }

  var amount by PlayerTable.amount
}

class Currency(
  plugin: Main,
  currency: String
) : BasicEconomy(currency, 0) {
  val db = plugin.db

  init {
    transaction(db) { SchemaUtils.create(PlayerTable) }
  }

  override fun getBalance(player: OfflinePlayer?) =
    transaction(db) { PlayerRow.getOrCreate(player!!.uniqueId).amount }

  override fun getBalance(player: OfflinePlayer?, world: String?) =
    getBalance(player)

  override fun has(player: OfflinePlayer?, amount: Double) =
    getBalance(player) >= amount

  override fun has(player: OfflinePlayer, world: String?, amount: Double) =
    has(player, amount)

  override fun withdrawPlayer(player: OfflinePlayer?, amount: Double) = transaction(db) {
    val row = PlayerRow.getOrCreate(player!!.uniqueId)
    player!!.setStatistic(Statistic.)

    if (row.amount >= amount) {
      row.amount -= amount
      EconomyResponse(amount, row.amount, SUCCESS, null)
    } else EconomyResponse(amount, row.amount, FAILURE, null)
  }

  override fun withdrawPlayer(player: OfflinePlayer?, world: String?, amount: Double) =
    withdrawPlayer(player, amount)

  override fun depositPlayer(player: OfflinePlayer?, amount: Double) = transaction(db) {
    val row = PlayerRow.getOrCreate(player!!.uniqueId)
    row.amount += amount

    EconomyResponse(amount, row.amount, SUCCESS, null)
  }

  override fun depositPlayer(player: OfflinePlayer?, world: String?, amount: Double) =
    depositPlayer(player, amount)
}