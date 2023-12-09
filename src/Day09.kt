fun main() {
    fun List<Int>.toSequenceTree(): MutableList<List<Int>> {
        val rows = mutableListOf<List<Int>>()
        rows.add(this)
        var next = this
        while (true) {
            rows.add(next.zipWithNext { a, b -> b - a }.also { next = it })
            if (next.distinct().size == 1) break
        }
        return rows
    }

    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { line -> line.split(' ').map { it.toInt() } }
            .onEach { it.println() }
            .map { list -> list.toSequenceTree() }
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
        return input
            .asSequence()
            .map { line -> line.split(' ').map { it.toInt() } }
            .map { list ->
                val rows = list.toSequenceTree()
                rows.reversed()
                    .drop(1)
                    .fold(rows.last().first()) { acc, ints ->
                        ints.first() - acc
                    }
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput).also { it.println() } == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
