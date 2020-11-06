package com.snowdango.inventorycontrol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

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
        }

    }
}