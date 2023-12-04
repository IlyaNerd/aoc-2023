import kotlin.math.pow

fun main() {
    val cardNumberRegex = Regex("Card\\s+(\\d+):")

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (winningNumbers, myNumbers) = line.replace(cardNumberRegex, "")
                .trim()
                .split("|")
                .map { set -> set.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() } }

            val wins = myNumbers
                .filter { it in winningNumbers }

            val result = when {
                wins.isEmpty() -> 0.0
                wins.size == 1 -> 1.0
                else -> 2.0.pow((wins.size - 1).toDouble())
            }
            result
        }.toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput).also { it.println() } == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
