fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val digits = line.filter { it.isDigit() }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val numbers = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            "zero" to 0,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
            "0" to 0,
        )
        return input.sumOf { line ->
            val firstDigit = numbers
                .mapKeys { (it, _) -> line.indexOf(it) }
                .filter { it.key != -1 }
                .minBy { it.key }
                .value

            val lastDigit = numbers
                .mapKeys { (it, _) -> line.lastIndexOf(it) }
                .filter { it.key != -1 }
                .maxBy { it.key }
                .value

            "${firstDigit}${lastDigit}".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput).also { it.println() } == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
