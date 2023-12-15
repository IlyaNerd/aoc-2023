fun main() {
    fun String.buildHash(): Int {
        var sum = 0
        this.forEach { c ->
            sum += c.code
            sum *= 17
            sum = sum.rem(256)
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        return input.flatMap { it.split(',') }
            .sumOf { str ->
                str.buildHash()
            }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput).also { it.println() } == 1320)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
