fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput).also { it.println() } == 281)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
