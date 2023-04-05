package com.example.testapp

import java.util.*


data class PostInfo(var userId:String?="",
                    var title:String="",
                    var story:String="",
                    var tag1:String="",
                    var tag2:String="",
                    var tag3:String="",
                    var imageUrl:String="", // 사진이 저장된 경로
                    var date: Date =Date(),

                    )