package cc.colorcat.netbird

import cc.colorcat.netbird.internal.*
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
        parseBody()
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

    private fun parseBody(): RequestBody? {
        if (parameters.isEmpty && fileBodies.isEmpty()) {
            return null
        }
        if (parameters.isEmpty && fileBodies.size == 1) {
            return fileBodies[0]
        }
        if (!parameters.isEmpty && fileBodies.isEmpty()) {
            FormBody.create(parameters, true)
        }
        return MultipartBody.create(FormBody.create(parameters, false), fileBodies, boundary)
    }

    open fun newBuilder(): Builder {
        if (freeze) {
            throw IllegalStateException("The request has been frozen, call isFreeze to check.")
        }
        return Builder.newBuilder(this)
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
        internal val boundary: String
        var tag: Any
            private set

        companion object {
            internal fun newBuilder(request: Request) = Builder(request)
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

        protected constructor(request: Request) {
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

        open fun build() = Request(this)
    }
}
