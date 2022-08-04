package me.kov_p.telegram_meeting_bot.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

interface DatabaseHandler {
    fun initDb()
}

class DatabaseHandlerImpl: DatabaseHandler {
    private val dbUrl = System.getenv("DATABASE_URL")

    override fun initDb() {
        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = DRIVER_CLASS_NAME
            jdbcUrl = dbUrl
            maximumPoolSize = MAX_POOL_SIZE
            isAutoCommit = false
            transactionIsolation = TRANSACTION_ISOLATION
            maxLifetime = MAX_LIFETIME_MS
            validate()
        }
        return HikariDataSource(config)
    }

    companion object {
        private const val DRIVER_CLASS_NAME = "org.postgresql.Driver"
        private const val TRANSACTION_ISOLATION = "TRANSACTION_REPEATABLE_READ"
        private const val MAX_POOL_SIZE = 3
        private const val MAX_LIFETIME_MS = 3*60*1000L // 3 minutes
    }
}