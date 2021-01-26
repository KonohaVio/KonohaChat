package com.mrwhoami.qqservices.file

import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.function.KeyWord
import com.mrwhoami.qqservices.util.BasicUtil

object KeyWordFile : AbstractFile() {
    override val file = BasicUtil.getLocation("keyword.json")
    lateinit var data: KeyWord.KeyWordChain
    override fun writeDefault() {
        val data = KeyWord.KeyWordChain()
        data.list.apply {
            add(KeyWord.KeyWord("复读", "复读", 10))
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
        data = BotData.objectMapper!!.readValue(file, KeyWord.KeyWordChain::class.java)
    }
}