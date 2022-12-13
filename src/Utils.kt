import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readLinesSplitedbyEmptyLine(name: String) = File("src", "$name.txt").readText().split("\n\n")

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/* Math */
fun Long.gcm(value: Long): Long {
    var gcd = 1L
    for (i in 1..minOf(this, value)) {
        // Checks if i is factor of both integers
        if (this % i == 0L && value % i == 0L) {
            gcd = i
        }
    }
    return gcd
}
fun Long.lcm(value: Long): Long = (this * value) / (this.gcm(value))
fun List<Long>.lcm(): Long = reduce(Long::lcm)

fun List<Int>.product() = reduce { op1, op2 -> op1 * op2 }
fun List<Long>.product() = reduce { op1, op2 -> op1 * op2 }
