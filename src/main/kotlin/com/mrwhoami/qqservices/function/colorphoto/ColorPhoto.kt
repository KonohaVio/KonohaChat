package com.mrwhoami.qqservices.function.colorphoto



import com.mrwhoami.qqservices.function.BotHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
//import com.mrwhoami.qqservices.bot.Main
//import com.mrwhoami.qqservices.file.Config
import com.mrwhoami.qqservices.util.interfaces.FunctionListener
import com.mrwhoami.qqservices.util.interfaces.PhotoSource
import net.mamoe.mirai.contact.Member
import me.lovesasuna.lanzou.util.NetWorkUtil
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Face
import net.mamoe.mirai.message.data.Image

class ColorPhoto : FunctionListener {
    lateinit var photoSource: PhotoSource
    var random = true
    var pixiv = true
    init{
        BotHelper.registerFunctions("涩图", listOf("/涩图+空格"))
        BotHelper.registerFunctions("百合", listOf("/百合"))
    }
    override suspend fun execute(event: MessageEvent, message: String, image: Image?, face: Face?): Boolean {
        //val bannotice = { event.reply("该图源已被禁用！") }
        if (message.startsWith("/涩图")) {
            when {

                message.endsWith("random") -> {
                    if (random) {
                        photoSource = Random()
                        event.reply(event.uploadImage(photoSource.fetchData()?.let { NetWorkUtil[it] }!!.second))
                    } else {
                        event.reply("该图源被禁用")
                    }
                }
                message.endsWith("switch") -> {
                    changeBanStatus(event, message)
                }

                else -> {
                    if (pixiv) {
                        photoSource = Pixiv()
                        val data = photoSource.fetchData()
                        val url = data
                        event.reply(event.uploadImage(NetWorkUtil[url.toString()]!!.second) )
                    }else {
                        //bannotice.invoke()
                        event.reply("该图源被禁用")
                    }
                }
            }
        }
        return true
    }

    private fun changeBanStatus(event: MessageEvent, message: String) {
        if (BotHelper.memberIsAdmin(event.sender as Member)) {
            GlobalScope.async {
                when (message.split(" ")[2]) {
                    "pixiv" -> {
                        event.reply("已${if (pixiv) "禁用" else "解禁"}pixiv图源")
                        pixiv = !pixiv
                    }
                    "random" -> {
                        event.reply("已${if (random) "禁用" else "解禁"}random图源")
                        random = !random
                    }
                }
            }

        }
    }
}