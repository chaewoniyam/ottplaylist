package com.example.testapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.testapp.databinding.ActivityLoginBinding
import com.example.testapp.databinding.ActivityPostingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class PostingActivity : AppCompatActivity() {
    private val GALLERY_CODE = 10
    var photo: ImageView? = null
    private lateinit var binding: ActivityPostingBinding
    private var storage: FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        findViewById<View>(R.id.profileImg).setOnClickListener(onClickListener)
        photo = findViewById<View>(R.id.profileImg) as ImageView
        storage = FirebaseStorage.getInstance()
        auth = Firebase.auth

        val title = findViewById<EditText>(R.id.title)
        val story = findViewById<EditText>(R.id.story)
        val tag1 = findViewById<EditText>(R.id.hashtag1)
        val tag2 = findViewById<EditText>(R.id.hashtag2)
        val tag3 = findViewById<EditText>(R.id.hashtag3)
        val postbutton =findViewById<Button>(R.id.postbutton)

        postbutton.setOnClickListener{

            var postInfo = PostInfo()

            postInfo.uid = fbAuth?.uid
            postInfo.title = title.toString()
            postInfo.story= story.toString()
            postInfo.tag1 = tag1.toString()
            postInfo.tag2 = tag2.toString()
            postInfo.tag3 = tag3.toString()

            fbFirestore?.collection("postInfo")?.document(fbAuth?.uid.toString())?.set(postInfo)
        }

    }

    var onClickListener =
        View.OnClickListener { v ->
            when (v.id) {
                R.id.profileImg -> loadAlbum()
            }
        }


    //갤러리 여는 함수
    private fun loadAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, GALLERY_CODE)
    }

    //가져온 사진 스토리지에 저장.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        fbAuth = FirebaseAuth.getInstance()
        fbFirestore = FirebaseFirestore.getInstance()

        if (requestCode == GALLERY_CODE) {

            if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
                val uri = data.data // 이미지의 URI를 가져옴
                val imageId = uri?.lastPathSegment?.toInt() ?: -1
                // TODO: 이후 작업 처리

                val file = data!!.data
                val storageRef = storage!!.reference

                val riversRef = storageRef.child("photo/"+imageId)
                val uploadTask = riversRef.putFile(file!!)
                try {
                    val `in` = contentResolver.openInputStream(data.data!!)
                    val img = BitmapFactory.decodeStream(`in`)
                    `in`!!.close()
                    photo!!.setImageBitmap(img)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                uploadTask.addOnFailureListener {
                    Toast.makeText(
                        this@PostingActivity,
                        "사진이 정상적으로 업로드 되지 않았습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener {


                    //데이터스토어에 저장

                    var postInfo = PostInfo()
                    println(postInfo)

                    postInfo.imageId = imageId.toString()
                    fbFirestore?.collection("postInfo")?.document(fbAuth?.uid.toString())?.set(postInfo)

                    Toast.makeText(
                        this@PostingActivity,
                        "사진이 정상적으로 업로드 되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    } // onActivityResult
}
