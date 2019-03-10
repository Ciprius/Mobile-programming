package com.example.cipri.exam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Btn1 = findViewById<Button>(R.id.Btn1)
        val Btn2 = findViewById<Button>(R.id.Btn2)

        Btn1.setOnClickListener {
            val intent = Intent(applicationContext,FirstActivity::class.java)
            startActivity(intent)
        }

        Btn2.setOnClickListener {
            val intent = Intent(applicationContext,SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
