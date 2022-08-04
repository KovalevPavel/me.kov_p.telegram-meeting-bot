package me.kov_p.telegram_meeting_bot.database.di

import me.kov_p.telegram_meeting_bot.database.DatabaseHandler
import me.kov_p.telegram_meeting_bot.database.DatabaseHandlerImpl
import me.kov_p.telegram_meeting_bot.database.dao.bot.BotDao
import me.kov_p.telegram_meeting_bot.database.dao.user.UserDao
import me.kov_p.telegram_meeting_bot.database.dao.user.UserDaoImpl
import org.koin.dsl.module

fun databaseModule() = module {
    single<DatabaseHandler> { DatabaseHandlerImpl() }
    single { BotDao() }
    single<UserDao> { UserDaoImpl() }
}