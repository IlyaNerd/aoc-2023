fun main() {
    data class SeedMap(
        val from: String,
        val fromRanges: List<LongRange>,
        val to: String,
        val toRanges: List<LongRange>,
    )

    fun parseMap(title: String, lines: List<String>): SeedMap {
        val (from, to) = title.removeSuffix(" map:")
            .split("-to-")
            .map { it.trim() }
        val ranges = lines.map { line ->
            line.split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        }.map { (dest, source, count) ->
            LongRange(source, count + source - 1) to LongRange(dest, count + dest - 1)
        }
        return SeedMap(from, ranges.map { it.first }, to, ranges.map { it.second })
    }

    fun List<String>.toSeedMaps(): List<SeedMap> {
        var lineNum = 0
        val groups = mutableListOf<SeedMap>()

        while (lineNum < this.size) {
            val group = this.drop(lineNum)
                .takeWhile { it.isNotBlank() }
            groups.add(parseMap(group.first(), group.drop(1)))
            lineNum += group.count() + 1
        }
        return groups.toList()
    }

    fun getSeedProperty(seedMaps: List<SeedMap>, property: String, seed: Long, next: String): Pair<String, Long> {
        val seedMap = seedMaps.first { it.from == next }
        val seedIndexes = seedMap.fromRanges.map { range ->
            if (range.contains(seed)) (seed - range.first).toInt()
            else -1
        }
        val listIndex = seedIndexes.indexOfFirst { it != -1 }
        val result = if (listIndex == -1) seed
        else {
            val target = seedMap.toRanges[listIndex]
            target.first + seedIndexes[listIndex]
        }
        return if (seedMap.to != property) getSeedProperty(seedMaps, property, result, seedMap.to)
        else seedMap.to to result
    }

    fun part1(input: List<String>): Long {
        val seedsToPlant = input.first { it.startsWith("seeds:") }.let { seeds ->
            seeds.removePrefix("seeds:").split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        }

        val seedMaps = input.drop(2).toSeedMaps()

        return seedsToPlant
            .minOf { seed ->
                getSeedProperty(seedMaps, "location", seed, "seed").second
            }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput).also { it.println() } == 35L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
