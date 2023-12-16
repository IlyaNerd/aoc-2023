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

    fun List<String>.toGroups() = fold(mutableListOf<MutableList<String>>()) { acc, line ->
        if (acc.isEmpty()) acc.add(mutableListOf())
        if (line.isBlank()) acc.add(mutableListOf())
        else acc.last().add(line)
        acc
    }

    fun part1(input: List<String>): Int {
        return input
            .toGroups()
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

    fun List<String>.findPerfectReflectionWithSmudges(): Int? {
        val original = findPerfectReflection()
        var smudge = Point(0, 0)
        val yMax = first().length - 1
        val xMax = size - 1
        while (smudge.x <= xMax && smudge.y <= yMax) {
            val list = this.toMutableList()
            list[smudge.x] = list[smudge.x]
                .toCharArray()
                .also { it[smudge.y] = if (it[smudge.y] == '#') '.' else '#' }
                .let(::String)

            val reflection = list.findPerfectReflection()
            if (reflection != null && original == null) return reflection
            else if (reflection != null && reflection != original) {
                return reflection
            } else {
                var x = smudge.x
                var y = smudge.y
                if (smudge.y == yMax) {
                    y = 0
                    x++
                } else {
                    y++
                }
                smudge = Point(x, y)
            }
        }
        return null
    }

    fun part2(input: List<String>): Int {
        return input
            .toGroups()
            .map { group ->
                val rowReflection = group.findPerfectReflectionWithSmudges()
                if (rowReflection != null) {
                    rowReflection * 100
                } else {
                    val colReflection = group.toColumns().findPerfectReflectionWithSmudges()
                    colReflection ?: error("smth went wrong \n${group.joinToString("\n")}\n\n${group.toColumns().joinToString("\n")}")
                }
            }
            .onEach { println("value $it") }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part2(testInput).also { it.println() } == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
