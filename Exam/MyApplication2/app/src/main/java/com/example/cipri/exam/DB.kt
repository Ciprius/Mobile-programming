package com.example.cipri.exam

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.util.ArrayList

const val DATABASE_NAME = "LocalDB"

const val PARKING_TABLE = "Bike"
const val COL_BIKE_NAME = "name"
const val COL_BIKE_TYPE = "type"
const val COL_BIKE_POWER = "power"
const val COL_BIKE_STATUS = "status"
const val COL_BIKE_ID = "id"


class  DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val tankTable = "CREATE TABLE " + PARKING_TABLE + " (" +
                COL_BIKE_ID + " INTEGER PRIMARY KEY , " +
                COL_BIKE_NAME + " VARCHAR(256)," +
                COL_BIKE_TYPE + " VARCHAR(256)," +
                COL_BIKE_POWER + " INTEGER," +
                COL_BIKE_STATUS + " VARCHAR(256) )"
        db?.execSQL(tankTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertProduct(product: Parking) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_BIKE_NAME, product.name)
        cv.put(COL_BIKE_TYPE, product.type)
        cv.put(COL_BIKE_POWER, product.power)
        cv.put(COL_BIKE_STATUS, product.status)
        val result = db.insert(PARKING_TABLE, null, cv)
        if (result == (-1).toLong())
            Toast.makeText(this.context, "Failed", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this.context, "Success", Toast.LENGTH_LONG).show()
    }

    fun updateProduct(prd: Int, product: String) {
        val db = this.writableDatabase
        val query = "Select * from $PARKING_TABLE"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val cv = ContentValues()
                cv.put(COL_BIKE_STATUS, product)
                db.update(PARKING_TABLE, cv, "$COL_BIKE_ID=?", arrayOf(prd.toString()))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun readProducts(): MutableList<Parking> {
        val list: MutableList<Parking> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $PARKING_TABLE"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val id: Int = result.getString(result.getColumnIndex(COL_BIKE_ID)).toInt()
                val name: String = result.getString(result.getColumnIndex(COL_BIKE_NAME))
                val type: String = result.getString(result.getColumnIndex(COL_BIKE_TYPE))
                val power: Int = result.getString(result.getColumnIndex(COL_BIKE_POWER)).toInt()
                val status: String = result.getString(result.getColumnIndex(COL_BIKE_STATUS))
                list.add(Parking(id, name, type, status,power))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }
}
