package me.kov_p.telegram_meeting_bot.bot.update_delegates

import com.github.kotlintelegrambot.entities.ChatId
import me.kov_p.telegram_meeting_bot.bot.UpdateVo
import me.kov_p.telegram_meeting_bot.bot.handler.BotHandler
import kotlin.random.Random

class NewMessageUpdateDelegate(
    private val botHandler: BotHandler,
) : UpdateEventDelegate {

    override fun isDelegateValid(updateVo: UpdateVo): Boolean {
        return updateVo is UpdateVo.NewMessage
    }

    override fun handleUpdate(updateVo: UpdateVo) {
        when (updateVo) {
            is UpdateVo.NewMessage -> {
                checkForMeetingsRequest(updateVo.text)
                    ?.let { replyToMessage(messageUpdateVo = updateVo, textToSend = it) }
                handleIsPersonIsUpToVisit(message = updateVo.text)
                    ?.let { replyToMessage(messageUpdateVo = updateVo, textToSend = it) }
            }
            else -> return
        }
    }

    private fun checkForMeetingsRequest(message: String): String? {
        val formattedMessage = message.getFormattedMessage()

        val triggers = System.getenv(MEETING_TRIGGERS_KEY).toSamplesList()

        val needToReply = triggers.any(formattedMessage::contains)

        return when {
            needToReply -> getMeetingAnswer()
            else -> null
        }
    }

    private fun getMeetingAnswer(): String = when (
        Random.nextInt(0, 100) <= System.getenv(MEETING_POSITIVE_CHANCE_KEY).toInt()
    ) {
        true -> System.getenv(MEETING_POSITIVE_ANSWERS_KEY).toSamplesList()
        false -> System.getenv(MEETING_NEGATIVE_ANSWERS_KEY).toSamplesList()
    }
        .random()

    private fun handleIsPersonIsUpToVisit(message: String): String? {
        if (message.contains(MENTION_CHAR).not()) return null

        val formattedMessage = message.getFormattedMessage()

        val triggers = System.getenv(MEETING_SOMEBODY_IS_UP_TO_VISIT_KEY).toSamplesList()

        val needToReply = triggers.any(formattedMessage::contains)

        return when {
            needToReply -> {
                System.getenv(MEETING_SOMEBODY_IS_UP_TO_VISIT_ANSWER_KEY).toSamplesList().random()
            }
            else -> {
                null
            }
        }
    }

    private fun replyToMessage(messageUpdateVo: UpdateVo.NewMessage, textToSend: String) {
        botHandler.sendMessage(
            message = textToSend,
            chatId = ChatId.fromId(messageUpdateVo.chatId),
            replyToMessage = messageUpdateVo.id
        )
    }

    private fun String.getFormattedMessage(): String {
        return this.replace(NEW_STRING_CHAR, ' ').lowercase()
    }

    private fun String.toSamplesList(): List<String> {
        return this.filterNot { it == NEW_STRING_CHAR }.split(DELIMITER_CHARACTER)
    }

    companion object {
        private const val MEETING_TRIGGERS_KEY = "we_gonna_meet_triggers"
        private const val MEETING_NEGATIVE_ANSWERS_KEY = "we_gonna_meet_negative_answers"
        private const val MEETING_POSITIVE_ANSWERS_KEY = "we_gonna_meet_positive_answers"
        private const val MEETING_POSITIVE_CHANCE_KEY = "we_gonna_meet_positive_chance"
        private const val MEETING_SOMEBODY_IS_UP_TO_VISIT_KEY = "somebody_is_up_to_visit_trigger"
        private const val MEETING_SOMEBODY_IS_UP_TO_VISIT_ANSWER_KEY =
            "somebody_is_up_to_visit_answer"
        private const val MENTION_CHAR = '@'
        private const val NEW_STRING_CHAR = '\n'
        private const val DELIMITER_CHARACTER = "%"
    }
}
