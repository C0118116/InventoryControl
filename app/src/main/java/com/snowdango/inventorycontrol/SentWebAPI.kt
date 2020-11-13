package com.snowdango.inventorycontrol

import android.view.KeyEvent
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException


class SentWebAPI {
    val SCHEME = "https"
    val HOST = "asia-northeast1-inventrycontrol-df703.cloudfunctions.net"
    val ROOT = "inventryControl"
    val PATH = "create"

    private fun <T>getBaseRequest(jsonBase: T): Request.Builder{
        val builder: HttpUrl.Builder = HttpUrl.Builder().apply {
            scheme(SCHEME)
            host(HOST)
            addPathSegment(ROOT)
            addPathSegment(PATH)
        }
        val jsonMedia = "application/json; charset=utf-8".toMediaTypeOrNull()
        val gson = Gson()
        val jsonString = gson.toJson(jsonBase)
        return Request.Builder().apply {
            url(builder.build())
            post(RequestBody.create(jsonMedia,jsonString))
            build()
        }
    }

    fun <T>sendApi(createData:T){
        val request = getBaseRequest(createData)
        val client = OkHttpClient()
        client.newCall(request.build()).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException){
                GlobalScope.launch(Dispatchers.Main){
                    (MainActivity.data as MutableLiveData<String>).value = "error"
                }
            }

            override fun onResponse(call: Call, response: Response){
                GlobalScope.launch(Dispatchers.Main) {
                    (MainActivity.data as MutableLiveData<String>).value = "ok"
                }
            }
        })
    }

}


