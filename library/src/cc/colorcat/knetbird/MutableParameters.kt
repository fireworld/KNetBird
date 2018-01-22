package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.MutablePair
import cc.colorcat.knetbird.internal.NameAndValue
import cc.colorcat.knetbird.internal.PairWriter

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
class MutableParameters internal constructor(override val pair: MutablePair) : Parameters(pair), PairWriter by pair {

    fun toParameters() = Parameters(pair.toPair())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MutableParameters

        if (pair != other.pair) return false

        return true
    }

    override fun iterator(): MutableIterator<NameAndValue> {
        return pair.iterator()
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pair.hashCode()
        return result
    }

    override fun toString(): String {
        return "MutableParameters(pair=$pair)"
    }
}
