package com.example.testapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchPlaylistTitleAdapter(val ContentDTOList: ArrayList<ContentDTO>) : RecyclerView.Adapter<SearchPlaylistTitleAdapter.CustomViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPlaylistTitleAdapter.CustomViewHolder { // onCreate랑 비슷 xml 연결
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_movie_list_skeleton, parent, false) // layout 끌고 와서 Adapter에 붙이기
        return CustomViewHolder(view) // CustomViewHolder에 view 전달
    }

    override fun getItemCount(): Int {
        return (ContentDTOList.size) // MovieProfileList 총 길이를 리턴
    }

    override fun onBindViewHolder(holder: SearchPlaylistTitleAdapter.CustomViewHolder, position: Int) { // onCreateViewHolder로 만들어진 view를 가져다가 실제 연결
        val playlistProfile = ContentDTOList[position]
        holder.PlaylistMadeUser.text = ContentDTOList.get(position).userId
        holder.PlaylistTitle.text = ContentDTOList.get(position).title
//        Glide.with(holder.itemView.context)
//            .load(playlistProfile.imageUrl)
//            .into(holder.PlylistImage)
        holder.PlaylistPostId.text = ContentDTOList.get(position).postId


    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // View를 잡아주는,,,
        val PlylistImage = itemView.findViewById<ImageView>(R.id.iv_poster) // 영화 포스터
        val PlaylistTitle = itemView.findViewById<TextView>(R.id.tv_movie_name) // 영화 제목
        val PlaylistMadeUser = itemView.findViewById<TextView>(R.id.tv_movie_genre_year) // 영화 장르와 년도
        val PlaylistPostId = itemView.findViewById<TextView>(R.id.tv_to_info_url) // 영화 정보
    }

}