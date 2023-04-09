package com.example.testapp

import android.os.Bundle

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore

class PostDetailActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private var postId: String? = null
    private lateinit var adapter: MoreMovieProfileListActivityAdapter
    private val movieProfilesList = ArrayList<MovieProfiles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        // RecyclerView에 대한 LayoutManager 설정
        findViewById<RecyclerView>(R.id.rv_account_movie).layoutManager = LinearLayoutManager(this)

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

                        // 해당 문서에 속한 movies 컬렉션을 얻어옴
                        val moviesCollection =
                            firestore.collection("post").document(postId!!).collection("movies")
                        moviesCollection.get().addOnSuccessListener { moviesQuerySnapshot ->
                            // QuerySnapshot을 MovieProfiles 객체 리스트로 변환함
                            for (movieDocument in moviesQuerySnapshot.documents) {
                                val movieProfiles =
                                    movieDocument.toObject(MovieProfiles::class.java)
                                if (movieProfiles != null) {
                                    movieProfilesList.add(movieProfiles)
                                }
                            }
                            // 어댑터에 MovieProfiles 객체 리스트를 설정해줌
                            adapter = MoreMovieProfileListActivityAdapter(movieProfilesList)
                            findViewById<RecyclerView>(R.id.rv_account_movie).adapter = adapter
                        }.addOnFailureListener { e ->
                            Log.w(TAG, "Error getting movies collection: ", e)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting document: ", e)
                }
        }
    }
}
