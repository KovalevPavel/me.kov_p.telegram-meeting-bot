package me.kov_p.telegram_meeting_bot.bot.update_delegates

import me.kov_p.telegram_meeting_bot.bot.UpdateVo
import me.kov_p.telegram_meeting_bot.database.dao.user.UserDao
import me.kov_p.telegram_meeting_bot.database.models.User
import org.koin.java.KoinJavaComponent.inject

class NewUserDelegate : UpdateEventDelegate {
    private val userDao: UserDao by inject(UserDao::class.java)

    override fun isDelegateValid(updateVo: UpdateVo): Boolean {
        return updateVo is UpdateVo.NewChatMember
    }

    override fun handleUpdate(updateVo: UpdateVo) {
        when (updateVo) {
            is UpdateVo.NewChatMember -> {
                userDao.createUser(
                    newUser = User(userName = updateVo.userName, chatId = updateVo.id)
                )
            }
            else -> {
                return
            }
        }
    }
}