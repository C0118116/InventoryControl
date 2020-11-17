package com.snowdango.inventorycontrol.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class Store {

    companion object{
        val inventory: LiveData<String> = MutableLiveData<String>().apply { value = "" }
        val order: LiveData<String> = MutableLiveData<String>().apply { value = "" }
    }
}