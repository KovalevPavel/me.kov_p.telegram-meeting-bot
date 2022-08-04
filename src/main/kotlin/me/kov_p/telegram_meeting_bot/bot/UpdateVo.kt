package me.kov_p.telegram_meeting_bot.bot

sealed class UpdateVo {

    data class NewMessage(
        val id: Long,
        val chatId: Long,
        val text: String,
        val author: NewChatMember,
        val date: Long,
    ) : UpdateVo()

    data class EditedMessage(
        val originalMessage: NewMessage,
        val editedText: String?,
        val editDate: Long,
    ) : UpdateVo()

    data class NewChatMember(
        val id: Long,
        val isBot: Boolean,
        val firstName: String,
        val secondName: String,
        val userName: String,
    ) : UpdateVo()

    data class DeletedChatMember(
        val id: Long,
        val isBot: Boolean,
        val userName: String,
    ) : UpdateVo()
}
