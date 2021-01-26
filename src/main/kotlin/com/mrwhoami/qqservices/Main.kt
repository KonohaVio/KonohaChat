package com.mrwhoami.qqservices


import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mrwhoami.qqservices.data.BotData
import com.mrwhoami.qqservices.file.Config
import com.mrwhoami.qqservices.function.*
import com.mrwhoami.qqservices.function.SafebooruPic
import com.mrwhoami.qqservices.util.FileManager
import com.mrwhoami.qqservices.util.plugin.Logger
import kotlinx.coroutines.*
import mu.KotlinLogging
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.join
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.sendImage
import net.mamoe.mirai.message.uploadImage
import net.mamoe.mirai.utils.BotConfiguration
import java.lang.management.ManagementFactory
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*



val logger = KotlinLogging.logger {}

var safePic :SafebooruPic?= null

var YuriImageSendFlag = false

var YuriImagCD :Int = 1


var jobClose :Job? = null







suspend fun main() {
    // Login QQ. Use another data class to avoid password tracking.
    //val login = BotInitInfo()//login.botQQId


    val runtimeMX = ManagementFactory.getRuntimeMXBean()
    val osMX = ManagementFactory.getOperatingSystemMXBean()
    if (runtimeMX != null && osMX != null) {
        val javaInfo = "Java " + runtimeMX.specVersion + " (" + runtimeMX.vmName + " " + runtimeMX.vmVersion + ")"
        val osInfo = "Host: " + osMX.name + " " + osMX.version + " (" + osMX.arch + ")"
        println("System Info: $javaInfo $osInfo")
    } else {
        println("Unable to read system info")
    }

    BotData.objectMapper = jacksonObjectMapper().also { it.propertyNamingStrategy = PropertyNamingStrategy.LOWER_CASE }
    BotHelper.botConfig = BotConfiguration.Default.also {
        it.randomDeviceInfo()
    }
    FileManager.readValue()

    val miraiBot = Bot(Config.data.account, Config.data.password) {
        fileBasedDeviceInfo("device.json")
    }.alsoLogin()

    miraiBot.getGroup(Config.data.testGroup).sendMessage("おはよう、今日もいい天気。風立ちぬ、いざ生きめやも。")
    logger.info { "${Config.data.account} is logged in." }
//    logger.info { "${Config.data.botOwnerQQId } is the master." }


    // Initialize helper
    BotHelper.loadbotOwner()
    // Initialize services

    safePic =  SafebooruPic()

    val qAndA = QuestionAnswer()
    val repeater = Repeater()
    val voteBan = VoteBan()
    val muteMenu = MuteMenu()
    val welcome = Welcome(miraiBot.groups)
    //val groupDaily = GroupDaily()

    logger.info { "Initialization finished." }

    miraiBot.messageDSL()


    miraiBot.subscribeAlways<GroupMessageEvent> {
        // repeater behaviour
        qAndA.onGrpMsg(it)
        repeater.onGrpMsg(it)
        voteBan.onGrpMsg(it)
        muteMenu.onGrpMsg(it)
        //welcome.onGrpMsg(it)
        //groupDaily.onGrpMsg(it)//群日志未启用
    }

    miraiBot.subscribeAlways<MemberJoinEvent> {
        welcome.onMemberJoin(it)
    }



    // Check per minute
    GlobalScope.launch {
        while (miraiBot.isActive) {
            logger.info { "5-min heart beat event." }
            delay(60 *5* 1000L)
        }
    }

    // Check per hour
    GlobalScope.launch {
        while (miraiBot.isActive) {

            logger.info { "1-hour heart beat event." }
            //groupDaily.onHourWake(miraiBot)
            delay(60 * 60 * 1000L)
        }
    }

    GlobalScope.launch {
        while (miraiBot.isActive) {
            if(YuriImageSendFlag) {
                val yuriImageUrlStr = safePic!!.yuriTimer()
                if(yuriImageUrlStr.length < 9)//没发图时告知我原因
                    miraiBot.getGroup(Config.data.testGroup).sendMessage(yuriImageUrlStr)
                else {//有两种类型的无效，其说明字符的长度都小于9，最高为8
                    val url = URL(yuriImageUrlStr)
                    val group = miraiBot.getGroup(Config.data.yuriImg)
                    group.sendMessage(group.uploadImage(url))
                }//if的{

                logger.info { "$YuriImagCD -hour heart beat event.<<<<<<" }
            }//最外面的if的括号，if(YuriImageSendFlag)
            delay(YuriImagCD * 60 * 60 * 1000L)//每隔指定时间发一次
        }

    }

    //这个协程和前面几个不一样，它不是立刻启动，而是采用了LAZY，即只在被需要的时候启动
    jobClose = GlobalScope.async(start = CoroutineStart.LAZY) {
        Logger.log(Logger.Messages.BOT_SHUTDOWN, Logger.LogLevel.CONSOLE)
        FileManager.writeValue()
        println("文件写入完成")
        miraiBot.close()
    }










    GlobalScope.launch{
        var dfHour = SimpleDateFormat("HH");
        var dfMimute = SimpleDateFormat("mm");
        var d = Date();
        var Hour_ : String
        var Minute_ :String


        while (miraiBot.isActive)
        {
            d =Date()
            Minute_ = dfMimute.format(d);
            Hour_ = dfHour.format(d);

            if(Hour_ == "07")
            {
                YuriImageSendFlag = true
                println("YuriImageSendFlag is $YuriImageSendFlag")
            }
            if(Hour_ == "02")
            {
                YuriImageSendFlag = false
                println("YuriImageSendFlag is $YuriImageSendFlag")
            }


            if(Hour_ == "11" && Integer.valueOf(Minute_) <= Integer.valueOf("30"))
            {
                val group = miraiBot.getGroup(Config.data.clockIn)//输入需要发送打卡信息的群，如果有多个，请自行更改格式为for in GroupList
                group.sendMessage("在家也还要打卡  " +
                        "https://ehall.jlu.edu.cn/taskcenter/workflow/index")
                val topImageUrlList:List<String?> = safePic!!.topImage()
                topImageUrlList.forEach {
                    var url = URL(it)
                    group.sendImage(url)
                }
            }



//            if(Hour_ == "21" && Integer.valueOf(Minute_) <= Integer.valueOf("30"))
//            {
//                val group = miraiBot.getGroup(Config.data.clockIn)
//                group.sendMessage("最后几次晚签到，签吧   " +
//                        "https://ehall.jlu.edu.cn/taskcenter/workflow/index")
//
//                val topImageUrlList:List<String?> = safePic!!.topImage()
//                topImageUrlList.forEach {
//                    var url = URL(it)
//                    group.sendImage(url)
//                }
//            }

//            if(Hour_ == "23"  && Integer.valueOf(Minute_) >= Integer.valueOf("30") )
//            {
//                //QuestionAnswer.Hayaneyou = true
//                //遍历bot加的群。
//                val group = miraiBot.getGroup(1139284923L)
//                group.sendMessage("睡觉啦！早睡早起身体好～")
//                val topImageUrlList:List<String?> = safePic.topImage()
//                topImageUrlList.forEach {
//                    var url = URL(it)
//                    group.sendImage(url)
//                }
//            }

            logger.info { "0.5 -hour heart beat event.<<<<<<" }
            delay(60 * 30 * 1000L)//半小时检测一次
        }

    }

//    Runtime.getRuntime().addShutdownHook(Thread {
//        Logger.log(Logger.Messages.BOT_SHUTDOWN, Logger.LogLevel.CONSOLE)
//        FileManager.writeValue()
//        println("文件写入完成")
//    })





    //这个很关键，千万不能删！
    miraiBot.join() // 等待 Bot 离线, 避免主线程退出

}