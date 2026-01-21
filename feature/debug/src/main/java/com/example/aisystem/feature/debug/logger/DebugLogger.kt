package com.example.aisystem.feature.debug.logger

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class LogEntry(
    val timestamp: Long,
    val level: LogLevel,
    val tag: String,
    val message: String
)

enum class LogLevel {
    DEBUG, INFO, WARNING, ERROR
}

@Singleton
class DebugLogger @Inject constructor() {
    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    private val maxLogs = 2000

    fun log(level: LogLevel, tag: String = "App", message: String) {
        val entry = LogEntry(
            timestamp = System.currentTimeMillis(),
            level = level,
            tag = tag,
            message = message
        )

        _logs.value = (_logs.value + entry).takeLast(maxLogs)

        // Also log to Android logcat
        when (level) {
            LogLevel.DEBUG -> android.util.Log.d(tag, message)
            LogLevel.INFO -> android.util.Log.i(tag, message)
            LogLevel.WARNING -> android.util.Log.w(tag, message)
            LogLevel.ERROR -> android.util.Log.e(tag, message)
        }
    }

    fun debug(tag: String = "App", message: String) = log(LogLevel.DEBUG, tag, message)
    fun info(tag: String = "App", message: String) = log(LogLevel.INFO, tag, message)
    fun warning(tag: String = "App", message: String) = log(LogLevel.WARNING, tag, message)
    fun error(tag: String = "App", message: String) = log(LogLevel.ERROR, tag, message)

    fun clear() {
        _logs.value = emptyList()
    }

    fun export(): String {
        return _logs.value.joinToString("\n") { entry ->
            val time = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
                .format(java.util.Date(entry.timestamp))
            "$time [${entry.level}] ${entry.tag}: ${entry.message}"
        }
    }
}
