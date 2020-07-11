package com.karthik.myweather.utils;

import io.reactivex.Scheduler;

public interface RxScheduler {
    Scheduler io();
    Scheduler mainThread();
}
