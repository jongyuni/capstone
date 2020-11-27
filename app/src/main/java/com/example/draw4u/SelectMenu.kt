package com.example.draw4u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_select_menu.*

class SelectMenu : AppCompatActivity() {
    var fname: String = "" //일기 저장을 위한 파일 이름 - 날짜 형식

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.activity_select_menu)

        if(intent.hasExtra("fname")){
            fname = intent.getStringExtra("fname").toString()
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }//일기 날짜 저장

        //키워드 선택 창으로 넘어가기
        btn_imageFromKeyword.setOnClickListener(){
            val intent = Intent(this, SelectKeyword::class.java)
            intent.putExtra("fname", fname)
            startActivity(intent)
            finish()
        }//키워드 선택 창으로 넘어가기

        btn_directDraw.setOnClickListener(){}

        //핸드폰에서 사진 가져오기
        btn_imageFromPhone.setOnClickListener(){
            val intent = Intent(this, PhotoFromPhone::class.java)
            intent.putExtra("fname", fname)
            startActivity(intent)
            finish()
        }//핸드폰에서 사진 가져오기

        //돌아가기 버튼
        btn_return.setOnClickListener(){
            val intent = Intent(this, DiaryDayView::class.java)
            intent.putExtra("fname", fname)
            startActivity(intent)
            finish()
        }//돌아가기 버튼
    }
}