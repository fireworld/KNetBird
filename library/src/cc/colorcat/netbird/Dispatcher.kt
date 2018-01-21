package cc.colorcat.netbird

import cc.colorcat.netbird.internal.StateIOException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.ExecutorService

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class Dispatcher {
    private lateinit var executor: ExecutorService
    private var maxRunning: Int = 6
    private val waitingAsyncCalls: Queue<RealCall.AsyncCall> = ConcurrentLinkedQueue()
    private val runningAsyncCalls: MutableSet<RealCall.AsyncCall> = CopyOnWriteArraySet()
    private val runningSyncCalls: MutableSet<RealCall> = CopyOnWriteArraySet()

    @Synchronized
    internal fun setExecutor(executor: ExecutorService) {
        this.executor = executor
    }

    @Synchronized
    internal fun setMaxRunning(maxRunning: Int) {
        this.maxRunning = maxRunning
    }

    internal fun executed(call: RealCall) = runningSyncCalls.add(call)

    internal fun enqueue(call: RealCall.AsyncCall) {
        if (!waitingAsyncCalls.contains(call) && waitingAsyncCalls.offer(call)) {
            promoteCalls()
        } else {
            onDuplicateRequest(call)
        }
    }

    @Synchronized
    private fun promoteCalls() {
        if (runningAsyncCalls.size >= maxRunning) return

        var call: RealCall.AsyncCall? = waitingAsyncCalls.poll()
        while (call != null) {
            if (runningAsyncCalls.add(call)) {
                executor?.execute(call)
                if (runningAsyncCalls.size >= maxRunning) return
            } else {
                onDuplicateRequest(call)
            }
            call = waitingAsyncCalls.poll()
        }
    }

    private fun onDuplicateRequest(call: RealCall.AsyncCall) {
        val callback = call.callback
        callback.onFailure(call.call,
                StateIOException(HttpStatus.MSG_DUPLICATE_REQUEST, HttpStatus.CODE_DUPLICATE_REQUEST))
        callback.onFinish()
    }

    internal fun finished(call: RealCall) {
        runningSyncCalls.remove(call)
    }

    internal fun finished(call: RealCall.AsyncCall) {
        runningAsyncCalls.remove(call)
        promoteCalls()
    }
}