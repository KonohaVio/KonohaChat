package com.mrwhoami.qqservices.data

import com.mrwhoami.qqservices.function.BotHelper
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


class URLSetData{
    data class URLSet(val url: String)
    data class URLSetChain(val hashSet: HashSet<URLSet> = HashSet())
}

