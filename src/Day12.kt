fun main() {
    data class Springs(val seq: String, val faulty: List<Int>) {
        val faultyRegex = faulty.mapIndexed { i, it ->
            if (i != faulty.size - 1) "#{$it}\\.+"
            else "#{$it}\\.*"
        }.joinToString(prefix = "^\\.*", postfix = "$", separator = "")
            .let { Regex(it) }
    }

    fun Springs.getArrangements(): Int {
        val regex = this.faultyRegex
            .also { println(it) }

        val results = mutableSetOf<String>()

        fun generate(content: CharArray, i: Int) {
            if (content.none { it == '?' }) {
                val result = String(content)
                if (results.add(result) && regex.matches(result)) {
                    results.add(result)
                }
                return
            }

            (i..<content.size).forEach { j ->
                if (content[j] == '?') {
                    val left = generate(content.copyOf().also { it[j] = '.' }, j + 1)
                    val right = generate(content.copyOf().also { it[j] = '#' }, j + 1)
//                    left + right
                } else {
                    // find next '?'
                    val u = (j..<content.size).find { u -> content[u] == '?' }
                    if (u == null) {
                        val result = String(content)
                        if (!results.contains(result) && regex.matches(result)) {
                            results.add(result)
                        }
                    }
                    else generate(content, u)
                }
            }
        }

        val start = System.currentTimeMillis()

        val generate = generate(seq.toCharArray(), 0)
//        val a = generate
//            .map { String(it) }
//            .distinct()
//            .count { regex.matches(it) }

        println("${System.currentTimeMillis() - start} ms")

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
        return input
            .map { line ->
                val (springs, numbers) = line.split(' ')
                Springs(springs, numbers.split(',').map { it.trim().toInt() })
            }
            .map { s ->
                s.copy(
                    seq = (1..5).joinToString("?") { s.seq },
                    faulty = (1..5).flatMap { s.faulty },
                )
            }
            .sumOf { a -> a.getArrangements().also { println(a to it) } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part2(testInput).also { it.println() } == 525152)

    val input = readInput("Day12")
//    part1(input).println()
//    part2(input).println()
}
