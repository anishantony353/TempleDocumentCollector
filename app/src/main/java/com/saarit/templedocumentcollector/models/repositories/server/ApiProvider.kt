package com.saarit.templedocumentcollector.models.repositories.server

import com.google.gson.GsonBuilder
import com.saarit.templedocumentcollector.utils.Constant
import com.saarit.templedocumentcollector.utils.Util
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {
    private val TAG:String = ApiProvider::class.java.simpleName

     lateinit var apiClient:ApiClient

    fun getClient():ApiClient{
        Util.log(TAG,"getClient()")
        if(!::apiClient.isInitialized){

            Util.log(TAG,"getClient()..apiClient is null")

            val gson = GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.APP_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            apiClient = retrofit.create(ApiClient::class.java)

        }
        return apiClient
    }
}