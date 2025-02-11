package com.example.network.di

import android.util.Log
import com.example.network.utils.BASE_URL
import com.example.network.utils.HTTP_ERROR_TAG
import com.example.network.utils.TAG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val errorInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                Log.e(TAG, "$HTTP_ERROR_TAG ${response.code}")
            }
            response
        }

        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .addInterceptor(errorInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}