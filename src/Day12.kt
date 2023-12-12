fun main() {
    data class Springs(val seq: String, val faulty: List<Int>) {
        val faultyRegex = faulty.mapIndexed { i, it ->
            if (i != faulty.size - 1) "#{$it}\\.+"
            else "#{$it}\\.*"
        }.joinToString(prefix = "^\\.*", postfix = "$", separator = "")
            .let(::Regex)
    }

    fun Springs.getArrangements(): Int {
        val regex = this.faultyRegex
            .also { println(it) }

        val results = mutableSetOf<String>()

        fun generate(content: CharArray, i: Int) {
            if (content.none { it == '?' }) {
                val result = String(content)
                if (!results.contains(result) && regex.matches(result)) {
                    results.add(result)
                }
                return
            }

            (i..<content.size).forEach { j ->
                if (content[j] == '?') {
                    generate(content.copyOf().also { it[j] = '.' }, j + 1)
                    generate(content.copyOf().also { it[j] = '#' }, j + 1)
                } else {
                    generate(content, j + 1)
                }
            }
        }

        generate(seq.toCharArray(), 0)

        return results.size
    }

    fun part1(input: List<String>): Int {
        return input
            .map { line ->
                val (springs, numbers) = line.split(' ')
                Springs(springs, numbers.split(',').map { it.trim().toInt() })
            }
            .sumOf { a -> a.getArrangements().also { println(a to it) } }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput).also { it.println() } == 21)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
