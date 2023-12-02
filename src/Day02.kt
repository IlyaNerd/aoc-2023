fun main() {
    data class CubeSet(
        val blue: Int,
        val green: Int,
        val red: Int,
    )

    val totalCubes = CubeSet(
        red = 12,
        green = 13,
        blue = 14,
    )

    data class Game(
        val index: Int,
        val sets: List<CubeSet>
    )

    fun String.toGameSet(): CubeSet {
        val regex = Regex("(\\d+) (blue|green|red)")
        val set = this.split(",")
            .map {
                val groupValues = regex.find(it.trim())?.groupValues ?: error("No matches in $this")
                val number = groupValues[1].toInt()
                val color = groupValues[2]
                number to color
            }
        return CubeSet(
            blue = set.firstOrNull { it.second == "blue" }?.first ?: 0,
            green = set.firstOrNull { it.second == "green" }?.first ?: 0,
            red = set.firstOrNull { it.second == "red" }?.first ?: 0,
        )
    }

    fun String.toGame(): Game {
        val indexRegex = Regex("Game (\\d+): ")
        val index = indexRegex.find(this)?.groupValues?.get(1)?.toInt() ?: error("No index in $this")
        val sets = this.replaceFirst(indexRegex, "")
            .split(";")
            .map { it.toGameSet() }
        return Game(index, sets)
    }

    fun Game.isValid(): Boolean {
        return this.sets.all {
            it.red <= totalCubes.red && it.green <= totalCubes.green && it.blue <= totalCubes.blue
        }
    }

    fun part1(input: List<String>): Int {
        return input
            .map { line -> line.toGame() }
            .filter { it.isValid() }
            .sumOf { it.index }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line -> line.toGame() }
            .sumOf { game ->
                if (game.sets.isEmpty()) 0
                else {
                    game.sets.maxOf { it.blue } *
                            game.sets.maxOf { it.green } *
                            game.sets.maxOf { it.red }
                }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput).also { it.println() } == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
