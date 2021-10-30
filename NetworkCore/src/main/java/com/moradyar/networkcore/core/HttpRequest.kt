package com.moradyar.networkcore.core

class HttpRequest {

    var endPoint: String = ""
        private set
    var headers: HashMap<String, String> = hashMapOf()
        private set
    var parameters: HashMap<String, String> = hashMapOf()
        private set
    var useCache: Boolean = false
        private set
    var body: String = ""
        private set
    var bodyMediaType: String = ""
        private set
    var method: Method = Method.GET
        private set

    class Builder(private val endPoint: String) {

        private var headers: HashMap<String, String> = hashMapOf()
        private var parameters: HashMap<String, String> = hashMapOf()
        private var useCache: Boolean = false
        private var body: String = ""
        private var method: Method = Method.GET
        private var bodyMediaType: String = ""

        fun headers(headers: Map<String, String>): Builder {
            headers.entries.forEach {
                this.headers[it.key] = it.value
            }
            return this
        }

        fun queryParameters(queryParameters: Map<String, String>): Builder {
            queryParameters.entries.forEach {
                this.parameters[it.key] = it.value
            }
            return this
        }

        fun useCache(useCache: Boolean): Builder {
            this.useCache = useCache
            return this
        }

        fun body(body: String): Builder {
            this.body = body
            return this
        }

        fun bodyMediaType(bodyMediaType: String): Builder {
            this.bodyMediaType = bodyMediaType
            return this
        }

        fun method(method: Method): Builder {
            this.method = method
            return this
        }

        fun build(): HttpRequest {
            val httpRequest = HttpRequest()
            httpRequest.endPoint = endPoint
            httpRequest.body = body
            httpRequest.headers = headers
            httpRequest.parameters = parameters
            httpRequest.useCache = useCache
            httpRequest.method = method
            httpRequest.bodyMediaType = bodyMediaType
            return httpRequest
        }
    }

    internal fun getQueryParametersString(): String {
        return if (parameters.isEmpty()) {
            ""
        } else {
            parameters.entries.map { entry ->
                "${entry.key}=${entry.value}"
            }.joinToString("&") { it }
        }
    }

    internal fun getEndPointWithQueryParameters(): String {
        val parameters = getQueryParametersString()
        return if (parameters.isEmpty()) {
            endPoint
        } else {
            "$endPoint?$parameters"
        }
    }

    enum class Method {
        GET, POST
    }
}