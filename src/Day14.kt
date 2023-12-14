import Day14.Direction
import Day14.Direction.*

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

    fun moveRock(input: List<CharArray>, i: Int, j: Int, direction: Direction) {
        val c = input[i][j]
        if (c != 'O') return

        when (direction) {
            NORTH -> {
                if (i == 0) return

                var n = i
                while (n > 0 && input[n - 1][j] == '.') {
                    n--
                }
                if (i != n) {
                    input[n][j] = 'O'
                    input[i][j] = '.'
                }
            }

            EAST -> {
                val lastIndex = input[i].indices.last
                if (j == lastIndex) return

                var n = j
                while (n < lastIndex && input[i][n + 1] == '.') {
                    n++
                }

                if (j != n && n <= lastIndex) {
                    input[i][n] = 'O'
                    input[i][j] = '.'
                }
            }

            SOUTH -> {
                val lastIndex = input.indices.last
                if (i == lastIndex) return

                var n = i
                while (n < lastIndex && input[n + 1][j] == '.') {
                    n++
                }

                if (i != n && n <= lastIndex) {
                    input[n][j] = 'O'
                    input[i][j] = '.'
                }
            }

            WEST -> {
                if (j == 0) return

                var n = j
                while (n > 0 && input[i][n - 1] == '.') {
                    n--
                }
                if (j != n && n >= 0) {
                    input[i][n] = 'O'
                    input[i][j] = '.'
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val newInput = input.map { it.toCharArray() }

        val start = System.currentTimeMillis()
        for (a in 0 until 1_000) {
            listOf(NORTH, WEST, SOUTH, EAST).forEach { direction ->
                when(direction) {
                    NORTH -> {
                        for (i in 1 until newInput.size) {
                            for (j in newInput[i].indices) {
                                moveRock(newInput, i, j, direction)
                            }
                        }
                    }
                    EAST -> {
                        for (i in newInput.indices) {
                            for (j in newInput[i].indices.reversed()) {
                                moveRock(newInput, i, j, direction)
                            }
                        }
                    }
                    SOUTH -> {
                        for (i in newInput.indices.reversed()) {
                            for (j in newInput[i].indices) {
                                moveRock(newInput, i, j, direction)
                            }
                        }
                    }
                    WEST -> {
                        for (i in newInput.indices) {
                            for (j in 1 until newInput[i].size) {
                                moveRock(newInput, i, j, direction)
                            }
                        }
                    }
                }
            }
        }
        println("${System.currentTimeMillis() - start} ms")

        return newInput
            .onEach { println(String(it)) }
            .reversed()
            .map { line -> line.count { it == 'O' } }
            .mapIndexed { i, rocks -> rocks * (i + 1) }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
//    check(part2(testInput).also { it.println() } == 64)

    val input = readInput("Day14")
//    part1(input).println()
    part2(input).println()
}

private object Day14 {
    enum class Direction {
        NORTH, EAST, SOUTH, WEST
    }
}
