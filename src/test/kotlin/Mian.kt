

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList


suspend fun main()
{
//    val url_yuriTimer = "https://safebooru.donmai.us/posts?tags=yuri"
//    var html: String? = getHTML(url_yuriTimer)
//
//    val tagsStr: String? = getTagsList(html)
//    if(tagsFilter(tagsStr)) {
//        println("realTimePicSend is filtered.<<<<<<")
//    }

//    var hashSet1 = hashMapOf<Int,String>()
//    println(hashSet1)
//
//    var count = 1
//    var count2 = 1
//    for (i in 120..150)
//    {
//        hashSet1[count2] = "$i"
//        println(hashSet1)
//
//        if(hashSet1.size>=10)
//        {
//            hashSet1.remove(count)
//            count++.also { if(it>=10) count=1 }
//            println("remove"+hashSet1)
//        }
//        count2++.also { if(it>=10) count2=1 }
//    }


    //println((Random.nextDouble(1.0,4.0)*1000).roundToLong())

    var list = listOf<String>("a", "b", "c", "d")
    var array = arrayOf(0,0,0,0)

    for (i in 1..Math.pow(10.0,7.0).toInt())
    {
        if(list[0] == "a")
            array[0]++
        if(list[1] == "a")
            array[1]++
        if(list[2] == "a")
            array[2]++
        if(list[3] == "a")
            array[3]++

        Collections.shuffle(list,Random(25))
//        var sysTime = System.currentTimeMillis()
//        print(sysTime)
//        val rnd = Random(sysTime)
//        Collections.shuffle(list, rnd)
//        println(list)
//        sysTime+=(50 * Math.random()*100).roundToInt()
    }

    array.forEach {
        println(it)
    }



}


private fun getTopDataUrl(html: String?): List<String>? {
    val dataFileUrl__ = Pattern.compile("data-large-file-url=\".*?\"").matcher(html)
    val list: MutableList<String> = java.util.ArrayList()
    while (dataFileUrl__.find()) {
        list.add(dataFileUrl__.group().split("\"".toRegex()).toTypedArray()[1])
    }

    return if (list.size <= 3 )
        list
    else
        listOf(list[0], list[1], list[2])
}


var filteredTagsSet = hashSetOf("1boy", "ass")


fun addFilteredTags(tag: String)
{
    filteredTagsSet.add(tag)
}

private fun tagsFilter(tagsStr: String?):Boolean
{
    val tagsArray = tagsStr!!.replace("\"", "").split(" ".toRegex())
    println(tagsArray)


    //第一个元素是，所以得额外识别，data-tags=1boy
    val firstTag = tagsArray[0].replace("data-tags=", "")
    filteredTagsSet.forEach{
        if(it == firstTag)
        {
            println("true<<<<<<<<<<<<<")
            return true
        }

    }
    println(tagsArray)

    if(tagsArray.contains("makima_(chainsaw_man)") &&
        (tagsArray.contains("quanxi_(chainsaw_man)") || tagsArray.contains("reze_(chainsaw_man)") ) )
    {
        println("true----------")
        return true
    }

    filteredTagsSet.forEach{ filteredTagsStr ->
        if(tagsArray.contains(filteredTagsStr))
        {
            println("true>>>>>>>>>>>>>>>")
            return true
        }

    }
    return false
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





//import java.net.Proxy
//import java.net.URL
//import java.util.*
//import java.util.regex.Pattern
//
//
//fun main(){
//    var nums = 1
//    val img_src = "img???.*src=.jpg"
//    val dataFileUrl = "data-large-file-url\".*.jpg\""
//    val path__ = "D:\\image"
//
//    val tags = "kousaka_reina+yuri"
//    println("正在执行")
//    //val url = "https://safebooru.donmai.us/posts?tags=$tags"
//    //https://gelbooru.com/index.php?page=post&s=random
//    val urlRandom  = "https://gelbooru.com/index.php?page=post&s=random"
//    //val urlRandom  = "https://danbooru.donmai.us/posts/3808854?tags=rating%3Aexplicit+yuri"
//    //val urlRandom = "https://safebooru.donmai.us/posts/random?tags=$tags"
////        val url = URL(urlRandom)
////        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
////        conn.getResponseCode()
////        val realUrl: String = conn.getURL().toString()
////        conn.disconnect()
////        println(conn.toString())
//
//    //val urlRandom1 =  "https://safebooru.donmai.us/posts/2769414?tags=kousaka_reina+yuri"
//    val html: String? = getHTML(urlRandom)
//    //println("this->"+html.toString())
//    val dataUrl: List<String>? = getdataUrl(html)
//    println(dataUrl)
//
//    //data-has-sound
//    //var url3: URL? = null
//    for(rand_ in dataUrl!!)
//    {
//        println(rand_)
//        val randSplit = rand_.split("=")
//        val rand_2 = randSplit[randSplit.size - 1].replace("\"", "")
//        println(rand_2)
////            url3 = URL(rand_)
////            event.reply(event.uploadImage(url3))
//    }
//
//
//
//
//
//
//}
//
//
//
//@Throws(Exception::class)
fun getHTML(srcUrl: String?): String? {

//    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 10808))
//    val obj = URL(url)
//    val con = obj.openConnection(proxy) as HttpURLConnection

    val url = URL(srcUrl)
    //val conn = url.openConnection(proxy) as HttpURLConnection
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

fun getdataUrl(html: String?): List<String>? {
    val dataFileUrl__ = Pattern.compile("data-large-file-url=\".*>").matcher(html)
    val list: MutableList<String> = java.util.ArrayList()
    while (dataFileUrl__.find()) {
        list.add(dataFileUrl__.group().split("\"".toRegex()).toTypedArray()[1])
    }
    return list
}


fun getYuriRandomDataUrl(html: String?): List<String>? {
    //val dataFileUrl = "data-large-file-url=\".*.jpg\""
    val img_src = "<img ?.*src=.*jpg\""
    val dataFileUrl__ = Pattern.compile(img_src).matcher(html)
    val list: MutableList<String> = ArrayList()
    while (dataFileUrl__.find()) {
        val srcStr = dataFileUrl__.group().split("\"")
        println(srcStr)
        list.add(srcStr[srcStr.size - 4])
        list.add(srcStr[srcStr.size - 2])
        //list.add(dataFileUrl__.group())
    }
    return list
}


fun realTimePicSend():String//定时百合图
{
    val url_realTime = "https://safebooru.donmai.us/posts?tags=chainsaw_man"
    var html: String? = getHTML(url_realTime)

    val tagsStr: String? = getTagsList(html)
    if(tagsFilter(tagsStr)) {
        println("realTimePicSend is filtered.<<<<<<")
        return "No"
    }
    val dataUrl: String? = getLargeDataUrl(html)

    return dataUrl!!
}

private fun getLargeDataUrl(html: String?): String? {
    //val dataFileUrl = "data-large-file-url=\".*.jpg\""
    val dataFileUrl__ = Pattern.compile("data-large-file-url=\".*?\"").matcher(html)


    var dataUrlStr : String? = "No result"//未寻到时初值为"No result"

    if(dataFileUrl__.find())
        dataUrlStr = dataFileUrl__.group()

    return if(dataUrlStr != "No result")
        dataUrlStr!!.split("\"".toRegex())[1]
    else
        "No result"
}

fun getFavoritesNum(html: String?): Int? {
    //val dataFileUrl = "data-large-file-url=\".*.jpg\""
    val img_src = "Favorites: .*</span>"
    //Favorites: <span id="favcount-for-post-2943625">21</span>
    val dataFileUrl__ = Pattern.compile(img_src).matcher(html)
    var dataUrlStr : String? = ""
    if(dataFileUrl__.find())
        dataUrlStr = dataFileUrl__.group()

    return dataUrlStr!!.replace("<", ">").split(">".toRegex())[2].toInt()
}


