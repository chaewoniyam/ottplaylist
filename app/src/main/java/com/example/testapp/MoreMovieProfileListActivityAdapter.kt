package com.example.testapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MoreMovieProfileListActivityAdapter(val MovieProfileList: ArrayList<MovieProfiles>) : RecyclerView.Adapter<MoreMovieProfileListActivityAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie_list_skeleton, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                val profile: MovieProfiles = MovieProfileList.get(curPos)
                val url = "https://m.kinolights.com${profile.toInfoUrl}"
                val intent = Intent(itemView.context, MovieInfoActivity::class.java)
                intent.putExtra("url", url)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return (MovieProfileList.size)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.movieName.text = MovieProfileList.get(position).movieName
        holder.movieGenreAndYear.text = MovieProfileList.get(position).movieGenreAndYear
        holder.toInfoUrl.text = MovieProfileList.get(position).toInfoUrl
//        val item = MovieProfileList[position]
//        // 영화 포스터 이미지 로드
//        Glide.with(holder.itemView.context)
//            .load(item.moviePoster)  // 이미지 URL
//            .into(holder.moviePoster)  // ImageView
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // View를 잡아주는,,,
       // val moviePoster = itemView.findViewById<ImageView>(R.id.iv_poster) // 영화 포스터
        val movieName = itemView.findViewById<TextView>(R.id.tv_movie_name) // 영화 제목
        val movieGenreAndYear = itemView.findViewById<TextView>(R.id.tv_movie_genre_year) // 영화 장르와 년도
        val toInfoUrl = itemView.findViewById<TextView>(R.id.tv_to_info_url) // 영화 정보
    }

}