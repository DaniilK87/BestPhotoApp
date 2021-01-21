package com.example.bestphotoapp.ui.main

sealed class BestPhotoData {
    data class Success(val serverResponseData: BPServerResponseData): BestPhotoData()
    data class Error(val error: Throwable): BestPhotoData()
    data class Loading(val progress: Int?): BestPhotoData()
}