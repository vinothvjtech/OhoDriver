/*
 * *
 *  * Created by Nethaji on 27/6/20 2:34 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 27/6/20 2:34 PM
 *
 */

package com.mlm.recharege.network.http

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

private class HttpClientManagerImpl(
    val context: Context
) : HttpClientManager {

    override val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {

                addNetworkInterceptor(StethoInterceptor())
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.BODY
                    }
                )

        }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            //  .callTimeout(25, TimeUnit.SECONDS)
            .build()
    }

    override val retrofit = Retrofit.Builder().baseUrl("https://www.evergreenn.in/api/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okHttpClient)
        .build()
}

inline fun <reified T> HttpClientManager.createApi(): T {
    return this.retrofit.create()
}

interface HttpClientManager {
    val okHttpClient: OkHttpClient
    val retrofit: Retrofit

    companion object {
        fun newInstance(context: Context): HttpClientManager = HttpClientManagerImpl(context)
    }
}