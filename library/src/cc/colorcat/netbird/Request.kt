package cc.colorcat.netbird

import cc.colorcat.netbird.internal.*
import java.io.File

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
open class Request protected constructor(builder: Builder) {
    val url = builder.url()
    val path = builder.path()
    val method = builder.method()
    val parameters = builder.parameters()
    val fileBodies = builder.fileBodies()
    val headers = builder.headers()
    val downloadListener = builder.downloadListener()
    val tag = builder.tag()
    internal val boundary = builder.boundary()
    private var body: RequestBody? = null
    private var freeze = false

    val isFreeze
        get() = freeze

    internal fun freeze(): Request {
        this.freeze = true
        return this
    }

    internal fun unfreeze(): Request {
        this.freeze = false
        return this
    }

    internal fun body(): RequestBody? {
        if (body == null) {
            body = parseBody()
        }
        return body
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
        private var url: String
        private var path: String
        private var method: Method
        private val parameters: MutableParameters
        private val fileBodies: MutableList<FileBody>
        private val headers: MutableHeaders
        private var downloadListener: DownloadListener?
        private val boundary: String
        private var tag: Any

        companion object {
            internal fun newBuilder(request: Request) = Builder(request)
        }

        constructor() {
            this.url = ""
            this.path = ""
            this.method = Method.GET
            this.parameters = mutableParametersOf(6)
            this.fileBodies = ArrayList(1)
            this.headers = mutableHeadersOf(6)
            this.downloadListener = null
            this.boundary = "==${System.currentTimeMillis()}=="
            this.tag = this.boundary
        }

        protected constructor(request: Request) {
            this.url = request.url
            this.path = request.path
            this.method = request.method
            this.parameters = request.parameters.toMutableParameters()
            this.fileBodies = request.fileBodies.toMutableList()
            this.headers = request.headers.toMutableHeaders()
            this.downloadListener = request.downloadListener
            this.boundary = request.boundary
            this.tag = request.tag
        }

        fun url() = url

        fun path() = path

        fun method() = method

        fun parameters() = parameters.toParameters()

        fun headers() = headers.toHeaders()

        fun fileBodies() = fileBodies.toImmutableList()

        fun downloadListener() = downloadListener

        fun tag() = tag

        fun names() = parameters.names()

        fun values() = parameters.values()

        fun value(name: String) = parameters.value(name)

        fun value(name: String, defaultValue: String = "") = parameters.value(name, defaultValue)

        fun headerNames() = headers.names()

        fun headerValues() = headers.values()

        fun headerValue(name: String) = headers.value(name)

        fun headerValue(name: String, defaultValue: String) = headers.value(name, defaultValue)

        fun headerValues(name: String) = headers.values(name)

        internal fun boundary() = boundary

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
            this.parameters.clear()
            this.parameters.addAll(parameters.names(), parameters.values())
            return this
        }

        open fun add(parameters: Parameters): Builder {
            this.parameters.addAll(parameters.names(), parameters.values())
            return this
        }

        open fun add(name: String, value: String): Builder {
            parameters.add(name, value)
            return this
        }

        open fun addIfNot(name: String, value: String): Builder {
            parameters.addIfNot(name, value)
            return this
        }

        open fun addAll(names: List<String>, values: List<String>): Builder {
            parameters.addAll(names, values)
            return this
        }

        open fun set(name: String, value: String): Builder {
            parameters.set(name, value)
            return this
        }

        open fun remove(name: String): Builder {
            parameters.removeAll(name)
            return this
        }

        open fun clear(): Builder {
            parameters.clear()
            return this
        }

        open fun addFile(name: String, contentType: String, file: File) = addFile(name, contentType, file, null)

        open fun addFile(name: String, contentType: String, file: File, listener: UploadListener?): Builder {
            fileBodies.add(FileBody.create(name, contentType, file, listener))
            return this
        }

        open fun clearFile(): Builder {
            fileBodies.clear()
            return this
        }

        open fun replaceHeaders(headers: Headers): Builder {
            this.headers.clear()
            this.headers.addAll(headers.names(), headers.values())
            return this
        }

        open fun addHeader(headers: Headers): Builder {
            this.headers.addAll(headers.names(), headers.values())
            return this
        }

        open fun addHeader(name: String, value: String): Builder {
            checkHeader(name, value)
            headers.add(name, value)
            return this
        }

        open fun addHeaderIfNot(name: String, value: String): Builder {
            checkHeader(name, value)
            headers.addIfNot(name, value)
            return this
        }

        open fun addAllHeader(names: List<String>, values: List<String>): Builder {
            headers.addAll(names, values)
            return this
        }

        open fun setHeader(name: String, value: String): Builder {
            checkHeader(name, value)
            headers.set(name, value)
            return this
        }

        open fun removeHeader(name: String): Builder {
            headers.removeAll(name)
            return this
        }

        open fun clearHeaders(): Builder {
            headers.clear()
            return this
        }

        open fun build() = Request(this)
    }
}
