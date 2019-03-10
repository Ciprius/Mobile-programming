package com.example.cipri.android.Crud

import okhttp3.*

class ReadData {

    fun readTanks(): Request{
        val url = "http://a409b0dc.ngrok.io/api/Tank"
        return Request.Builder().url(url).build()
    }

    fun readProjectiles(): Request{
        val url = "http://a409b0dc.ngrok.io/api/Projectile"
        return Request.Builder().url(url).get().build()
    }
}