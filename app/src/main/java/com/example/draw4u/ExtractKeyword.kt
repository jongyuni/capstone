package com.example.draw4u

import android.app.Activity
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

        val jsonObject = JSONObject()
        try {
            jsonObject.put("diary", content)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        post(fname, jsonObject.toString())
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    fun post(fname: String?, jsonMessage: String?) {

        val requestURL = "http://34.64.108.156:8000/"
        val diarydate = fname
        val list2 = ArrayList<String>()

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()

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

                }

                override fun onResponse(response: Response) {

                    val parsing = response.body()?.string()
                    val list = ArrayList<String>()
                    val str:String = response.body().string()
                    //Log.d("keyword", str)

                }
            })
        } catch (e: Exception) {
            System.err.println(e.toString())
        }*/

    }
}