package cc.colorcat.knetbird.internal

import cc.colorcat.knetbird.HttpStatus
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

    internal companion object {
        internal val ConnectError by lazy {
            StateIOException(HttpStatus.MSG_CONNECT_ERROR, HttpStatus.CODE_CONNECT_ERROR)
        }

        internal val DuplicateRequest by lazy {
            StateIOException(HttpStatus.MSG_DUPLICATE_REQUEST, HttpStatus.CODE_DUPLICATE_REQUEST)
        }

        internal val RequestCanceled by lazy {
            StateIOException(HttpStatus.MSG_REQUEST_CANCELED, HttpStatus.CODE_REQUEST_CANCELED)
        }
    }
}
