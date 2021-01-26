package com.mrwhoami.qqservices.data

data class ConfigData(val protocol: String = "ANDROID_PAD",
                      val account: Long = 0,
                      val botOwnerQQId : Long = 0,
                      val password: String = "",
                      val clockIn:Long = 0,//打卡群
                      val yuriImg:Long = 0,//定时百合图的推送群
                      val testGroup:Long = 0//bebug测试群
//                      var pictureSearchAPI: String = "",
//                      var bilibiliCookie: String = "",
//                      var lanzouCookie: String = "",
//                      var disableFunction: List<String> = ArrayList()
//ANDROID_PAD
                      )

