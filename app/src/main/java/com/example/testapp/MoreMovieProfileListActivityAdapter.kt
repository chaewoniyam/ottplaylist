package com.example.testapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

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
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName = itemView.findViewById<TextView>(R.id.tv_movie_name)
        val movieGenreAndYear = itemView.findViewById<TextView>(R.id.tv_movie_genre_year)
        val toInfoUrl = itemView.findViewById<TextView>(R.id.tv_to_info_url)
    }
}
