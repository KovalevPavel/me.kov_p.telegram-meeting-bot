package me.kov_p.telegram_meeting_bot.database.dao.user

import me.kov_p.telegram_meeting_bot.database.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface UserDao {
    fun createUser(newUser: User)
    fun getUser(userName: String): User?
    fun deleteUser(userIdToDelete: Long)
}

class UserDaoImpl: UserDao {
    override fun createUser(newUser: User) {
        transaction {
            Users.insert {
                it[userName] = newUser.userName
                it[userChatId] = newUser.chatId
            }
        }
    }

    override fun getUser(userName: String): User? {
        return transaction {
            Users.select {
                Users.userName.eq(userName)
            }
                .firstOrNull()
                ?.toUser()
        }
    }

    override fun deleteUser(userIdToDelete: Long) {
        transaction {
            Users.deleteWhere {
                Users.userChatId.eq(userIdToDelete)
            }
        }
    }

    private fun ResultRow.toUser(): User = User(
        userName = this[Users.userName],
        chatId = this[Users.userChatId]
    )
}