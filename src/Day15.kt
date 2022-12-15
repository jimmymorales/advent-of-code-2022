import kotlin.math.abs

fun main() {
    fun List<String>.parseSensorAreas() = map { line ->
        val regex = """Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)""".toRegex()
        val (sx, sy, bx, by) = regex.find(line)!!.destructured
        (sx.toInt() to sy.toInt()) to (bx.toInt() to by.toInt())
    }.map { (sensor, beacon) ->
        val distance = abs(sensor.first - beacon.first) + abs(sensor.second - beacon.second)
        MapArea(sensor, beacon, distance)
    }

    fun part1(input: List<String>, row: Int = 10): Int {
        val areas = input.parseSensorAreas()
        val min = areas.minOf { it.sensor.first - it.distance }
        val max = areas.maxOf { it.sensor.first + it.distance }

        return (min..max).asSequence()
            .map { x -> x to row }
            .filter { point -> areas.all { point != it.beacon && point != it.sensor } }
            .count { point -> areas.any { point in it } }
    }

    fun part2(input: List<String>, range: Int = 20): Long {
        val areas = input.parseSensorAreas()

        return (0..range)
            .firstNotNullOf { y ->
                areas.filter { abs(y - it.sensor.second) <= it.distance }
                    .flatMap {
                        val diff = it.distance - abs(y - it.sensor.second)
                        listOf(it.sensor.first - diff to 1, it.sensor.first + diff + 1 to -1)
                    }
                    .sortedWith(compareBy(Pair<Int, Int>::first, Pair<Int, Int>::second))
                    .let {
                        var x = it.first().first
                        var count = 0
                        for (e in it) {
                            if (e.first > x) {
                                if (count == 0 && x in 0..range) {
                                    return@let x to y
                                }
                                x = e.first
                            }
                            count += e.second
                        }
                        null
                    }
            }
            .let { (x, y) -> (x * 4_000_000L) + y }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 26)

    val input = readInput("Day15")
    println(part1(input, row = 2_000_000))

    // part 2
    check(part2(testInput) == 56000011L)
    println(part2(input, range = 4_000_000))
}

typealias Point = Pair<Int, Int>

private data class MapArea(val sensor: Point, val beacon: Point, val distance: Int)

private operator fun MapArea.contains(point: Point): Boolean {
    if (point.second !in sensor.second - distance..sensor.second + distance) return false
    val diff = abs(point.second - sensor.second)
    return point.first in sensor.first - distance + diff..sensor.first + distance - diff
}