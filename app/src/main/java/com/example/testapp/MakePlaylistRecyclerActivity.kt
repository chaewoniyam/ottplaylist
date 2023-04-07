package com.example.testapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MakePlaylistRecyclerActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_playlist)

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.rv_click_playlist)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 현재 사용자의 uid 가져오기
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val postCollection = firestore.collection("post")

        val movieProfileList = arrayListOf<MovieProfiles>()

        postCollection.orderBy("date", Query.Direction.DESCENDING).limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val latestPostId = documents.documents[0].id
                    // latestPostId 변수에 최근에 생성된 문서의 ID가 저장됩니다.
                    if (userId != null) {
                        firestore.collection("post").document(latestPostId).collection("movies")
                            .get()
                            .addOnSuccessListener { movieDocuments ->
                                for (document in movieDocuments) {
                                    val posterImageUrl = document.getString("posterImageUrl")
                                    val movieName = document.getString("movieName")
                                    val movieGenreAndYear = document.getString("movieGenreAndYear")
                                    val toInfoUrl = document.getString("toInfoUrl")

                                    val movieProfile =
                                        MovieProfiles(
                                            posterImageUrl,
                                            movieName,
                                            movieGenreAndYear,
                                            toInfoUrl
                                        )

                                    movieProfileList.add(movieProfile)
                                }

                                // RecyclerView Adapter에 데이터 전달
                                val adapter = makeplalistrecycleradapter(movieProfileList)
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged() // 어댑터에 데이터 전달 후 갱신
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error getting documents: ", exception)
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}
