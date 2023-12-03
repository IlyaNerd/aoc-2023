fun main() {
    data class Point(val x: Int, val y: Int)

    data class Box(
        val number: Int,
        val left: Char?,
        val right: Char?,
        val top: String?,
        val bottom: String?,
        val location: Point,
    ) {
        fun hasGear() =
            left == '*' || right == '*' || (top?.any { it == '*' } ?: false) || (bottom?.any { it == '*' } ?: false)

        fun gearLocation(): Point {
            return when {
                left == '*' -> Point(location.x, location.y)
                right == '*' -> Point(location.x + number.toString().length + if (location.x == 0) 0 else 1, location.y)
                top?.any { it == '*' } ?: false -> Point(location.x + top!!.indexOf('*'), location.y - 1)
                bottom?.any { it == '*' } ?: false -> Point(location.x + bottom!!.indexOf('*'), location.y + 1)
                else -> error("No gear found!")
            }
        }
    }

    val numberRegex = Regex("(\\d+)")

    fun List<String>.toBoxes(): List<Box> {
        return this.flatMapIndexed { i, line ->
            val numbers = numberRegex.findAll(line).map { it.groupValues[1] }
            var lastIndex = 0
            numbers.map { number ->
                val numberIndex = line.indexOf(number, lastIndex).also { lastIndex = it + number.length }
                val leftChar = line.getOrNull(numberIndex - 1)
                val rightChar = line.getOrNull(numberIndex + number.length)
                val startIndex = if (numberIndex == 0) 0 else numberIndex - 1
                Box(
                    number = number.toInt(),
                    left = leftChar,
                    right = rightChar,
                    top = this.getOrNull(i - 1)?.substringSafe(startIndex, numberIndex + number.length + 1),
                    bottom = this.getOrNull(i + 1)?.substringSafe(startIndex, numberIndex + number.length + 1),
                    location = Point(startIndex, i),
                )
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input.toBoxes()
            .filter { box ->
                (box.left != null && box.left != '.')
                        || (box.right != null && box.right != '.')
                        || (box.top?.any { it != '.' } ?: false)
                        || (box.bottom?.any { it != '.' } ?: false)
            }
            .sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        return input.toBoxes()
            .asSequence()
            .filter { it.hasGear() }
            .groupBy({ box -> box.gearLocation() }) { box -> box.number }
            .map { if (it.value.size == 2) it.value[0] * it.value[1] else 0 }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput).also { it.println() } == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
