package ru.inforion.lab403.common.logging.common

import ru.inforion.lab403.common.logging.handlers.AbstractHandler
import java.util.logging.Level
import java.util.logging.Level.*

class Logger(val name: String, level: Level, vararg handlers: AbstractHandler) {

    private val offValue = OFF.intValue()
    private val levelValue = level.intValue()

    private val handlers = handlers.toMutableSet()

    fun flush() = handlers.forEach { it.flush() }

    fun addHandler(handler: AbstractHandler) = handlers.add(handler)

    fun removeHandler(handler: AbstractHandler) = handlers.remove(handler)

    private fun millis() = System.currentTimeMillis()

    private fun caller() = Thread.currentThread().stackTrace[3]

    fun doLog(level: Level, message: String) {
        val time = millis()
        val caller = caller()
        val info = Info(this, level, time, caller)
        handlers.forEach { it.log(message, info) }
    }

    fun isLoggable(level: Level) = level.intValue() >= levelValue && levelValue != offValue

    inline fun log(level: Level, message: () -> String) {
        if (!isLoggable(level)) return
        doLog(level, message())
    }

    inline fun severe(message: () -> String) = log(SEVERE, message)

    inline fun warning(message: () -> String) = log(WARNING, message)

    inline fun info(message: () -> String) = log(INFO, message)

    inline fun config(message: () -> String) = log(CONFIG, message)

    inline fun fine(message: () -> String) = log(FINE, message)

    inline fun finer(message: () -> String) = log(FINER, message)

    inline fun finest(message: () -> String) = log(FINEST, message)
}