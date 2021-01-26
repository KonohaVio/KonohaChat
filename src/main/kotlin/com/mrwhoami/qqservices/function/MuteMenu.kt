package com.mrwhoami.qqservices.function;

import mu.KotlinLogging
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*

class MuteMenu {

    private val logger = KotlinLogging.logger {}

    init {
        BotHelper.registerFunctions("禁言套餐", listOf("我要休息x分钟", "我要休息x小时"))
    }

    suspend fun onGrpMsg (event: GroupMessageEvent) {
        // Check if the bot is a admin in the group
        if (! event.group.botAsMember.isOperator() ){//如果bot不是管理员or群主，直接退出
            return
        }
        // Check if this is text message
        val msg = event.message
        if (!msg.all{ block -> block.isContentEmpty() || block.isPlain() }) {
            return
        }
        // Check if this is an order
        val msgContent = msg.content
        if (!msgContent.contains("我要休息") && !msgContent.contains("我睡了")) {
            return
        }


        val customer = event.sender
        // Check for the privilege
        if (BotHelper.memberIsAdmin(customer)) {
            event.group.sendMessage(customer.at() + "请好好休息吧，我的主人")
            return
        }
        // Check for the unit
        val scale = when {
            msgContent.contains("小时") -> 60 * 60
            msgContent.contains("分钟") -> 60
            else -> {
                event.group.sendMessage(customer.at() + "请设置合适的时间。目前不支持除了小时与分钟以外的时间单位，不支持小数点，且不能混用，否则……嘿嘿……")
                return
            }
        }
        // Parse time and check.
        val time = msgContent.filter { it.isDigit() }
        var timeNum = -1
        if (time.isEmpty() || time.length > 6) {
            val timeChar = mapOf<Char, Int>(
                Pair('一', 1), Pair('二', 2), Pair('两', 2), Pair('俩', 2), Pair('三', 3),
                Pair('四', 4), Pair('五', 5), Pair('六', 6),
                Pair('七', 7), Pair('八', 8), Pair('九', 9))
            if (msgContent.any { timeChar.any { p: Map.Entry<Char, Int> -> p.key == it }}) {
                // Convert Time
                var current = 0
                var total = 0
                msgContent.forEach {
                    if (it == '十') {
                        if (current == 0) total += 10
                        else {
                            total += current * 10
                            current = 0
                        }
                    }
                    if (it == '百') {
                        total += current * 100
                        current = 0
                    }
                    current += timeChar.getOrDefault(it, 0)
                }
                total += current
                if (total > 0) timeNum = total * scale
            }
            else {
                event.group.sendMessage(customer.at() + "你这个时间不对劲，我看不懂，先来24小时吧")
                timeNum = 24 * 60 * 60
            }
        }
        else timeNum = time.toInt() * scale
        if (time.toInt() == 114514 || time.toInt() == 364364 || time.toInt() == 889464) {
            customer.mute(1200)
            event.group.sendMessage("以为我看不懂么，爬！( `д´)")
            return
        }
        if (timeNum < 0 || timeNum >= 2592000) {
            event.group.sendMessage(customer.at() + "你这个时间太长了，先来24小时吧")
            timeNum = 24 * 60 * 60
        }
        if (timeNum == 0) {
            event.group.sendMessage("不点餐在这玩什么呢(|||ﾟдﾟ)")
            return
        }
        // Now, complete the order
        customer.mute(timeNum)
        event.group.sendMessage("欢迎使用禁言套餐，您的餐齐了，请慢用")
    }
}
