fun main() {

    fun Int.countVisibleTrees(
        axisPos: Int,
        changePosition: (Int) -> Int,
        condition: (Int) -> Boolean,
        newTree: (Int) -> Int,
    ): Int {
        var newPos = changePosition(axisPos)
        var count = 0
        while (condition(newPos)) {
            count++
            if (newTree(newPos) < this) {
                newPos = changePosition(newPos)
            } else {
                break
            }
        }
        return count
    }

    fun Int.isVisibleFromEdge(
        axisPos: Int,
        changePosition: (Int) -> Int,
        condition: (Int) -> Boolean,
        newTree: (Int) -> Int,
    ): Boolean {
        var newPos = changePosition(axisPos)
        while (condition(newPos)) {
            if (newTree(newPos) < this) {
                newPos = changePosition(newPos)
            } else {
                return false
            }
        }
        return true
    }

    fun <T> List<List<Int>>.generateVisibleTreesCount(
        parser: List<List<Int>>.(Pair<Int, Int>) -> T,
    ): List<T> = buildList {
        for (i in this@generateVisibleTreesCount.indices) {
            // ignore edges
            if (i == 0 || i == this@generateVisibleTreesCount.lastIndex) continue
            for (j in this@generateVisibleTreesCount[i].indices) {
                // ignore edges
                if (j == 0 || j == this@generateVisibleTreesCount[i].lastIndex) continue
                add(this@generateVisibleTreesCount.parser(i to j))
            }
        }
    }

    fun part1(input: List<String>): Int {
        val trees = input.map { it.map(Char::digitToInt) }
        val visibleTrees = (2 * trees.size) + (2 * trees.first().size) - 4
        return trees.generateVisibleTreesCount { (i, j) ->
            val tree = this[i][j]
            tree.isVisibleFromEdge(
                axisPos = j,
                changePosition = { it - 1 },
                condition = { it >= 0 },
                newTree = { this[i][it] }
            ) || tree.isVisibleFromEdge(
                axisPos = i,
                changePosition = { it - 1 },
                condition = { it >= 0 },
                newTree = { this[it][j] }
            ) || tree.isVisibleFromEdge(
                axisPos = j,
                changePosition = { it + 1 },
                condition = { it < this[i].size },
                newTree = { this[i][it] }
            ) || tree.isVisibleFromEdge(
                axisPos = i,
                changePosition = { it + 1 },
                condition = { it < this.size },
                newTree = { this[it][j] }
            )
        }.count { it } + visibleTrees
    }

    fun part2(input: List<String>): Int = input.map { it.map(Char::digitToInt) }
        .generateVisibleTreesCount { (i, j) ->
            val tree = this[i][j]
            VisibleTreesCount(
                treePos = i to j,
                left = tree.countVisibleTrees(
                    axisPos = j,
                    changePosition = { it - 1 },
                    condition = { it >= 0 },
                    newTree = { this[i][it] }
                ),
                top = tree.countVisibleTrees(
                    axisPos = i,
                    changePosition = { it - 1 },
                    condition = { it >= 0 },
                    newTree = { this[it][j] }
                ),
                right = tree.countVisibleTrees(
                    axisPos = j,
                    changePosition = { it + 1 },
                    condition = { it < this[i].size },
                    newTree = { this[i][it] }
                ),
                bottom = tree.countVisibleTrees(
                    axisPos = i,
                    changePosition = { it + 1 },
                    condition = { it < this.size },
                    newTree = { this[it][j] }
                ),
            )
        }
        .maxOf { it.left * it.top * it.right * it.bottom }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)

    val input = readInput("Day08")
    println(part1(input))

    // part 2
    check(part2(testInput) == 8)
    println(part2(input))
}

private data class VisibleTreesCount(
    val treePos: Pair<Int, Int>,
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
)
