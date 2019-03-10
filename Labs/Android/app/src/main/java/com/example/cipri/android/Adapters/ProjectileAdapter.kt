package com.example.cipri.android.Adapters

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.cipri.android.Arsenal
import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Projectiles
import com.example.cipri.android.R
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class ProjectileAdapter(var context: Context, var projectiles : ArrayList<Projectiles>, var db:DataBaseHandler) : BaseAdapter() {

    private val handler = Handler(context.mainLooper)
    private var animationIndex : Int = 1

    private class ViewHolder(row : View?) {
        var projectileName : TextView
        var projectileType : TextView
        var projectileCaliber : TextView
        var projectileNumber : TextView
        var deleteBtn : ImageButton

        init {
            this.projectileName = row?.findViewById(R.id.ProjectileName) as TextView
            this.projectileType = row?.findViewById(R.id.TypeInsert) as TextView
            this.projectileCaliber = row?.findViewById(R.id.CaliberInsert) as TextView
            this.projectileNumber = row?.findViewById(R.id.NumberInsertProjectile) as TextView
            this.deleteBtn = row?.findViewById(R.id.DeleteBtn) as ImageButton
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val viewHolder : ViewHolder

        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.projectile_item_list, parent, false)
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

        val projectile : Projectiles = getItem(position) as Projectiles
        viewHolder.projectileName.text = projectile.Name
        viewHolder.projectileType.text = projectile.Type
        val caliber = projectile.Caliber.toString() + "mm"
        viewHolder.projectileCaliber.text = caliber
        viewHolder.projectileNumber.text = projectile.Number.toString()
        viewHolder.deleteBtn.setOnClickListener {
            val url = "http://a409b0dc.ngrok.io/api/Projectile/" + projectile.Id.toString()
            val client = OkHttpClient()
            val request = Request.Builder().url(url).delete().build()

            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) { runOnUiThread( Runnable {
                    Toast.makeText(context, "No server connection", Toast.LENGTH_LONG).show() })}

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful)
                        runOnUiThread( Runnable {
                        projectiles.remove(projectile)
                        db.deleteProjectile(projectile.Name)
                        notifyDataSetChanged()
                        Toast.makeText(context, "deleting " + projectile.Name, Toast.LENGTH_LONG).show() })
                    else
                        runOnUiThread( Runnable {
                            Toast.makeText(context, "No server connection", Toast.LENGTH_LONG).show() })
                }})
        }
        return view as View
    }

    private fun runOnUiThread(runnable: Runnable){
        handler.post(runnable)
    }

    override fun getItem(position: Int): Any {
        return this.projectiles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.projectiles.count()
    }

    fun setAnimation(index: Int){
        this.animationIndex = index
    }

    fun setList(proj : ArrayList<Projectiles>){
        projectiles = proj
    }

}