import java.io.File
import java.util.concurrent.TimeUnit


data class BashResult(val exitCode: Int, val stdout: Iterable<String>, val stderr: Iterable<String>) {
    fun sout() = stdout.joinToString("\n").trim()

    fun serr() = stderr.joinToString("\n").trim()

    fun getOrNull(): String? {
        return if (exitCode == 0) sout() else null
    }

    fun exceptionOrNull(): Throwable? = if (exitCode != 0) RuntimeException(serr()) else null

    fun getOrDefault(defaultValue: String) = if (exitCode == 0) sout() else defaultValue

    fun getOrThrow(): String {
        if (exitCode != 0) throw java.lang.RuntimeException(serr())
        return sout()
    }

    fun onFailure(action: BashResult.() -> Unit): BashResult {
        if (exitCode != 0) action()
        return this
    }

    fun onSuccess(action: BashResult.(output: String) -> Unit): BashResult {
        if (exitCode == 0) action(sout())
        return this
    }
}

fun BashResult.throwIfError(): BashResult {
    if (this.exitCode != 0) {
        throw kotlin.RuntimeException("Process exec error ${toString()}")
    }
    return this
}


fun evalBash(cmd: String, showOutput: Boolean = false, wd: File? = null): BashResult {
    return cmd.runCommand(0) {
        redirectOutput(ProcessBuilder.Redirect.PIPE)
        redirectInput(ProcessBuilder.Redirect.PIPE)
        redirectError(ProcessBuilder.Redirect.PIPE)
        wd?.let { directory(it) }
    }.run {
        val stdout = inputStream.reader().readLines()
        val stderr = errorStream.reader().readLines()
        waitFor(1, TimeUnit.HOURS)
        val exitCode = exitValue()
        BashResult(exitCode, stdout, stderr).also {
            if (showOutput) {
                if (exitCode == 0) {
                    println(it.sout())
                } else {
                    println(it.serr())
                }
            }
        }
    }
}


fun String.runCommand(
    timeoutValue: Long = 60,
    timeoutUnit: TimeUnit = TimeUnit.MINUTES,
    processConfig: ProcessBuilder.() -> Unit = {}
): Process {
    ProcessBuilder("/bin/bash", "-c", this).run {
        directory(File("."))
        inheritIO()
        processConfig()
        val process = start()
        if (timeoutValue > 0L) {
            process.waitFor(timeoutValue, timeoutUnit)
        } else if (timeoutValue < 0) {
            process.waitFor()
        }
        return process
    }
}

@JvmName("evalBashForKotlinStringExtension")
fun String.evalBash(showOutput: Boolean = false, wd: File? = null): BashResult {
    return evalBash(this, showOutput, wd)
}

fun Process.throwIfError(): Process {
    if (this.exitValue() != 0) {
        throw kotlin.RuntimeException("Process exec error ${toString()}")
    }
    return this
}

fun killAllSubProcesses() {
    ProcessHandle.current().descendants().forEach { it.destroy() }
    while (ProcessHandle.current().descendants().anyMatch { it.isAlive }) {
        Thread.sleep(100)
    }
}

fun Process.exceptionOrNull(): Throwable? =
    if (exitValue() != 0) RuntimeException("Program exit with code ${exitValue()}") else null


fun Process.onFailure(action: Process.() -> Unit): Process {
    if (exitValue() != 0) action()
    return this
}

fun Process.onSuccess(action: Process.() -> Unit): Process {
    if (exitValue() == 0) action()
    return this
}
