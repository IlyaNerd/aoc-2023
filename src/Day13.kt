fun main() {
    fun List<String>.toColumns(): List<String> {
        return (0..<first().length).map { i -> this.map { it[i] }.toCharArray().let(::String) }
    }

    fun List<String>.findPerfectReflection(): Int? {
        val s = (1..<this.size).find { i ->
            val left = this.take(i).reversed()
            val right = this.drop(i)
            val zip = left.zip(right)
            zip.all { (a, b) -> a == b }
        }
        return s
    }

    fun part1(input: List<String>): Int {
        return input
            .fold(mutableListOf<MutableList<String>>()) { acc, line ->
                if (acc.isEmpty()) acc.add(mutableListOf())
                if (line.isBlank()) acc.add(mutableListOf())
                else acc.last().add(line)
                acc
            }
            .sumOf { group ->
                val rowReflection = group.findPerfectReflection()
                if (rowReflection != null) {
                    rowReflection * 100
                } else {
                    val colReflection = group.toColumns().findPerfectReflection()
                    colReflection ?: error("smth went wrong \n${group.joinToString("\n")}\n\n${group.toColumns().joinToString("\n")}")
                }
            }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput).also { it.println() } == 405)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
