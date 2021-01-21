package com.example.bestphotoapp.ui.main


import retrofit2.http.GET
import retrofit2.http.Query

interface BestPhotoApi {
        @GET("planetary/apod")
        fun getBestPhotoApi(@Query("api_key")apiKey:String):
                retrofit2.Call<BPServerResponseData>
}
