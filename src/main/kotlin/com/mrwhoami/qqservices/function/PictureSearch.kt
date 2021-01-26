package com.mrwhoami.qqservices.function

import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.util.BasicUtil
import com.mrwhoami.qqservices.util.pictureSearchUtil.Ascii2d
import com.mrwhoami.qqservices.util.interfaces.FunctionListener
import com.mrwhoami.qqservices.util.pictureSearchUtil.Saucenao
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.*
import java.lang.StringBuilder

class PictureSearch : FunctionListener {
    init{
        BotHelper.registerFunctions("搜图", listOf("发P站或推特的图就好了"))
    }
    private val idSet = HashSet<Long>()

    @ExperimentalCoroutinesApi
    override suspend fun execute(event: MessageEvent, message: String, image: Image?, face: Face?): Boolean {
        val senderID = event.sender.id
        val at = At(event.sender as Member)
        if (message.startsWith("/搜图") && !idSet.contains(senderID)) {
            idSet.add(senderID)
            event.reply(at + "请发送图片")
        }

        if (idSet.contains(senderID) && image != null) {

            idSet.remove(senderID)
            event.reply(at + "Saucenao查找中!")
            val imgUrl = image.queryUrl()
            if (BotData.debug) event.reply("图片URL: $imgUrl")
            var results = Saucenao.search(imgUrl)//先搜这个
            if (results.isEmpty()) {
                results = Ascii2d.search(imgUrl)//再搜备用图源
                if(results.isEmpty())
                {
                    event.reply("肥肠抱歉，未查找到结果..")
                    return true
                }
            }
            //event.reply("搜索完成!")
            if (BotData.debug) event.reply(results.toString())
            results.forEach { result ->
                val builder = StringBuilder()
                result.extUrls.forEach {
                    builder.append(it).append("\n")
                }
                BotHelper.scheduler.withTimeOut(suspend {
                    val uploadImage =
                        event.uploadImage(me.lovesasuna.lanzou.util.NetWorkUtil.get(result.thumbnail)!!.second) as Message
                    event.reply(
                        uploadImage + PlainText(
                            "\n相似度: ${result.similarity} \n画师名: ${result.memberName} \n相关链接: \n${
                                builder.toString().replace(Regex("\n$"), "")
                            }"
                        )
                    )
                    uploadImage
                }, 7500) {
                    event.reply("缩略图上传超时")
                    event.reply(
                        PlainText(
                            "空图像(上传失败)\n相似度: ${result.similarity} \n画师名: ${result.memberName} \n相关链接: \n${
                                builder.toString().replace(Regex("\n$"), "")
                            }"
                        )
                    )
                }

            }
        }
        return true
    }


}