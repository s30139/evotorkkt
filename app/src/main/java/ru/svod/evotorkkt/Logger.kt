package ru.svod.evotorkkt

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileLogger {
    companion object {

        private const val MAX_FILE_SIZE = 5L * 1024 * 1024 // 5 МБ
        private lateinit  var logFile: File
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        private val mutex = Mutex()
        private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

        @JvmStatic
        fun init(context: Context) {
            val dir = File(context.getExternalFilesDir(null), "logs").apply { mkdirs() }
            logFile = File(dir, "app.log")
        }

        @JvmStatic
        fun d(tag: String, msg: String) = write("D", tag, msg)

        fun e(tag: String, msg: String, t: Throwable? = null) = write("E", tag, msg + (t?.let { "\n" + it.stackTraceToString() } ?: ""))

        private fun write(level: String, tag: String, msg: String) {
            Log.println(if (level == "E") Log.ERROR else Log.DEBUG, tag, msg)
            scope.launch {
                mutex.withLock {
                    rotateIfNeeded()
                    logFile.appendText("${fmt.format(Date())} $level/$tag: $msg\n")
                }
            }
        }

        private fun rotateIfNeeded() {
            if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
                val backup = File(logFile.parentFile, "app.log.old")
                backup.delete()
                logFile.renameTo(backup)
            }
        }
    }
}

/*
object FileLogger {
    private const val MAX_FILE_SIZE = 5L * 1024 * 1024 // 5 МБ
    private lateinit  var logFile: File
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutex = Mutex()
    private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    fun init(context: Context) {
        val dir = File(context.getExternalFilesDir(null), "logs").apply { mkdirs() }
        logFile = File(dir, "app.log")
    }
    fun d(tag: String, msg: String) = write("D", tag, msg)

    fun e(tag: String, msg: String, t: Throwable? = null) = write("E", tag, msg + (t?.let { "\n" + it.stackTraceToString() } ?: ""))

    private fun write(level: String, tag: String, msg: String) {
        Log.println(if (level == "E") Log.ERROR else Log.DEBUG, tag, msg)
        scope.launch {
            mutex.withLock {
                rotateIfNeeded()
                logFile.appendText("${fmt.format(Date())} $level/$tag: $msg\n")
            }
        }
    }

    private fun rotateIfNeeded() {
        if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
            val backup = File(logFile.parentFile, "app.log.old")
            backup.delete()
            logFile.renameTo(backup)
        }
    }

}
*/


class LogcatCapture {
    private var process: Process? = null
    private var job: Job? = null

    fun start(context: Context) {

        val logFile = File(context.getExternalFilesDir(null), "logs/logcat_full.log").apply {
            parentFile?.mkdirs()
        }
        Toast.makeText(context, "LOGS start() to file<${logFile}>", Toast.LENGTH_LONG).show()

        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                process = ProcessBuilder("logcat", "-v", "threadtime")
                    .redirectErrorStream(true)
                    .start()
                process?.inputStream?.bufferedReader()?.use { reader ->
                    BufferedWriter(FileWriter(logFile, true)).use { writer ->
                        var line: String? = ""
                        while (isActive && reader.readLine().also { line = it } != null) {
                            writer.write(line)
                            writer.newLine()
                            writer.flush() }
                    }
                }
            } catch (e: IOException) {
                Log.e("LogcatCapture", "Ошибка захвата логов", e)
            }
        }

    }
    fun stop() {
        job?.cancel()
        process?.destroy()
    }

}