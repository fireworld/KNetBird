package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.*
import java.io.File

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
open class Request protected constructor(builder: Builder) {
    val url = builder.url
    val path = builder.path
    val method = builder.method
    val parameters = builder.parameters
    val fileBodies = builder.fileBodies
    val headers = builder.headers
    val downloadListener = builder.downloadListener
    val tag = builder.tag
    internal val boundary = builder.boundary
    internal val requestBody: RequestBody? by lazy {
        buildRequestBody(parameters, fileBodies, boundary)
    }
    var freeze = false
        private set

    internal fun freeze(): Request {
        this.freeze = true
        return this
    }

    internal fun unfreeze(): Request {
        this.freeze = false
        return this
    }

    open fun newBuilder(): Builder {
        if (freeze) {
            throw IllegalStateException("The request has been frozen, call freeze to check.")
        }
        return Builder(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Request

        if (url != other.url) return false
        if (path != other.path) return false
        if (method != other.method) return false
        if (parameters != other.parameters) return false
        if (fileBodies != other.fileBodies) return false
        if (headers != other.headers) return false
        if (downloadListener != other.downloadListener) return false
        if (tag != other.tag) return false
        if (boundary != other.boundary) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + parameters.hashCode()
        result = 31 * result + fileBodies.hashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + (downloadListener?.hashCode() ?: 0)
        result = 31 * result + tag.hashCode()
        result = 31 * result + boundary.hashCode()
        return result
    }

    override fun toString(): String {
        return "Request(url='$url', path='$path', method=$method, parameters=$parameters, fileBodies=$fileBodies, headers=$headers, downloadListener=$downloadListener, tag=$tag, boundary='$boundary', freeze=$freeze)"
    }


    open class Builder {
        var url: String
            private set
        var path: String
            private set
        var method: Method
            private set
        private val _parameters: MutableParameters
        val parameters
            get() = _parameters.toParameters()
        private val _fileBodies: MutableList<FileBody>
        val fileBodies
            get() = _fileBodies.toImmutableList()
        private val _headers: MutableHeaders
        val headers
            get() = _headers.toHeaders()
        var downloadListener: DownloadListener?
            private set
        internal lateinit var boundary: String
            private set
        var tag: Any
            private set
        internal val requestBody: RequestBody? by lazy {
            buildRequestBody(parameters, fileBodies, boundary)
        }

        constructor() {
            this.url = ""
            this.path = ""
            this.method = Method.GET
            this._parameters = mutableParametersOf(6)
            this._fileBodies = ArrayList(1)
            this._headers = mutableHeadersOf(6)
            this.downloadListener = null
            this.boundary = "==${System.currentTimeMillis()}=="
            this.tag = this.boundary
        }

        internal constructor(request: Request) {
            this.url = request.url
            this.path = request.path
            this.method = request.method
            this._parameters = request.parameters.toMutableParameters()
            this._fileBodies = request.fileBodies.toMutableList()
            this._headers = request.headers.toMutableHeaders()
            this.downloadListener = request.downloadListener
            this.boundary = request.boundary
            this.tag = request.tag
        }

        fun names() = _parameters.names()

        fun values() = _parameters.values()

        fun value(name: String) = _parameters.value(name)

        fun value(name: String, defaultValue: String = "") = _parameters.value(name, defaultValue)

        fun values(name: String) = _parameters.values(name)

        fun headerNames() = _headers.names()

        fun headerValues() = _headers.values()

        fun headerValue(name: String) = _headers.value(name)

        fun headerValue(name: String, defaultValue: String) = _headers.value(name, defaultValue)

        fun headerValues(name: String) = _headers.values(name)

        open fun url(url: String): Builder {
            this.url = checkedHttpUrl(url)
            return this
        }

        open fun path(path: String): Builder {
            this.path = path
            return this
        }

        open fun method(method: Method): Builder {
            this.method = method
            return this
        }

        open fun get(): Builder {
            this.method = Method.GET
            return this
        }

        open fun head(): Builder {
            this.method = Method.HEAD
            return this
        }

        open fun trace(): Builder {
            this.method = Method.TRACE
            return this
        }

        open fun options(): Builder {
            this.method = Method.OPTIONS
            return this
        }

        open fun post(): Builder {
            this.method = Method.POST
            return this
        }

        open fun put(): Builder {
            this.method = Method.PUT
            return this
        }

        open fun delete(): Builder {
            this.method = Method.DELETE
            return this
        }

        open fun replace(parameters: Parameters): Builder {
            this._parameters.clear()
            this._parameters.addAll(parameters.names(), parameters.values())
            return this
        }

        open fun add(parameters: Parameters): Builder {
            this._parameters.addAll(parameters.names(), parameters.values())
            return this
        }

        open fun add(name: String, value: String): Builder {
            _parameters.add(name, value)
            return this
        }

        open fun addIfNot(name: String, value: String): Builder {
            _parameters.addIfNot(name, value)
            return this
        }

        open fun addAll(names: List<String>, values: List<String>): Builder {
            _parameters.addAll(names, values)
            return this
        }

        open fun set(name: String, value: String): Builder {
            _parameters.set(name, value)
            return this
        }

        open fun remove(name: String): Builder {
            _parameters.removeAll(name)
            return this
        }

        open fun clear(): Builder {
            _parameters.clear()
            return this
        }

        open fun addFile(name: String, contentType: String, file: File) = addFile(name, contentType, file, null)

        open fun addFile(name: String, contentType: String, file: File, listener: UploadListener?): Builder {
            _fileBodies.add(FileBody.create(name, contentType, file, listener))
            return this
        }

        open fun clearFile(): Builder {
            _fileBodies.clear()
            return this
        }

        open fun replaceHeaders(headers: Headers): Builder {
            this._headers.clear()
            this._headers.addAll(headers.names(), headers.values())
            return this
        }

        open fun addHeader(headers: Headers): Builder {
            this._headers.addAll(headers.names(), headers.values())
            return this
        }

        open fun addHeader(name: String, value: String): Builder {
            checkHeader(name, value)
            _headers.add(name, value)
            return this
        }

        open fun addHeaderIfNot(name: String, value: String): Builder {
            checkHeader(name, value)
            _headers.addIfNot(name, value)
            return this
        }

        open fun addAllHeader(names: List<String>, values: List<String>): Builder {
            _headers.addAll(names, values)
            return this
        }

        open fun setHeader(name: String, value: String): Builder {
            checkHeader(name, value)
            _headers.set(name, value)
            return this
        }

        open fun removeHeader(name: String): Builder {
            _headers.removeAll(name)
            return this
        }

        open fun clearHeaders(): Builder {
            _headers.clear()
            return this
        }

        open fun downloadListener(listener: DownloadListener?): Builder {
            this.downloadListener = listener
            return this
        }

        open fun build() = Request(this)
    }
}
