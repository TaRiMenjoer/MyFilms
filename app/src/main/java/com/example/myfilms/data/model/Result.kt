package com.example.myfilms.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(

    @SerializedName("results")
    @Expose
    val movies: List<Movie>
)


