package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class InfoActivity : AppCompatActivity() {

    companion object{
        const val TAG: String = "로그"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
    }

    // 로그아웃
    fun logout_to_join(v : View){
        Log.d(InfoActivity.TAG, "logout - onCreate() called")
        FirebaseAuth.getInstance().signOut() // 로그아웃하는 함수
        startActivity(Intent(this, LoginActivity :: class.java ))
    }

    // 비밀번호 재설정
    fun reset_password(v: View){
        Log.d(InfoActivity.TAG, "resetPassword - onCreate() called")
        startActivity(Intent(this, PasswordResetActivity :: class.java ))

    }
}