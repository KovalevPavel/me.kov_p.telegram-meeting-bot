package me.kov_p.telegram_meeting_bot.database.dao.bot

import org.jetbrains.exposed.sql.Table

object BotData: Table("tg_bot") {
    private const val COLUMN_TOKEN = "token"
    private const val COLUMN_WEBHOOK_URL = "webhook_url"

    private const val MAX_TOKEN_LENGTH = 75
    private const val MAX_WEBHOOK_URL_LENGTH = 75

    val botToken = BotData.varchar(COLUMN_TOKEN, MAX_TOKEN_LENGTH)
    val botUpdateUrl = BotData.varchar(COLUMN_WEBHOOK_URL, MAX_WEBHOOK_URL_LENGTH)
}