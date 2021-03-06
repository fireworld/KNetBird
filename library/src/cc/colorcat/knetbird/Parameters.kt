package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.Pair
import cc.colorcat.knetbird.internal.PairReader

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
open class Parameters internal constructor(protected open val pair: Pair) : PairReader by pair {

    companion object {
        internal val emptyParameters = Parameters(Pair.emptyPair)
    }

    fun toMutableParameters() = MutableParameters(pair.toMutablePair())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Parameters

        if (pair != other.pair) return false

        return true
    }

    override fun hashCode(): Int {
        return pair.hashCode()
    }

    final override fun toString(): String {
        return "${javaClass.simpleName}(${pair.string(", ")})"
    }
}
