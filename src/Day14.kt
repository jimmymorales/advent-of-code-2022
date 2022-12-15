fun main() {
    fun List<String>.parseCave(): MutableSet<Pair<Int, Int>> = map { line ->
        line.split(" -> ")
            .map { path -> path.split(",").let { (x, y) -> x.toInt() to y.toInt() } }
            .zipWithNext { (x1, y1), (x2, y2) ->
                if (y1 == y2) {
                    val range = if (x1 < x2) x1..x2 else x2..x1
                    range.map { it to y1 }
                } else {
                    val range = if (y1 < y2) y1..y2 else y2..y1
                    range.map { x1 to it }
                }
            }
            .flatten()
    }.flatten().toMutableSet()

    fun MutableSet<Pair<Int, Int>>.sandEmulation(
        caveLowerLimit: Int,
        onRest: MutableSet<Pair<Int, Int>>.(Pair<Int, Int>, () -> Unit) -> SandLoop,
        onEndReached: MutableSet<Pair<Int, Int>>.(Pair<Int, Int>, () -> Unit) -> SandLoop,
    ): Int {
        var sand = 0
        timeUnit@ while (true) {
            var sandX = 500
            var sandY = 0
            while (sandY < caveLowerLimit) {
                if (sandX to sandY + 1 !in this) {
                    sandY++
                } else if (sandX - 1 to sandY + 1 !in this) {
                    sandY++
                    sandX--
                } else if (sandX + 1 to sandY + 1 !in this) {
                    sandY++
                    sandX++
                } else {
                    // rest
                    when (onRest(sandX to sandY) { sand++ }) {
                        SandLoop.Finish -> break@timeUnit
                        SandLoop.NextSand -> continue@timeUnit
                        SandLoop.EndSand -> break
                        SandLoop.None -> Unit
                    }
                }
            }
            when (onEndReached(sandX to sandY) { sand++ }) {
                SandLoop.Finish -> break
                SandLoop.EndSand,
                SandLoop.NextSand,
                SandLoop.None -> Unit
            }
        }
        return sand
    }

    fun part1(input: List<String>): Int {
        val cave = input.parseCave()
        val caveLowerLimit = cave.maxOf(Pair<Int, Int>::second)
        return cave.sandEmulation(
            caveLowerLimit,
            onRest = { (x, y), operator ->
                operator()
                this.add(x to y)
                SandLoop.NextSand
            },
            onEndReached = { _, _ -> SandLoop.Finish },
        )
    }

    fun part2(input: List<String>): Int {
        val cave = input.parseCave()
        val caveLowerLimit = cave.maxOf(Pair<Int, Int>::second) + 1

        return cave.sandEmulation(
            caveLowerLimit,
            onRest = { (x, y), operator ->
                if (x == 500 && y == 0) {
                    operator()
                    SandLoop.Finish
                } else {
                    SandLoop.EndSand
                }
            },
            onEndReached = { (x, y), operator ->
                operator()
                this.add(x to y)
                SandLoop.None
            },
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)

    val input = readInput("Day14")
    println(part1(input))

    // part 2
    check(part2(testInput) == 93)
    println(part2(input))
}

private enum class SandLoop { Finish, NextSand, EndSand, None }
