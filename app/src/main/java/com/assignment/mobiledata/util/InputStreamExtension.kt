package com.assignment.mobiledata.util

import java.io.BufferedReader
import java.io.InputStream

fun InputStream.readStream() = this.bufferedReader().use(BufferedReader::readText)
