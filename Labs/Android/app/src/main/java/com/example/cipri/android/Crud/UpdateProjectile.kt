package com.example.cipri.android.Crud

import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Projectiles
import okhttp3.*
import java.util.ArrayList

class UpdateProjectile(var pname:String , var type:String, var caliber:String , var number:String) {

    fun update(db: DataBaseHandler)
    {
        db.updateProjectile(pname,type,caliber,number)
    }

    fun update(projlist: ArrayList<Projectiles>) : Request{
        var id = 0
        for (i in projlist)
            if (i.Name == pname) {
                id = i.Id
                if (type == "")
                    type = i.Type
                if (caliber == "")
                    caliber = i.Caliber.toString()
                if (number == "")
                    number = i.Number.toString()
            }
        val json = "{" +
                "'Id' : " + id + " , " +
                "'Name' : '" + pname + "' , " +
                "'Type' : '" + type + "' , " +
                "'Caliber' : " + caliber.toInt() + " , " +
                "'Number' : " + number.toInt() + "}"
        val url = "http://a409b0dc.ngrok.io/api/Projectile/" + id.toString()

        val JSON =  MediaType.get("application/json; charset=utf-8")
        val requestBody = RequestBody.create(JSON,json)
        return Request.Builder()
            .url(url)
            .put(requestBody)
            .build()
    }
}