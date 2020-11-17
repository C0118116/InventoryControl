package com.snowdango.inventorycontrol.profile.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.snowdango.inventorycontrol.R
import com.snowdango.inventorycontrol.action.SentWebAPI
import com.snowdango.inventorycontrol.profile.main.MainActivity
import com.snowdango.inventorycontrol.utility.CreateData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FromActivity:AppCompatActivity(),View.OnClickListener {

    private val sentWebApi = SentWebAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_item_register)
        findViewById<Button>(R.id.regist_send).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val code: String = intent.getStringExtra("code").toString()
        val name: String = findViewById<EditText>(R.id.form_edit).text.toString()
        val limit: Int = Integer.parseInt(findViewById<EditText>(R.id.form_edit2).text.toString())
        val num: Int = Integer.parseInt(findViewById<EditText>(R.id.form_edit3).text.toString())

        val createData = CreateData(code,name,num,limit)
        try {
            CoroutineScope(Dispatchers.Default).launch {
                sentWebApi.sendApiCreate(createData)
            }
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }catch (e:Exception){
            showFailed()
        }
    }

    private fun showFailed(){
        Toast.makeText(this, "this request is missing",Toast.LENGTH_SHORT).show()
    }
}