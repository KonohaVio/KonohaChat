package com.mrwhoami.qqservices.data

import com.mrwhoami.qqservices.function.BotHelper
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


class FilterTagData{
    data class filterTagChain(val list: HashSet<String> = hashSetOf())
}

