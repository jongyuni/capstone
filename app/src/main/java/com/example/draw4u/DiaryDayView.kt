package com.example.draw4u

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_diary_day_view.*
import kotlinx.android.synthetic.main.activity_modify_keyword.*
import kotlinx.android.synthetic.main.item.*


class DiaryDayView : AppCompatActivity() {
    var fname: String = "" //일기 저장을 위한 파일 이름 - 날짜 형식
    var str: String = "" //일기 저장을 위한 String

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    var tempdiaryinfo = DiaryInfo()
    var keyword: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(supportActionBar != null)
            supportActionBar?.hide()
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
            KeywordView.visibility = View.VISIBLE
            save_Btn.visibility = View.GONE
            keyword_Btn.visibility = View.GONE
            mod_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            scrollView.visibility = View.VISIBLE
            contextEditText.visibility = View.GONE
            diaryView.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
        }//일기 저장 버튼 클릭시

        keyword_Btn.setOnClickListener {//키워드 추출 Button이 클릭되면
            saveDiary(fname)// saveDiary 메소드 호출
            makeKeyword(fname) //keyword 추출 메소드 호출
            str = contextEditText.getText().toString() // str 변수에 edittext내용을 toString형으로 저장
            diaryView.text = "${str}" // textView에 str 출력
            KeywordView.visibility = View.VISIBLE
            save_Btn.visibility = View.GONE
            keyword_Btn.visibility = View.GONE
            mod_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE
            scrollView.visibility = View.VISIBLE
            contextEditText.visibility = View.GONE
            diaryView.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
        }//키워드 추출 버튼 클릭시

        KeywordView.setOnClickListener(){//키워드 수정시
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.activity_modify_keyword, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.EditKeyword)
            dialogText.setText(keyword)

            builder.setView(dialogView)
                .setPositiveButton("저장"){dialogInterface, i ->
                    var input = dialogText.text.toString()
                    var token = input.split('#',' ')
                    //키워드 수정하여 저장

                    //키워드 재설정
                    if(token.size.toInt() > 4){
                        if(token[1].isNullOrEmpty()){
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",null)
                        }
                        else{
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",token[1])
                            keyword = "#" + token[1]
                        }
                        if(token[3].isNullOrEmpty()){
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword2",null)
                        }
                        else{
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword2",token[3])
                            keyword = keyword + " #" + token[3]
                        }
                        if(token[5].isNullOrEmpty()){
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword3",null)
                        }
                        else{
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword3",token[5])
                            keyword = keyword + " #" + token[5]
                        }
                    }
                    else if(token.size.toInt() > 2){
                        if(token[1].isNullOrEmpty()){
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",null)
                        }
                        else{
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",token[1])
                            keyword = "#" + token[1]
                        }
                        if(token[3].isNullOrEmpty()){
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword2",null)
                        }
                        else{
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword2",token[3])
                            keyword = keyword + " #" + token[3]
                        }
                        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword3",null)
                    }
                    else if(token.size.toInt() > 0){
                        if(token[1].isNullOrEmpty()){
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",null)
                        }
                        else{
                            fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",token[1])
                            keyword = "#" + token[1]
                        }
                        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword2",null)
                        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword3",null)
                    }

                    KeywordView.text = "${keyword}"
                    KeywordView.visibility = View.VISIBLE
                    //키워드 재설정

                }
                .setNegativeButton("취소"){dialogInterface, i ->}
                .setNeutralButton("삭제"){dialogInterface, i ->
                    fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword1",null)
                    fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword2",null)
                    fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.update("keyword3",null)
                    
                    KeywordView.visibility = View.INVISIBLE
                    //키워드 재설정
                }
                .show()
        }//키워드가 눌리면 수정창이 뜬다

        imageView.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("그림을 넣으시겠습니까?")
                .setNeutralButton("네"){dialogInterface, i ->
                    val intent = Intent(this, SelectMenu::class.java)
                    intent.putExtra("fname", fname)
                    startActivity(intent)
                    finish()

                }
                .setNegativeButton("아니오"){dialogInterface, i ->}
                .show()
        }

    }

    fun checkedDay(fname: String) {


        fbFirestore?.collection(fbAuth?.uid.toString())?.document(fname)?.get()
            ?.addOnSuccessListener { documentSnapshot ->
                val diaryinfo =  documentSnapshot.toObject(DiaryInfo::class.java)
                if (diaryinfo != null) {//이미 씌여진 일기가 있다면
                    str = diaryinfo.diary.toString()
                    contextEditText.visibility = View.GONE
                    diaryView.visibility = View.VISIBLE
                    diaryView.text = "${str}" // textView에 str 출력
                    imageView.visibility = View.VISIBLE
                    if(diaryinfo.imageURL.isNullOrEmpty()){ }
                    else{
                        tempdiaryinfo.imageURL = diaryinfo.imageURL
                        Glide.with(this).load(diaryinfo.imageURL).into(imageView)//이미지 출력
                    }

                    tempdiaryinfo.diary =str
                    tempdiaryinfo.keyword1 = diaryinfo.keyword1
                    tempdiaryinfo.keyword2 = diaryinfo.keyword2
                    tempdiaryinfo.keyword3 = diaryinfo.keyword3

                    if(tempdiaryinfo.keyword1.isNullOrEmpty()){
                        keyword = "  "
                    }
                    else{
                        keyword = "#" + tempdiaryinfo.keyword1
                    }
                    if(tempdiaryinfo.keyword2.isNullOrEmpty()){ }
                    else{
                        keyword = keyword + " #" + tempdiaryinfo.keyword2
                    }
                    if(tempdiaryinfo.keyword3.isNullOrEmpty()){ }
                    else{
                        keyword = keyword + " #" + tempdiaryinfo.keyword3
                    }

                    KeywordView.text = "${keyword}"
                    KeywordView.visibility = View.VISIBLE
                    save_Btn.visibility = View.GONE
                    keyword_Btn.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE
                    mod_Btn.visibility = View.VISIBLE
                    del_Btn.visibility = View.VISIBLE

                    mod_Btn.setOnClickListener {
                        KeywordView.visibility = View.GONE
                        diaryView.visibility = View.GONE
                        scrollView.visibility = View.GONE
                        contextEditText.visibility = View.VISIBLE
                        contextEditText.setText(str) // editText에 textView에 저장된 내용을 출력
                        save_Btn.visibility = View.VISIBLE
                        keyword_Btn.visibility = View.VISIBLE
                        mod_Btn.visibility = View.GONE
                        del_Btn.visibility = View.GONE
                        diaryView.text = "${contextEditText.getText()}"
                    }// 수정 버튼을 누를 시

                    del_Btn.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        val dialogView = layoutInflater.inflate(R.layout.activity_delete_check, null)

                        builder.setView(dialogView)
                            .setPositiveButton("확인"){dialogInterface, i ->
                                KeywordView.visibility = View.INVISIBLE
                                diaryView.visibility = View.GONE
                                scrollView.visibility = View.GONE
                                contextEditText.visibility = View.VISIBLE
                                contextEditText.setText("")
                                save_Btn.visibility = View.VISIBLE
                                keyword_Btn.visibility = View.VISIBLE
                                mod_Btn.visibility = View.GONE
                                del_Btn.visibility = View.GONE
                                removeDiary(fname)
                            }
                            .setNegativeButton("취소"){dialogInterface, i ->
                            }
                            .show()

                    }//삭제 버튼 클릭시
                }//저장된 일기가 있을때
                else{
                    str = ""
                    diaryView.visibility = View.GONE
                    KeywordView.visibility = View.GONE
                    save_Btn.visibility = View.VISIBLE
                    keyword_Btn.visibility = View.VISIBLE
                    scrollView.visibility = View.GONE
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

        var content: String = contextEditText.getText().toString()
        val intent = Intent(this,ExtractKeyword::class.java)
        intent.putExtra("fname", fname)
        intent.putExtra("content",content)
        startActivityForResult(intent,100)

    }//키워드 추출

    //키워드 추출 후 키워드 선택 창으로 넘어가기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    this.checkedDay(fname)
                }
            }
        }
    }

}

