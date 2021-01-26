package com.mrwhoami.qqservices.function

import com.mrwhoami.qqservices.data.FilterTagData
import com.mrwhoami.qqservices.data.URLSetData
import com.mrwhoami.qqservices.file.Config
import com.mrwhoami.qqservices.file.FileterTagFile
import com.mrwhoami.qqservices.file.URLSetFile
import com.mrwhoami.qqservices.util.BasicUtil
import com.mrwhoami.qqservices.util.interfaces.FunctionListener
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Face
import net.mamoe.mirai.message.data.Image
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern
import kotlin.math.abs


class SafebooruPic : FunctionListener {

    init{

        BotHelper.registerFunctions("safe", listOf("/safe:罗马音标签:数量(数字)(可选)", "多标签用空格隔开"))
    }

    val NORESULT :String  = "noResult";
    val EXISTED : String =  "existed";
    val FILTERED :String  = "filtered";

//    private var urlSet : HashSet<URLSetData.URLSet> = URLSetFile.data.hashSet//所有发图函数共用
//    private var filteredTagsSet = FileterTagFile.data.list//屏蔽标签
    var tag_top :String? = "";

    ///posts?tags=

    fun yuriTimer():String//定时百合图
    {
        val url_yuriTimer = "https://safebooru.donmai.us/posts?tags=yuri"
        var html: String? = getHTML(url_yuriTimer)

        val tagsStr: String? = getTagsList(html)
        if(tagsFilter(tagsStr)) {
            println("realTimePicSend is filtered.<<<<<<")
            return FILTERED
        }

        val dataUrl: String? = getLargeDataUrl(html)
        if(dataUrl!! == NORESULT)
            return NORESULT

        return if (addURLSet(dataUrl!!))
            dataUrl!!
        else
            EXISTED
    }

    fun yuriPicSend():String?//来点百合，返回名字+url
    {

        val url_yuri = "https://safebooru.donmai.us/posts/random?tags=yuri"

        var html: String? = getHTML(url_yuri)
        val tagsStr: String? = getTagsList(html)
        if(tagsFilter(tagsStr)) {
            println("YuriPicSend is filtered.<<<<<<")
            return FILTERED
        }

        var dataUrl:String? = getLargeDataUrl(html)//未找到时初值为空
        if(dataUrl!! == NORESULT)
            return NORESULT
        return if(addURLSet(dataUrl!!))
            dataUrl!!
        else
            EXISTED
    }

    fun topImage(): List<String?> {//top，热门图，返回三个最新热门图的url List，没有标签过滤

        //val url_yuri = "https://safebooru.donmai.us/posts?d=1&tags=order%3Arank"
        val url_top = "https://safebooru.donmai.us/posts?d=1&tags=order%3Arank+$tag_top"
        var html: String? = getHTML(url_top)
        val dataUrl: List<String>? = getTopDataUrl(html)

        var dataUrlListCopy:MutableList<String?> = ArrayList()
        if(dataUrl!!.isNotEmpty()) {
            dataUrl!!.forEach{
                if(!tagsFilter(getTagsList(html))){//若没有被屏蔽
                    if(addURLSet(it))//且无重复
                        dataUrlListCopy.add(it)
                }
            }
        }
        else
            println(">>> topImage No result or existed or filtered.")

        return dataUrlListCopy!!
    }

    override suspend fun execute(event: MessageEvent, message: String, image: Image?, face: Face?): Boolean {
        when {
            //指定标签图
            message.startsWith("/safe") ->{
                val message1 = message.replace("：",":")
                val messageSplit_List = message1.split(":")
                //val booruName = messageSplit[0].replace("/", "")
                if(messageSplit_List.size==1) {//如果只有一个，那也就是说格式不对
                    event.reply("形如/safe:kousaka_reina yuri:3")
                    return false
                }
                val tags = messageSplit_List[1].replace(" ", "+")

                var nums = when {
                    messageSplit_List.size>=3 -> abs(  message1.split(":")[2].toInt() )
                    else -> 1
                }

                println("new loading...")
                //val url = "https://safebooru.donmai.us/posts?tags=$tags"
                //https://danbooru.donmai.us/posts?tags=rating%3Aexplicit+yuri

                val urlRandom = "https://safebooru.donmai.us/posts/random?tags=$tags"
                var html: String? = ""

                //测试该tags有无结果
                try {
                    html = getHTML(urlRandom)
                } catch (e: Exception) {
                    e.printStackTrace()
                    event.reply("...这个tags它...没有结果...")
                    return false
                }

                var dataUrl: List<String>?
                var tagsStr : String?

                var url3: URL? = null
                var finalUrl:String
                var count = 0
                while(nums>0)
                {
                    try {
                        html = getHTML(urlRandom)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        event.reply("此次搜寻未果...")
                        continue
                    }

                    tagsStr = getTagsList(html)
                    if(tagsFilter(tagsStr)) {
                        event.reply("一张图片被屏蔽...")
                        nums--
                        continue
                    }

                    dataUrl = getdataUrl(html)
                    //println(dataUrl)
                    if(dataUrl!!.isEmpty())
                    {
                        event.reply("没有结果...")
                        nums--
                        continue
                    }


                    else{
                        finalUrl = dataUrl!!.first()
                        if(addURLSet(finalUrl))
                        {
                            url3 = URL(finalUrl)
                            println(url3)
                            event.reply(event.uploadImage(url3))
                            nums--
                        }
                        else{
                            count++
                            if(count>4)
                                return false
                        }
                    }//else的{
                }//while的{
                return true
            }//指定标签代码段结束


            message.contentEquals("/filtertag list") && event.sender.id == Config.data.botOwnerQQId ->{
                val builder = StringBuilder()
                builder.append("定时推送图中的过滤标签如下\n")
                builder.append("======================\n")
                var index = 0
                FileterTagFile.data.list.forEach {
                    builder.append("$index. $it \n")
                    index++
                }
                event.reply(builder.toString())
                return true

            }

            message.startsWith("/filtertag rm ") && event.sender.id == Config.data.botOwnerQQId -> {
                val index = message.split(" ")[2]
                FileterTagFile.data.list.remove(index)
                event.reply("tag删除成功")
                FileterTagFile.writeValue()
                println("KeyWordFile写入成功")
                return true
            }

            message.startsWith("/filtertag add ") && event.sender.id == Config.data.botOwnerQQId ->{
                val parms = message.split(" ")
                if(FileterTagFile.data.list.add(parms[2]))
                    event.reply("好的，香织以后会减少此类推送")
                else
                    event.reply("此tag已经被屏蔽了...")
                FileterTagFile.writeValue()
                println("KeyWordFile写入成功")
            }

        }


        return true
    }

    //data-file-url="https://safebooru.donmai.us/data/e3c5b259b7c10d46d72a5c53a9e8ef0b.jpg" data-large-file-url="https://safebooru.donmai.us/data/e3c5b259b7c10d46d72a5c53a9e8ef0b.jpg"
    @Throws(Exception::class)
    fun getHTML(srcUrl: String?): String? {
        val url = URL(srcUrl)
        val conn = url.openConnection()
        val `is` = conn.getInputStream()
        val isr = InputStreamReader(`is`)
        val br = BufferedReader(isr)
        var line: String? = null
        val buffer = StringBuffer()
        while (br.readLine().also { line = it } != null) {
            buffer.append(line)
            buffer.append("\n")
        }
        br.close()
        isr.close()
        `is`.close()
        return buffer.toString()
    }

    private val dataLargeFileURL = "data-large-file-url=\".*?\""
    //private val path__ = "${BotHelper.dataFolder.path}/YuriImage"


    private fun getTopDataUrl(html: String?): List<String>? {
        val dataFileUrl__ = Pattern.compile(dataLargeFileURL).matcher(html)
        val list: MutableList<String> = ArrayList()
        while (dataFileUrl__.find()) {
            list.add(dataFileUrl__.group().split("\"".toRegex()).toTypedArray()[1])
        }

        return if (list.size < 3 )
            list
        else
            listOf(list[0],list[1],list[2])
    }

    private fun getdataUrl(html: String?): List<String>? {
        val dataFileUrl__ = Pattern.compile(dataLargeFileURL).matcher(html)
        val list: MutableList<String> = ArrayList()
        while (dataFileUrl__.find()) {
            list.add(dataFileUrl__.group().split("\"".toRegex()).toTypedArray()[1])
        }
        return list
    }

    fun getTagsList(html: String?): String? {
        val dataFileUrl__ = Pattern.compile("data-tags=\".*?\"").matcher(html)
//    val list: MutableList<String> = java.util.ArrayList()
//
//    while (dataFileUrl__.find()) {
//        list.add(dataFileUrl__.group())
//    }
//    return list

        var tagStr :String? = ""
        if((dataFileUrl__.find()))
            tagStr = dataFileUrl__.group()!!
        return tagStr
    }

    private fun tagsFilter(tagsStr:String?):Boolean
    {
        val tagsArray = tagsStr!!.replace("\"","").split(" ".toRegex())
        println(tagsArray)

        //第一个元素是，所以得额外识别，data-tags=1boy
        FileterTagFile.data.list.forEach{
            if(it == tagsArray[0].replace("data-tags=",""))
                println("true<<<<<<<<<<<<<")
                return true
        }
        println(tagsArray)

        if(tagsArray.contains("makima_(chainsaw_man)") &&
            (tagsArray.contains("quanxi_(chainsaw_man)") || tagsArray.contains("reze_(chainsaw_man)") ) )
        {
            println("true----------")
            return true
        }

        FileterTagFile.data.list.forEach{ filteredTagsStr ->
            if(tagsArray.contains(filteredTagsStr))
                println("true>>>>>>>>>>>>>>>")
                return true
        }
        return false
    }

    private fun getLargeDataUrl(html: String?): String? {
        //val dataFileUrl = "data-large-file-url=\".*.jpg\""
        val dataFileUrl__ = Pattern.compile(dataLargeFileURL).matcher(html)
        var dataUrlStr : String? = NORESULT//未寻到时初值为"No result"
        if(dataFileUrl__.find())
            dataUrlStr = dataFileUrl__.group()

        return if(dataUrlStr != NORESULT)
            dataUrlStr!!.split("\"".toRegex())[1]
        else
            NORESULT
    }

    private fun getYuriRandomDataUrl(html: String?): MutableList<String>? {
        //val dataFileUrl = "data-large-file-url=\".*.jpg\""
        val img_src = "<img ?.*src=.*jpg\""
        val dataFileUrl__ = Pattern.compile(img_src).matcher(html)


        val list: MutableList<String> = ArrayList()//未找到时初值为空，没有任何元素

        while (dataFileUrl__.find()) {
            val srcStr = dataFileUrl__.group().split("\"")
            //println(srcStr)
            list.add(srcStr[srcStr.size-4])//返回名字
            list.add(srcStr[srcStr.size-2])//返回url
            //list.add(dataFileUrl__.group())
        }
        return list
    }




    private fun addURLSet(URLStr : String):Boolean
    {
        if(URLSetFile.data.hashSet.add(URLSetData.URLSet(URLStr))) {
            println(URLSetFile.data.hashSet)
            println("URLSet更新成功")

            if(URLSetFile.data.hashSet.size>=10000)
            {
                URLSetFile.data.hashSet.remove(URLSetFile.data.hashSet.first())
                println("超100更新成功")
            }

            FileterTagFile.writeValue()
            println("URLSetFile写入成功")
            return true
        } else
            return false
    }




    fun getFavoritesNum(html: String?): Int? {
        //val dataFileUrl = "data-large-file-url=\".*.jpg\""
        val img_src = "Favorites: .*</span>"
        //Favorites: <span id="favcount-for-post-2943625">21</span>
        val dataFileUrl__ = Pattern.compile(img_src).matcher(html)
        var dataUrlStr : String? = ""
        if(dataFileUrl__.find())
            dataUrlStr = dataFileUrl__.group()

        return dataUrlStr!!.replace("<",">").split(">".toRegex())[2].toInt()
    }

    fun getPicture(picUrl: List<String>, destPath: String) {
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        var url: URL? = null
        for (temp in picUrl) {
            //System.out.println(temp);


            val regex = temp.split("/".toRegex()).toTypedArray()
            val name = regex[regex.size - 1]
            try {
                /*
                /images/github-logo.png
        /images/twitter-logo.png
        /images/discord-logo.png
                 */
                url = URL(temp)
                bis = BufferedInputStream(url.openStream())
                val b = ByteArray(1024)
                var len = 0
                bos = BufferedOutputStream(FileOutputStream(File(destPath + name)))
                while (bis.read(b).also { len = it } != -1) {
                    bos.write(b, 0, len)
                    bos.flush()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}