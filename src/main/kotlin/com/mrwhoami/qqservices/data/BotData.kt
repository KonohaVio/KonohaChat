package com.mrwhoami.qqservices.data

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

object BotData {
    var objectMapper: ObjectMapper? = null

    var debug: Boolean = false

    val error = Stack<Throwable>()
}

fun Throwable.pushError() {
    BotData.error.push(this)
}