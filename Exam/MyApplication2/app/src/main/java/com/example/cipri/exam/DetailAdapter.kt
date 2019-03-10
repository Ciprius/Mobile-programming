package com.example.cipri.exam

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class DetailAdapter (var context: Context, var list: ArrayList<Parking>, var index: Int, var db: DataBaseHandler): BaseAdapter() {

    private val handler = Handler(context.mainLooper)

    private class ViewHolder(row: View?){
        var listName : TextView
        var listType : TextView
        var listStatus : TextView
        var listPower : TextView
        var listView : Button

        init {
            this.listName = row?.findViewById(R.id.NameInsert) as TextView
            this.listType = row?.findViewById(R.id.TypeInsert) as TextView
            this.listPower = row?.findViewById(R.id.PowerInsert) as TextView
            this.listStatus = row?.findViewById(R.id.StatusInsert) as TextView
            this.listView = row?.findViewById(R.id.OkBtn) as Button
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val viewHolder : ViewHolder

        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.detail_item_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position) as Parking
        viewHolder.listName.text = item.name
        viewHolder.listPower.text = item.power.toString()
        viewHolder.listType.text = item.type
        viewHolder.listStatus.text = item.status

        if (item.status != "taken")
            viewHolder.listView.visibility = View.INVISIBLE
        else
            viewHolder.listView.visibility = View.VISIBLE

        viewHolder.listView.text = "Free"
        viewHolder.listView.setOnClickListener {
            val url = "http://10.0.2.2:2029/free"
            val requestBody = FormBody.Builder()
                .add("id", item.id.toString())
            val request = Request.Builder().url(url).post(requestBody.build()).build()
            val client = OkHttpClient()

            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Loading...")
            progressDialog.show()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread(
                        Runnable {
                            Log.d("DetailAdapter", "No server connection")
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                            Thread.sleep(1_000)
                            progressDialog.dismiss()
                        })
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread(
                            Runnable {
                                Thread.sleep(1_000)
                                progressDialog.dismiss()
                                Log.d("DetailAdapter", "Returned")
                                Log.d("DetailAdapter", "Update the status in local database")
                                Toast.makeText(context, "Returned", Toast.LENGTH_LONG).show()
                                db.updateProduct(item.id,"available")
                            })
                    } else {
                        runOnUiThread(
                            Runnable {
                                Log.d("DetailAdapter", "No server connection")
                                Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG).show()
                                Thread.sleep(1_000)
                                progressDialog.dismiss()
                            })
                    }
                }
            })

        }
        return view as View
    }

    private fun runOnUiThread(runnable: Runnable){
        handler.post(runnable)
    }

    override fun getItem(position: Int): Any {
        return  this.list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return  this.list.count()
    }
}