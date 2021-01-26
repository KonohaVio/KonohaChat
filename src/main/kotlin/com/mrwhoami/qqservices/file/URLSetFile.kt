package com.mrwhoami.qqservices.file

import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.data.URLSetData
import com.mrwhoami.qqservices.util.BasicUtil

object URLSetFile : AbstractFile() {
    override val file = BasicUtil.getLocation("URLSet.json")
    lateinit var data: URLSetData.URLSetChain
    override fun writeDefault() {
        val data = URLSetData.URLSetChain()
        data.hashSet.apply {
            add(URLSetData.URLSet("https://cdn.donmai.us/original/ab/95/ab95085963f8dded875075449b390928.jpg"))
        }
        if (!file.exists()) {
            BotData.objectMapper!!.writerWithDefaultPrettyPrinter().writeValue(file, data)
        }
        readValue()
    }

    override fun writeValue() {
        BotData.objectMapper!!.writerWithDefaultPrettyPrinter().writeValue(file, data)
    }

    override fun readValue() {
        data = BotData.objectMapper!!.readValue(file, URLSetData.URLSetChain::class.java)
    }
}