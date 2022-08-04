package me.kov_p.telegram_meeting_bot.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import me.kov_p.telegram_meeting_bot.bot.TelegramUpdate
import me.kov_p.telegram_meeting_bot.bot.UpdateVo
import me.kov_p.telegram_meeting_bot.bot.handler.BotHandler
import me.kov_p.telegram_meeting_bot.bot.update_delegates.DeleteUserDelegate
import me.kov_p.telegram_meeting_bot.bot.update_delegates.EditMessageDelegate
import me.kov_p.telegram_meeting_bot.bot.update_delegates.NewMessageUpdateDelegate
import me.kov_p.telegram_meeting_bot.bot.update_delegates.NewUserDelegate
import me.kov_p.telegram_meeting_bot.utils.inject
import me.kov_p.telegram_meeting_bot.utils.orFalse
import me.kov_p.telegram_meeting_bot.utils.orZero

fun Application.configureBotRouting() {
    routing {
        val botHandler by inject<BotHandler>()

        post(botHandler.botUpdatePath) {
            call.respond(status = HttpStatusCode.Accepted, message = "Success")

            val delegates by lazy {
                listOf(
                    NewMessageUpdateDelegate(botHandler = botHandler),
                    EditMessageDelegate(botHandler = botHandler),
                    NewUserDelegate(),
                    DeleteUserDelegate()
                )
            }

            fun mapAuthor(authorDto: TelegramUpdate.ChatInfo?) = UpdateVo.NewChatMember(
                id = authorDto?.chatId.orZero(),
                firstName = authorDto?.firstName.orEmpty(),
                secondName = authorDto?.lastName.orEmpty(),
                userName = authorDto?.userName.orEmpty(),
                isBot = authorDto?.isBot.orFalse()
            )

            fun mapMessage(messageInfo: TelegramUpdate.MessageInfo) = UpdateVo.NewMessage(
                id = messageInfo.messageId.orZero(),
                chatId = messageInfo.chatInfo?.chatId.orZero(),
                text = messageInfo.text.orEmpty(),
                author = messageInfo.authorInfo.let(::mapAuthor),
                date = messageInfo.date.orZero(),
            )

            fun mapChatMessageUpdate(messageInfo: TelegramUpdate.MessageInfo): UpdateVo = when {
                messageInfo.newChatMember != null -> mapAuthor(authorDto = messageInfo.newChatMember)
                messageInfo.leftChatMember != null -> UpdateVo.DeletedChatMember(
                    id = messageInfo.leftChatMember.chatId.orZero(),
                    isBot = messageInfo.leftChatMember.isBot.orFalse(),
                    userName = messageInfo.leftChatMember.userName.orEmpty()
                )
                else -> mapMessage(messageInfo = messageInfo)
            }

            val response = call.receive<TelegramUpdate>()

            when {
                response.messageInfo != null -> response.messageInfo.let(::mapChatMessageUpdate)
                response.editedMessage != null -> UpdateVo.EditedMessage(
                    originalMessage = response.editedMessage.let(::mapMessage),
                    editedText = response.editedMessage.editText.orEmpty(),
                    editDate = response.editedMessage.editDate.orZero(),
                )
                else -> return@post
            }
                .let { update ->
                    delegates.firstOrNull { it.isDelegateValid(update) }
                        ?.handleUpdate(update)
                }
        }
    }
}
