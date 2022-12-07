fun main() {

    fun parseDir(input: List<String>, node: TreeNode, currentIndex: Int = 1): Int {
        var index = currentIndex
        while (index < input.count()) {
            val parts = input[index].split(' ')
            val (op1, op2) = parts
            when {
                op1 == "$" && op2 == "ls" -> {}
                op1 == "$" && op2 == "cd" -> {
                    val dir = parts[2]
                    if (dir == "..") {
                        return index
                    } else {
                        index = parseDir(input, node.getDir(dir), index + 1)
                    }
                }
                op1 == "dir" -> node.addDir(op2)
                else -> node.addFile(op1.toInt())
            }
            index++
        }
        return index
    }

    fun flattenDir(node: TreeNode): List<TreeNode> = buildList {
        addAll(node.children)
        node.children.forEach {
            addAll(flattenDir(it))
        }
    }

    fun part1(input: List<String>): Int {
        val root = TreeNode("/")
        parseDir(input, root, currentIndex = 1)
        return flattenDir(root)
            .map(TreeNode::calculateTotalSize)
            .filter { it <= 100_000 }
            .sum()
    }



    fun part2(input: List<String>): Int {
        val root = TreeNode("/")
        parseDir(input, root, currentIndex = 1)
        val totalUsedSpace = root.calculateTotalSize()
        val neededSpace = 30_000_000 - (70_000_000 - totalUsedSpace)
        return flattenDir(root)
            .map(TreeNode::calculateTotalSize)
            .sorted()
            .first { it > neededSpace }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)

    cache.clear()
    val input = readInput("Day07")
    println(part1(input))

    // part 2
    cache.clear()
    check(part2(testInput) == 24933642)
    cache.clear()
    println(part2(input))
}

data class TreeNode(val value: String) {
    private val _children: MutableList<TreeNode> = mutableListOf()
    val children: List<TreeNode> get() = _children.toList()
    private var sizeOfFiles = 0

    fun addFile(size: Int) {
        sizeOfFiles += size
    }

    fun addDir(name: String) {
        _children.add(TreeNode("$value/$name"))
    }

    fun getDir(name: String): TreeNode {
        return _children.first {
            it.value == "$value/$name"
        }
    }

    fun calculateTotalSize(): Int {
        if (value in cache) {
            return cache[value]!!
        }
        var totalSize = sizeOfFiles
        for (child in children) {
            totalSize += if (child.value in cache) cache[child.value]!! else child.calculateTotalSize()
        }
        cache[value] = totalSize
        return totalSize
    }
}


private val cache = mutableMapOf<String, Int>()