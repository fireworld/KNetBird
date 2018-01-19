package cc.colorcat.netbird.internal

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class StateIOException : IOException {
    val state: Int

    constructor(detailMessage: String, state: Int) : super(detailMessage) {
        this.state = state
    }

    constructor(detailMessage: String, state: Int, cause: Throwable) : super(detailMessage, cause) {
        this.state = state
    }

    constructor(state: Int, cause: Throwable) : super(cause) {
        this.state = state
    }

    override fun toString(): String {
        return "StateIOException(state=$state, detail=$message, cause=$cause)"
    }
}