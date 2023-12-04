import kotlin.math.pow

fun main() {
    val cardNumberRegex = Regex("Card\\s+(\\d+):")

    data class Card(
        val number: Int,
        val wins: List<Int>,
    )

    fun String.calculateWins(): List<Int> {
        val (winningNumbers, myNumbers) = this.replace(cardNumberRegex, "")
            .trim()
            .split("|")
            .map { set -> set.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() } }

        return myNumbers
            .filter { it in winningNumbers }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val wins = line.calculateWins()

            when {
                wins.isEmpty() -> 0.0
                wins.size == 1 -> 1.0
                else -> 2.0.pow((wins.size - 1).toDouble())
            }
        }.toInt()
    }

    fun getDupes(originalWins: Map<Int, Card>, card: Card): List<Card> {
        return List(card.wins.size) { i ->
            val dupe = card.number + i + 1
            originalWins[dupe]
        }.filterNotNull()
            .flatMap { getDupes(originalWins, it) + it }
    }

    fun part2(input: List<String>): Int {
        val originalWins = input.map { line ->
            val cardNumber = cardNumberRegex.find(line)?.groupValues?.get(1) ?: error("No matches!")
            val wins = line.calculateWins()
            Card(cardNumber.toInt(), wins)
        }.associateBy { it.number }

        return originalWins.values.flatMap { card ->
            getDupes(originalWins, card) + card
        }.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part2(testInput).also { it.println() } == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
