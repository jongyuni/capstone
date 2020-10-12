package com.example.draw4u

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_menu.*
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Year

private val TAG: String = MenuActivity::class.java.simpleName

class MenuActivity : AppCompatActivity() {
    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    var year: Int = 0
    var month: Int = 0
    var dayOfMonth: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        if(intent.hasExtra("year")){
            year = intent.getIntExtra("year",0)
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }

        if(intent.hasExtra("month")){
            month = intent.getIntExtra("month",0) + 1
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }

        if(intent.hasExtra("dayOfMonth")){
            dayOfMonth = intent.getIntExtra("dayOfMonth",0)
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }

        textView2.text = "${year}" + "-" +"${month}" + "-" + "${dayOfMonth}"

        btn_senddiary.setOnClickListener{

            var inputtext = input_diary.text.toString()
            var tempinfo = UserInfo()
            tempinfo.emotion = fbAuth?.uid
            tempinfo.keyword = fbAuth?.currentUser?.email
            tempinfo.diary = inputtext
            
            fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(tempinfo)

        }

    }
}