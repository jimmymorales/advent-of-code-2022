fun main() {

    fun part1(input: List<String>, windowSize: Int = 4): Int = input.first()
        .asSequence()
        .windowed(size = windowSize, transform = List<Char>::toSet)
        .indexOfFirst { it.count() == windowSize }
        .let { it + windowSize }

    fun part2(input: List<String>): Int = part1(input, windowSize = 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)

    val input = readInput("Day06")
    println(part1(input))

    // part 2
    check(part2(testInput) == 19)
    println(part2(input))
}
