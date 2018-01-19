package cc.colorcat.netbird.platform

import cc.colorcat.netbird.*

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
open class HttpConnection : Connection {
    override fun connect(netBird: KNetBird, request: Request) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeHeaders(headers: Headers) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeRequestBody(requestBody: RequestBody) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseCode(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseMsg(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseHeaders(): Headers {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseBody(headers: Headers): ResponseBody? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clone(): Connection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}