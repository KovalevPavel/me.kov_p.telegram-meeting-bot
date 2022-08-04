package me.kov_p.telegram_meeting_bot.database.dao.bot

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class BotDao {
    fun getTgBot(): TgBot {
        return transaction {
            BotData.select {
                BotData.botToken.isNotNull()
            }
                .single()
                .toTgBot()
        }
    }

    private fun ResultRow.toTgBot(): TgBot = TgBot(
        botToken = this[BotData.botToken],
        webhookUrl = this[BotData.botUpdateUrl]
    )
}
