package com.mrwhoami.qqservices.file

import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.data.FilterTagData
import com.mrwhoami.qqservices.util.BasicUtil

object FileterTagFile : AbstractFile() {
    override val file = BasicUtil.getLocation("FileterTag.json")
    lateinit var data: FilterTagData.filterTagChain
    override fun writeDefault() {
        val data = FilterTagData.filterTagChain()
        data.list.apply {
            add("1boy")
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
        data = BotData.objectMapper!!.readValue(file, FilterTagData.filterTagChain::class.java)
    }
}