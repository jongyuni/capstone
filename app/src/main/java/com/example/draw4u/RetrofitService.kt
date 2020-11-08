package com.example.draw4u

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface RetrofitService {
    @GET("keyword_abstract/{content}")
    fun getkeyword(@Path("content") content: String?): Call<ResultKeyword>

    @GET("picture/{keyword}")
    fun getimage(@Path("keyword") keyword: String?): Call<ResultImage>

    @GET
    fun fetchCaptcha(@Url url: String?): Call<ResponseBody?>?
}

