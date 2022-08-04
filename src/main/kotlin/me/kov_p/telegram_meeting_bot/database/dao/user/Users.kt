package me.kov_p.telegram_meeting_bot.database.dao.user

import org.jetbrains.exposed.sql.Table

internal object Users : Table("users") {
    private const val COLUMN_USERNAME = "user_name"
    private const val COLUMN_CHAT_ID = "chat_id"

    val userName = Users.varchar(COLUMN_USERNAME, 30)
    val userChatId = Users.long(COLUMN_CHAT_ID)
}

