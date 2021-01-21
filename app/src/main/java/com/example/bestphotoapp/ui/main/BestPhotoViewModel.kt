package com.example.bestphotoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bestphotoapp.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BestPhotoViewModel : ViewModel() {
    private val liveDataForViewToObserve: MutableLiveData<BestPhotoData> = MutableLiveData()
    private val retrofitImpl: BPRetrofitImpl = BPRetrofitImpl()

        fun getData(): LiveData<BestPhotoData> {
            sendServerRequest()
            return liveDataForViewToObserve
        }

        private fun sendServerRequest() {
            liveDataForViewToObserve.value = BestPhotoData.Loading(null)
            val apiKey: String = BuildConfig.NASA_API_KEY
            if (apiKey.isBlank()) {
                BestPhotoData.Error(Throwable("You need API key"))
            } else {
                retrofitImpl.getRetrofitImpl().getBestPhotoApi(apiKey).enqueue(object :
                        Callback<BPServerResponseData> {
                    override fun onResponse(
                            call: Call<BPServerResponseData>,
                            response: Response<BPServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataForViewToObserve.value =
                                    BestPhotoData.Success(response.body()!!)
                        } else {
                            val message = response.message()
                            if (message.isNullOrEmpty()) {
                                liveDataForViewToObserve.value =
                                        BestPhotoData.Error(Throwable("Unidentified error"))
                            } else {
                                liveDataForViewToObserve.value =
                                        BestPhotoData.Error(Throwable(message))
                            }
                        }
                    }

                    override fun onFailure(call: Call<BPServerResponseData>, t: Throwable) {
                        liveDataForViewToObserve.value = BestPhotoData.Error(t)
                    }
                })
            }
        }
}