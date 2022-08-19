import java.io.File
import java.security.MessageDigest

val userHome = File(System.getProperty("user.home"))

val workDir = File(System.getProperty("user.dir"))

fun bytesToHex(`in`: ByteArray): String {
    val sb = StringBuilder(128)
    for (b in `in`) {
        val hex = Integer.toHexString(b.toInt() and 0xFF)
        if (hex.length < 2) {
            sb.append(0)
        }
        sb.append(hex)
    }
    return sb.toString()
}

fun String.sha256(): String {
    return bytesToHex(MessageDigest.getInstance("SHA-256").digest(encodeToByteArray()))
}