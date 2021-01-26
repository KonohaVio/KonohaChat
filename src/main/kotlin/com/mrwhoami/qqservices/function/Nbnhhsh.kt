package com.mrwhoami.qqservices.function

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.util.interfaces.FunctionListener
import me.lovesasuna.lanzou.util.NetWorkUtil
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Face
import net.mamoe.mirai.message.data.Image

//能不能好好说话
class Nbnhhsh : FunctionListener{
    init{
        BotHelper.registerFunctions("能不能好好说话(缩写查询)", listOf("/sx+空格+缩写"))
    }

    override suspend fun execute(event: MessageEvent, message: String, image: Image?, face: Face?): Boolean {
        if (message.startsWith("/sx ")) {
            val abbreviation = message.split(" ")[1]
            val mapper = ObjectMapper()
            val text = mapper.createObjectNode().put("text", abbreviation)
            val post = NetWorkUtil.post("https://lab.magiconch.com/api/nbnhhsh/guess", text.toString().toByteArray(),
                arrayOf("content-type", "application/json"))
            val result = post?.second?.bufferedReader()?.lineSequence()?.joinToString()
            if (result != null) {
                event.reply("可能的结果: ${mapper.readTree(result)[0]["trans"] ?: "[]"}")

            }
            return true
        }
        return false
    }
}