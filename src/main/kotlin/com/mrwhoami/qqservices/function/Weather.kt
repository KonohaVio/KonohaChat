package com.mrwhoami.qqservices.function

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.net.URL


//import net.sf.json.JSONObject;
object Weather {

    //这是和风天气的API接口
    private const val HeWeatherKey = "9a4009f8b1a4485f83c9ae170ab517df"

    //以下是两个API的使用说明，完整功能应该为，获得城市名搜索城市ID，然后用ID访问当地天气，以及未来三天天气预报
    //https://dev.heweather.com/docs/api/geo
    //https://dev.heweather.com/docs/api/weather

    @Throws(IOException::class, NullPointerException::class)
    fun getTodayWeather(Cityid: String): HashMap<String, String> {



        // 连接和风天气的API
        val url =
            URL(
                "https://devapi.heweather.net/v7/weather/now?location=$Cityid$HeWeatherKey"
            )


        val connectionData = url.openConnection()
        connectionData.connectTimeout = 1000


        var weatherMap =HashMap<String, String>();
        var datas:String?
        var br:BufferedReader? = null
        try {
            val mapper = ObjectMapper()

            br = BufferedReader(
                InputStreamReader(
                    connectionData.getInputStream(), "UTF-8")
            )
            val sb = StringBuilder()
            var line: String? = null
            while ((br.readLine().also { line = it }) != null) sb.append(line)
            datas = sb.toString()
            //截取[]转化为json格式
            datas = datas.replace(datas.substring(datas.indexOf(":") + 1, datas.indexOf(":") + 2), "")
            datas = datas.replace(datas.substring(datas.length - 2, datas.length - 1), "")
            println(datas)

            val `object1` = mapper.readTree(datas)
            val `object2` =`object1`["HeWeather6"]
            val `object3` =`object2`["basic"]
            val `object4` =`object2`["now"]

            //获得text键的内容，并转化为string
            //val back = json["text"].toString()



            //城市 "location":"长春"
            weatherMap.put("city", `object3`["location"].toString().replace("\"", "")) // 城市

            //当前气温 "tmp":"21"
            weatherMap.put("temp", `object4`["tmp"].toString().replace("\"", "")) // 最高温度

            //体感气温 "fl tmp":"21"
            weatherMap.put("fl", `object4`["fl"].toString().replace("\"", "")) // 最高温度

//            weatherMap.put("temp2", `object`["temp2"].toString().replace("\"", "")) // 最低温度
//            weatherMap.put("weather", `object`["weather"].toString().replace("\"", "")) //天气
//            weatherMap.put("ptime", `object`["ptime"].toString().replace("\"", "")) // 发布时间
        } catch (e: SocketTimeoutException) {
            println("连接超时")
        } catch (e: FileNotFoundException) {
            println("加载文件出错")
        }
        finally {
            //br.close();
            //关闭流
            try {
                if(br!=null){
                    br.close();
                }

            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
        return weatherMap
    }
}