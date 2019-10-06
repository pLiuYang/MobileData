package com.assignment.mobiledata.data

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLStreamHandler

/**
 * [URLStreamHandler] that allows us to control the [HttpURLConnection][HttpURLConnection] that
 * are returned by [URLs][URL] in the code under test.
 */
class HttpUrlStreamHandler : URLStreamHandler() {

    private var connections: MutableMap<URL, HttpURLConnection> = HashMap()

    @Throws(IOException::class)
    override fun openConnection(url: URL): HttpURLConnection? {
        return connections[url]
    }

    fun resetConnections() {
        connections = HashMap()
    }

    fun addConnection(url: URL, urlConnection: HttpURLConnection): HttpUrlStreamHandler {
        connections[url] = urlConnection
        return this
    }
}
