fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { line -> line.split(' ').map { it.toInt() } }
            .onEach { it.println() }
            .map { list ->
                val rows = mutableListOf<List<Int>>()
                rows.add(list)
                var next = list
                while (true) {
                    rows.add(next.zipWithNext { a, b -> b - a }.also { next = it })
                    if (next.distinct().size == 1) break
                }
                rows
            }
            .onEach { it.println() }
            .map { rows ->
                rows.reversed()
                    .fold(0) { acc, ints ->
                        acc + ints.last()
                    }
            }
            .onEach { it.println() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput).also { it.println() } == 114)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
