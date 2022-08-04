package me.kov_p.telegram_meeting_bot.utils

import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

fun Boolean?.orFalse() = this ?: false

fun Long?.orZero() = this ?: 0

inline fun <reified T : Any> inject(qualifier: Qualifier? = null): Lazy<T> {
    return KoinJavaComponent.inject(clazz = T::class.java, qualifier = qualifier)
}