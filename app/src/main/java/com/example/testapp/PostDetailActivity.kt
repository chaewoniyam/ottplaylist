package com.example.testapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.testapp.ContentDTO
import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore

class PostDetailActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private var postId: String? = null
    private lateinit var adapter: addMovieProfileListActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        firestore = FirebaseFirestore.getInstance()


        postId = intent.getStringExtra("postId")
        if (postId != null) {
            // Firestore에서 해당 postId에 해당하는 문서를 쿼리하고, 그 결과를 UI에 설정
            firestore.collection("post").document(postId!!)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val contentDTO = document.toObject(ContentDTO::class.java)
                        findViewById<TextView>(R.id.tv_account_nickname).text = contentDTO?.userId
                        findViewById<TextView>(R.id.tv_account_title).text = contentDTO?.title
                        findViewById<TextView>(R.id.tv_account_explain).text = contentDTO?.story
                        findViewById<TextView>(R.id.tv_tag1).text = contentDTO?.tag1
                        findViewById<TextView>(R.id.tv_tag2).text = contentDTO?.tag2
                        findViewById<TextView>(R.id.tv_tag3).text = contentDTO?.tag3
                        if (!contentDTO?.imageUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(contentDTO?.imageUrl)
                                .into(findViewById(R.id.iv_account_image))
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting document: ", e)
                }
        }


    }
}