package cc.colorcat.netbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class ConnectInterceptor(private val netBird: KNetBird) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val conn = chain.connection()
        val request = chain.request()
        conn.connect(netBird, request)
        conn.writeHeaders(request.headers)
        val method = request.method
        if (method.needBody()) {
            val body = request.requestBody
                    ?: throw IllegalArgumentException("method ${method.name} must have a request body")
            conn.writeRequestBody(body)
        }
        val headers = conn.responseHeaders()
        val code = conn.responseCode()
        val msg = conn.responseMsg()
        var body: ResponseBody? = null
        if (code == 200) {
            body = conn.responseBody(headers)
        }
        return Response.Builder().code(code).msg(msg).replaceHeaders(headers).responseBody(body).build()
    }
}
