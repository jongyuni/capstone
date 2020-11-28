package com.example.draw4u

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_diary_day_view.*
import kotlinx.android.synthetic.main.activity_select_keyword.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class SelectKeyword : AppCompatActivity() {

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    var fname: String = "" //일기 저장을 위한 파일 이름 - 날짜 형식

    var keyword1: String = ""
    var keyword2: String = ""
    var keyword3: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.activity_select_keyword)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        if(intent.hasExtra("fname")){
            fname = intent.getStringExtra("fname").toString()
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }//파일 이름


        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.get()
            ?.addOnSuccessListener { document ->
                val tempdiaryinfo =  document.toObject(DiaryInfo::class.java)

                if (tempdiaryinfo != null) {//이미 씌여진 일기가 있다면
                    if(tempdiaryinfo.keyword1.isNullOrEmpty()){
                        Keyword1.visibility = View.GONE
                    }
                    else{
                        this.keyword1 = tempdiaryinfo.keyword1.toString()
                        Keyword1.visibility = View.VISIBLE
                        Keyword1.setText(this.keyword1)
                    }
                    if(tempdiaryinfo.keyword2.isNullOrEmpty()){
                        Keyword2.visibility = View.GONE
                    }
                    else{
                        this.keyword2 = tempdiaryinfo.keyword2.toString()
                        Keyword2.visibility = View.VISIBLE
                        Keyword2.setText(this.keyword2)
                    }
                    if(tempdiaryinfo.keyword3.isNullOrEmpty()){
                        Keyword3.visibility = View.GONE
                    }
                    else{
                        this.keyword3 = tempdiaryinfo.keyword3.toString()
                        Keyword3.visibility = View.VISIBLE
                        Keyword3.setText(this.keyword3)
                    }

                }//키워드 저장
                else{
                    Log.d("CometChatAPI::", "fail")
                }
            }
            ?.addOnFailureListener(){ exception ->
                Log.d("CometChatAPI::", "fail here")
            }

        Keyword1.setOnClickListener(){
            makeImage(this.keyword1)
            val intent = Intent(this,SelectImage::class.java)
            intent.putExtra("fname",fname)
            intent.putExtra("keyword",this.keyword1)
            startActivity(intent)
            finish()
        }
        Keyword2.setOnClickListener(){
            makeImage(this.keyword2)
            val intent = Intent(this,SelectImage::class.java)
            intent.putExtra("fname",fname)
            intent.putExtra("keyword",this.keyword2)
            startActivity(intent)
            finish()
        }
        Keyword3.setOnClickListener(){
            makeImage(this.keyword3)
            val intent = Intent(this,SelectImage::class.java)
            intent.putExtra("fname",fname)
            intent.putExtra("keyword",this.keyword3)
            startActivity(intent)
            finish()
        }
        return_btn.setOnClickListener(){
            val intent = Intent(this, SelectMenu::class.java)
            intent.putExtra("fname", fname)
            startActivity(intent)
            finish()
        }
    }


    fun makeImage(keyword: String){
        val selKeyword: String
        selKeyword = keyword
        //val filename: String
        //filename = fname
        var result_image :String
        //val id: String
        //id = uid

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://34.64.108.156:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val service = retrofit.create(RetrofitService::class.java)

        val call: Call<ResultImage> = service.getimage(selKeyword)

        call.enqueue(object : Callback<ResultImage> {

            override fun onFailure(call: Call<ResultImage>,t: Throwable){
                Log.d("CometChatAPI::", "Failed API call with call: " + call +
                        " + exception: " + t)
            }//서버 통신 실패

            override fun onResponse(
                call: Call<ResultImage>,
                response: Response<ResultImage>
            ) {
                result_image = response.body().toString()
                var compa: String = "ResultImage(message=No value!)"

                if(result_image.equals(compa)){
                    Toast.makeText(this@SelectKeyword,"대상 이미지가 없습니다.",Toast.LENGTH_SHORT).show()
                }
                else{

                }
            }//서버 통신 성공
        })

    }
}