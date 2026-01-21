package com.example.aisystem.feature.debug.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aisystem.feature.debug.logger.DebugLogger
import com.example.aisystem.feature.debug.logger.LogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val debugLogger: DebugLogger
) : ViewModel() {

    val logs: StateFlow<List<LogEntry>> = debugLogger.logs.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun clearLogs() {
        debugLogger.clear()
    }

    fun exportLogs() {
        viewModelScope.launch {
            try {
                val logsText = debugLogger.export()
                val file = File(context.cacheDir, "debug_logs_${System.currentTimeMillis()}.txt")
                file.writeText(logsText)

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context.startActivity(
                    Intent.createChooser(shareIntent, "Export Debug Logs").apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            } catch (e: Exception) {
                debugLogger.error("DebugViewModel", "Failed to export logs: ${e.message}")
            }
        }
    }
}
