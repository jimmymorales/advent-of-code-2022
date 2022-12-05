fun main() {

    fun List<String>.parseStacksAndInstructions(): Pair<List<Triple<Int, String, String>>, Map<String, ArrayDeque<Char>>> {
        val sepIndex = indexOfFirst(String::isEmpty)
        val stacksLines = take(sepIndex)
        val stacks = stacksLines.last().trim().split("   ").associateWith { ArrayDeque<Char>() }
        for (line in stacksLines.reversed().drop(1)) {
            var count = 1
            for (i in 1 until line.length step 4) {
                val crate = line[i]
                if (crate != ' ') {
                    stacks[count.toString()]!!.addLast(crate)
                }
                count++
            }
        }
        val instructions = drop(sepIndex + 1)
            .map { ins ->  ins.split(" ") .let { Triple(it[1].toInt(), it[3], it[5]) } }
        return instructions to stacks
    }

    fun Map<String, ArrayDeque<Char>>.joinSortedHeadsToString() = entries
        .sortedBy { it.key.toInt() }
        .joinToString(separator = "") { it.value.last().toString() }

    fun part1(input: List<String>): String {
        val (instructions, stacks) = input.parseStacksAndInstructions()

        instructions.forEach { (count, origin, dest) ->
            repeat(count) {
                stacks[dest]!!.addLast(stacks[origin]!!.removeLast())
            }
        }

        return stacks.joinSortedHeadsToString()
    }

    fun part2(input: List<String>): String {
        val (instructions, stacks) = input.parseStacksAndInstructions()

        instructions.forEach { (count, origin, dest) ->
            val queue = ArrayDeque<Char>()
            repeat(count) {
                queue.addFirst(stacks[origin]!!.removeLast())
            }
            queue.forEach { stacks[dest]!!.addLast(it) }
        }

        return stacks.joinSortedHeadsToString()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")

    val input = readInput("Day05")
    println(part1(input))

    // part 2
    check(part2(testInput) == "MCD")
    println(part2(input))
}
