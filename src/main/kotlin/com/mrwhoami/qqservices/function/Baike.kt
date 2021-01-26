package com.mrwhoami.qqservices.function

import com.mrwhoami.qqservices.util.interfaces.FunctionListener
import me.lovesasuna.lanzou.util.NetWorkUtil
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Face
import net.mamoe.mirai.message.data.Image
import java.net.URLEncoder
import kotlin.random.Random


class Baike : FunctionListener {
    override suspend fun execute(event: MessageEvent, message: String, image: Image?, face: Face?): Boolean {
        if (message.startsWith("/百科 ")) {
            val string = message.split(" ")[1]
            val url = "https://baike.baidu.com/item/${URLEncoder.encode(string, "UTF-8")}"
            val reader = NetWorkUtil.get(url)!!.second.bufferedReader()
            for (i in 0 until 10) reader.readLine()
            val desc = reader.readLine()
            val args = desc.split("\"")
            if (args.size > 1) {
                event.reply(args[3].replace(Regex("...$"), ""))
            } else {
                if(Random.nextInt(2)>=1)
                    event.reply("肥肠抱歉，百科百科它……没有收录这个伟大的词条")
                else
                    event.reply("肥肠抱歉，百科百科说这个词条……等待着您的撰写")
            }
            reader.close()
            return true
        }
        return false

    }

}