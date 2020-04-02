package com.saarit.templedocumentcollector.models.repositories.server

import com.saarit.templedocumentcollector.models.dto.Temple_master_DTO
import com.saarit.templedocumentcollector.models.objects.BaseResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiClient {

    @GET("temple/getTemples")
    fun getTempleMater():Single<Temple_master_DTO>

    @FormUrlEncoded
    @POST("user/loginuser")
    fun login(
        @Field("email") id:String,
        @Field("password") password:String
    ):Single<Temple_master_DTO>

    @Multipart
    @POST("otherforms/add_form_data")
    fun uploadData(
        @Part("type") type:RequestBody,
        @Part("temple_id") templeId:RequestBody,
        @Part("user_id") userId:RequestBody,
        @Part("no_of_images") numImages:RequestBody,
        @Part images:List<MultipartBody.Part>
    ):Single<BaseResponse>

}