package com.mrwhoami.qqservices

import com.mrwhoami.qqservices.file.Config
import com.mrwhoami.qqservices.function.BotHelper
import com.mrwhoami.qqservices.function.Homeru
import com.mrwhoami.qqservices.function.SafebooruPic

import com.mrwhoami.qqservices.util.BasicUtil
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.io.*
import java.net.URL
import java.util.regex.Pattern
import kotlin.random.Random


class QuestionAnswer {

//    init {
//        //BotHelper.registerFunctions("人工智障聊天模式", listOf("香织陪我聊天", "香织来聊天", "结束人工智障模式：/退下吧"))
//
//
//        val indexF = File(indexFilePath)
//        if(!indexF.exists())
//        {
//            val writer: FileWriter?
//            try {
//                indexF.createNewFile()
//                writer = FileWriter(indexFilePath)
//                writer.write("1")
//                writer?.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        else
//        {
//            try {
//                val br = BufferedReader(InputStreamReader(FileInputStream(indexFilePath)))
////                while (br.readLine().also { starFrom = it.toInt() } != null) {
////                    println("$starFrom<<<读入完成")
////                }
//                starFrom = br.readLine().toInt()
//                br.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    private val inGroup = listOf(Config.data.clockIn, Config.data.yuriImg, Config.data.testGroup)
    //特定的功能可以只给特定的群用，以免社会性死亡，这里只加载了打卡群，百合图群，测试群，可自行调整

    //private var turingFlag = false//图灵机器人开关
    private var repeat = hashSetOf<String>()//复读限制器，利用hashSet的元素互异性，使得相同的句子不被复读多次
    private var msgContent_Copy: String?  =  ""//句子副本，保存该复读的句子

    private var safePic = SafebooruPic()//safebooru的对象

    //静态变量，等价于static
    companion object {
        //var starFrom  = 1
        //var Hayaneyou = false
        val Path_resources = "${BasicUtil.getLocation(BotHelper::class.java).path}${File.separator}"
            .replace("${File.separator}build${File.separator}classes${File.separator}kotlin","${File.separator}src${File.separator}main${File.separator}resources")
            .replace("%20"," ")
        //val indexFilePath = "$Path_resources${File.separator}YuriImage${File.separator}index.txt"
        //val YuriImagePath = "$Path_resources${File.separator}YuriImage"

        var BotNameList  : MutableList<String> = mutableListOf(
            "香织","香织织","人工智能","人工智障","香织铃","kaori","kaorin","かおり","かおりん","さわい","沢井","さ～わ～い","か～お～り","织织",
            "阿香"
        )

        fun isHKIRepeater(msgContent:String):Boolean {
            if(!contains_BotName(msgContent))
                return false

            return  msgContent.contentEquals("喵喵喵")//
                    ||msgContent.endsWith("（确信）") || msgContent.endsWith("(确信)")
                    ||msgContent.contains("生日快乐")//生日快乐
                    ||(msgContent.contains("yyds") || msgContent.contains("永远滴神") && msgContent.length<=8) //永远滴神复读
                    ||msgContent.startsWith("不愧是")//不愧是复读
                    ||msgContent.startsWith("是，是")//是，是大佬复读
                    ||(msgContent.contains("志水") && msgContent.contains("傻逼"))//群文明正确
                    ||msgContent.contains("加油")//傻逼xx复读
                    ||msgContent.contains("喜糖")//？你有问题复读
                    ||msgContent.contentEquals("太甜了")//草复读
                    ||msgContent.contentEquals("惹")//eva大佬专属复读
                    ||msgContent.contentEquals("磕到了") || msgContent.contentEquals("好！")//好好怪复读
                    ||msgContent.endsWith("（√）")||msgContent.endsWith("(√)")
                    ||msgContent.contentEquals("我好了")//好好怪复读
                    //||msgContent.endsWith("！！！")//猫村专属复读
                    //||(msgContent.startsWith("“")  && msgContent.endsWith("”"))//都教授引用式复读
                    ||msgContent.contentEquals("不要直女") || msgContent.contentEquals("直女滚啊") || msgContent.contentEquals("小心直女")
                    ||Pattern.matches(". . . .", msgContent)//阴 阳 怪 气
        }

        fun contains_BotName(msgContent: String) : Boolean{
            BotNameList.forEach{
                if(msgContent.contains(it)) {
                    return true
                }
            }
            return false
        }


    }

//    fun starFrom_Write2File()
//    {
//
//        val indexF = File(indexFilePath)
//        if(!indexF.exists())
//        {
//            try {
//                indexF.createNewFile()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        val writer = FileWriter(indexFilePath)
//        writer.write(starFrom.toString())
//        writer?.close()
//        println("$starFrom>>>>写入完成")
//    }

//    fun sendYuriImage(num:Int) : HashSet<String>
//    {
//        var picR15Size = 1
//        try {
//            val files = File(YuriImagePath).listFiles()
//            picR15Size = files.size-2
//            //多的两个是rename.cmd 和 index.txt
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        var IndexStr : String?
//
//        var howMany = num //数量
//
//        var IndexStrList = HashSet<String>()
//        for(index in starFrom until starFrom+howMany) {//原来它不同于python的左闭右开，..它是左闭右闭区间,until才是左闭右开
//            //println(index)
//            IndexStr = "/YuriImage/${index}.jpg"
//            //println(IndexStr)
//            IndexStrList.add(IndexStr)
//        }
//        starFrom = (starFrom+howMany)%picR15Size
//        starFrom_Write2File()
//
//        return IndexStrList
//
//    }


    //设定bot名字，不同的叫法都可以触发

    //包含名字
    private fun containsBotName(msgContent: String) : Boolean{
        BotNameList.forEach{
            if(msgContent.contains(it)) {
                return true
            }
        }
        return false
    }

    //刚好就是名字
    private fun contentEqualsBotName(msgContent: String) : Boolean{
        BotNameList.forEach{
            if(msgContent.contentEquals(it)) {
                return true
            }
        }
        return false
    }


    private fun addBotName(newName : String) : Boolean{
        BotNameList.add(newName)
        return true
    }





    private fun getPlainText(messageChain: MessageChain) : String? {
        var buffer = ""
        for (msg in messageChain) {
            if (msg.isContentEmpty()) continue
            else if (msg.isPlain()) {
                buffer += msg.content
            } else continue
        }
        if (buffer.isEmpty()) return null
        return buffer
    }


    suspend fun onGrpMsg(event: GroupMessageEvent) {
        if (!inGroup.contains(event.group.id)) return

        val msg = event.message
        val msgContent = getPlainText(msg) ?: return//这就是消息主体，是String类型的文本了
        val grp = event.group


        //第一优先级是图灵机器人，如果在开启状态，那么就会进入
//        if(turingFlag == true)
//        {//如果在开启状态
//
//            // 读取群消息，传递给Turing机器人
//            var content = msgContent
//
//            //取消聊天
//            if(content.contentEquals("/退下吧"))
//            {
//                turingFlag = false
//                grp.sendMessage("好的，你们继续聊，我就先退下了(❁´◡`❁)")
//            }
//            else{
//                //接收返回的结果
//                val result: String = TuringApiUtil.getResult(content)
//                //把json格式的字符串转化为json对象
//                val json = JsonParser().parse(result).asJsonObject
//                //获得text键的内容，并转化为string
//                val back = json["text"].toString().replace("\"", "")
//                //println(back)
//                //传送结果
//                grp.sendMessage(back)
//            }
//            //如果开始了图灵机器人，不想执行接下来的代码，那么加个return即可
//            //return
//
//        }



        //以下一大段，直到结尾，都在这个when结构里面
        when {
            //开启聊天
//            containsBotName(msgContent)
//                    && msgContent.endsWith("陪我聊天") ->{
//                turingFlag = true
//                grp.sendMessage("我来啦~")
//            }


            //包含关键字contains，完全相等contentEquals，前缀startsWith，后缀endsWith
//            msgContent.contains("zaima") -> grp.sendMessage("buzai, ShuoZhongWen (　^ω^)")

//            Hayaneyou && event.sender.specialTitle=="心葉" -> {
//                if(msgContent.contains("晚安") || msgContent.contains("おやすみ")) {
//                    Hayaneyou = false
//                    grp.sendMessage("はい、よろしい、おやすみなさい")
//                }
//                else
//                {
//                    var str1 = ""
//                    val RandomNum = Random.nextInt(5)
//                    str1 = when(RandomNum)
//                    {
//                        0->"いいから、はやく"
//                        1->"朝一授業あるでしょう？"
//                        2->"いつまでしゃべるつもり？今すぐ"
//                        else->"さっさと"
//                    }
//
//                    grp.sendMessage(event.sender.at() + str1+"寝なさい")
//                }
//            }

//            msgContent.contains("测试") -> {
            //发图
//                val picture = this::class.java.getResource("/QuestionAnswer/XieXiao.jpg")
//                grp.sendImage(picture)
            //引用+艾特+信息
                //grp.sendMessage(msg.quote() + event.sender.at() + "来了来了")

                //msg.quote()
                //event.sender.at()
                //grp.sendMessage(Face(Face.taiyang))//发送qq原生表情，采用这样的形式增加可读性
//            }


            msgContent.startsWith("来点百合") || msgContent.startsWith("/百合")-> {
                var howMany = when{
                    msgContent.contains("3") || msgContent.contains("三")->3
                    msgContent.contains("4") || msgContent.contains("四")->3
                    msgContent.contains("5") || msgContent.contains("五")->5
                    msgContent.contains("6") || msgContent.contains("六")->6
                    msgContent.contains("10") || msgContent.contains("十")->6
                    else -> 1
                }//数量

                var urlStr:String?
                for (i in 1..howMany)
                {
                    urlStr = safePic.yuriPicSend()
                    if(urlStr!!.length<9)
                        grp.sendMessage(urlStr!!)

                    else{
                        val url = URL(urlStr!!)
                        try {
                            event.reply(event.uploadImage(url))
                        }catch (e: Exception) {
                            e.printStackTrace()
                            println(url)
                        }
                    }
                }

            }

//            msgContent.startsWith("/百合")
//            -> {
//
//                var howMany = when{
//                    msgContent.contains("3") || msgContent.contains("三")->3
//                    msgContent.contains("5") || msgContent.contains("五")->5
//                    msgContent.contains("6") || msgContent.contains("半打")->6
//                    else -> 1
//                }//数量
//
//                val IndexStrList = sendYuriImage(howMany)
//
//                IndexStrList.forEach{
//                    val pictureR15 = this::class.java.getResource(it)
//                    try {
//                        event.reply(event.uploadImage(pictureR15))
//                        //grp.sendImage(pictureR15)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        println(it)
//                    }
//                }
//
//            }


//            msgContent.contains("配叫涩图")->{
//                grp.sendMessage(msg.quote() + event.sender.at() + "那你发给给我康康啊")
//                grp.sendMessage(Face(Face.heng))
//            }
//            msgContent.contains("叫涩图")->{
//                grp.sendMessage(msg.quote() + event.sender.at() + "那你发！！")
//                grp.sendMessage(Face(Face.aoman))//傲慢
//            }



            //这是天气模块，问题在于读取API接口的信息时乱码了，加之图灵机器人有天气功能，所以这功能就搁置了
//            msgContent.contains("天气")
//                    || msgContent.contains("How's the weather")
//                    || msgContent.contains("好使的威德")->
//            {
//                try {
//                    val weatherMap = getTodayWeather("101060101")//长春城市代码
//                    grp.sendMessage("为您播报今日天气"+
//                        weatherMap.get("city").toString() +"天气"+
//                                "，  当前气温" + weatherMap.get("temp")
//                    +"，  体感温度"+weatherMap.get("fl"))
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }

            msgContent.startsWith("/addname")-> {
                if(addBotName(msgContent.split("：")[1]))
                    grp.sendMessage("add successed")
            }

            (msgContent.contains("有狒") || msgContent.contains("有ff"))  && msgContent.length<=7-> {
                if(Random.nextInt(10) >= 3)
                    grp.sendMessage("没有狒狒")
                else
                    grp.sendMessage("no 3090 no ff14")
            }
//            Pattern.matches("(娇娇|饺饺|姣姣)姐(。。|)", msgContent) -> {
//                if(event.sender.specialTitle=="萌音")//群头衔
//                    grp.sendMessage("该下狒狒了。。")
////                else{
////                    if(Random.nextInt(10) >= 4)
////                        grp.sendMessage("戒管了")
////                    else
////                        grp.sendMessage("该看管了")
////                }
//            }

//            msgContent.contentEquals("姐姐大人觉得很赞") -> {
//                val path = "/QuestionAnswer/onesama.jpg"
//                val picture = this::class.java.getResource(path)
//                event.reply(event.uploadImage(picture))
//
//            }



            Pattern.matches("(|.)(|.)(快|)月底(|.)", msgContent) ||
                    Pattern.matches("(|你)(|的)稿子(|.)", msgContent)-> {


                if(event.sender.specialTitle == "姐姐大人")
                {
                    for (i in 1..5)
                    {
                        val path = "/QuestionAnswer/simekiri$i.jpg"
                        val picture = this::class.java.getResource(path)
                        try {
                            event.reply(event.uploadImage(picture))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            println(path)
                        }
                    }
                    return
                }


                val indexNum = Random.nextInt(1,5+1)
//                val path = "${BotHelper.dataFolder.path}\\QuestionAnswer\\simekiri$indexNum.jpg"
                val path = "/QuestionAnswer/simekiri$indexNum.jpg"
                val picture = this::class.java.getResource(path)
                try {
                    event.reply(event.uploadImage(picture))
                } catch (e: Exception) {
                    e.printStackTrace()
                    println(path)
                }
            }

            Pattern.matches("^(小心|警惕|抵制).*", msgContent) && containsBotName(msgContent)-> {
                event.reply("不准$msgContent")
            }
            Pattern.matches("^(不准不准).*", msgContent) && containsBotName(msgContent)-> {
                event.reply("不准$msgContent")
            }



            Pattern.matches(".*你是男([生的人]|孩[子纸])就好了.*", msgContent)
                    || Pattern.matches("(|来点|是|/|小心|这就是|.)直女(.|)", msgContent)-> {
                if(Random.nextInt(10) >= 4)
                    grp.sendMessage("不要直女")
                else
                    grp.sendMessage("小心直女")
            }
            msgContent.contains("直女不香么") -> {
                if(Random.nextInt(10) >= 4)
                    grp.sendMessage("不香，没我香")
                else
                    grp.sendMessage("不香，哪里香了")
            }

            msgContent.endsWith("+2") -> {
                grp.sendMessage(msgContent.replace("+2","+3"))
            }

            //白衣组专属复读
            isHKIRepeater(msgContent)-> {

                //下面的代码是解决：短时间内多人发送了关键词，或者大家在复读关键词时，如何保证bot只复读一次
                //利用hashSet的元素互异性，使得相同的句子不被读入多次

                if(repeat.add(msgContent) == true)//如果你成功录入，说明是新句子，可以复读，并更新句子副本
                {
                    //群里可能出现，一句话能被断断续续的复读多次，其中时间间隔特别长的情况，于是不能用时间作为标准
                    //应该还是以互异性为准，保证过了这波复读之后再纳入新的句子
                    if(repeat.size>=2  && msgContent_Copy != "")//如果纳入了新的 含关键字的句子，说明上一句已经没用了，可以将其去掉
                        repeat.remove(msgContent_Copy)

                    //复读，备份
                    grp.sendMessage(msgContent)//复读，将句子原封不动的发送回去
                    msgContent_Copy = msgContent

                }
            }




//            msgContent.contains("傻逼")
//                    ||msgContent.contains("你妈")
//                    ||msgContent.contains("智障")
//                    ||msgContent.contains("sb")
//            -> {
//                if (grp.botPermission.isOperator())
//                {
//                    msg.recall()
//                    grp.sendMessage("不许素质用语").recallIn(3000) // 3 秒后自动撤回这条消息
//                }
//
//            }

//            msgContent.contains("香织叫叫他")->{
//                val targetId = event.message[At]!!.target
//                val target = event.group[targetId]
//                grp.sendMessage(target.at())
//
//            }

//            msgContent.contains("解禁")->{
//                if (!BotHelper.memberIsAdmin(event.sender))//非特权人员无法解禁
//                    return
//                val targetId = event.message[At]!!.target
//                val target = event.group[targetId]
//                if (grp.botPermission.isOperator()){
//                    target.unmute()
//                    grp.sendMessage("赐予你爱与温柔")
//                }
//                else
//                    grp.sendMessage("我还不是管理员。。")
//
//
//            }


            containsBotName(msgContent) &&
                    (msgContent.contains("谢谢")
                            ) ->{
                grp.sendMessage("どういたしまして")
            }

            containsBotName(msgContent) &&
                    (msgContent.contains("睡觉")
                            || msgContent.contains("睡你")
                            || msgContent.contains("困觉")
                            ) ->{
                grp.sendMessage("お添い寝してあげようか")
                grp.sendMessage(Face(Face.yueliang))
            }

            containsBotName(msgContent) &&
                    (msgContent.contains("抱抱")) ->{

                if(Random.nextInt(10)>6) {
                    grp.sendMessage(Face(Face.baobao))
                    grp.sendMessage("よしよし～")
                }
                else {
                    grp.sendMessage(Face(Face.yongbao))
                    grp.sendMessage("蹭蹭～")
                }

            }


//            //主动被禁言
//            msgContent == "给我精致睡眠" -> {
//                if(BotHelper.memberIsAdmin(event.sender))
//                    grp.sendMessage("ゆめの中でお会いしましょう(❁´◡`❁)")
//                else
//                {
//                    if (grp.botPermission.isOperator())
//                    {
//                        event.sender.mute(5 * 60 * 60)
//                        grp.sendMessage("祝您好梦(❁´◡`❁)")
//                    }
//
//                }
//            }

            msgContent.contains("早上好") ||
                    msgContent.contains("おはよう") ||
                    msgContent.contains("早啊")-> {
                //val picture = this::class.java.getResource("/QuestionAnswer/morning.jpg")
                //grp.sendImage(picture)

                if (Random.nextInt(1,6)>2)
                    grp.sendMessage("ごきげんよう、今日も一日がんばってね～")
                else{
                    grp.sendMessage("ごきげんよう、"+event.sender.specialTitle)

                }
                grp.sendMessage(Face(Face.taiyang))

            }


            msgContent.contains("おやすみ")
                    || msgContent.contains("晚安")
                    || msgContent.contains("睡了") && msgContent.length<=4
            -> {
//                val num = Random.nextInt(1, 3 + 1)//左开右闭区间
//                val PicNmae = "/QuestionAnswer/goodNight"+num+".jpg"
//                val picture = this::class.java.getResource(PicNmae)
//                grp.sendImage(picture)

                val str:String = when {
                    Random.nextInt(1, 6)>2 -> "、${event.sender.specialTitle}"
                    else -> ""
                }
                    grp.sendMessage("お休みなさい$str")

                //无论是谁都会被禁言
                if (grp.botPermission.isOperator())
                    event.sender.mute(5 * 60 * 60)
            }

//            containsBotName(msgContent) && msgContent.contains("爬") -> {
//                if (BotHelper.memberIsAdmin(event.sender)) {
//                    //val num = Random.nextInt(1, 2 + 1)//左开右闭区间
////                    val PicNmae = "/QuestionAnswer/naku"+num+".jpg"
////                    val picture = this::class.java.getResource(PicNmae)
////                    grp.sendImage(picture)
//                    grp.sendMessage("呜呜呜，不要欺负我( TдT)")
//                } else {
//                    if (grp.botPermission.isOperator()) {
//                        event.sender.mute(Random.nextInt(1, 120) * 60)
//                    }
////                    val picture = this::class.java.getResource("/QuestionAnswer/zhazha.jpg")
////                    grp.sendImage(picture)
//                    grp.sendMessage("谁爬还不一定呢")//你爬( `д´)
//                }
//            }

            containsBotName(msgContent) && msgContent.contains("傻") -> {
                grp.sendMessage("かおりん才不傻呢！(>д<)")
            }

//            Pattern.matches(".*(t|T|踢)了(.|)",msgContent) && msgContent.contains("bot")
//            -> {
//                val picture = this::class.java.getResource("/QuestionAnswer/gomen.png")
//                event.reply(event.uploadImage(picture))
//            }

            Pattern.matches("(|我)(好|太|.|)(累|困)(死我了|.|)",msgContent)
                    || msgContent.contains("辛苦了")
                    || msgContent.contains("下班了")
                    || msgContent.contains("想下班")
            -> {
                val picture = this::class.java.getResource("/QuestionAnswer/otukare.png")
                event.reply(event.uploadImage(picture))
            }


            Pattern.matches("(我|)(好|太|真的|快|.|)(难|苦|哭)(了|.|)(.|)",msgContent)
                    ||Pattern.matches("(你|)(会|)没事的",msgContent)
                    ||msgContent.contentEquals("呜呜")
            ->{
                val picture = this::class.java.getResource("/QuestionAnswer/nagusame1.jpg")
                event.reply(event.uploadImage(picture))
            }

//            containsBotName(msgContent) && msgContent.contains("萌")  -> {
////                val picture = this::class.java.getResource("/QuestionAnswer/nya.jpg")
////                grp.sendImage(picture)
//            }

            containsBotName(msgContent) &&
                    (msgContent.contains("可爱")
                    || msgContent.contains("乖")
                    || msgContent.contains("好看")
                    || msgContent.contains("天使")
                    || msgContent.contains("卡哇伊")
                    || msgContent.contains("かわいい")
                            || msgContent.contains("可愛い")
                            || msgContent.contains("干得好")
                            || msgContent.contains("聪明")
                            || msgContent.contains("夸夸香织织"))  -> {

                when(Random.nextInt(0,5)){
                    0->   {grp.sendMessage("欸嘿o(*≧▽≦)ツ")}
                    1 -> {  grp.sendMessage("耶～")}
                    2,3 -> {grp.sendMessage("ヾ(≧▽≦*)o")}
                    4,5-> { grp.sendMessage("(*/ω＼*)")}
//                    else->{}
                }
                return;

            }

            containsBotName(msgContent) &&
                    (msgContent.contains("夸夸")
                            || msgContent.contains("夸奖")) ->{
                grp.sendMessage(Homeru.chp)
            }

            containsBotName(msgContent) && (msgContent.contains("膝枕")  || msgContent.contains("ひざまくら"))-> {

                if(Random.nextInt(10)>4) {
                    grp.sendMessage("はい、どうぞ～")
                }
                else {

                    grp.sendMessage("はいはい～よしよし")
                }
            }


            containsBotName(msgContent) && msgContent.contains("出来")  -> {
//                try {
//
//                    var num = Random.nextInt(1, 11 + 1)
//                    var IndexStr = ""
//
//                    IndexStr = "/QuestionAnswer/deru$num.jpg"
//
//                    val picture = this::class.java.getResource(IndexStr)
//                    try {
//                        grp.sendImage(picture)
//                    } catch (e: Exception) {
//                        System.err.println(num)
//                        System.err.println(IndexStr)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }

                grp.sendMessage("来～了～")
            }

            containsBotName(msgContent) && (msgContent.contains("亲亲") ||
                                            msgContent.contains("啾啾") ||
                                            msgContent.contains("mua")  ||
                                            msgContent.contains("kiss")) -> {

                when(Random.nextInt(0,6)){
                    0->   {grp.sendMessage("${event.sender.specialTitle}～大好き～")}
                    1 -> {  grp.sendMessage("对不起，你是个好人...")}
                    2,3,4 -> {grp.sendMessage("うれしい～ヾ(≧▽≦*)o")}
                    5,6-> { grp.sendMessage("は、恥ずかしいよ～ (*/ω＼*)")}
//                    else->{}
                }


//                if (Random.nextInt(10)>6) {
////                    val picture = this::class.java.getResource("/QuestionAnswer/love1.jpg")
////                    grp.sendImage(picture)
//                    grp.sendMessage("うれしい～ヾ(≧▽≦*)o")
//                }
//                if (Random.nextInt(10)>5) {
////                    val picture = this::class.java.getResource("/QuestionAnswer/love1.jpg")
////                    grp.sendImage(picture)
//                    grp.sendMessage("は、恥ずかしいよ～ (*/ω＼*)")//不要啾啾我⊂彡☆))∀`)
//                }
            }


//            containsBotName(msgContent) && (msgContent.contains("日我") ||
//                                            msgContent.contains("上我") ||
//                                            msgContent.contains("曰我")) -> {
//                val picture = this::class.java.getResource("/QuestionAnswer/？.png")
//                grp.sendImage(picture)
//                grp.sendMessage("えっ？こ...こわい...")//你不对劲，你有问题，你快点爬(`ヮ´ )
//                if (grp.botPermission.isOperator()) {
//                    event.sender.mute(Random.nextInt(1, 120) * 60)
//                }
//            }

//            containsBotName(msgContent) && msgContent.contains("活着") -> {
//                val answers = listOf(
//                    "もちろん！",
//                    "いるよ",
//                    "いる...かな？"
//                )
//                grp.sendMessage(answers[Random.nextInt(answers.size)])
//            }

            msgContent.contentEquals("/自我介绍") -> {

                grp.sendMessage(
                    "我是由心葉开发的基于Mirai白衣群御用人工智障群聊bot，不会做饭，只会水群，无情复读姬。" +
                            "发送给功能列表可以查看我能干什么，现在还很弱鸡，但是未来可期。（只要有大佬一起码代码的话:" +
                            "https://github.com/KonohaVio/KonohaChat"
                )
            }
            msgContent.contentEquals("/交出代码") -> {

                grp.sendMessage(
                    "为大佬献上代码 " +
                            "https://github.com/KonohaVio/KonohaChat"
                )
            }
            msgContent.contentEquals("/功能列表") -> {
                grp.sendMessage(BotHelper.functionsToString(event.group.id))
            }


            contentEqualsBotName(msgContent) -> {
                val random = Random(System.currentTimeMillis())
                if(random.nextInt(10)>4) {
                    val senderName = when {
                        event.sender.specialTitle == "toki" -> "tokiki"
                        event.sender.specialTitle == "贝贝哥" -> "被哥哥。。"
                        event.sender.specialTitle == "赤色色" -> "色色大佬"
                        event.sender.specialTitle == "豆腐" -> "df大佬"
                        event.sender.specialTitle == "X" -> "x大佬"
                        event.sender.specialTitle == "萌音" -> "萌音音"

                        else -> event.sender.specialTitle
                    }
                    grp.sendMessage("${senderName}")
                }

                else{
                    val answers = listOf(
                        "お呼びでしょうか",
                        "はい",
                        "哎～",
                        "我来了～～",
                        "嗯嗯"
                    )

//                    val dalao = when {//叫不叫大佬，随着脸熟度而变化
//                        random.nextInt(10) > 6 -> "大佬，"
//                        else -> "，"
//                    }
                    val idx = Random.nextInt(0, answers.size)
                    grp.sendMessage(answers[idx])
                }
//                if(Random.nextInt(10)>6)
//                {
//                    val picture = this::class.java.getResource("/QuestionAnswer/hello.png")
//                    event.reply(event.uploadImage(picture))
//                }
            }
        }
    }
}
