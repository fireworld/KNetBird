package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.MutablePair
import cc.colorcat.knetbird.internal.NameAndValue
import cc.colorcat.knetbird.internal.PairWriter

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
class MutableHeaders internal constructor(override val pair: MutablePair) : Headers(pair), PairWriter by pair {

    fun toHeaders() = Headers(pair.toPair())

    override fun iterator(): MutableIterator<NameAndValue> {
        return pair.iterator()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MutableHeaders

        if (pair != other.pair) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pair.hashCode()
        return result
    }
}
