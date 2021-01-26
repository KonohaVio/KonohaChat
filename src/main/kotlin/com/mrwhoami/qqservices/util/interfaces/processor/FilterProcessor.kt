package com.mrwhoami.qqservices.util.interfaces.processor

interface FilterProcessor {
    fun filter(groupID: Long): Boolean {
        return false
    }
}