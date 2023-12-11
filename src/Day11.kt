import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    data class Point(val x: Int, val y: Int) : Comparable<Point> {
        override fun compareTo(other: Point): Int = compareBy<Point>({ it.x }, { it.y }).compare(this, other)
    }

    fun List<String>.expand(): List<String> {
        val lines = this.flatMap { line ->
            if (line.all { it == '.' }) {
                listOf(line, line)
            } else listOf(line)
        }
        val columns = mutableListOf<Int>()
        var count = 0
        (0..<lines.first().length).forEach { j ->
            if (lines.map { it[j] }.all { it == '.' }) {
                columns.add(
                    if (j == 0) 0
                    else j + count++
                )
            }
        }
        return lines.map { it.toMutableList() }
            .onEach { line ->
                columns.forEach { j ->
                    line.add(j, '.')
                }
            }
            .map { String(it.toCharArray()) }
    }

    fun List<String>.findGalaxies() = this.mapIndexed { i, line ->
        line.mapIndexed { j, c ->
            if (c == '#') Point(i, j) else null
        }.filterNotNull()
    }.flatten()

    fun List<Point>.toPairs(): List<Pair<Point, Point>> = this.flatMap { p1 ->
        this.mapNotNull { p2 ->
            if (p1 > p2) p2 to p1
            else if (p1 == p2) null
            else p1 to p2
        }
    }.distinct()

    fun Pair<Point, Point>.distance(): Int {
        val lx = first.x
        val ly = first.y

        val rx = second.x
        val ry = second.y

        return when {
            lx == rx -> ry - ly
            ly == ry -> rx - lx
            (lx - rx).absoluteValue == 1 -> 1 + (ry - ly).absoluteValue
            (ly - ry).absoluteValue == 1 -> 1 + (rx - lx).absoluteValue
            else -> {
                val xLen = (rx - lx).absoluteValue
                val yLen = (ry - ly).absoluteValue
                xLen + yLen
            }
        }.absoluteValue
    }

    fun part1(input: List<String>): Int {
        return input
            .expand()
            .onEach { it.println() }
            .findGalaxies()
            .toPairs()
            .sumOf { points -> points.distance().also { println(points to it) } }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput).also { it.println() } == 374)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

