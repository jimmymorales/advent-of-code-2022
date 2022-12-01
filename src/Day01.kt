fun main() {
    fun List<String>.windowedSumCalories(): List<Int> = buildList {
        var sum = 0
        for (calories in this@windowedSumCalories) {
            val cal = calories.toIntOrNull()
            if (cal != null) {
                sum += cal
            } else {
                add(sum)
                sum = 0
            }
        }
        add(sum)
    }.sortedDescending()

    fun part1(input: List<String>): Int = input
        .windowedSumCalories()
        .first()

    fun part2(input: List<String>): Int = input
        .windowedSumCalories()
        .take(3)
        .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))

    // part 2
    check(part2(testInput) == 45000)
    println(part2(input))
}
