package com.example.draw4u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

open class LoginActivity : AppCompatActivity(),View.OnClickListener {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    //private const val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(supportActionBar != null)
            supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        //firebase auth 객체
        firebaseAuth = FirebaseAuth.getInstance()

        // SharedPreferences 안에 값이 저장되어 있지 않을 때 -> Login
        if(MySharedPreferences.getUserId(this).isNullOrBlank()
            || MySharedPreferences.getUserPass(this).isNullOrBlank()) {
        }
        else { // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity로 이동
            Toast.makeText(this, "${MySharedPreferences.getUserId(this)}님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            //val intent = Intent(this, MainActivity::class.java)
            toMainActivity(firebaseAuth?.currentUser)
            finish()
        }

        btn_googleSignIn.setOnClickListener {signIn()}

        btn_login.setOnClickListener{generalLogIn()}//일반 로그인

        btn_signup.setOnClickListener{//회원 가입
            startActivity(Intent(this, SignUpActivity::class.java))
        }//회원 가입

        btn_findPW.setOnClickListener{//비밀번호 찾기
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.activity_find_p_w, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.EmailAddress)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    firebaseAuth?.sendPasswordResetEmail(dialogText.text.toString())
                        ?.addOnCompleteListener(this){
                            if(it.isSuccessful){
                                //비밀번호 재설정 메일을 보내기가 성공했을때 이벤트
                                var toast = Toast.makeText(this, "메일을 확인해주세요.", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                            else{
                                var toast = Toast.makeText(this, "입력이 제대로 안 됐습니다.", Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                     //확인일 때 main의 View의 값에 dialog View에 있는 값을 적용

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                     //취소일 때 아무 액션이 없으므로 빈칸
                }
                .show()
        }//비밀번호 찾기 팝업창

        //Google 로그인 옵션 구성. requestIdToken 및 Email 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    // onStart. 유저가 앱에 이미 구글 로그인을 했는지 확인
    public override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!==null){ // 이미 로그인 되어있을시 바로 메인 액티비티로 이동
           //toMainActivity(firebaseAuth.currentUser)
        }
    } //onStart End

    // onActivityResult
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("LoginActivity", "Google sign in failed", e)
            }
        }
    } // onActivityResult End

    // firebaseAuthWithGoogle
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + acct.id!!)

        //Google SignInAccount 객체에서 ID 토큰을 가져와서 Firebase Auth로 교환하고 Firebase에 인증
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 성공", task.exception)
                    toMainActivity(firebaseAuth?.currentUser)
                } else {
                    Log.w("LoginActivity", "firebaseAuthWithGoogle 실패", task.exception)
                }
            }
    }// firebaseAuthWithGoogle END

    // toMainActivity
    fun toMainActivity(user: FirebaseUser?) {
        if(user !=null) { // MainActivity 로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    } // toMainActivity End

    // signIn
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // signIn End

    override fun onClick(p0: View?) {
    }

    private fun generalLogIn(){
        var email = edtEmail.text.toString()
        var password = edtPassword.text.toString()
        if (email.length < 1 || password.length < 1 ) {
            var toast = Toast.makeText(this, "입력이 제대로 안 됐습니다.", Toast.LENGTH_SHORT)
            toast.show()
        }else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if(firebaseAuth.currentUser?.isEmailVerified!!){
                            if(switch1.isChecked){//자동 로그인 스위치 ON
                                MySharedPreferences.setUserId(this, email)
                                MySharedPreferences.setUserPass(this, password)
                                toMainActivity(firebaseAuth?.currentUser)
                                finish()
                            }
                            else{//자동 로그인 스위치 OFF
                                toMainActivity(firebaseAuth?.currentUser)
                                finish()
                            }

                        }
                        else{
                            var toast = Toast.makeText(baseContext, "이메일 인증을 해주세요", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        var toast = Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT)
                        toast.show()
                        //updateUI(null)
                    }
                }
        }
    }//일반 로그인

    fun signOut() {
        // Firebase sign out
        firebaseAuth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            //updateUI(null)
        }
    }//구글 로그아웃

    private fun revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut()
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {

        }
    } //구글 회원탈퇴

}