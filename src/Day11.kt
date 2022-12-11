fun main() {

    fun List<String>.parseMonkeys() = map { section ->
        val lines = section.split('\n')
        val index = lines[0].split(' ')[1].dropLast(1).toInt()
        val items = lines[1].trim().split(' ').drop(2).map { it.removeSuffix(",").toLong() }.toMutableList()
        val condition: (Long) -> Long = lines[2].trim().split(' ').takeLast(2).let { (op, value) ->
            if (op == "*") ({
                it * if (value == "old") it else value.toLong()
            }) else ({
                it + if (value == "old") it else value.toLong()
            })
        }
        val divisibleBy = lines[3].trim().split(' ').last().toLong()
        val testTrue = lines[4].trim().split(' ').last().toInt()
        val testFalse = lines[5].trim().split(' ').last().toInt()
        Monkey(index, items, condition, divisibleBy, testTrue, testFalse)
    }.sortedBy(Monkey::index)

    fun List<Monkey>.calculateInspections(rounds: Int, worryReducer: (Long) -> Long): LongArray {
        val inspections = LongArray(size) { 0 }
        repeat(rounds) {
            forEach { monkey ->
                monkey.items.forEach { item ->
                    val new = worryReducer(monkey.operation(item))
                    if (new % monkey.divisibleBy == 0L) {
                        this[monkey.testTrue].items.add(new)
                    } else {
                        this[monkey.testFalse].items.add(new)
                    }
                }
                inspections[monkey.index] = inspections[monkey.index] + monkey.items.size
                monkey.items.clear()
            }
        }
        return inspections
    }

    fun part1(input: List<String>): Long {
        return input.parseMonkeys()
            .calculateInspections(rounds = 20) { it / 3 }
            .sortedDescending()
            .take(2)
            .produce()
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.parseMonkeys()
        val lcm = monkeys.map(Monkey::divisibleBy).lcm()
        return input.parseMonkeys()
            .calculateInspections(rounds = 10_000) { it % lcm }
            .sortedDescending()
            .take(2)
            .produce()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLinesSplitedbyEmptyLine("Day11_test")
    check(part1(testInput) == 10605L)

    val input = readLinesSplitedbyEmptyLine("Day11")
    println(part1(input))

    // part 2
    check(part2(testInput) == 2713310158)
    println(part2(input))
}

private data class Monkey(
    val index: Int,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val divisibleBy: Long,
    val testTrue: Int,
    val testFalse: Int,
)
