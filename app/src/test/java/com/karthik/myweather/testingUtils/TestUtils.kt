package com.karthik.myweather.testingUtils

import org.mockito.Mockito

fun <T> anyNotNull(): T {
    Mockito.any<T>()
    return uninitialized()
}

fun <T> anyNotNull(type: Class<T>): T {
    Mockito.any<T>(type)
    return uninitialized()
}

private fun <T> uninitialized(): T = null as T