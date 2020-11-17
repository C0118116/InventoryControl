package com.snowdango.inventorycontrol.profile.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.snowdango.inventorycontrol.R
import com.snowdango.inventorycontrol.profile.form.FromActivity
import com.snowdango.inventorycontrol.profile.inventory.ListActivity
import com.snowdango.inventorycontrol.profile.order.OrderActivity

class MainActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inventoryViewButton = findViewById<Button>(R.id.Button1)
        val orderViewButton = findViewById<Button>(R.id.Button2)
        val addItemButton = findViewById<Button>(R.id.Button3)

        inventoryViewButton.setOnClickListener(this)
        orderViewButton.setOnClickListener(this)
        addItemButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.Button1 -> {
                val intent = Intent(this,
                    ListActivity::class.java)
                startActivity(intent)
            }

            R.id.Button2 -> {
                val intent = Intent(this,
                    OrderActivity::class.java)
                startActivity(intent)
            }

            R.id.Button3 -> {
                val intentIntegrator = IntentIntegrator(this).apply {
                    setBeepEnabled(false)
                    setOrientationLocked(false)
                    setCameraId(0)
                    setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                    setPrompt("SCAN")
                    setBarcodeImageEnabled(false)
                    initiateScan()
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            }else{
                Log.d("jancode",result.formatName)
                    val intent = Intent(
                        this,
                        FromActivity::class.java
                    )
                    intent.putExtra("code", result.contents)
                    startActivity(intent)

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    public fun showFailed(context: Context, returnMain: Boolean){
        Toast.makeText(context, "this request is missing",Toast.LENGTH_SHORT).show()
    }
}