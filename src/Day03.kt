fun main() {
    data class Box(
        val number: Int,
        val left: Char?,
        val right: Char?,
        val top: String?,
        val bottom: String?,
    )

    val numberRegex = Regex("(\\d+)")

    fun List<String>.toBoxes(): List<Box> {
        return this.flatMapIndexed { i, line ->
            val numbers = numberRegex.findAll(line).map { it.groupValues[1] }
            var lastIndex = 0
            numbers.map { number ->
                val numberIndex = line.indexOf(number, lastIndex).also { lastIndex = it + number.length }
                val startIndex = if (numberIndex == 0) 0 else numberIndex - 1
                val leftChar = line.getOrNull(numberIndex - 1)
                val rightChar = line.getOrNull(numberIndex + number.length)
                Box(
                    number = number.toInt(),
                    left = leftChar,
                    right = rightChar,
                    top = this.getOrNull(i - 1)?.substringSafe(startIndex, numberIndex + number.length + 1),
                    bottom = this.getOrNull(i + 1)?.substringSafe(startIndex, numberIndex + number.length + 1),
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
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput).also { it.println() } == 4361)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
