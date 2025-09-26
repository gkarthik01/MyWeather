package com.karthik.myweather.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.karthik.myweather.BuildConfig
import com.karthik.myweather.data.WeatherDatabase
import com.karthik.myweather.network.WeatherService
import com.karthik.myweather.utils.BusinessUtils
import com.karthik.myweather.utils.RxScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient?): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(context,
                WeatherDatabase::class.java, "weather-database")
                .build()
    }

    @Provides
    @Singleton
    fun provideSchedulerInjector(): RxScheduler {
        return object : RxScheduler {
            override fun io(): Scheduler? {
                return Schedulers.io()
            }

            override fun mainThread(): Scheduler? {
                return AndroidSchedulers.mainThread()
            }
        }
    }

    @Provides
    @Singleton
    fun provideBusinessUtils(): BusinessUtils {
        return BusinessUtils()
    }
}