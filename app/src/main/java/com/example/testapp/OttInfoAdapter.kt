package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class OttInfoAdapter (val ottPlatformList: ArrayList<OttInfo>) : RecyclerView.Adapter<OttInfoAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OttInfoAdapter.CustomViewHolder { // onCreate랑 비슷 xml 연결
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_movie_ott_platforms, parent, false) // layout 끌고 와서 Adapter에 붙이기
        return CustomViewHolder(view) // CustomViewHolder에 view 전달
    }

    override fun getItemCount(): Int {
        return minOf(ottPlatformList.size,3)
    }

    // OttInfoAdapter 클래스의 onBindViewHolder 함수
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val ottInfo = ottPlatformList[position]
        holder.ottName.text = ottInfo.ottName

        // Glide를 사용하여 이미지 로드 및 표시
//        Glide.with(holder.itemView.context)
//            .load(ottInfo.ottPlatformImageResource)
//            .placeholder(R.drawable.baseline_data_thresholding_24)
//            .error(R.drawable.baseline_error_24)
//            .into(holder.ottPlatformImg)


//        holder.streamingVodList.text = ottInfo.streamingVodList
//
//        holder.itemView.setOnClickListener {
//            val url = ottInfo.streamingVodList
//            val intent = if (url.startsWith("http://") || url.startsWith("https://")) {
//                Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            } else {
//                Intent(Intent.ACTION_VIEW, Uri.parse("https://$url"))
//            }
//            val requestCode = 1 // 원하는 코드로 변경 가능
//
//            // startActivityForResult로 액티비티 시작
//            (holder.itemView.context as Activity).startActivityForResult(intent, requestCode)
//        }
    }



    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ottName = itemView.findViewById<TextView>(R.id.tv_ott_name)
        //val streamingVodList = itemView.findViewById<TextView>(R.id.tv_ott_url)
       // val ottPlatformImg = itemView.findViewById<ImageView>(R.id.iv_ott_img)
    }
}