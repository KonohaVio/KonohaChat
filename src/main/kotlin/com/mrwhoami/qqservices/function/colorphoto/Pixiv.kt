package com.mrwhoami.qqservices.function.colorphoto

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrwhoami.qqservices.data.BotData.objectMapper

import com.mrwhoami.qqservices.data.pushError
import com.mrwhoami.qqservices.util.interfaces.PhotoSource
import me.lovesasuna.lanzou.util.NetWorkUtil
import java.io.IOException

/**
 * @author LovesAsuna
 * @date 2020/4/19 14:06
 */
/**
 * @author LovesAsuna
 * @date 2020/4/19 14:06
 */
class Pixiv : PhotoSource {
    override fun fetchData(): String? {
        //这是一个能获取P站随机涩图的API，有每日次数限制
        // 备用976835505edf70ff564238
        val source = "https://api.lolicon.app/setu/?apikey=560424975e992113ed5977"
        val result = NetWorkUtil[source]
        println(result)
        return try {
            val inputStream = result!!.second
            val mapper = ObjectMapper()
            val `object` = mapper.readTree(inputStream)

            val quota = `object`["quota"].asText()
            val url = `object`["data"][0]?.let { it["url"].asText() } ?: return "|0"
            println("$url|$quota")
            return "$url"
        } catch (e: IOException) {
            e.pushError()
            e.printStackTrace()
            null
        } catch (e: NullPointerException) {
            e.pushError()
            e.printStackTrace()
            null
        }
    }
}