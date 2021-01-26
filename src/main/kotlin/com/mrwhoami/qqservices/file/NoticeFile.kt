package com.mrwhoami.qqservices.file

import com.mrwhoami.qqservices.function.BotHelper
import com.mrwhoami.qqservices.data.NoticeData
import com.mrwhoami.qqservices.function.Notice
import com.mrwhoami.qqservices.util.interfaces.file.FileManipulate
import java.io.*

object NoticeFile : AbstractFile() {
    override val file = File(BotHelper.dataFolder.toString() + File.separator + "notice.dat")

    override fun writeDefault() {
        readValue()
    }

    override fun writeValue() {
        ObjectOutputStream(FileOutputStream(File(BotHelper.dataFolder.toString() + File.separator + "notice.dat"))).writeObject(Notice.data)
    }

    override fun readValue() {
        if (file.exists()) {
            Notice.data = ObjectInputStream(FileInputStream(file)).readObject() as NoticeData
        }
    }
}