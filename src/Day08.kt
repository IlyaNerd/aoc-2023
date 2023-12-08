fun main() {
    fun part1(input: List<String>): Int {
        val steps = input[0].also { it.println() }
        val lineRegex = Regex("^(.+) = \\((.+), (.+)\\)$")
        val instructions = input.drop(2)
            .associate { line ->
                val (_, start, left, right) = lineRegex.find(line)!!.groupValues
                start to (left to right)
            }
            .onEach { it.println() }
        var key = "AAA"
        var count = 0
        while (true) {
            steps.forEach { step ->
                if (key == "ZZZ") return count
                count++
                key = if (step == 'L') {
                    instructions[key]!!.first
                } else {
                    instructions[key]!!.second
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput).also { it.println() } == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
