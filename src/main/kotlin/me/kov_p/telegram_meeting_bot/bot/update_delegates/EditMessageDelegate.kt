package me.kov_p.telegram_meeting_bot.bot.update_delegates

import com.github.kotlintelegrambot.entities.ChatId
import me.kov_p.telegram_meeting_bot.bot.UpdateVo
import me.kov_p.telegram_meeting_bot.bot.handler.BotHandler

class EditMessageDelegate(
    private val botHandler: BotHandler,
) : UpdateEventDelegate {

    override fun isDelegateValid(updateVo: UpdateVo): Boolean {
        return updateVo is UpdateVo.EditedMessage
    }

    override fun handleUpdate(updateVo: UpdateVo) {
        when (updateVo) {
            is UpdateVo.EditedMessage -> {
                println("sending message $updateVo")
                botHandler.sendMessage(
                    message = System.getenv(EDIT_ALERT_CONFIG_KEY),
                    chatId = ChatId.fromId(updateVo.originalMessage.chatId),
                    replyToMessage = updateVo.originalMessage.id
                )
            }
            else -> {
                return
            }
        }
    }

    companion object {
        private const val EDIT_ALERT_CONFIG_KEY = "edit_alert"
    }
}
