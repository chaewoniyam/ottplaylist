package com.example.testapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.testapp.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding


    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        binding.joinBtn.setOnClickListener {
            var isGoToJoin = true
            val nickname = binding.nicknameArea.text.toString()
            val email = binding.emailArea.text.toString()
            val password = binding.passwordArea.text.toString()
            val passwordCheck = binding.checkPasswordArea.text.toString()

            fbAuth = FirebaseAuth.getInstance()
            fbFirestore = FirebaseFirestore.getInstance()

            if (nickname.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "password1을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (passwordCheck.isEmpty()) {
                Toast.makeText(this, "password2을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if (password.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }



            //이메일, 비번 인증에 넣고, uid, 이메일, 닉네임 파이어스토어에 넣기
            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var userInfo = ModelFriends()

                            userInfo.uid = fbAuth?.uid
                            userInfo.userId = fbAuth?.currentUser?.email
                            userInfo.userNickName = nickname
                            fbFirestore?.collection("users")?.document(fbAuth?.uid.toString())?.set(userInfo)

                            //회원가입한 후, 로그인화면으로 이동
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "회원가입 실패 ", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}