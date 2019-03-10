package com.example.cipri.android.Crud

import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Projectiles
import okhttp3.*
import java.lang.Exception

class AddProjectile(private var name:String,private var type:String,private var caliber:String,private var number:String) {

    constructor(projectiles: Projectiles): this(projectiles.Name,projectiles.Type,projectiles.Caliber.toString(),projectiles.Number.toString())

    fun check() :Boolean{
        return try {
            caliber.toInt()
            number.toInt()
            true
        } catch (exp:Exception) {
            false
        }
    }

    fun compose() :Projectiles{ return Projectiles(0,name,type,caliber.toInt(),number.toInt()) }
    fun addLocal(db: DataBaseHandler){ db.insertProjectile(compose()) }

    fun addServer(): Request {
        val url = "http://a409b0dc.ngrok.io/api/Projectile"
        val json = "{" +
                "'Id' : " + 0 + " , " +
                "'Name' : '" + name+ "' , " +
                "'Type' : '" + type + "' , " +
                "'Caliber' : " + caliber.toInt() + " , " +
                "'Number' : " + number.toInt() + "}"

        val JSON =  MediaType.get("application/json; charset=utf-8")
        val requestBody = RequestBody.create(JSON,json)
        return Request.Builder().url(url).post(requestBody).build()
    }
}