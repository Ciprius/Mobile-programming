package com.example.cipri.android.Crud

import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Tank
import okhttp3.*
import java.util.*

class UpdateTank(var tname:String, var country:String, var gun:String, var weight:String,var number:String) {

    fun update(db : DataBaseHandler)  {
        db.updateTank(tname,country,weight,gun,number)
    }

    fun update(tanklist: ArrayList<Tank>) :Request{
        var id = 0
        for (i in tanklist)
            if (i.Name == tname) {
                id = i.Id
                if (country == "")
                    country = i.Country
                if (gun == "")
                    gun = i.Gun.toString()
                if (weight == "")
                    weight = i.Weight.toString()
                if (number == "")
                    number = i.Number.toString()
            }

        val url = "http://a409b0dc.ngrok.io/api/Tank/" + id.toString()
        val json = "{ " +
                "'Id' : " + id + ", " +
                "'Name' : '" + tname + "' , " +
                "'Weight' : " + weight.toInt() + " , " +
                "'Gun' : " + gun.toInt() + " , " +
                "'Country' : '" + country + "' , " +
                "'Number' : " + number.toInt() + "}"

        val JSON =  MediaType.get("application/json; charset=utf-8")
        val requestBody = RequestBody.create(JSON,json)
        return Request.Builder()
            .url(url)
            .put(requestBody)
            .build()
    }

}