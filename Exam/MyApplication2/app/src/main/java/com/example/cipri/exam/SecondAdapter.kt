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
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

class SecondAdapter(var context: Context, var list: ArrayList<Parking>) : BaseAdapter() {
    private val handler = Handler(context.mainLooper)

    private class ViewHolder(row: View?) {
        var listName: TextView
        var listType: TextView
        var listPower: TextView
        var listStatus: TextView
        var listButtonDelete: Button

        init {
            this.listName = row?.findViewById(R.id.NameTxtO) as TextView
            this.listType = row?.findViewById(R.id.TypeTxtInsertO) as TextView
            this.listPower = row?.findViewById(R.id.PowerTxtInsertO) as TextView
            this.listStatus = row?.findViewById(R.id.StatusTxtInsertO) as TextView
            this.listButtonDelete = row?.findViewById(R.id.DeleteBtn) as Button
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.owner_item_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position) as Parking
        viewHolder.listName.text = item.name
        viewHolder.listType.text = item.type
        viewHolder.listStatus.text = item.status
        viewHolder.listPower.text = item.power.toString()
        viewHolder.listButtonDelete.setOnClickListener {
            val url = "http://10.0.2.2:2029/place/" + item.id.toString()
            val client = OkHttpClient()
            val request = Request.Builder().url(url).delete().build()

            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Loading...")
            progressDialog.show()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread(Runnable {
                        Log.d("SecondAdapter","No server connection")
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                        Thread.sleep(1_000)
                        progressDialog.dismiss() }) }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful)
                        runOnUiThread(Runnable {
                            Log.d("SecondAdapter","Deleting" + item.name)
                            list.remove(item)
                            notifyDataSetChanged()
                            Toast.makeText(context, "deleting " + item.name, Toast.LENGTH_LONG).show()
                            Thread.sleep(1_000)
                            progressDialog.dismiss() })
                    else
                        runOnUiThread(Runnable {
                            Log.d("SecondAdapter","No server connection")
                            Toast.makeText(context, response.message().toString(), Toast.LENGTH_LONG).show()
                            Thread.sleep(1_000)
                            progressDialog.dismiss() }) }
            }) }
        return view as View
    }

    private fun runOnUiThread(runnable: Runnable) {
        handler.post(runnable)
    }

    override fun getItem(position: Int): Any {
        return this.list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.list.count()
    }
}