fun main() {

    fun part1(input: List<String>, knotsCount: Int = 2): Int = buildSet {
        val knots = Array(knotsCount) { 0 to 0 }
        input.map { line -> line.split(' ').let { it[0] to it[1].toInt() } }
            .forEach { move ->
                repeat(move.second) {
                    knots[0] = knots[0].let {
                        when (move.first) {
                            "R" -> (it.first + 1) to it.second
                            "L" -> (it.first - 1) to it.second
                            "U" -> it.first to (it.second + 1)
                            else -> it.first to (it.second - 1)
                        }
                    }
                    for (i in 0 until knots.lastIndex) {
                        val (headX, headY) = knots[i]
                        var (tailX, tailY) = knots[i + 1]
                        if (tailX in headX - 1..headX + 1 && tailY in headY - 1..headY + 1) {
                            continue
                        }
                        if (headY == tailY) {
                            tailX += if (headX > tailX) 1 else -1
                        } else if (headX == tailX) {
                            tailY += if (headY > tailY) 1 else -1
                        } else {
                            tailX += if (headX > tailX) 1 else -1
                            tailY += if (headY > tailY) 1 else -1
                        }
                        knots[i + 1] = tailX to tailY
                    }
                    add(knots.last())
                }
            }
    }.count()

    fun part2(input: List<String>): Int = part1(input, knotsCount = 10)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 88)

    val input = readInput("Day09")
    println(part1(input))

    // part 2
    check(part2(testInput) == 36)
    println(part2(input))
}
