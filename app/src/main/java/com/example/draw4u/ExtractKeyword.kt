package com.example.draw4u

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.okhttp.*
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class ExtractKeyword : AppCompatActivity() {
    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fname: String?
        val content: String?
        val intent: Intent
        intent = getIntent()
        fname = intent.getStringExtra("fname")
        content = intent.getStringExtra("content")
        Log.d("keyword","1")
        val jsonObject = JSONObject()
        try {
            jsonObject.put("diary", content)
            Log.d("keyword","2")
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d("keyword","3")
        }
        post(fname, jsonObject.toString())
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    fun post(fname: String?, jsonMessage: String?) {
        Log.d("keyword","4")
        val requestURL = "http://34.64.108.156:8000/"
        val diarydate = fname
        val list2 = ArrayList<String>()

        /*try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(requestURL)
                .post(RequestBody.create(MediaType.parse("application/json"), jsonMessage))
                .addHeader("Content-Type", "application/json")
                .build()
           *//* val request = Request.Builder()
                .url(requestURL)
                .post(RequestBody.create(MediaType.parse("application/json"), jsonMessage))
                .build()*//*
            //비동기 처리 (enqueue 사용)

            client.newCall(request).enqueue(object : Toast.Callback(), Callback {
                //비동기 처리를 위해 Callback 구현

                override fun onFailure(request: Request?, e: IOException?) {
                    Log.d("keyword","5")
                }

                override fun onResponse(response: Response) {

                    Log.d("keyword","6")
                    val parsing = response.body()?.string()
                    val list = ArrayList<String>()
                    val str:String = response.body().string()
                    //Log.d("keyword", str)

                }
            })
        } catch (e: Exception) {
            System.err.println(e.toString())
            Log.d("keyword","7")
        }*/

    }
}