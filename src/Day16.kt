import Day16.Direction
import Day16.Direction.*

fun main() {
    data class Step(val i: Int, val j: Int, val direction: Direction)

    fun goTo(input: List<String>, startStep: Step): Set<Point> {
        val seen = mutableSetOf<Point>()

        fun getSteps(i: Int, j: Int, direction: Direction): List<Step> {
            if (i < 0 || i >= input.size || j < 0 || j >= input[i].length) return emptyList()
            val steps = mutableListOf<Step>()
            when (val c = input[i][j]) {
                '.' -> when (direction) {
                    Up -> steps.add(Step(i - 1, j, Up))
                    Down -> steps.add(Step(i + 1, j, Down))
                    Left -> steps.add(Step(i, j - 1, Left))
                    Right -> steps.add(Step(i, j + 1, Right))
                }

                '/' -> when (direction) {
                    Up -> steps.add(Step(i, j + 1, Right))
                    Down -> steps.add(Step(i, j - 1, Left))
                    Left -> steps.add(Step(i + 1, j, Down))
                    Right -> steps.add(Step(i - 1, j, Up))
                }

                '\\' -> when (direction) {
                    Up -> steps.add(Step(i, j - 1, Left))
                    Down -> steps.add(Step(i, j + 1, Right))
                    Left -> steps.add(Step(i - 1, j, Up))
                    Right -> steps.add(Step(i + 1, j, Down))
                }

                '-' -> {
                    when (direction) {
                        Up, Down -> {
                            steps.add(Step(i, j - 1, Left))
                            steps.add(Step(i, j + 1, Right))
                        }

                        Left -> steps.add(Step(i, j - 1, Left))
                        Right -> steps.add(Step(i, j + 1, Right))
                    }
                }

                '|' -> when (direction) {
                    Up -> steps.add(Step(i - 1, j, Up))
                    Down -> steps.add(Step(i + 1, j, Down))
                    Left, Right -> {
                        steps.add(Step(i - 1, j, Up))
                        steps.add(Step(i + 1, j, Down))
                    }
                }

                else -> error("unexpected location $c $i $j")
            }
            return steps
        }

        val dupes = mutableSetOf<Step>()

        val goTo = DeepRecursiveFunction { step: Step ->
            val (i, j, direction) = step
            if (!dupes.add(step)) Unit
            else if (i < 0 || i >= input.size || j < 0 || j >= input[i].length) Unit
            else {
                seen.add(Point(i, j))
                getSteps(i, j, direction).forEach {
                    callRecursive(it)
                }
            }

        }

        goTo(startStep)
        return seen
    }

    fun part1(input: List<String>): Int {
        return goTo(input, Step(0, 0, Right)).size
    }

    fun part2(input: List<String>): Int {
        val rowSteps = input.indices.flatMap { i ->
            val lastJ = input[i].length - 1
            listOf(Step(i, 0, Right), Step(i, lastJ, Left))
        }
        val colSteps = input.first().indices.flatMap { j ->
            val lastI = input.size - 1
            listOf(Step(0, j, Down), Step(lastI, j, Up))
        }

        return (rowSteps + colSteps)
            .maxOf { goTo(input, it).size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part2(testInput).also { it.println() } == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

private object Day16 {
    enum class Direction {
        Up, Down, Left, Right
    }
}