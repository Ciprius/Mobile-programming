package com.example.cipri.exam

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class FirstActivity : AppCompatActivity() {

    private lateinit var firstAdapter: FirstAdapter
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var context: Context
    private lateinit var  dataBaseHandler: DataBaseHandler
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        context = this@FirstActivity
        val refreshBtn = findViewById<Button>(R.id.RefreshBtn)
        val myTaken = findViewById<Button>(R.id.MyTakenBtn)
        initData()
        dataBaseHandler = DataBaseHandler(context)
        refreshBtn.setOnClickListener { initData() }
        
        myTaken.setOnClickListener {
            if (index == 0){
                index = 1
                refreshBtn.setOnClickListener { displayMyTaken() }
                displayMyTaken()
                myTaken.text = "Show Parking"
            } else {
                index = 0
                refreshBtn.setOnClickListener { initData() }
                initData()
                myTaken.text = "History"
            }
        }
    }

    private fun initData(){
        val client = OkHttpClient()
        val url = "http://10.0.2.2:2029/places"
        val request = Request.Builder().url(url).get().build()

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) { runOnUiThread {
                Log.d("FirstActivity", "No server connection")
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                Thread.sleep(1_000)
                progressDialog.dismiss()}
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val collectionType = object: TypeToken<ArrayList<Parking>>() {}.type
                    val gamefeed = gson.fromJson<ArrayList<Parking>>(body, collectionType)
                    runOnUiThread {
                        Log.d("FirstActivity", "Load data from server Client side")
                        val gameList = findViewById<ListView>(R.id.list_items)
                        firstAdapter = FirstAdapter(context,gamefeed,0,dataBaseHandler)
                        gameList.adapter = firstAdapter
                        Thread.sleep(1_000)
                        progressDialog.dismiss()
                    }
                }else{
                    runOnUiThread {
                        Log.d("FirstActivity", "No server connection")
                        Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG).show()
                        Thread.sleep(1_000)
                        progressDialog.dismiss()}}
            }
        })
    }
    
    private  fun displayMyTaken(){
        val prodList = findViewById<ListView>(R.id.list_items)
        Log.d("FirstActivity", "Loading data from local database")
        detailAdapter = DetailAdapter(context,ArrayList(dataBaseHandler.readProducts()),1,dataBaseHandler)
        prodList.adapter = detailAdapter
    }
}
