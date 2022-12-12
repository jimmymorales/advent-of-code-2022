import kotlin.collections.ArrayDeque

fun main() {
    fun Pair<Int, Int>.neighbors(): List<Pair<Int, Int>> = buildList {
        add(first + 1 to second)
        add(first to second + 1)
        add(first - 1 to second)
        add(first to second - 1)
    }

    fun Array<IntArray>.search(start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        val queue = ArrayDeque<Pair<Pair<Int, Int>, Int>>().apply { addLast(start to 0) }
        val visited = mutableSetOf<Pair<Int, Int>>().apply { add(start) }
        while (queue.isNotEmpty()) {
            val (pos, level) = queue.removeFirst()

            if (pos == end) {
                return level
            }

            pos.neighbors()
                .filter { it.first in indices && it.second in this[0].indices }
                .forEach { node ->
                    if (node !in visited && this[node.first][node.second] - this[pos.first][pos.second] <= 1) {
                        visited.add(node)
                        queue.addLast(node to level + 1)
                    }
                }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        var start = 0 to 0
        var end = 0 to 0
        val grid = Array(input.size) { x ->
            IntArray(input[0].length) { y ->
                val value = when (val cell = input[x][y]) {
                    'S' -> {
                        start = x to y
                        'a'
                    }
                    'E' -> {
                        end = x to y
                        'z'
                    }
                    else -> cell
                }
                value - 'a'
            }
        }

        return grid.search(start, end)
    }

    fun part2(input: List<String>): Int {
        val startingPoints = mutableListOf<Pair<Int, Int>>()
        var end = 0 to 0
        val grid = Array(input.size) { x ->
            IntArray(input[0].length) { y ->
                val value = when (val cell = input[x][y]) {
                    'S', 'a' -> {
                        startingPoints.add(x to y)
                        'a'
                    }
                    'E' -> {
                        end = x to y
                        'z'
                    }
                    else -> cell
                }
                value - 'a'
            }
        }

        return startingPoints.map { grid.search(it, end) }
            .filter { it != -1 }
            .min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)

    val input = readInput("Day12")
    println(part1(input))

    // part 2
    check(part2(testInput) == 29)
    println(part2(input))
}
