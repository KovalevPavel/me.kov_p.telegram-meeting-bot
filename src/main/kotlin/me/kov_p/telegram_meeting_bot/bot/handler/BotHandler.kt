@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package me.kov_p.telegram_meeting_bot.bot.handler

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.types.TelegramBotResult
import com.github.kotlintelegrambot.webhook
import com.sun.jndi.toolkit.url.Uri
import me.kov_p.telegram_meeting_bot.database.dao.bot.BotDao

interface BotHandler {
    val botUpdatePath: String

    fun initBot()
    fun sendMessage(
        message: String,
        chatId: ChatId,
        replyToMessage: Long? = null
    ): TelegramBotResult<Message>
}

class BotHandlerImpl(
    botDao: BotDao,
) : BotHandler {
    override val botUpdatePath: String
        get() = Uri(tgBot.webhookUrl).path

    private val tgBot = botDao.getTgBot()

    private val allowedUpdatesList = listOf(
        "message",
        "edited_message",
    )

    private val bot by lazy {
        bot {
            token = tgBot.botToken
            webhook {
                allowedUpdates = allowedUpdatesList
                url = tgBot.webhookUrl
            }
        }
    }

    override fun initBot() {
        bot.startWebhook()
    }

    override fun sendMessage(
        message: String,
        chatId: ChatId,
        replyToMessage: Long?
    ): TelegramBotResult<Message> {
        return bot.sendMessage(
            chatId = chatId,
            text = message,
            replyToMessageId = replyToMessage,
            allowSendingWithoutReply = true,
            parseMode = ParseMode.MARKDOWN_V2
        )
    }
}