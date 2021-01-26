package com.mrwhoami.qqservices.file

import com.mrwhoami.qqservices.function.BotHelper
import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.data.ConfigData
import com.mrwhoami.qqservices.util.BasicUtil
import com.mrwhoami.qqservices.util.interfaces.file.FileManipulate
import net.mamoe.mirai.utils.BotConfiguration

object Config : AbstractFile() {
    override val file = BasicUtil.getLocation("config.json")
    lateinit var data: ConfigData

    override fun writeDefault() {
        val data = ConfigData()
        if (!file.exists()) {
            BotData.objectMapper!!.writerWithDefaultPrettyPrinter().writeValue(file, data)
        }
        readValue()
    }

    override fun writeValue() {
        return
    }

    override fun readValue() {
        data = BotData.objectMapper!!.readValue(file, ConfigData::class.java)
        BotHelper.botConfig.protocol = BotConfiguration.MiraiProtocol.valueOf(data.protocol.toUpperCase())
    }

}


