package com.snowdango.inventorycontrol.profile.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.snowdango.inventorycontrol.utility.JsonData
import com.snowdango.inventorycontrol.R
import com.snowdango.inventorycontrol.action.SentWebAPI
import com.snowdango.inventorycontrol.databinding.OrderItemBinding
import com.snowdango.inventorycontrol.store.Store
import com.snowdango.inventorycontrol.utility.ChangeData
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.databinding.BindableItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class OrderActivity :AppCompatActivity(), View.OnClickListener{

    private val sentWebAPI = SentWebAPI()
    private var requestID = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reget,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        switchSpinner(true)
        getData()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<Button>(R.id.order_button_use).apply {
            setOnClickListener(this@OrderActivity)
        }
        findViewById<Button>(R.id.order_button_add).apply {
            setOnClickListener(this@OrderActivity)
        }
        switchSpinner(true)

        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(applicationContext).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }

        getData()

        Store.order.observe(this, Observer {
            groupAdapter.clear()
            val gson = Gson()
            val json = gson.fromJson(it,Array<JsonData>::class.java)
            if(json != null) {
                for (data in json) {
                    groupAdapter.add(
                        OrderDataItem(
                            data
                        )
                    )
                }
                switchSpinner(false)
            }
        })
    }

    private fun getData(){
        CoroutineScope(Dispatchers.Default).launch {
            sentWebAPI.sendApiGet()
        }
    }

    override fun onClick(p0: View?) {
        val intentIntegrator = IntentIntegrator(this).apply {
            setBeepEnabled(false)
            setOrientationLocked(false)
            setCameraId(0)
            setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
            setPrompt("SCAN")
            setBarcodeImageEnabled(false)
            when(p0?.id){
                R.id.order_button_add -> requestID = 2222
                R.id.order_button_use -> requestID = 1111
            }
        }
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            Log.d("result","not null")
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            }else{
                switchSpinner(true)
                val position = searchPosition(result.contents)
                Log.d("position" , position.toString())
                if(position.first != -1) {
                    try {
                        CoroutineScope(Dispatchers.Default).launch {
                            if (requestID == 1111) {
                                sentWebAPI.sendApiDecrease(ChangeData(result.contents, 1))
                                requestID = 0
                            } else if (requestID == 2222) {
                                sentWebAPI.sendApiIncrease(ChangeData(result.contents, 1))
                                requestID = 0
                            }
                        }
                    }catch (e: Exception){
                        switchSpinner(false)
                        showFailed()
                    }
                }else{
                    Toast.makeText(this,"not data",Toast.LENGTH_SHORT).show()
                }
            }
        }else {
            Log.d("result","null")
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun searchPosition(code: String): Pair<Int,Int>{
        val gson = Gson()
        val inventoryList = gson.fromJson(Store.inventory.value, Array<JsonData>::class.java)
        for (num in inventoryList.indices) {
            if (inventoryList[num].code == code) return Pair(0,num)
        }
        val orderList = gson.fromJson(Store.order.value, Array<JsonData>::class.java)
        for(num in orderList.indices){
            if (orderList[num].code == code) return Pair(1,num)
        }
        return Pair(-1,-1)
    }

    private fun switchSpinner(mode: Boolean){
        val progress = findViewById<ProgressBar>(R.id.progress)
        if(mode){
            progress.visibility = View.VISIBLE
        }else{
            progress.visibility = View.GONE
        }
    }

    private fun showFailed() {
        Toast.makeText(this, "this request is missing",Toast.LENGTH_SHORT).show()
    }
}

class OrderDataItem(private val data: JsonData): BindableItem<OrderItemBinding>(){
    override fun getLayout(): Int = R.layout.order_item
    override fun bind(viewBinding: OrderItemBinding, position: Int) {
        viewBinding.nameText = data.name
        viewBinding.numText = data.num.toString()
    }
}