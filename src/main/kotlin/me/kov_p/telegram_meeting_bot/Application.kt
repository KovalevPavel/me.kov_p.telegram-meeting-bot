package me.kov_p.telegram_meeting_bot

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import me.kov_p.telegram_meeting_bot.bot.di.botModule
import me.kov_p.telegram_meeting_bot.bot.handler.BotHandler
import me.kov_p.telegram_meeting_bot.database.DatabaseHandler
import me.kov_p.telegram_meeting_bot.database.di.databaseModule
import me.kov_p.telegram_meeting_bot.plugins.configureBotRouting
import me.kov_p.telegram_meeting_bot.plugins.configureSerialization
import me.kov_p.telegram_meeting_bot.utils.inject
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(
            botModule(),
            databaseModule()
        )
    }

    val databaseHandler by inject<DatabaseHandler>()
    val botHandler by inject<BotHandler>()

    databaseHandler.initDb()
    botHandler.initBot()

    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        configureSerialization()
        configureBotRouting()
    }
        .start(wait = true)
}
