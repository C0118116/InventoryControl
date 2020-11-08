package com.snowdango.inventorycontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

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

            }

            R.id.Button2 -> {

            }

            R.id.Button3 -> {
                val intentIntegrator = IntentIntegrator(this).apply {
                    setBeepEnabled(false)
                    setOrientationLocked(false)
                    setCameraId(0)
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
                Log.d("Scan", "Scanned")
                Log.d("Scan", result.toString())
                Toast.makeText(this, "Scanned -> " + result.contents, Toast.LENGTH_SHORT).show()
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}