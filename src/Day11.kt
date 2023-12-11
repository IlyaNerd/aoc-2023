import java.util.*
import kotlin.math.absoluteValue

fun main() {
    data class Point(val x: Long, val y: Long) : Comparable<Point> {
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

    fun List<String>.findEmptySpace(): Pair<List<Int>, List<Int>> {
        val rows = this.mapIndexedNotNull { i, s -> if (s.all { c -> c == '.' }) i else null }
        val colNumbers = this.first().toList().indices
        val cols = colNumbers.mapIndexedNotNull { j, col ->
            if (this.all { row -> row[col] == '.' }) j else null
        }
        return rows to cols
    }

    fun List<String>.findGalaxies() = this.mapIndexed { i, line ->
        line.mapIndexed { j, c ->
            if (c == '#') Point(i.toLong(), j.toLong()) else null
        }.filterNotNull()
    }.flatten()

    fun List<Point>.toPairs(): List<Pair<Point, Point>> = this.flatMap { p1 ->
        this.mapNotNull { p2 ->
            if (p1 > p2) p2 to p1
            else if (p1 == p2) null
            else p1 to p2
        }
    }.distinct()

    fun Pair<Point, Point>.distance(): Long {
        val lx = first.x
        val ly = first.y

        val rx = second.x
        val ry = second.y

        val xLen = (rx - lx).absoluteValue
        val yLen = (ry - ly).absoluteValue
        return xLen + yLen
    }

    fun part1(input: List<String>): Long {
        return input
            .expand()
            .onEach { it.println() }
            .findGalaxies()
            .toPairs()
            .sumOf { points -> points.distance() }
    }


    fun Point.adjustBy(emptyRows: List<Int>, emptyCols: List<Int>, multiplier: Int): Point {
        val (x, y) = this
        val rowIndex = emptyRows.indexOfLast { x > it }
        val colIndex = emptyCols.indexOfLast { y > it }

        val newX = if (rowIndex != -1) {
            x + (rowIndex + 1) * (multiplier - 1)
        } else x

        val newY = if (colIndex != -1) {
            y + (colIndex + 1) * (multiplier - 1)
        } else y
        return copy(x = newX, y = newY)
    }

    fun part2(input: List<String>): Long {
        val (emptyRows, emptyCols) = input.findEmptySpace()
        val multiplier = 1000000

        val galaxies = input
            .findGalaxies()
            .map { it.adjustBy(emptyRows, emptyCols, multiplier).also { n -> println(it to n) } }
            .toPairs()
        return galaxies
            .sumOf { points -> points.distance() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part2(testInput).also { it.println() } == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

