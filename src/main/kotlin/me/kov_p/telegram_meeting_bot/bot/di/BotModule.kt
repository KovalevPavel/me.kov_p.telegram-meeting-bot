package me.kov_p.telegram_meeting_bot.bot.di

import me.kov_p.telegram_meeting_bot.bot.handler.BotHandler
import me.kov_p.telegram_meeting_bot.bot.handler.BotHandlerImpl
import org.koin.dsl.module

fun botModule() = module {
    single<BotHandler> { BotHandlerImpl(botDao = get()) }
}