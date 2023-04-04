package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore

class MakePlaylistRecyclerActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_recycler_playlist)

        // Firestore 초기화
        db = FirebaseFirestore.getInstance()

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.rv_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val movieProfileList = arrayListOf<MovieProfiles>()
        db.collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val posterImageUrl = document.getString("posterImageUrl")
                    val movieName = document.getString("movieName")
                    val movieGenreAndYear = document.getString("movieGenreAndYear")
                    val toInfoUrl = document.getString("toInfoUrl")

                    val movieProfile =
                        MovieProfiles(posterImageUrl, movieName, movieGenreAndYear, toInfoUrl)
                    movieProfileList.add(movieProfile)
                }

                // RecyclerView Adapter에 데이터 전달
                val adapter = makeplalistrecycler(movieProfileList)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged() // 어댑터에 데이터 전달 후 갱신
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}