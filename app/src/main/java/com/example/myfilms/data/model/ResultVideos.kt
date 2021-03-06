package com.example.myfilms.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultVideos(

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("key")
    @Expose
    val key: String
)
