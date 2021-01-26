package com.mrwhoami.qqservices.util.interfaces

import com.mrwhoami.qqservices.util.pictureSearchUtil.Result


interface PictureSearchSource {
    fun search(url: String): List<Result>
}