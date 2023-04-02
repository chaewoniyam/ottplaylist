package com.example.testapp

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.InfoActivity.Companion.TAG
import com.google.firebase.firestore.FirebaseFirestore

class addMovieProfileListActivityAdapter(private val movieProfileList: ArrayList<MovieProfiles>) : RecyclerView.Adapter<addMovieProfileListActivityAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_add_movie_list_skeleton, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieProfileList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        // Firestore 초기화
        val firestore = FirebaseFirestore.getInstance()

        val movieProfile = movieProfileList[position]
        Glide.with(holder.itemView.context)
            .load(movieProfile.posterImageUrl)
            .into(holder.moviePoster)
        holder.movieName.text = movieProfile.movieName
        holder.movieGenreAndYear.text = movieProfile.movieGenreAndYear
        holder.toInfoUrl.text = movieProfile.toInfoUrl
        holder.addButton.setOnClickListener {
            val curPos: Int = holder.adapterPosition
            val profile: MovieProfiles = movieProfileList.get(curPos)

            // 해당 버튼의 순서와 함께 console 출력
            println("영화 추가 버튼이 눌린 위치: $curPos")
            // Firestore에 데이터 저장
            val data = hashMapOf(
                "posterImageUrl" to profile.posterImageUrl,
                "movieName" to profile.movieName,
                "movieGenreAndYear" to profile.movieGenreAndYear,
                "toInfoUrl" to profile.toInfoUrl
            )
            firestore.collection("movies").add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster = itemView.findViewById<ImageView>(R.id.iv_poster) // 영화 포스터
        val movieName = itemView.findViewById<TextView>(R.id.tv_movie_name) // 영화 제목
        val movieGenreAndYear = itemView.findViewById<TextView>(R.id.tv_movie_genre_year) // 영화 장르와 년도
        val toInfoUrl = itemView.findViewById<TextView>(R.id.tv_to_info_url) // 영화 정보
        val addButton: ImageButton = itemView.findViewById<ImageButton>(R.id.bt_add_playlist) // 추가 버튼

    }
}
