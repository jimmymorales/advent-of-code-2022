fun main() {
    fun CharArray.parseInput(currentIndex: Int = 0): Pair<Input, Int> {
        val current = this[currentIndex]
        var nextIndex = currentIndex + 1
        return if (current == '[') {
            val items = mutableListOf<Input>()
            while (this[nextIndex] != ']') {
                if (this[nextIndex] == ',') {
                    nextIndex++
                    continue
                }
                val result = parseInput(currentIndex = nextIndex)
                items.add(result.first)
                nextIndex = result.second
            }
            Input.Packet(items) to nextIndex + 1
        } else {
            var intInput = current.toString()
            while (this[nextIndex] != ',' && this[nextIndex] != ']') {
                intInput += this[nextIndex]
                nextIndex++
            }
            Input.IntInput(intInput.toInt()) to nextIndex
        }
    }

    fun Input.toPacketInput(): Input.Packet = when (this) {
        is Input.IntInput -> Input.Packet(listOf(this))
        is Input.Packet -> this
    }

    fun compareInputsOrder(left: Input, right: Input): Int {
        fun comparePacketsOrder(left: Input.Packet, right: Input.Packet): Int {
            val leftSize = left.values.size
            val rightSize = right.values.size
            var i = 0
            while (i < minOf(leftSize, rightSize)) {
                val res = compareInputsOrder(left.values[i], right.values[i])
                if (res != 0) return res
                i++
            }
            return rightSize - leftSize
        }
        return when (left) {
            is Input.Packet -> comparePacketsOrder(left, right.toPacketInput())
            is Input.IntInput -> when (right) {
                is Input.IntInput -> right.value - left.value
                is Input.Packet -> comparePacketsOrder(left.toPacketInput(), right)
            }
        }
    }

    fun part1(input: List<String>): Int = input.asSequence()
        .map {
            val (left, right) = it.split('\n')
            left.toCharArray().parseInput().first to right.toCharArray().parseInput().first
        }
        .map { (left, right) -> compareInputsOrder(left, right) }
        .withIndex()
        .filter { it.value > 0 }
        .sumOf { it.index + 1 }

    fun part2(input: List<String>): Int = input.flatMap {
        val (left, right) = it.split('\n')
        listOf(left.toCharArray().parseInput().first, right.toCharArray().parseInput().first)
    }.asSequence()
        .plus(listOf(twoDivider, sixDivider))
        .sortedWith { o1, o2 -> compareInputsOrder(o2, o1) }
        .filterIsInstance<Input.Packet>()
        .withIndex()
        .filter { it.value.isDivider }
        .map { it.index + 1 }
        .toList()
        .product()

    // test if implementation meets criteria from the description, like:
    val testInput = readLinesSplitedbyEmptyLine("Day13_test")
    check(part1(testInput) == 13)

    val input = readLinesSplitedbyEmptyLine("Day13")
    println(part1(input))

    // part 2
    check(part2(testInput) == 140)
    println(part2(input))
}

private val twoDivider = Input.Packet(
    values = listOf(Input.Packet(values = listOf(Input.IntInput(2)))),
    isDivider = true,
)
private val sixDivider = Input.Packet(
    values = listOf(Input.Packet(values = listOf(Input.IntInput(6)))),
    isDivider = true,
)

private sealed class Input {
    data class IntInput(val value: Int) : Input()
    data class Packet(val values: List<Input>, val isDivider: Boolean = false) : Input()
}
