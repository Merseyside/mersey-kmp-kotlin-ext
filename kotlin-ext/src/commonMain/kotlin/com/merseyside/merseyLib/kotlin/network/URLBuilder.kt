package com.merseyside.merseyLib.kotlin.network

class URLBuilder : Appendable {

    var protocol: String = "http"
    var host: String = ""
    var method: String = ""
    private var queryParams: MutableList<Pair<String, String>> = mutableListOf()

    private val stringBuilder = StringBuilder()

    private fun appendProtocol() {
        requireNotEmpty(protocol, "protocol") {
            val formattedProtocol = "$protocol://"
            stringBuilder.insert(0, formattedProtocol)
        }
    }

    private fun appendMethod() {
        if (method.isNotEmpty()) append("/").append(method)
    }

    private fun appendHost() {
        requireNotEmpty(host, "host") {
            append(host)
        }
    }

    private fun appendParams() {
        if (queryParams.isNotEmpty()) {
            append("?")
            queryParams.forEachIndexed { index, (key, value) ->
                if (index != 0) append("&")
                append(key).append("=").append(value)
            }
        }
    }

    fun queryParam(key: String, param: String) {
        if (key.isNotEmpty() && param.isNotEmpty()) {
            queryParams.add(key to param)
        } else throw IllegalArgumentException("Key or param is empty!")
    }

    fun build(): String {
        appendProtocol()
        appendHost()
        appendMethod()
        appendParams()

        return stringBuilder.toString()
    }

    override fun append(value: Char): Appendable {
        stringBuilder.append(value)
        return this
    }

    override fun append(value: CharSequence?): Appendable {
        stringBuilder.append(value)
        return this
    }

    override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Appendable {
        stringBuilder.append(value)
        return this
    }

    private fun requireNotEmpty(value: String, part: String, block: (String) -> Unit) {
        if (value.isNotEmpty()) block(value)
        else throw IllegalArgumentException("$part is empty!")
    }
}