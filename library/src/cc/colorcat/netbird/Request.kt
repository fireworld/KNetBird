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
        private val bParameters: MutableParameters
        val parameters
            get() = bParameters.toParameters()
        private val bFileBodies: MutableList<FileBody>
        val fileBodies
            get() = bFileBodies.toImmutableList()
        private val bHeaders: MutableHeaders
        val headers
            get() = bHeaders.toHeaders()
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
            this.bParameters = mutableParametersOf(6)
            this.bFileBodies = ArrayList(1)
            this.bHeaders = mutableHeadersOf(6)
            this.downloadListener = null
            this.boundary = "==${System.currentTimeMillis()}=="
            this.tag = this.boundary
        }

        protected constructor(request: Request) {
            this.url = request.url
            this.path = request.path
            this.method = request.method
            this.bParameters = request.parameters.toMutableParameters()
            this.bFileBodies = request.fileBodies.toMutableList()
            this.bHeaders = request.headers.toMutableHeaders()
            this.downloadListener = request.downloadListener
            this.boundary = request.boundary
            this.tag = request.tag
        }

        fun names() = bParameters.names()

        fun values() = bParameters.values()

        fun value(name: String) = bParameters.value(name)

        fun value(name: String, defaultValue: String = "") = bParameters.value(name, defaultValue)

        fun values(name: String) = bParameters.values(name)

        fun headerNames() = bHeaders.names()

        fun headerValues() = bHeaders.values()

        fun headerValue(name: String) = bHeaders.value(name)

        fun headerValue(name: String, defaultValue: String) = bHeaders.value(name, defaultValue)

        fun headerValues(name: String) = bHeaders.values(name)

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
            this.bParameters.clear()
            this.bParameters.addAll(parameters.names(), parameters.values())
            return this
        }

        open fun add(parameters: Parameters): Builder {
            this.bParameters.addAll(parameters.names(), parameters.values())
            return this
        }

        open fun add(name: String, value: String): Builder {
            bParameters.add(name, value)
            return this
        }

        open fun addIfNot(name: String, value: String): Builder {
            bParameters.addIfNot(name, value)
            return this
        }

        open fun addAll(names: List<String>, values: List<String>): Builder {
            bParameters.addAll(names, values)
            return this
        }

        open fun set(name: String, value: String): Builder {
            bParameters.set(name, value)
            return this
        }

        open fun remove(name: String): Builder {
            bParameters.removeAll(name)
            return this
        }

        open fun clear(): Builder {
            bParameters.clear()
            return this
        }

        open fun addFile(name: String, contentType: String, file: File) = addFile(name, contentType, file, null)

        open fun addFile(name: String, contentType: String, file: File, listener: UploadListener?): Builder {
            bFileBodies.add(FileBody.create(name, contentType, file, listener))
            return this
        }

        open fun clearFile(): Builder {
            bFileBodies.clear()
            return this
        }

        open fun replaceHeaders(headers: Headers): Builder {
            this.bHeaders.clear()
            this.bHeaders.addAll(headers.names(), headers.values())
            return this
        }

        open fun addHeader(headers: Headers): Builder {
            this.bHeaders.addAll(headers.names(), headers.values())
            return this
        }

        open fun addHeader(name: String, value: String): Builder {
            checkHeader(name, value)
            bHeaders.add(name, value)
            return this
        }

        open fun addHeaderIfNot(name: String, value: String): Builder {
            checkHeader(name, value)
            bHeaders.addIfNot(name, value)
            return this
        }

        open fun addAllHeader(names: List<String>, values: List<String>): Builder {
            bHeaders.addAll(names, values)
            return this
        }

        open fun setHeader(name: String, value: String): Builder {
            checkHeader(name, value)
            bHeaders.set(name, value)
            return this
        }

        open fun removeHeader(name: String): Builder {
            bHeaders.removeAll(name)
            return this
        }

        open fun clearHeaders(): Builder {
            bHeaders.clear()
            return this
        }

        open fun build() = Request(this)
    }
}
