package com.mrwhoami.qqservices.util.interfaces.file

import java.io.File

interface FileManipulate {
    fun writeDefault()

    fun writeValue()

    fun readValue()

    val file: File
}