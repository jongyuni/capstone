package com.example.draw4u

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_diary_day_view.*


class DiaryDayView : AppCompatActivity() {
    var fname: String = "" //일기 저장을 위한 파일 이름 - 날짜 형식
    var str: String = "" //일기 저장을 위한 String

    var year: Int = 0
    var month: Int = 0
    var dayOfMonth: Int = 0 //사용자가 선택한 날짜

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_day_view)

        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        if(intent.hasExtra("year")){
            year = intent.getIntExtra("year",0)
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }//연도 저장

        if(intent.hasExtra("month")){
            month = intent.getIntExtra("month",0)
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }//월 저장

        if(intent.hasExtra("dayOfMonth")){
            dayOfMonth = intent.getIntExtra("dayOfMonth",0)
        }else{
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
        }//일 저장
        
        diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth) // 날짜를 보여주는 텍스트에 해당 날짜를 넣는다.
        contextEditText.setText("") // EditText에 공백값 넣기
        checkedDay(year, month, dayOfMonth) // checkedDay 메소드 호출

        save_Btn.setOnClickListener{// 저장 Button이 클릭되면
            saveDiary(fname) // saveDiary 메소드 호출
            str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString형으로 저장
            textView2.text = "${str}" // textView에 str 출력
            save_Btn.visibility = View.INVISIBLE
            mod_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            contextEditText.visibility = View.INVISIBLE
            textView2.visibility = View.VISIBLE
        }//일기 저장 버튼 클릭시

    }

    fun checkedDay(cYear: Int, cMonth: Int, cDay: Int) {

        if(cDay < 10){
            fname = "" + cYear + "-" + (cMonth + 1) + "" + "-0" + cDay
        }else{
            fname = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay
        }//일기 이름 설정. Ex) 2020-10-01, 2020-10-10

        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.get()
            ?.addOnSuccessListener {
                    documentSnapshot ->
                val tempdiaryinfo =  documentSnapshot.toObject(DiaryInfo::class.java)
                if (tempdiaryinfo != null) {
                    str = tempdiaryinfo.diary.toString()
                    contextEditText.visibility = View.INVISIBLE
                    textView2.visibility = View.VISIBLE
                    textView2.text = "${str}" // textView에 str 출력

                    save_Btn.visibility = View.INVISIBLE
                    mod_Btn.visibility = View.VISIBLE
                    del_Btn.visibility = View.VISIBLE

                    mod_Btn.setOnClickListener {
                        contextEditText.visibility = View.VISIBLE
                        textView2.visibility = View.INVISIBLE
                        contextEditText.setText(str) // editText에 textView에 저장된 내용을 출력
                        save_Btn.visibility = View.VISIBLE
                        mod_Btn.visibility = View.INVISIBLE
                        del_Btn.visibility = View.INVISIBLE
                        textView2.text = "${contextEditText.getText()}"
                    }// 수정 버튼을 누를 시

                    del_Btn.setOnClickListener {
                        textView2.visibility = View.INVISIBLE
                        contextEditText.setText("")
                        contextEditText.visibility = View.VISIBLE
                        save_Btn.visibility = View.VISIBLE
                        mod_Btn.visibility = View.INVISIBLE
                        del_Btn.visibility = View.INVISIBLE
                        removeDiary(fname)
                    }//삭제 버튼 클릭시
                }//저장된 일기가 있을때
                else{
                    str = ""
                    textView2.visibility = View.INVISIBLE
                    diaryTextView.visibility = View.VISIBLE
                    save_Btn.visibility = View.VISIBLE
                    mod_Btn.visibility = View.INVISIBLE
                    del_Btn.visibility = View.INVISIBLE
                    contextEditText.visibility = View.VISIBLE
                }//저장된 일기가 없을때
            }//일기 내용 불러오기


    }//저장된 일기 확인

    @SuppressLint("WrongConstant")
    fun saveDiary(readyDay: String) {

        var content: String = contextEditText.getText().toString()
        var tempdiaryinfo = DiaryInfo()
        tempdiaryinfo.diary = content

        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.set(tempdiaryinfo)

    }//일기 저장

    @SuppressLint("WrongConstant")
    fun removeDiary(readyDay: String) {

        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.delete()

    }//일기 삭제

}

