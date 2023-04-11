package com.example.testapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.tiles.LayoutElementBuilders.Image
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.NonDisposableHandle.parent

class HomeFragment : Fragment() {

    var firestore: FirebaseFirestore? = null
    var uid : String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        val rv_upload = view.findViewById<RecyclerView>(R.id.rv_upload)

        rv_upload.adapter = DetailViewRecyclerViewAdapter()
        rv_upload.layoutManager = LinearLayoutManager(activity)

        val heartImageButton = view.findViewById<ImageButton>(R.id.heartimage)

        heartImageButton.setOnClickListener {
            val intent = Intent(activity, AlarmActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {

            firestore?.collection("post")?.orderBy("date", Query.Direction.DESCENDING )
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreExec ->
                    contentDTOs.clear()
                    contentUidList.clear()
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomViewHolder(view)
        }
        override fun getItemCount(): Int {
            return contentDTOs.size
        }
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder = holder as CustomViewHolder
            //userId
            viewholder.tv_profile_nickname.text = contentDTOs!![position].userId

            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl)
                .into(viewholder.iv_post_image)

            //Explain of content
            viewholder.tv_explain_post.text = contentDTOs!![position].title

            //likes
            viewholder.tv_favoritecounter_post.text =
                "Likes " + contentDTOs!![position].favoriteCount

            //ProfileImage
//            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl)
//                .into(viewholder.iv_profile_image)

            //This code is when the button is clicked
            viewholder.iv_favorite_post.setOnClickListener{
                favoriteEvent(position)
            }
            //This code is when the page is loaded
            if(contentDTOs!![position].favorites.containsKey(uid)){
                viewholder.iv_favorite_post.setImageResource(R.drawable.baseline_favorite_24)
            }else{
                viewholder.iv_favorite_post.setImageResource(R.drawable.baseline_favorite_border_24)
            }
            viewholder.iv_comment_post.setOnClickListener{ v ->
                val intent = Intent(v.context,CommentActivity::class.java)
                intent.putExtra("contentUid",contentUidList[position])
                startActivity(intent)
            }
            viewholder.iv_post_image.setOnClickListener {
                val intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra("postId", contentUidList[position])
                startActivity(intent)
            }

        }

        fun favoriteEvent(position: Int){
            var tsDoc = firestore?.collection("post")?.document(contentUidList[position])
            firestore?.runTransaction { transaction ->
                var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if(contentDTO!!.favorites.containsKey(uid)){ // 좋아요가 이미 눌러져 있는 경우
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount!! - 1
                    contentDTO?.favorites?.remove(uid)
                }else{
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount!! + 1
                    contentDTO?.favorites?.set(uid!!, true)
                }
                transaction.set(tsDoc, contentDTO)
            }
        }


        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tv_profile_nickname: TextView = view.findViewById(R.id.tv_profile_nickname)
            val iv_post_image: ImageView = view.findViewById(R.id.iv_post_image)
            val tv_explain_post: TextView = view.findViewById(R.id.tv_explain_post)
            val iv_comment_post: ImageView = view.findViewById(R.id.iv_comment_post)
            val tv_favoritecounter_post: TextView = view.findViewById(R.id.tv_favoritecounter_post)
            val iv_profile_image: ImageView = view.findViewById(R.id.detailviewitem_profile_image)
            val iv_favorite_post: ImageView = view.findViewById(R.id.iv_favorite_post)


        }
    }
}