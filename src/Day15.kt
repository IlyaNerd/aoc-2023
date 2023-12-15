import Day15.Operation

fun main() {
    fun String.buildHash(): Int {
        var sum = 0
        this.forEach { c ->
            sum += c.code
            sum *= 17
            sum = sum.rem(256)
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        return input
            .flatMap { it.split(',') }
            .sumOf { str ->
                str.buildHash()
            }
    }

    data class Step(val label: String, val op: Operation, val hash: Int)

    fun String.toStep(): Step {
        return if (this.contains('-')) {
            val label = this.substringBefore('-')
            Step(label, Operation.Minus, label.buildHash())
        } else if (contains('=')) {
            val (label, value) = this.split("=")
            Step(label, Operation.Assign(value.toInt()), label.buildHash())
        } else error("smth gone wrong $this")
    }

    fun part2(input: List<String>): Int {
        return input
            .flatMap { it.split(',') }
            .map { str ->
                str.toStep()
            }
            .groupBy { it.hash }
            .mapValues { (_, steps) ->
                val newSteps = mutableListOf<Step>()
                steps.forEach { step ->
                    when (step.op) {
                        is Operation.Assign -> {
                            val index = newSteps.indexOfFirst { it.label == step.label }
                            if (index == -1) {
                                newSteps.add(step)
                            } else {
                                newSteps[index] = step
                            }
                        }

                        Operation.Minus -> newSteps.removeIf { it.label == step.label }
                    }
                }
                newSteps.toList()
            }
            .map { (key, steps) ->
                steps.mapIndexed { i, step ->
                    (key + 1) * (i + 1) * (step.op as Operation.Assign).num
                }.sum()
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part2(testInput).also { it.println() } == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}

private object Day15 {
    sealed interface Operation {
        data object Minus : Operation
        data class Assign(val num: Int) : Operation
    }
}
