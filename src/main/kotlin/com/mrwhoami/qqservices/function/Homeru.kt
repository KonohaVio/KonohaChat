package com.mrwhoami.qqservices.function

import lombok.experimental.UtilityClass
import net.dreamlu.mica.http.HttpRequest


/**
 * author pt
 * email plexpt@gmail.com
 * date 2020-04-14 16:02
 */
@UtilityClass
class Homeru {

    init {
        BotHelper.registerFunctions("夸奖", listOf("蕾姆夸我","蕾姆我要夸奖"))
    }
    companion object {
        val chp: String
            get() {
                val url = "https://chp.shadiao.app/api.php"
                return HttpRequest.get(url).execute().asString()
            }
    }


}