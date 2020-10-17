package com.example.draw4u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_cal_dia_view.*

class CalDiaView : AppCompatActivity() {

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal_dia_view)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        calendarView.setOnDateChangeListener {view, year, month, dayOfMonth ->
            // 달력에서 날짜 선택
            val intent = Intent(this, DiaryDayView::class.java)
            intent.putExtra("year", year)
            intent.putExtra("month", month)
            intent.putExtra("dayOfMonth", dayOfMonth)
            startActivity(intent)//선택한 날짜로 이동 및 날짜 정보 전송
        }
        
    }

}