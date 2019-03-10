package com.example.cipri.exam

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.add_parking.view.*
import okhttp3.*
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

class SecondActivity : AppCompatActivity() {

    private lateinit var secondAdapter: SecondAdapter
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        context = this@SecondActivity
        val addBtn = findViewById<Button>(R.id.AddBtn)

        addBtn.setOnClickListener {
            val addDialogView = LayoutInflater.from(this).inflate(R.layout.add_parking, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(addDialogView)
            val addAlertDialog = builder.show()

            addDialogView.CancelBtn.setOnClickListener {
                addAlertDialog.dismiss()
            }

            addDialogView.AddBtnItem.setOnClickListener {
                val nameP = addDialogView.NameBox.text.toString()
                val typeP = addDialogView.TypeBox.text.toString()
                val powerP = addDialogView.PowerBox.text.toString()

                val url = "http://10.0.2.2:2029/place"
                val requestBody =  FormBody.Builder()
                    .add("id","0")
                    .add("name", nameP)
                    .add("type", typeP)
                    .add("power", powerP)
                    .build()
                val request = Request.Builder().url(url).post(requestBody).build()
                val client = OkHttpClient()

                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Loading...")
                progressDialog.show()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {runOnUiThread {
                        Log.d("SecondActivity","No server connection")
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                        Thread.sleep(1_000)
                        progressDialog.dismiss()}}
                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            runOnUiThread {
                                Log.d("SecondActivity","Added to the server")
                                Toast.makeText(context, "Added", Toast.LENGTH_LONG).show()
                                Thread.sleep(1_000)
                                progressDialog.dismiss()
                                initData()}
                        }else{
                            runOnUiThread {
                                Log.d("SecondActivity","No server connection")
                                Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG).show()
                                Thread.sleep(1_000)
                                progressDialog.dismiss()}}}
                })
                addAlertDialog.dismiss() }
        }
        initData()
    }

    private fun initData(){
        val client = OkHttpClient()
        val url = "http://10.0.2.2:2029/allPlaces"
        val request = Request.Builder().url(url).get().build()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { runOnUiThread {
                Log.d("SecondActivity","No server connection")
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                Thread.sleep(1_000)
                progressDialog.dismiss() }}

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val collectionType = object: TypeToken<ArrayList<Parking>>() {}.type
                    val gamefeed = gson.fromJson<ArrayList<Parking>>(body, collectionType)
                    val  sortedList =  gamefeed.sortedWith(compareBy(Parking::status,Parking::power))
                    runOnUiThread {
                        Log.d("SecondActivity","Getting the data from the server")
                        val gameList = findViewById<ListView>(R.id.Clerk_list)
                        secondAdapter = SecondAdapter(context, ArrayList(sortedList.reversed()))
                        gameList.adapter = secondAdapter
                        Thread.sleep(1_000)
                        progressDialog.dismiss()
                    }
                }else{
                    runOnUiThread {
                        Log.d("SecondActivity","No server connection")
                        Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG).show()
                        Thread.sleep(1_000)
                        progressDialog.dismiss() }}
            }
        })
    }
}
