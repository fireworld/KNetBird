package cc.colorcat.netbird

import cc.colorcat.netbird.internal.mutableHeadersOf
import cc.colorcat.netbird.internal.mutableParametersOf
import cc.colorcat.netbird.internal.toImmutableList

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
open class Request(builder: Builder) {
    val url = builder.url()
    val path = builder.path()
    val parameters = builder.parameters()
    val headers = builder.headers()
    val fileBodies = builder.fileBodies()
    val method = builder.method()
    internal val boundary = builder.boundary()
    val downloadListener = builder.downloadListener()
    val tag = builder.tag()
    private var body: RequestBody? = null
    private var freeze = false

    open class Builder {
        private var url: String
        private var path: String
        private val parameters: MutableParameters
        private val headers: MutableHeaders
        private val fileBodies: MutableList<FileBody>
        private var method: Method
        private var boundary: String
        private var downloadListener: DownloadListener? = null
        private var tag: Any

        constructor() {
            this.url = ""
            this.path = ""
            this.parameters = mutableParametersOf(6)
            this.headers = mutableHeadersOf(6)
            this.fileBodies = ArrayList(1)
            this.method = Method.GET
            this.boundary = "==${System.currentTimeMillis()}=="
            this.tag = this.boundary
        }

        protected constructor(request: Request) {
            this.url = request.url
            this.path = request.path
            this.parameters = request.parameters.toMutableParameters()
            this.headers = request.headers.toMutableHeaders()
            this.fileBodies = request.fileBodies.toMutableList()
            this.method = request.method
            this.boundary = request.boundary
            this.downloadListener = request.downloadListener
            this.tag = request.tag
        }

        fun url() = url

        fun path() = path

        internal fun parameters() = parameters.toParameters()

        internal fun headers() = headers.toHeaders()

        internal fun fileBodies() = fileBodies.toImmutableList()

        fun method() = method

        internal fun boundary() = boundary

        internal fun downloadListener() = downloadListener

        fun tag() = tag
    }
}