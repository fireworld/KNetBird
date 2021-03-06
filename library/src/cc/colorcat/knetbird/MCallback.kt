package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.StateIOException
import cc.colorcat.knetbird.internal.isTargetThread
import cc.colorcat.knetbird.internal.onTargetThread
import java.io.IOException

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
internal class MCallback<T> internal constructor(
        private val parser: Parser<T>,
        private val listener: MRequest.Listener<T>
) : Callback {
    private lateinit var data: NetworkData<T>

    override fun onStart() {
        if (isTargetThread()) {
            listener.onStart()
        } else {
            onTargetThread(Runnable { listener.onStart() })
        }
    }

    @Throws(IOException::class)
    override fun onResponse(call: Call, response: Response) {
        if (response.code == 200 && response.responseBody != null) {
            data = parser.parse(response)
        }
        if (!this::data.isInitialized) {
            data = NetworkData.newFailure(response.code, response.msg)
        }
    }

    override fun onFailure(call: Call, cause: StateIOException) {
        data = NetworkData.newFailure(cause.state, cause.message ?: "")
    }

    override fun onFinish() {
        if (isTargetThread()) {
            deliverData()
        } else {
            onTargetThread(Runnable { deliverData() })
        }
    }

    private fun deliverData() {
        val result = data.data
        if (result != null) {
            listener.onSuccess(result)
        } else {
            listener.onFailure(data.code, data.msg)
        }
        listener.onFinish()
    }
}
