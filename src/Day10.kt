import Direction.*

enum class Direction {
    TOP, BOTTOM, LEFT, RIGHT
}

data class Point(val x: Int, val y: Int)

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

//    | is a vertical pipe connecting north and south.
//    - is a horizontal pipe connecting east and west.
//    L is a 90-degree bend connecting north and east.
//    J is a 90-degree bend connecting north and west.
//    7 is a 90-degree bend connecting south and west.
//    F is a 90-degree bend connecting south and east.
//    . is ground; there is no pipe in this tile.
//    S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.

    data class PossibleStep(val point: Point, val from: Direction?, val to: Direction?)


    fun isEnclosedByLoop(input: List<String>, x: Int, y: Int): Boolean {
        fun sReplacement(value: Char): Char {
            return if (value == 'S') {
                // todo S
                // S is | in the main file
//                '|'
                '|'
            } else value
        }

        val seen = mutableSetOf<PossibleStep>()

        fun getPossibleSteps(i: Int, j: Int, from: Direction?, to: Direction?): List<PossibleStep> {
            val possibleSteps = mutableListOf<PossibleStep>()

            if (from == null || to == null || from == TOP && to == LEFT) {
                // bottom left
                if (i + 1 >= input.size) possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, LEFT))
                else when (input[i + 1][j].let(::sReplacement)) {
                    '|', 'L', 'F', '.' -> possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, LEFT))
                    '7', '-', 'J' -> Unit
//                    'J' ->  possibleSteps.add(PossibleStep(Point(i + 1, j - 1), RIGHT, TOP))
                    else -> error("unreachable $i $j")
                }
                // left top
                if (j - 1 < 0) possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, TOP))
                else when (input[i][j - 1].let(::sReplacement)) {
                    '-', 'F', '7', '.' -> possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, TOP))
                    '|', 'L', 'J' -> Unit
                    else -> error("unreachable $i $j")
                }
                // left bottom
                if (j - 1 < 0) possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, BOTTOM))
                else when (input[i][j - 1].let(::sReplacement)) {
                    '-', 'J', 'L', '.' -> possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, BOTTOM))
                    '|', 'F', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == TOP && to == RIGHT) {
                // bottom right
                if (i + 1 >= input.size) possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, RIGHT))
                else when (input[i + 1][j].let(::sReplacement)) {
                    '|', 'J', '7', '.' -> possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, RIGHT))
                    'F', 'L', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
                // right top
                if (j + 1 >= input[i].length) possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, TOP))
                else when (input[i][j + 1].let(::sReplacement)) {
                    '-', 'F', '7', '.' -> possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, TOP))
                    '|', 'L', 'J' -> Unit
                    else -> error("unreachable $i $j")
                }
                // right bottom
                if (j + 1 >= input[i].length) possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, BOTTOM))
                else when (input[i][j + 1].let(::sReplacement)) {
                    '-', 'J', 'L', '.' -> possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, BOTTOM))
                    '|', 'F', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == BOTTOM && to == LEFT) {
                // top left
                if (i - 1 < 0) possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, LEFT))
                else when (input[i - 1][j].let(::sReplacement)) {
                    '|', 'L', 'F', '.' -> possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, LEFT))
                    '-', 'J', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
                // left top
                if (j - 1 < 0) possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, TOP))
                else when (input[i][j - 1].let(::sReplacement)) {
                    '-', 'F', '7', '.' -> possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, TOP))
                    '|', 'L', 'J' -> Unit
                    else -> error("unreachable $i $j")
                }
                // left bottom
                if (j - 1 < 0) possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, BOTTOM))
                else when (input[i][j - 1].let(::sReplacement)) {
                    '-', 'J', 'L', '.' -> possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, BOTTOM))
                    '|', 'F', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == BOTTOM && to == RIGHT) {
                // top right
                if (i - 1 < 0) possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, RIGHT))
                else when (input[i - 1][j].let(::sReplacement)) {
                    '|', 'J', '7', '.' -> possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, RIGHT))
                    'F', 'L', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
                // right top
                if (j + 1 >= input[i].length) possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, LEFT))
                else when (input[i][j + 1].let(::sReplacement)) {
                    '-', 'F', '7', '.' -> possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, TOP))
                    '|', 'L', 'J' -> Unit
                    else -> error("unreachable $i $j")
                }
                // right bottom
                if (j + 1 >= input[i].length) possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, BOTTOM))
                else when (input[i][j + 1].let(::sReplacement)) {
                    '-', 'J', 'L', '.' -> possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, BOTTOM))
                    '|', 'F', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == LEFT && to == TOP) {
                // top left
                if (i - 1 < 0) possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, LEFT))
                else when (input[i - 1][j].let(::sReplacement)) {
                    '|', 'L', 'F', '.' -> possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, LEFT))
                    '-', 'J', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
                // top right
                if (i - 1 < 0) possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, RIGHT))
                else when (input[i - 1][j].let(::sReplacement)) {
                    '|', 'J', '7', '.' -> possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, RIGHT))
                    'F', 'L', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
                // right top
                if (j + 1 >= input[i].length) possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, LEFT))
                else when (input[i][j + 1].let(::sReplacement)) {
                    '-', 'F', '7', '.' -> possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, TOP))
                    '|', 'L', 'J' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == LEFT && to == BOTTOM) {
                // right bottom
                if (j + 1 >= input[i].length) possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, BOTTOM))
                else when (input[i][j + 1].let(::sReplacement)) {
                    '-', 'J', 'L', '.' -> possibleSteps.add(PossibleStep(Point(i, j + 1), LEFT, BOTTOM))
                    '|', 'F', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
                // bottom left
                if (i + 1 >= input.size) possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, LEFT))
                else when (input[i + 1][j].let(::sReplacement)) {
                    '|', 'L', 'F', '.' -> possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, LEFT))
                    '7', 'J', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
                // bottom right
                if (i + 1 >= input.size) possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, RIGHT))
                else when (input[i + 1][j].let(::sReplacement)) {
                    '|', 'J', '7', '.' -> possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, RIGHT))
                    'F', 'L', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == RIGHT && to == TOP) {
                // top left
                if (i - 1 < 0) possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, LEFT))
                else when (input[i - 1][j].let(::sReplacement)) {
                    '|', 'L', 'F', '.' -> possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, LEFT))
                    '-', 'J', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
                // top right
                if (i - 1 < 0) possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, RIGHT))
                else when (input[i - 1][j].let(::sReplacement)) {
                    '|', 'J', '7', '.' -> possibleSteps.add(PossibleStep(Point(i - 1, j), BOTTOM, RIGHT))
                    'F', 'L', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
                // left top
                if (j - 1 < 0) possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, TOP))
                else when (input[i][j - 1].let(::sReplacement)) {
                    '-', 'F', '7', '.' -> possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, TOP))
                    '|', 'L', 'J' -> Unit
                    else -> error("unreachable $i $j")
                }
            }
            if (from == null || to == null || from == RIGHT && to == BOTTOM) {
                // left bottom
                if (j - 1 < 0) possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, BOTTOM))
                else when (input[i][j - 1].let(::sReplacement)) {
                    '-', 'J', 'L', '.' -> possibleSteps.add(PossibleStep(Point(i, j - 1), LEFT, BOTTOM))
                    '|', 'F', '7' -> Unit
                    else -> error("unreachable $i $j")
                }
                // bottom left
                if (i + 1 >= input.size) possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, LEFT))
                else when (input[i + 1][j].let(::sReplacement)) {
                    '|', 'L', 'F', '.' -> possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, LEFT))
                    '7', 'J', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
                // bottom right
                if (i + 1 >= input.size) possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, RIGHT))
                else when (input[i + 1][j].let(::sReplacement)) {
                    '|', 'J', '7', '.' -> possibleSteps.add(PossibleStep(Point(i + 1, j), TOP, RIGHT))
                    'F', 'L', '-' -> Unit
                    else -> error("unreachable $i $j")
                }
            }

            return possibleSteps
        }

        val canReachBorder = DeepRecursiveFunction<PossibleStep, Boolean> { step ->
            val i = step.point.x
            val j = step.point.y
            if (i < 0 || i >= input.size || j < 0 || j >= input[i].length) true
            else getPossibleSteps(i, j, step.from, step.to)
                .filter { seen.add(it.copy(from = null)) }
                .any { callRecursive(it) }
        }


        return  !getPossibleSteps(x, y, null, null)
            .any { step ->
                canReachBorder(step)
            }
    }


    fun part2(input: List<String>): Int {
        val loop = input.toLoop().toSet()
        return input
            .mapIndexed { i, line ->
                line.mapIndexed { j, c ->
                    val contains = loop.contains(Point(i, j))
                    if (!contains) '.'
                    else c
                }.toCharArray().let(::String)
            }
            .let { newInput ->
                val output = newInput.map { it.toMutableList() }
                newInput.mapIndexed { i, line ->
                    line.filterIndexed { j, c ->
                        if (loop.contains(Point(i, j))) {
                            false
                        } else isEnclosedByLoop(newInput, i, j).also {
                            output[i][j] = if (it) '.' else ' '
                        }
                    }
                }
                    .map { it.length }
                    .sum()
                    .also {
                        println()
                        println(output.joinToString("\n") { String(it.toCharArray()) })
                    }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part2(testInput).also { it.println() } == 4)

    val input = readInput("Day10")
//    part1(input).println()
    part2(input).println()
}
