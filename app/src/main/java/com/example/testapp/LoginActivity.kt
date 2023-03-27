package com.example.testapp

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.wear.tiles.material.Text
import com.example.testapp.databinding.ActivityLoginBinding
import com.example.testapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern

//화면이 메모리에 올라갔을 때
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth // 인스턴스 선언
    private lateinit var binding: ActivityLoginBinding

    private val RC_SIGN_IN = 9001
    private var googleSignInClient: GoogleSignInClient? = null

    companion object{
        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //레이아웃과 연결
        setContentView(R.layout.activity_login)


        Log.d(TAG, "LoginActivity - onCreate() called")

        auth = Firebase.auth // onCreate() 메서드에서 FirebaseAuth 인스턴스 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 로그인 버튼 눌렀을 경우
        binding.loginBtn.setOnClickListener {
            val email = binding.emailArea.text.toString() // xml이랑 연결
            val password = binding.passwordArea.text.toString()
            //  if (email.length > 0 && password.length > 0) {
            auth.signInWithEmailAndPassword(email, password)  // 여기서부턴 긁어온 거
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "로그인에 성공하였습니다!", Toast.LENGTH_LONG).show()
                        // 일치했을 시, 홈 화면으로 넘어가기
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()
                    }
                }

        }

        //회원가입 버튼 눌렀을 경우
        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        // 구글 로그인

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("680409621515-92625268bt84cqo1f3ei62fd7prr400t.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val signInGoogleBtn: SignInButton = findViewById(R.id._sign_in_button)

        signInGoogleBtn.setOnClickListener { //구글 로그인 버튼 이벤트 처리

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

    }

    // 이게 먹는지 확인 불가함
    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAffinity(this);
        System.exit(0)
    }


    override fun onDestroy() {
        super.onDestroy()
        firebaseAuthSignOut()
    }

    //구글 인증 부분. 객체에서 id토큰 가져옴
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try { //로그인 성공시
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } catch (e: ApiException) {//로그인 실패

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //성공
                    val user = auth.currentUser
                    user?.let {

                        val name = user.displayName
                        val email = user.email
                        val displayName = user.displayName
                        val photoUrl = user.isEmailVerified
                        val emailVerified = user.isEmailVerified
                        val uid = user.uid
                        Log.d("xxxx name", name.toString())
                        Log.d("xxxx email", name.toString())
                        Log.d("xxxx displayName", name.toString())
                    }
//                    val please = Intent(this, BottomNavAndFragment::class.java)
//                    startActivity(please)
                } else { //실패
                    Log.d("xxxx ", "signInWithCredential:failure", task.exception)
                }

            }
    }

    private fun firebaseAuthSignOut(){ //로그아웃시키기
        Firebase.auth.signOut()
        googleSignInClient!!.signOut() //로그인 선택되게
    }


}