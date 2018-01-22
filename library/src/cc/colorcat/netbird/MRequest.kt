package cc.colorcat.netbird

import cc.colorcat.netbird.internal.EmptyListener
import java.io.File

/**
 * Created by cxx on 18-1-17.
 * xx.ch@outlook.com
 */
class MRequest<T> internal constructor(builder: Builder<T>) : Request(builder) {
    val parser: Parser<T> = builder.parser
    val listener: Listener<T> = builder.listener

    override fun newBuilder(): MRequest.Builder<T> {
        if (freeze) {
            throw IllegalStateException("The request has been frozen, call freeze to check.")
        }
        return Builder(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MRequest<*>

        if (parser != other.parser) return false
        if (listener != other.listener) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + parser.hashCode()
        result = 31 * result + listener.hashCode()
        return result
    }

    override fun toString(): String {
        return "MRequest(parser=$parser, listener=$listener)"
    }


    interface Listener<in T> {

        fun onStart()

        fun onSuccess(result: T)

        fun onFailure(code: Int, msg: String)

        fun onFinish()
    }

    class Builder<T> : Request.Builder {
        var parser: Parser<T>
            private set

        var listener: Listener<T>
            private set

        constructor(parser: Parser<T>) : super() {
            this.parser = parser
            this.listener = EmptyListener
        }

        internal constructor(request: MRequest<T>) : super(request) {
            this.parser = request.parser
            this.listener = request.listener
        }

        fun parser(parser: Parser<T>): Builder<T> {
            this.parser = parser
            return this
        }

        fun listener(listener: Listener<T>): Builder<T> {
            this.listener = listener
            return this
        }

        override fun url(url: String): Builder<T> {
            super.url(url)
            return this
        }

        override fun path(path: String): Builder<T> {
            super.path(path)
            return this
        }

        override fun method(method: Method): Builder<T> {
            super.method(method)
            return this
        }

        override fun get(): Builder<T> {
            super.get()
            return this
        }

        override fun head(): Builder<T> {
            super.head()
            return this
        }

        override fun trace(): Builder<T> {
            super.trace()
            return this
        }

        override fun options(): Builder<T> {
            super.options()
            return this
        }

        override fun post(): Builder<T> {
            super.post()
            return this
        }

        override fun put(): Builder<T> {
            super.put()
            return this
        }

        override fun delete(): Builder<T> {
            super.delete()
            return this
        }

        override fun replace(parameters: Parameters): Builder<T> {
            super.replace(parameters)
            return this
        }

        override fun add(parameters: Parameters): Builder<T> {
            super.add(parameters)
            return this
        }

        override fun add(name: String, value: String): Builder<T> {
            super.add(name, value)
            return this
        }

        override fun addIfNot(name: String, value: String): Builder<T> {
            super.addIfNot(name, value)
            return this
        }

        override fun addAll(names: List<String>, values: List<String>): Builder<T> {
            super.addAll(names, values)
            return this
        }

        override fun set(name: String, value: String): Builder<T> {
            super.set(name, value)
            return this
        }

        override fun remove(name: String): Builder<T> {
            super.remove(name)
            return this
        }

        override fun clear(): Builder<T> {
            super.clear()
            return this
        }

        override fun addFile(name: String, contentType: String, file: File): Builder<T> {
            super.addFile(name, contentType, file)
            return this
        }

        override fun addFile(name: String, contentType: String, file: File, listener: UploadListener?): Builder<T> {
            super.addFile(name, contentType, file, MUploadListener.wrap(listener))
            return this
        }

        override fun clearFile(): Builder<T> {
            super.clearFile()
            return this
        }

        override fun replaceHeaders(headers: Headers): Builder<T> {
            super.replaceHeaders(headers)
            return this
        }

        override fun addHeader(headers: Headers): Builder<T> {
            super.addHeader(headers)
            return this
        }

        override fun addHeader(name: String, value: String): Builder<T> {
            super.addHeader(name, value)
            return this
        }

        override fun addHeaderIfNot(name: String, value: String): Builder<T> {
            super.addHeaderIfNot(name, value)
            return this
        }

        override fun addAllHeader(names: List<String>, values: List<String>): Builder<T> {
            super.addAllHeader(names, values)
            return this
        }

        override fun setHeader(name: String, value: String): Builder<T> {
            super.setHeader(name, value)
            return this
        }

        override fun removeHeader(name: String): Builder<T> {
            super.removeHeader(name)
            return this
        }

        override fun clearHeaders(): Builder<T> {
            super.clearHeaders()
            return this
        }

        override fun downloadListener(listener: DownloadListener?): Builder<T> {
            super.downloadListener(MDownloadListener.wrap(listener))
            return this
        }

        override fun build(): MRequest<T> {
            return MRequest(this)
        }
    }
}
