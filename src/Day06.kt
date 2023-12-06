fun main() {
    data class TimeDistance(
        val time: Long,
        val minDistance: Long,
    )

    fun TimeDistance.countPossibleWins() = (1..<time).count { holdTime ->
        val leftOverTime = time - holdTime
        val distanceTraveled = leftOverTime * holdTime
        distanceTraveled > minDistance
    }

    fun part1(input: List<String>): Int {
        val (times, distances) = input.map {
            it.substringAfter(':').split(' ').filter { it.isNotBlank() }.map { it.toLong() }
        }
        val timeDistances = times.zip(distances) { a, b -> TimeDistance(a, b) }
        return timeDistances
            .map { it.countPossibleWins() }
            .reduce { a, b -> a * b }
    }

    fun part2(input: List<String>): Int {
        val timeDistance = input.map {
            it.substringAfter(':').replace(" ", "").toLong()
        }.let {
            check(it.size == 2)
            TimeDistance(it[0], it[1])
        }
        return timeDistance.countPossibleWins()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput).also { it.println() } == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
