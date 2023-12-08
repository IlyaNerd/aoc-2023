import java.math.BigInteger

fun main() {
    val lineRegex = Regex("^(.+) = \\((.+), (.+)\\)$")

    fun List<String>.toPairs() = associate { line ->
        val (_, start, left, right) = lineRegex.find(line)!!.groupValues
        start to (left to right)
    }

    fun part1(input: List<String>): Int {
        val steps = input[0].also { it.println() }
        val instructions = input.drop(2)
            .toPairs()
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

    fun part2(input: List<String>): BigInteger {
        val steps = input[0].also { it.println() }
        val instructions = input.drop(2)
            .toPairs()
            .onEach { it.println() }

        fun findCount(start: String): Int {
            var key = start
            var count = 0
            while (true) {
                steps.forEach { step ->
                    if (key.endsWith("Z")) return count
                    count++
                    key = if (step == 'L') {
                        instructions[key]!!.first
                    } else {
                        instructions[key]!!.second
                    }
                }
            }
        }

        val keys = instructions.keys.filter { it.endsWith("A") }
            .also { it.println() }

        return keys.map { key ->
            val count = findCount(key)
            println(key to count)
            BigInteger(count.toString())
        }.reduce { acc,  num ->
            acc * num / num.gcd(acc)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
//    check(part1(testInput).also { it.println() } == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
