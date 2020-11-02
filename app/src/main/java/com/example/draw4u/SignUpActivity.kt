package com.example.draw4u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    var TAG = "SignActivity"
    var isRun = true
    var thread = PassChk()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        thread.start()

        btn_Signup.setOnClickListener {
            signUp()
        }//회원 가입 버튼 클릭시
        
        btn_Login.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }//로그인 버튼 클릭시 로그인 화면으로 돌아가기

    }
    public override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
    }

    //회원가입
    fun signUp(){
        var email = edtEmail.getText().toString()
        var password = edtPass1.getText().toString()
        var intent = Intent(this,LoginActivity::class.java)
        
        //아이디 제대로 입력했는지 확인
        if (!email.contains("@")&&email.length<6){
            var toast = Toast.makeText(this,"이메일 형식이 맞지 않습니다",Toast.LENGTH_SHORT)
            toast.show()
        }else {//제대로 입력했으면 서버에 회원 등록
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //회원가입 성공시 로그인 화면으로 넘어가기
                        //Log.d(TAG, "createUserWithEmail:success")
                        firebaseAuth.currentUser
                            ?.sendEmailVerification()
                            ?.addOnCompleteListener { verifitask ->
                                if(verifitask.isSuccessful){
                                    isRun = false
                                    var toast = Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT)
                                    toast.show()
                                    startActivity(intent)
                                    finish()}
                                else{
                                    var toast = Toast.makeText(this, "이메일 인증을 해주세요", Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }

                    } else {
                        //회원가입 실패시 메세지 출력
                        //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        var toast =Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT)
                            toast.show()
                    }
                }
        }
    }
    //비밀번호 제대로 입력했는지 확인
    inner class PassChk:Thread(){
        override fun run() {
            while (isRun){
                SystemClock.sleep(1000)
                var pass1:String = edtPass1.text.toString()
                var pass2:String = edtPass2.text.toString()
                if (pass1.equals(pass2)){
                    runOnUiThread{
                        tvError.setText("")
                        btn_Signup.setEnabled(true)
                    }//비밀번호 일치시
                }else{
                    runOnUiThread{
                        tvError.setText("비밀번호가 일치하지 않습니다")
                        btn_Signup.setEnabled(false)
                    }//비밀번호 불일치시
                }
                if (pass1.length<6){
                    runOnUiThread {
                        tvError.setText("비밀번호는 6자 이상이여야 합니다")
                    }//비밀번호 길이가 짧을때
                }
            }
        }
    }
}