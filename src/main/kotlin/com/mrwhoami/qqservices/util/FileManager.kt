package com.mrwhoami.qqservices.util

import com.mrwhoami.qqservices.file.*
import com.mrwhoami.qqservices.util.interfaces.file.FileManipulate

class AccountNotFoundException : Exception {
    constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
}

object FileManager : AbstractFile() {
    private val fileList = setOf(
            Config, NoticeFile,KeyWordFile,FileterTagFile,URLSetFile
    )

    @Throws(AccountNotFoundException::class)
    override fun readValue() {
        fileList.forEach(FileManipulate::writeDefault)
        if (Config.data.account == 0L || Config.data.password.isEmpty())
            throw AccountNotFoundException("账号信息未填写，请找到build目录下的config.josn文件填入相应信息，(Win用户的文件路径名可能有些奇怪...)")
    }

    override fun writeDefault() {
        throw UnsupportedOperationException()
    }

    override fun writeValue() {
        fileList.forEach(FileManipulate::writeValue)
    }
}