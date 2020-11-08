package com.example.draw4u

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_diary_day_view.*
import okhttp3.OkHttpClient
import org.conscrypt.Conscrypt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Array
import java.security.Security
import java.util.concurrent.TimeUnit


class DiaryDayView : AppCompatActivity() {
    var fname: String = "" //일기 저장을 위한 파일 이름 - 날짜 형식
    var str: String = "" //일기 저장을 위한 String

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    var tempdiaryinfo = DiaryInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_day_view)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        if(intent.hasExtra("fname")){
            fname = intent.getStringExtra("fname").toString()
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }//일기 날짜 저장

        dateView.text = String.format(fname)
        contextEditText.setText("") // EditText에 공백값 넣기
        checkedDay(fname) //일기 써있는지 확인

        save_Btn.setOnClickListener{// 저장 Button이 클릭되면
            saveDiary(fname) // saveDiary 메소드 호출
            str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString형으로 저장
            diaryView.text = "${str}" // textView에 str 출력
            save_Btn.visibility = View.GONE
            keyword_Btn.visibility = View.GONE
            mod_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.GONE
            diaryView.visibility = View.VISIBLE
        }//일기 저장 버튼 클릭시

        keyword_Btn.setOnClickListener {//키워드 추출 Button이 클릭되면
            saveDiary(fname)// saveDiary 메소드 호출
            makeKeyword(fname) //keyword 추출 메소드 호출
            str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString형으로 저장
            diaryView.text = "${str}" // textView에 str 출력
            save_Btn.visibility = View.GONE
            keyword_Btn.visibility = View.GONE
            mod_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.GONE
            diaryView.visibility = View.VISIBLE
        }

    }

    fun checkedDay(fname: String) {

        var keyword: String=""

        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.get()
            ?.addOnSuccessListener {
                    documentSnapshot ->
                val diaryinfo =  documentSnapshot.toObject(DiaryInfo::class.java)
                if (diaryinfo != null) {//이미 씌여진 일기가 있다면
                    str = diaryinfo.diary.toString()
                    contextEditText.visibility = View.GONE
                    diaryView.visibility = View.VISIBLE
                    diaryView.text = "${str}" // textView에 str 출력
                    imageView.visibility = View.VISIBLE
                    val url_str : String = diaryinfo.imageURL.toString()
                    val new_str = "\"" + url_str + "\""
                    //Log.d("urL_str" , new_str)
                    tempdiaryinfo.diary =str
                    tempdiaryinfo.imageURL = url_str
                    tempdiaryinfo.keyword1 = diaryinfo.keyword1
                    tempdiaryinfo.keyword2 = diaryinfo.keyword2
                    tempdiaryinfo.keyword3 = diaryinfo.keyword3
                    //Log.d("diaryinfo.keyword1" , diaryinfo.keyword1.toString())
                    //Log.d("tempdiaryinfo.keyword1", tempdiaryinfo.keyword1.toString())
                    keyword = "#" + tempdiaryinfo.keyword1 + " #" + tempdiaryinfo.keyword2 + " #" + tempdiaryinfo.keyword3
                    Picasso.get().load(url_str).into(imageView)
                    KeywordView.text = "${keyword}"
                    KeywordView.visibility = View.VISIBLE
                    save_Btn.visibility = View.GONE
                    keyword_Btn.visibility = View.GONE
                    mod_Btn.visibility = View.VISIBLE
                    del_Btn.visibility = View.VISIBLE

                    mod_Btn.setOnClickListener {
                        contextEditText.visibility = View.VISIBLE
                        diaryView.visibility = View.GONE
                        KeywordView.visibility = View.GONE
                        contextEditText.setText(str) // editText에 textView에 저장된 내용을 출력
                        save_Btn.visibility = View.VISIBLE
                        keyword_Btn.visibility = View.VISIBLE
                        mod_Btn.visibility = View.GONE
                        del_Btn.visibility = View.GONE
                        diaryView.text = "${contextEditText.getText()}"
                    }// 수정 버튼을 누를 시

                    del_Btn.setOnClickListener {
                        diaryView.visibility = View.GONE
                        contextEditText.setText("")
                        contextEditText.visibility = View.VISIBLE
                        save_Btn.visibility = View.VISIBLE
                        keyword_Btn.visibility = View.VISIBLE
                        KeywordView.visibility = View.INVISIBLE
                        mod_Btn.visibility = View.GONE
                        del_Btn.visibility = View.GONE
                        removeDiary(fname)
                    }//삭제 버튼 클릭시
                }//저장된 일기가 있을때
                else{
                    str = ""
                    diaryView.visibility = View.GONE
                    KeywordView.visibility = View.GONE
                    save_Btn.visibility = View.VISIBLE
                    keyword_Btn.visibility = View.VISIBLE
                    mod_Btn.visibility = View.GONE
                    del_Btn.visibility = View.GONE
                    contextEditText.visibility = View.VISIBLE
                }//저장된 일기가 없을때
            }//일기 내용 불러오기


    }//저장된 일기 확인

    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {

        var content: String = contextEditText.getText().toString()

        tempdiaryinfo.diary = content
        tempdiaryinfo.date = fname
        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.set(tempdiaryinfo)

    }//일기 저장

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {

        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.delete()

    }//일기 삭제

    fun makeKeyword(fname: String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        var content = tempdiaryinfo.diary

        val intent1 = Intent(this, ExtractKeyword::class.java)
        intent1.putExtra("fname", fname)
        intent1.putExtra("content",content)
        startActivity(intent1)

        /*val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://34.64.108.156:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val service = retrofit.create(RetrofitService::class.java)

        val call: Call<ResultKeyword> = service.getkeyword(content)

        call.enqueue(object : Callback<ResultKeyword> {

            override fun onFailure(call: Call<ResultKeyword>,t: Throwable){
                Toast.makeText(this@DiaryDayView,"실패",Toast.LENGTH_LONG).show()
                Log.d("CometChatAPI::", "Failed API call with call: " + call +
                        " + exception: " + t)
            }//서버 통신 실패

            override fun onResponse(
                call: Call<ResultKeyword>,
                response: Response<ResultKeyword>
            ) {
                var result_keyword :ResultKeyword
                result_keyword = response.body()!!
                Toast.makeText(this@DiaryDayView,"성공",Toast.LENGTH_LONG).show()
                Log.d("CometChatAPI::", result_keyword.toString())
                Log.d("CometChatAPI::", fbAuth?.uid.toString())
                tempdiaryinfo.keyword1 = result_keyword.keywords[0]
                tempdiaryinfo.keyword2 = result_keyword.keywords[1]
                tempdiaryinfo.keyword3 = result_keyword.keywords[2]

                fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.set(tempdiaryinfo)
            }//서버 통신 성공시 키워드 추출
        })*/

        /*var id= fbAuth?.uid.toString()
        Log.d("CometChatAPI::", fbAuth?.uid.toString())
        val intent = Intent(this, SelectKeyword::class.java)
        intent.putExtra("fname", fname)
        intent.putExtra("uid",id)
        Handler().postDelayed({startActivity(intent)},5000)
        finish()*/


    }

}

