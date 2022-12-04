fun main() {

    fun String.parsePairs(): Pair<IntRange, IntRange> = split(",").map { pair ->
        pair.split("-").map(String::toInt).let { (l, h) -> l..h }
    }.let { (p1, p2) -> p1 to p2 }

    fun part1(input: List<String>): Int = input.map(String::parsePairs)
        .count { (p1, p2) ->
            p1.intersect(p2).let { it.containsAll(p1.toList()) || it.containsAll(p2.toList()) }
        }

    fun part2(input: List<String>): Int = input.map(String::parsePairs)
        .count { (p1, p2) -> p1.intersect(p2).isNotEmpty() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    println(part1(input))

    // part 2
    check(part2(testInput) == 4)
    println(part2(input))
}
