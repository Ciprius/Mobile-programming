package com.example.cipri.android.Adapters

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.cipri.android.Arsenal
import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Tank
import com.example.cipri.android.R
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class TankAdapter(var context: Context , var tanks : ArrayList<Tank>, var db: DataBaseHandler) : BaseAdapter() {

    lateinit var tankItem : Tank
    private val handler = Handler(context.mainLooper)
    private var animationIndex : Int = 1

    private class ViewHolder(row : View?) {
        var tankName : TextView
        var weightInput : TextView
        var gunInput : TextView
        var countryName : TextView
        var number : TextView
        var deleteBtn : ImageButton

        init {
            this.tankName = row?.findViewById(R.id.TankName) as TextView
            this.weightInput = row?.findViewById(R.id.WeightsInsert) as TextView
            this.gunInput = row?.findViewById(R.id.GunInsert) as TextView
            this.countryName = row?.findViewById(R.id.CountryInsert) as TextView
            this.number = row?.findViewById(R.id.NumberInsert) as TextView
            this.deleteBtn = row?.findViewById(R.id.DeleteBtn) as ImageButton
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val viewHolder : ViewHolder

        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.tank_item_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        if (this.animationIndex == 1) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            view!!.startAnimation(animation)
        }else{
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_left)
            view!!.startAnimation(animation)
        }

        val tank : Tank = getItem(position) as Tank
        viewHolder.tankName.text = tank.Name
        val weight = tank.Weight.toString() + "tonnes"
        viewHolder.weightInput.text = weight
        val gun = tank.Gun.toString() + "mm"
        viewHolder.gunInput.text = gun
        viewHolder.countryName.text =tank.Country
        viewHolder.number.text = tank.Number.toString()

        viewHolder.deleteBtn.setOnClickListener {
            val url = "http://a409b0dc.ngrok.io/api/Tank/" + tank.Id.toString()
            val client = OkHttpClient()
            val request = Request.Builder().url(url).delete().build()

            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) { runOnUiThread( Runnable {
                        Toast.makeText(context, "No server connection!!!", Toast.LENGTH_LONG).show() })}

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful)
                        runOnUiThread( Runnable {
                        tanks.remove(tank)
                        db.deleteTank(tank.Name)
                        notifyDataSetChanged()
                        Toast.makeText(context, "deleting " + tank.Name, Toast.LENGTH_LONG).show() })
                    else
                        runOnUiThread( Runnable {
                            Toast.makeText(context, "No server connection!!!", Toast.LENGTH_LONG).show() })
                }})
        }
        return view as View
    }

    private fun runOnUiThread(runnable: Runnable){
        handler.post(runnable)
    }

    override fun getItem(position: Int): Any {
        return tanks.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return tanks.count()
    }

    fun setAnimation(index: Int){
            this.animationIndex = index
    }

    fun setList(tank: ArrayList<Tank>){
        this.tanks = tank
    }
}