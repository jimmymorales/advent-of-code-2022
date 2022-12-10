fun main() {

    fun List<String>.parseInstructions(operation: (Int, String) -> Unit) {
        var x = 1
        forEach { line ->
            val (inst, n) = line.split(" ").let { it[0] to it.getOrNull(1)?.toInt() }
            operation(x, inst)
            n?.let { x += it }
        }
    }

    fun part1(input: List<String>): Int {
        var cycle = 0
        var signalStrength = 0
        input.parseInstructions { x, inst ->
            if (inst == "noop") {
                cycle++
            } else {
                cycle++
                signalStrength += if ((cycle + 20) % 40 == 0) cycle * x else 0
                cycle++
            }
            signalStrength += if ((cycle + 20) % 40 == 0) cycle * x else 0
        }
        return signalStrength
    }

    fun Pair<Int, Int>.movePixel(size: Int) = let { (x, y) ->
        var newY = y + 1
        var newX = x
        if (newY >= size) {
            newY = 0
            newX++
        }
        newX to newY
    }

    fun part2(input: List<String>) {
        val crt = Array(6) { Array(40) { '.' } }
        var pixelIndex = 0 to 0
        input.parseInstructions { x, inst ->
            crt[pixelIndex.first][pixelIndex.second] = if (pixelIndex.second in x-1..x+1) '#' else '.'
            if (inst == "addx") {
                pixelIndex = pixelIndex.movePixel(crt[0].size)
                crt[pixelIndex.first][pixelIndex.second] = if (pixelIndex.second in x-1..x+1) '#' else '.'
            }
            pixelIndex = pixelIndex.movePixel(crt[0].size)
        }

        crt.forEach { println(it.joinToString("")) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))

    // part 2
    part2(testInput)
    part2(input)
}
