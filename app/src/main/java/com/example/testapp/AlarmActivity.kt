package com.example.testapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AlarmActivity : AppCompatActivity() {
    var memberUid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val alarm_recyclerView = findViewById<RecyclerView>(R.id.alarm_recyclerview)

        alarm_recyclerView.adapter = AlarmRecyclerviewAdapter()
        alarm_recyclerView.layoutManager = LinearLayoutManager(this)


    }

    inner class AlarmRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var alarms: ArrayList<ModelFriends.Alarms> = arrayListOf()

        init {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(memberUid!!)
                .collection("alarms")
                .orderBy("date")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    alarms.clear()
                    for (snapshot in querySnapshot.documents) {
                        alarms.add(snapshot.toObject(ModelFriends.Alarms::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm,parent,false)

            return CustomViewHolder(view)
        }

        private inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view){
            var memberEmailTextView: TextView = view.findViewById(R.id.alarms_textView)
            var messageTextView: TextView = view.findViewById(R.id.alarms_message)
            var dateTextView: TextView = view.findViewById(R.id.alarms_date)
        }

        override fun getItemCount(): Int {
            return alarms.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder.itemView
            val customViewHolder = holder as CustomViewHolder

            customViewHolder.messageTextView.text = alarms[position].message
            customViewHolder.memberEmailTextView.text = alarms[position].memberId
            customViewHolder.dateTextView.text = alarms[position].date.toString()
        }


    }
}
