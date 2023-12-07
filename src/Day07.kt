val cardMapping = mapOf(
    'J' to 1,
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'Q' to 12,
    'K' to 13,
    'A' to 14,
)

sealed interface CardSet : Comparable<CardSet> {
    val cards: List<Char>

    data class Five(val card: Char, override val cards: List<Char>) : CardSet
    data class Four(val card: Char, val other: Char, override val cards: List<Char>) : CardSet
    data class FH(val three: Char, val two: Char, override val cards: List<Char>) : CardSet
    data class Three(val card: Char, val other: Char, val other2: Char, override val cards: List<Char>) : CardSet
    data class Pairs(val card1: Char, val card2: Char, val other: Char, override val cards: List<Char>) : CardSet
    data class Pair1(val pair: Char, val other: Char, val other2: Char, val other3: Char, override val cards: List<Char>) : CardSet

    data class HighHand(override val cards: List<Char>) : CardSet

    override fun compareTo(other: CardSet): Int {
        return if (this.javaClass != other.javaClass) {
            this.typeIndex().compareTo(other.typeIndex())
        } else {
            compareCards(other)
        }
    }

    private fun typeIndex() = when (this) {
        is Five -> 700
        is Four -> 600
        is FH -> 500
        is Three -> 400
        is Pairs -> 300
        is Pair1 -> 200
        is HighHand -> 100
    }

    private fun compareCards(other: CardSet): Int =
        compareBy(*(0..4).map { i -> { it: CardSet -> cardMapping[it.cards[i]]!! } }.toTypedArray())
            .compare(this, other)

    fun hasJoker() = cards.any { it == 'J' }

    fun withCards(cards: List<Char>) = when(this) {
        is FH -> copy(cards = cards)
        is Five -> copy(cards = cards)
        is Four -> copy(cards = cards)
        is HighHand -> copy(cards = cards)
        is Pair1 -> copy(cards = cards)
        is Pairs -> copy(cards = cards)
        is Three -> copy(cards = cards)
    }
}

data class Hand(
    val cards: String,
    val bid: Int,
) {
    val cardSet: CardSet
        get() {
            val cardSet = calculatedCardSet(cards)
            return if (cardSet.hasJoker()) {
                (cardMapping - 'J')
                    .keys
                    .maxOf { calculatedCardSet(cards.replace('J', it)) }
                    .withCards(cards.toList())
            } else cardSet
        }
}

private fun calculatedCardSet(cards: String): CardSet {
    val listCards = cards.toList()
    val (c1, c2, c3, c4, c5) = listCards.sortedDescending()
    val distinct = cards.toList().distinct()
    return when (distinct.size) {
        1 -> { // all same
            CardSet.Five(c1, listCards)
        }

        2 -> { // FH or 4 + 1
            if (c1 == c2 && c2 == c3 && c3 == c4) { // 4 + 1
                CardSet.Four(c1, c5, listCards)
            } else if (c2 == c3 && c3 == c4 && c4 == c5) { // 1 + 4
                CardSet.Four(c2, c1, listCards)
            } else { // FH
                if (c2 == c3) { // 3 + 2
                    CardSet.FH(c2, c4, listCards)
                } else { // 2 + 3
                    CardSet.FH(c3, c1, listCards)
                }
            }
        }

        3 -> { // 3+ or 2 pairs
            if (c1 == c2 && c2 == c3) { // 3 + 1 + 1
                CardSet.Three(c3, c4, c5, listCards)
            } else if (c2 == c3 && c3 == c4) { // 1 + 3 + 1
                CardSet.Three(c3, c1, c5, listCards)
            } else if (c3 == c4 && c4 == c5) { // 1 + 1 + 3
                CardSet.Three(c3, c1, c2, listCards)
            } else { // 2 pairs
                if (c1 == c2 && c3 == c4) { // 2 + 2 + 1
                    CardSet.Pairs(c1, c3, c5, listCards)
                } else if (c2 == c3 && c4 == c5) { // 1 + 2 + 2
                    CardSet.Pairs(c2, c4, c1, listCards)
                } else { // 2 + 1 + 2
                    CardSet.Pairs(c1, c4, c3, listCards)
                }
            }
        }

        4 -> { // 1 pair
            if (c1 == c2) { // 2+
                CardSet.Pair1(c1, c3, c4, c5, listCards)
            } else if (c2 == c3) { // 1 + 2+
                CardSet.Pair1(c2, c1, c4, c5, listCards)
            } else if (c3 == c4) { // 1 + 1 + 2+
                CardSet.Pair1(c3, c1, c2, c5, listCards)
            } else { // 1 + 1 + 1 + 2
                CardSet.Pair1(c4, c1, c2, c3, listCards)
            }
        }

        5 -> { // high card
            CardSet.HighHand(distinct)
        }

        else -> error("too many elements $cards")
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { line ->
                val (cards, bid) = line.split(' ')
                Hand(cards, bid.toInt())
            }
            .sortedBy { hand ->
                hand.cardSet
            }
            .onEach { println(it to it.cardSet) }
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return part1(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part2(testInput).also { it.println() } == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
