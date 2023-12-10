import Direction.*

enum class Direction {
    TOP, BOTTOM, LEFT, RIGHT
}

data class Point(val x: Int, val y: Int, val s: Char)

fun main() {

    fun List<String>.toLoop(): List<Point> {
        val startLineIndex = this.indexOfFirst { it.contains('S') }
        val startLine = this[startLineIndex]
        val startIndex = startLine.indexOf('S')

        val points = mutableListOf<Point>()
        points.add(Point(startLineIndex, startIndex, startLine[startIndex]))

        var from: Direction
        var i = startLineIndex
        var j = startIndex

        when {
            this.getOrNull(i - 1)?.get(j) in listOf('F', '7', '|') -> {
                i--
                from = BOTTOM
            }

            this.getOrNull(i + 1)?.get(j) in listOf('L', 'J', '|') -> {
                i++
                from = TOP
            }

            this[i].getOrNull(j - 1) in listOf('F', 'L', '-') -> {
                j--
                from = RIGHT
            }

            this[i].getOrNull(j + 1) in listOf('7', 'J', '-') -> {
                j++
                from = LEFT
            }

            else -> error("unreachable")
        }

        while (true) {
            points.add(Point(i, j, this[i][j]))
            when (this[i][j]) {
                '|' -> when (from) {
                    TOP -> i++
                    BOTTOM -> i--
                    LEFT,
                    RIGHT -> error("unreachable $i $j")
                }

                '-' -> when (from) {
                    LEFT -> j++
                    RIGHT -> j--
                    TOP,
                    BOTTOM -> error("unreachable $i $j")
                }

                'L' -> when (from) {
                    TOP -> {
                        j++
                        from = LEFT
                    }

                    RIGHT -> {
                        i--
                        from = BOTTOM
                    }

                    LEFT,
                    BOTTOM -> error("unreachable $i $j")
                }

                'J' -> when (from) {
                    TOP -> {
                        j--
                        from = RIGHT
                    }

                    LEFT -> {
                        i--
                        from = BOTTOM
                    }

                    RIGHT,
                    BOTTOM -> error("unreachable $i $j")
                }

                '7' -> when (from) {
                    BOTTOM -> {
                        j--
                        from = RIGHT
                    }

                    LEFT -> {
                        i++
                        from = TOP
                    }

                    TOP,
                    RIGHT -> error("unreachable $i $j")
                }

                'F' -> when (from) {
                    BOTTOM -> {
                        j++
                        from = LEFT
                    }

                    RIGHT -> {
                        i++
                        from = TOP
                    }

                    TOP,
                    LEFT -> error("unreachable $i $j")
                }

                '.' -> error("Should not be here $i, $j")
                'S' -> break
                else -> error("unreachable $i $j")
            }
        }
        return points.toList()
    }

    fun part1(input: List<String>): Int {
        return input.toLoop().size / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput).also { it.println() } == 8)

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}
