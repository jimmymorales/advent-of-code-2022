fun main() {

    fun part1(input: List<String>): Int = input.sumOf {
        val s1 = it.first() - 'A' + 1
        val s2 = it.last() - 'X' + 1
        when {
            s2 == s1 -> 3 + s2
            (s2 == 3 && s1 == 2) ||
            (s2 == 2 && s1 == 1) ||
            (s2 == 1 && s1 == 3) -> 6 + s2
            else -> s2
        }
    }

    fun part2(input: List<String>): Int = input.sumOf { round ->
        val s1 = round.first() - 'A' + 1
        when (round.last()) {
            'Y' -> 3 + s1
            'X' -> (s1 - 1).let { if (it == 0) 3 else it }
            else -> 6 + (s1 + 1).let { if (it == 4) 1 else it }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))

    // part 2
    check(part2(testInput) == 12)
    println(part2(input))
}
