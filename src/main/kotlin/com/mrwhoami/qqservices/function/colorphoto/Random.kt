package com.mrwhoami.qqservices.function.colorphoto

import com.fasterxml.jackson.databind.ObjectMapper
import com.mrwhoami.qqservices.util.interfaces.PhotoSource
import me.lovesasuna.lanzou.util.NetWorkUtil
import java.io.IOException

/**
 * @author LovesAsuna
 * @date 2020/4/19 14:06
 */
class Random : PhotoSource {
    override fun fetchData(): String? {
        val source = "http://api.mtyqx.cn/api/random.php?return=json"
        val result = NetWorkUtil[source]
        return try {
            val mapper = ObjectMapper()
            val root = mapper.readTree(result!!.second)
            root["imgurl"].asText()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}