package com.mrwhoami.qqservices

import com.mrwhoami.qqservices.file.Config
import com.mrwhoami.qqservices.function.*
import com.mrwhoami.qqservices.function.colorphoto.ColorPhoto
import com.mrwhoami.qqservices.util.BasicUtil
import com.mrwhoami.qqservices.util.interfaces.FunctionListener
import com.mrwhoami.qqservices.util.plugin.Logger
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.*
import net.mamoe.mirai.message.*
import net.mamoe.mirai.message.data.*
import java.lang.Thread.sleep
import kotlin.math.roundToLong
import kotlin.random.Random



var randomTrue :Long = 0

fun Bot.messageDSL(){



    val listeners = ArrayList<FunctionListener>()
    val listenersClass = arrayOf<Class<*>>(
        KeyWord::class.java,
        Bilibili::class.java,
        Hitokoto::class.java,
        PictureSearch::class.java,
        //Notice::class.java,
        ColorPhoto::class.java,
        //Dynamic::class.java,
        Baike::class.java,
        Nbnhhsh::class.java ,
        SafebooruPic::class.java
    )

    listenersClass.forEach {
        listeners.add(it.getConstructor().newInstance() as FunctionListener)
        Logger.log("注册功能: ${it.simpleName}", Logger.LogLevel.INFO)
    }

    this.subscribeGroupMessages{
        always {
            listeners.forEach {
                BotHelper.scheduler.asyncTask {
                    it.execute(this, this.message.contentToString(), this.message[Image], this.message[Face])
                }
            }
        }
    }





//这里提供了和QuestionAnswer稍微不一样的回复方式，具体请ctrl+左键点击那些函数的定义查看细节

    var subscribe1 = this.subscribeMessages{
        //this是bot

        //var RePicFlag = false//复读图片的开关
        //"a" reply "b" 这是个模式，收到a，回复b
        "atall" reply AtAll
//        "这bot可以t了" reply Face(Face.liulei)//发qq原生表情，具体有哪些可以点入Face的定义
        //"语音" reply Voice("vioce.m4a")//具体得看
        "戳一戳" reply PokeMessage.Poke
        "比心" reply PokeMessage.ShowLove



//        case("repic") {
//            RePicFlag = true
//            reply("成功开启图片复读"+RePicFlag)
//        }
//        case("unrepic") {
//            RePicFlag = false
//            reply("成功取消图片复读"+RePicFlag)
//        }

        case("yuriimg") {
            YuriImageSendFlag = true
            reply("成功开启yuri推送$YuriImageSendFlag")
        }
        case("unyuriimg") {
            YuriImageSendFlag = false
            reply("成功取消yuri推送$YuriImageSendFlag")
        }
        case("isyuriimg") {
            reply("现在YuriImageSendFlag=$YuriImageSendFlag")
        }
        case("yuricd ") {
            YuriImagCD = BasicUtil.extractInt(message.toString())
            reply("现在YuriImageSendFlag=$YuriImagCD")
        }





        //如果消息包含以下类型，如Image
        has<Image> {
            val imageID= "${message[Image]}" //获取第一个 Image 类型的消息

            //图片id如何获得？因为bot收到信息会在控制台打印一份给你，所以可以复制下来
//            if(ID == "[mirai:image:{F08A4F0C-E431-C279-D21A-991FFFC06E0E}.mirai]"){//不够色
//                reply("你发一张给我康康啊")
//                //如果ta真发图片了
//                val value = nextMessage { message.any(Image)}//下一条信息
//                //反过来嘲讽ta：这也叫涩图？！
//                val picture = this::class.java.getResource("/QuestionAnswer/Hdame2.jpg")
//                this.sendImage(picture)
//            }

            //猫村大佬可了不得复读
            if(imageID == "[mirai:image:{61A9570E-F327-802C-A72E-D88194756FA6}.mirai]")
                reply(message)
            if(imageID == "[mirai:image:{A40F272C-CD8B-4AB8-752F-85F7E86AF78E}.mirai]")
                reply(message)


//            if(RePicFlag == true)//无情的图片复读，实用性不高
//            {
//                reply(message)
//            }
        }

        //包含关键词
//        contains("a") {
//            reply("b")
//        }

    }

    subscribeGroupMessages{

//        startsWith("//shuffle random")
//        {
//            if(BotHelper.memberIsBotOwner(sender)) {
//
//                var buffer = ""
//                for (msg in message) {
//                    if (msg.isContentEmpty()) continue
//                    else if (msg.isPlain()) {
//                        buffer += msg.content
//                    } else continue
//                }
//                var megText1: String? = ""
//                if (buffer.isNotEmpty()) {
//                    megText1 = buffer
//                }
//
//                val parms = megText1!!.split(":")
//                if(parms.size>=2)
//                {
//                    randomTrue = BasicUtil.extractInt(parms[1]).toLong()
//                    reply("shuffle random is $randomTrue now!")
//                }
//            }
//        }
//
//
//        startsWith("/shuffle")
//        {
//            if(BotHelper.memberIsBotOwner(sender))
//            {
//                reply("准备进行第二届白衣麻将脱衣杯分组 y/n")
//                var nextMes = nextMessage()
//                if(nextMes.contentEquals("y"))
//                {
//
//                    jobShuffle!!.start()
//                    reply("正在开始分组......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("正在从大气噪声中获得真随机数......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("获得真随机数为:$randomTrue o(*≧▽≦)ツ")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("开始随缘分组......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("正在注入灵魂......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("开始人工智能干预\n(こっそりと)......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("抽空去维护一下世界和平......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//
//                    reply("正在揣测各位神仙的隐藏实力\n(集中意念)......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    reply("随带预测一下有奖竞猜......")
//                    sleep((Random.nextDouble(1.0,3.0)*1000).roundToLong())
//
//                    reply(".")
//                    sleep(1000L)
//                    reply("..")
//                    sleep(1000L)
//                    reply("...")
//                    sleep(1000L)
//
//                    reply("啊")
//                    sleep(500L)
//
//                    reply("分组结果出来啦！将将将将！......")
//                    sleep((Random.nextDouble(1.0,4.0)*1000).roundToLong())
//
//                    var count = 0
//                    var str :String? =""
//                    for(i in 1..8)
//                    {
//                        str=
//                                ">>>>>>>>>>>>>>>>>>\n"+
//                                "第${i}组:\n"+
//                                "|--${playerList[count++]}\n"+
//                                "|--${playerList[count++]}\n"+
//                                "|--${playerList[count++]}\n"+
//                                "|--${playerList[count++]}\n"+
//                                "<<<<<<<<<<<<<<<<<<"
//
//
//                        reply(str)
//                        sleep(4*1000L)
//
//                    }
//
//
//                    reply("---------------")
//                    reply("以上就是所有分组啦！诸君加油ヾ(≧▽≦*)o")
//
//
//
//
//                }
//                else
//                    reply("那等准备好了再叫香织吧")
//
//
//
//
//
//            }
//        }



        startsWith("！toptag"){
            if(BotHelper.memberIsBotOwner(sender))
            {
                var buffer = ""
                for (msg in message) {
                    if (msg.isContentEmpty()) continue
                    else if (msg.isPlain()) {
                        buffer += msg.content
                    } else continue
                }
                var megText1 :String? = ""
                if (buffer.isNotEmpty()) {
                    megText1 = buffer
                }
                val splitText = megText1?.split(":")
                safePic!!.tag_top = splitText?.get(1)
                reply("tag_top is ${safePic!!.tag_top} now.")
            }
        }


        startsWith("！send"){
            if(BotHelper.memberIsBotOwner(sender))
            {
                var buffer = ""
                for (msg in message) {
                    if (msg.isContentEmpty()) continue
                    else if (msg.isPlain()) {
                        buffer += msg.content
                    } else continue
                }
                var megText1 :String? = ""
                if (buffer.isNotEmpty()) {
                    megText1 = buffer
                }
                val splitText = megText1?.split(":")
                val messageText:String? = splitText?.get(1)
                var groupId = Config.data.yuriImg//默认白衣
                if(splitText!!.size >= 3)
                {
                    if(splitText[2]=="test")
                        groupId = Config.data.testGroup
                }

                if (messageText != null) {
                    if(messageText == "pic")
                    {
                        reply("pic")
                        val picture = nextMessage()
                        getGroup(groupId).sendMessage(picture)
                    }

                    else
                        getGroup(groupId).sendMessage(messageText)
                }
            }
        }

        startsWith("//Good Night;;") {
            if(BotHelper.memberIsBotOwner(sender)){
                reply("Close the miraiBot ? y/n")
                var nextMes = nextMessage()
                var buffer:String? = ""
                for (msg in nextMes) {
                    if (msg.isContentEmpty()) continue
                    else if (msg.isPlain()) {
                        buffer += msg.content
                    } else continue
                }
                if(buffer!! == "y"){
                    reply("次会えるのはいつかな、楽しみだな...")
                    jobClose!!.start()

                }
            }
        }




        atBot(){
            reply("はい")
        }

//        startsWith("群名=") {
//            if (!sender.isOperator()) {
//                sender.mute(5)
//                return@startsWith
//            }
//            else
//                group.name = it
//        }
//        startsWith("/改名") {
//            if (!BotHelper.memberIsAdmin(sender)) {
//                sender.mute(5)
//                Face(Face.qiaoda).plus("大胆！")?.let { it1 -> reply(it1) }
//            }
//            if(!this.group.botPermission.isOperator())
//            {
//                reply("我还不是管理员，爱莫能助")
//            }
//            else
//            {
//                reply("主人请艾特一位幸运儿")
//                var atBy: At? = nextMessage { message.any(At) }[At]
//                var theMember = atBy?.asMember()
//
//                if(this.message.contentEquals("/改名++"))//改群名片＋改群头衔
//                {
//                    reply("请赐予ta新的乌纱帽")
//                    var nextMes = nextMessage()
//                    var buffer = ""
//                    for (msg in nextMes) {
//                        if (msg.isContentEmpty()) continue
//                        else if (msg.isPlain()) {
//                            buffer += msg.content
//                        } else continue
//                    }
//                    var megText :String? = ""
//                    if (!buffer.isEmpty())
//                    {
//                        megText = buffer
//                        println(megText)
//                        theMember?.specialTitle = megText
//                    }
//                }
//
//                reply("以后叫ta什么？")
//                var nextMes = nextMessage()
//                var buffer = ""
//                for (msg in nextMes) {
//                    if (msg.isContentEmpty()) continue
//                    else if (msg.isPlain()) {
//                        buffer += msg.content
//                    } else continue
//                }
//                var megText :String? = ""
//                if (!buffer.isEmpty())
//                {
//                    megText = buffer
//                    //println(megText)
//                    theMember?.nameCard = megText
//                    theMember?.at()?.plus(",你好呀")?.let { it1 -> reply(it1) }
//
//                }
//
//            }
//
//        }
    }
}

