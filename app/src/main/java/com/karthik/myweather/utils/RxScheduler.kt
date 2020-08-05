package com.karthik.myweather.utils

import io.reactivex.Scheduler

interface RxScheduler {
    fun io(): Scheduler?
    fun mainThread(): Scheduler?
}