package com.example.cipri.android.Crud


import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Tank
import okhttp3.*
import java.lang.Exception

class AddTank(private var name:String, private var country:String, private var gun:String,private var weight:String, private var number:String) {

    constructor(tank: Tank) : this(tank.Name,tank.Country,tank.Gun.toString(),tank.Weight.toString(),tank.Number.toString())

    fun check() : Boolean{
        return try {
            gun.toInt()
            weight.toInt()
            number.toInt()
            true
        } catch (exp: Exception) {
            false
        }
    }

    fun compose(): Tank{ return Tank(0,name,weight.toInt(),gun.toInt(),country,number.toInt()) }
    fun addLocal(db: DataBaseHandler){ db.insertTank(compose()) }

    fun addServer(): Request{
        val url = "http://a409b0dc.ngrok.io/api/Tank"
        val json = "{ " +
                "'Id' : " + 0 + ", " +
                "'Name' : '" + name+ "' , " +
                "'Weight' : " + weight.toInt() + " , " +
                "'Gun' : " + gun.toInt() + " , " +
                "'Country' : '" + country + "' , " +
                "'Number' : " + number.toInt() + "}"

        val JSON =  MediaType.get("application/json")
        val requestBody = RequestBody.create(JSON,json)
        return Request.Builder().url(url).post(requestBody).build()
    }
}