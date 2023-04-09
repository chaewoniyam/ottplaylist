package com.example.testapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.testapp.ContentDTO
import com.example.testapp.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class PostDetailActivity : AppCompatActivity() {
    private lateinit var postRef: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        // 이전 활동에서 postId를 가져오기
        val postId = intent.getStringExtra("postId")!!


        // "posts" 컬렉션에서 특정 postId와 일치하는 문서 참조하기
        postRef = FirebaseFirestore.getInstance().collection("post").document(postId)

        // ...
    }

    // ...
}