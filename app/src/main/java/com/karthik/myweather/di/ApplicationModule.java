package com.karthik.myweather.di;

import android.content.Context;

import androidx.room.Room;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.karthik.myweather.BuildConfig;
import com.karthik.myweather.data.WeatherDatabase;
import com.karthik.myweather.network.WeatherService;
import com.karthik.myweather.utils.RxScheduler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {ViewModelModule.class, ContextModule.class})
public class ApplicationModule {

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public WeatherService provideWeatherService(Retrofit retrofit){
        return retrofit.create(WeatherService.class);
    }

    @Provides
    @Singleton
    static OkHttpClient provideHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @Provides
    @Singleton
    static WeatherDatabase provideWeatherDatabase(Context context) {
        return Room.databaseBuilder(context,
                WeatherDatabase.class, "weather-database")
                .build();
    }

    @Provides
    @Singleton
    static RxScheduler provideSchedulerInjector(){
        return new RxScheduler() {
            @Override
            public Scheduler io() {
                return Schedulers.io();
            }

            @Override
            public Scheduler mainThread() {
                return AndroidSchedulers.mainThread();
            }
        };
    }

}
