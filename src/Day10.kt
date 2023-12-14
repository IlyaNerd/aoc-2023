import Direction.*

private enum class Direction {
    TOP, BOTTOM, LEFT, RIGHT
}

data class Point(val x: Int, val y: Int)

fun sReplacement(value: Char): Char {
    return if (value == 'S') {
        // S is | in the main file
        '7'
    } else value
}

fun main() {

    fun List<String>.toLoop(): List<Point> {
        val startLineIndex = this.indexOfFirst { it.contains('S') }
        val startLine = this[startLineIndex]
        val startIndex = startLine.indexOf('S')

        val points = mutableListOf<Point>()
        points.add(Point(startLineIndex, startIndex))

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
            points.add(Point(i, j))
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

    data class Step(val x: Int, val y: Int, val to: Direction)

    fun isInTheLoop(input: List<String>, x: Int, y: Int): Boolean {
        val seen = mutableSetOf<Step>()

        fun get(i: Int, j: Int): Char? {
            return if (i < 0 || i >= input.size || j < 0 || j >= input[i].length) null
            else input[i][j].let(::sReplacement)
        }

        // sticking to walls of pipes
        fun getPossibleSteps(i: Int, j: Int, location: Direction): List<Step> {
            val steps = mutableListOf<Step>()
            when (val c = get(i, j)) {
                null -> steps.add(Step(i, j, TOP))
                '.' -> {
                    steps.add(Step(i, j + 1, LEFT))
                    steps.add(Step(i, j - 1, RIGHT))
                    steps.add(Step(i - 1, j, BOTTOM))
                    steps.add(Step(i + 1, j, TOP))
                }

                else -> when (location) {
                    TOP -> when (c) {
                        // im in the -, 7, F, .
                        '-' -> {
                            steps.add(Step(i - 1, j, BOTTOM))
                            steps.add(Step(i, j - 1, TOP))
                            steps.add(Step(i, j + 1, TOP))
                        }

                        '7' -> {
                            steps.add(Step(i - 1, j, BOTTOM))
                            steps.add(Step(i + 1, j, RIGHT))
                            steps.add(Step(i, j - 1, TOP))
                            steps.add(Step(i, j + 1, LEFT))
                        }

                        'F' -> {
                            steps.add(Step(i - 1, j, BOTTOM))
                            steps.add(Step(i + 1, j, LEFT))
                            steps.add(Step(i, j - 1, RIGHT))
                            steps.add(Step(i, j + 1, TOP))
                        }

                        'L' -> {
                            steps.add(Step(i - 1, j, RIGHT))
                            steps.add(Step(i, j + 1, TOP))
                        }

                        'J' -> {
                            steps.add(Step(i - 1, j, LEFT))
                            steps.add(Step(i, j - 1, TOP))
                        }

                        else -> error("unreachable $i $j")
                    }

                    BOTTOM -> when (c) {
                        // im in the -, L, J, .
                        '-' -> {
                            steps.add(Step(i + 1, j, TOP))
                            steps.add(Step(i, j - 1, BOTTOM))
                            steps.add(Step(i, j + 1, BOTTOM))
                        }

                        'L' -> {
                            steps.add(Step(i - 1, j, LEFT))
                            steps.add(Step(i + 1, j, TOP))
                            steps.add(Step(i, j - 1, RIGHT))
                            steps.add(Step(i, j + 1, BOTTOM))
                        }

                        'J' -> {
                            steps.add(Step(i - 1, j, RIGHT))
                            steps.add(Step(i + 1, j, TOP))
                            steps.add(Step(i, j - 1, BOTTOM))
                            steps.add(Step(i, j + 1, LEFT))
                        }

                        'F' -> {
                            steps.add(Step(i + 1, j, RIGHT))
                            steps.add(Step(i, j + 1, BOTTOM))
                        }

                        '7' -> {
                            steps.add(Step(i + 1, j, LEFT))
                            steps.add(Step(i, j - 1, BOTTOM))
                        }

                        else -> error("unreachable $i $j")
                    }

                    LEFT -> when (c) {
                        // im in the |, ., L, J, 7, F
                        '|' -> {
                            steps.add(Step(i - 1, j, LEFT))
                            steps.add(Step(i + 1, j, LEFT))
                            steps.add(Step(i, j - 1, RIGHT))
                        }

                        'L' -> {
                            steps.add(Step(i - 1, j, LEFT))
                            steps.add(Step(i + 1, j, TOP))
                            steps.add(Step(i, j - 1, RIGHT))
                            steps.add(Step(i, j + 1, BOTTOM))
                        }

                        'J' -> {
                            steps.add(Step(i - 1, j, LEFT))
                            steps.add(Step(i, j - 1, TOP))
                        }

                        '7' -> {
                            steps.add(Step(i + 1, j, LEFT))
                            steps.add(Step(i, j - 1, BOTTOM))
                        }

                        'F' -> {
                            steps.add(Step(i - 1, j, BOTTOM))
                            steps.add(Step(i + 1, j, LEFT))
                            steps.add(Step(i, j - 1, RIGHT))
                            steps.add(Step(i, j + 1, TOP))
                        }

                        else -> error("unreachable $i $j")
                    }

                    RIGHT -> when (c) {
                        // im in the |, ., L, J, 7, F

                        '|' -> {
                            steps.add(Step(i - 1, j, RIGHT))
                            steps.add(Step(i + 1, j, RIGHT))
                            steps.add(Step(i, j + 1, LEFT))
                        }

                        'L' -> {
                            steps.add(Step(i - 1, j, RIGHT))
                            steps.add(Step(i, j + 1, TOP))
                        }

                        'J' -> {
                            steps.add(Step(i - 1, j, RIGHT))
                            steps.add(Step(i + 1, j, TOP))
                            steps.add(Step(i, j - 1, BOTTOM))
                            steps.add(Step(i, j + 1, LEFT))
                        }

                        '7' -> {
                            steps.add(Step(i - 1, j, BOTTOM))
                            steps.add(Step(i + 1, j, RIGHT))
                            steps.add(Step(i, j - 1, TOP))
                            steps.add(Step(i, j + 1, LEFT))
                        }

                        'F' -> {
                            steps.add(Step(i + 1, j, RIGHT))
                            steps.add(Step(i, j + 1, BOTTOM))
                        }

                        else -> error("unreachable $i $j")
                    }
                }
            }
            return steps
        }

        val canReachBorder = DeepRecursiveFunction<Step, Boolean> { step ->
            val i = step.x
            val j = step.y
            if (i < 0 || i >= input.size || j < 0 || j >= input[i].length) true
            else getPossibleSteps(i, j, step.to)
                .filter { seen.add(it) }
                .any { callRecursive(it) }
        }

        // i am '.'
        return Direction.entries
            .flatMap { step -> getPossibleSteps(x, y, step) }
            .none { step -> canReachBorder(step) }
    }

    fun List<String>.removeExtraPipes(loop: Set<Point>) = mapIndexed { i, line ->
        line.mapIndexed { j, c ->
            val contains = loop.contains(Point(i, j))
            if (!contains) '.'
            else c
        }.toCharArray().let(::String)
    }

    fun part2(input: List<String>): Int {
        val loop = input.toLoop().toSet()
        val cleanInput = input.removeExtraPipes(loop)
        val output = cleanInput.map { it.toMutableList() }
        return cleanInput
            .asSequence()
            .flatMapIndexed { i, line ->
                line.mapIndexed { j, _ -> Point(i, j) }
            }
            .filterNot { loop.contains(it) }
            .filter { (i, j) ->
                isInTheLoop(cleanInput, i, j)
                    .also { canExit -> output[i][j] = if (canExit) '.' else ' ' }
            }
            .count()
            .also {
                println()
                println(output.joinToString("\n") { String(it.toCharArray()) })
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part2(testInput).also { it.println() } == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
