fun main() {
    data class TimeDistance(
        val time: Int,
        val minDistance: Int,
    )

    fun part1(input: List<String>): Int {
        val (times, distances) = input.map {
            it.substringAfter(':').split(' ').filter { it.isNotBlank() }.map { it.toInt() }
        }
        val timeDistances = times.zip(distances) { a, b -> TimeDistance(a, b) }
        return timeDistances
            .map {
                val timeRange = 1..<it.time
                timeRange.count {holdTime ->
                    val leftOverTime = it.time - holdTime
                    val distanceTraveled = leftOverTime * holdTime
                    distanceTraveled > it.minDistance
                }
            }
            .reduce { a, b -> a * b }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput).also { it.println() } == 288)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
