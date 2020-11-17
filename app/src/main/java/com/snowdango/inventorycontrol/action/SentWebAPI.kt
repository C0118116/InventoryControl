package com.snowdango.inventorycontrol.action

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.snowdango.inventorycontrol.BuildConfig
import com.snowdango.inventorycontrol.profile.form.FromActivity
import com.snowdango.inventorycontrol.profile.inventory.ListActivity
import com.snowdango.inventorycontrol.profile.order.OrderActivity
import com.snowdango.inventorycontrol.store.Store
import com.snowdango.inventorycontrol.utility.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.Exception

class SentWebAPI {

    val gson = Gson()
    private val client = OkHttpClient()
    private val jsonMedia = "application/json; charset=utf-8".toMediaTypeOrNull()

    suspend fun sendApiCreate(createData:CreateData){
        Log.d("request","create")
        val jsonString = gson.toJson(createData)
        val request = Request.Builder().apply {
            url(BuildConfig.CREATE_URL)
            post(RequestBody.create(jsonMedia,jsonString))
            build()
        }
        client.newCall(request.build()).execute()
        sendApiGet()
    }

    suspend fun sendApiIncrease(changeData: ChangeData){
        Log.d("request","increase")
        val jsonString = gson.toJson(changeData)
        val request = Request.Builder().apply {
            url(BuildConfig.INCREASE_URL)
            post(RequestBody.create(jsonMedia,jsonString))
            build()
        }
        client.newCall(request.build()).execute()
        sendApiGet()
    }

    suspend fun sendApiDecrease(changeData: ChangeData){
        Log.d("request","decrease")
        val jsonString = gson.toJson(changeData)
        val request = Request.Builder().apply {
            url(BuildConfig.DECREASE_URL)
            post(RequestBody.create(jsonMedia,jsonString))
            build()
        }
        client.newCall(request.build()).execute()
        sendApiGet()
    }

    suspend fun sendApiGet(){
        Log.d("request","get")
        val jsonString = gson.toJson(ChangeData(code = "SSS",num = 2))
        val request = Request.Builder().apply {
            url(BuildConfig.GET_URL)
            post(RequestBody.create(jsonMedia,jsonString))
            build()
        }
        client.newCall(request.build()).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                throw Exception()
            }

            override fun onResponse(call: Call, response: Response) {
                val stringBody = response.body?.string()
                Log.d("response",""+stringBody)
                val json = stringBody?.let { gson.fromJson(it,ReturnData::class.java) }
                Log.d("response",""+json?.inventory)
                Log.d("response",""+json?.order)
                CoroutineScope(Dispatchers.Main).launch {
                    (Store.inventory as MutableLiveData<String>).value = gson.toJson(json?.inventory)
                    (Store.order as MutableLiveData<String>).value = gson.toJson(json?.order)
                }
            }
        })
    }
}


