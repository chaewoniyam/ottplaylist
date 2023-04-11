package com.example.testapp

import java.util.Date


data class ModelFriends(var userNickName : String? = null,
                        var uid : String? = null,
                        var userId : String? = null){

    data class Alarms(
        var memberId: String? = null,
        var message: String? = null,
        var date: Date = Date(),
        var contentId: String? = null)

}