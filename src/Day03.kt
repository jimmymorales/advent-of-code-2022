fun main() {

    fun Char.priority() = (if (this <= 'Z') this - 'A' + 26 else this - 'a') + 1

    fun part1(input: List<String>): Int = input.map { rubsack ->
        rubsack.chunked(rubsack.length / 2, CharSequence::toSet)
            .reduce(Set<Char>::intersect)
            .single()
    }.sumOf(Char::priority)

    fun part2(input: List<String>): Int = input.windowed(step = 3, size = 3)
        .map { groups ->
            groups.map(String::toSet)
                .reduce(Set<Char>::intersect)
                .single()
        }.sumOf(Char::priority)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    println(part1(input))

    // part 2
    check(part2(testInput) == 70)
    println(part2(input))
}
