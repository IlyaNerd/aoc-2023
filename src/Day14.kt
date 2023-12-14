fun main() {
    fun moveUp(input: List<CharArray>, i: Int, j: Int) {
        if (i == 0) return
        val c = input[i][j]
        if (c == 'O') {
            if (input[i - 1][j] == '.') {
                input[i - 1][j] = 'O'
                input[i][j] = '.'
                moveUp(input, i - 1, j)
            }
        }
    }

    fun moveRocksUp(input: List<String>): List<CharArray> {
        val newInput = input.map { it.toCharArray() }

        for (i in 1 until newInput.size) {
            for (j in 0 until newInput[i].size) {
                moveUp(newInput, i, j)
            }
        }
        return newInput
    }

    fun part1(input: List<String>): Int {
        return moveRocksUp(input)
//            .onEach { println(String(it)) }
            .reversed()
            .map { line -> line.count { it == 'O' } }
            .mapIndexed { i, rocks -> rocks * (i + 1) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part2(testInput).also { it.println() } == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
