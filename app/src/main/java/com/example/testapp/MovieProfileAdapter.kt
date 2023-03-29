package com.example.testapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieProfileAdapter(val MovieProfileList: ArrayList<MovieProfiles>) : RecyclerView.Adapter<MovieProfileAdapter.CustomViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieProfileAdapter.CustomViewHolder { // onCreate랑 비슷 xml 연결
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_list_skeleton, parent, false) // layout 끌고 와서 Adapter에 붙이기
        return CustomViewHolder(view) // CustomViewHolder에 view 전달
    }

    override fun getItemCount(): Int {
        return minOf(MovieProfileList.size,3) // MovieProfileList 총 길이를 리턴
    }

    override fun onBindViewHolder(holder: MovieProfileAdapter.CustomViewHolder, position: Int) { // onCreateViewHolder로 만들어진 view를 가져다가 실제 연결
        val movieProfile = MovieProfileList[position]
        Glide.with(holder.itemView.context)
            .load(movieProfile.posterImageUrl)
            .into(holder.moviePoster)
        holder.movieName.text = MovieProfileList.get(position).movieName // Int는 다르게 해야함 .toString()으로
        holder.movieGenreAndYear.text = MovieProfileList.get(position).movieGenreAndYear
        holder.toInfoUrl.text = MovieProfileList.get(position).toInfoUrl

    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // View를 잡아주는,,,
        val moviePoster = itemView.findViewById<ImageView>(R.id.iv_poster) // 영화 포스터
        val movieName = itemView.findViewById<TextView>(R.id.tv_movie_name) // 영화 제목
        val movieGenreAndYear = itemView.findViewById<TextView>(R.id.tv_movie_genre_year) // 영화 장르와 년도
        val toInfoUrl = itemView.findViewById<TextView>(R.id.tv_to_info_url) // 영화 정보
    }

}