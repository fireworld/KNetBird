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

    internal fun executed(call: RealCall): Boolean = runningSyncCalls.add(call)

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
                executor.execute(call)
                if (runningAsyncCalls.size >= maxRunning) return
            } else {
                onDuplicateRequest(call)
            }
            call = waitingAsyncCalls.poll()
        }
    }

    private fun onDuplicateRequest(asyncCall: RealCall.AsyncCall) {
        val callback = asyncCall.callback
        callback.onFailure(asyncCall.call, StateIOException.duplicateRequest)
        callback.onFinish()
    }

    internal fun finished(call: RealCall) {
        runningSyncCalls.remove(call)
    }

    internal fun finished(call: RealCall.AsyncCall) {
        runningAsyncCalls.remove(call)
        promoteCalls()
    }

    internal fun cancelWaiting(tag: Any) {
//        val itr = waitingAsyncCalls.iterator()
//        while (itr.hasNext()) {
//            if (itr.next().request.tag == tag) {
//                itr.remove()
//            }
//        }
        waitingAsyncCalls.removeIf { it.request.tag == tag }
    }

    internal fun cancelAll(tag: Any) {
        cancelWaiting(tag)
        runningAsyncCalls
                .filter { it.request.tag == tag }
                .forEach { it.call.cancel() }
        runningSyncCalls
                .filter { it.request.tag == tag }
                .forEach { it.cancel() }
    }

    internal fun cancelAll() {
        waitingAsyncCalls.clear()
        runningAsyncCalls.forEach { it.call.cancel() }
        runningSyncCalls.forEach { it.cancel() }
    }
}
