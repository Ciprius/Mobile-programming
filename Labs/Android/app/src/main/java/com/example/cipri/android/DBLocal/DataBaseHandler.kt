package com.example.cipri.android.DBLocal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.cipri.android.Model.Projectiles
import com.example.cipri.android.Model.Tank
import java.util.ArrayList

const val DATABASE_NAME = "LocalDB"

const val TANK_TABLE = "Tanks"
const val COL_TANK_NAME = "name"
const val COL_TANK_WEIGHT = "weight"
const val COL_TANK_GUN = "gun"
const val COL_TANK_COUNTRY = "country"
const val COL_TANK_NUMBER = "number"
const val COL_TANK_ID = "id"

const val PROJECTILE_TABLE = "Projectiles"
const val COL_PROJECTILE_NAME = "name"
const val COL_PROJECTILE_TYPE = "type"
const val COL_PROJECTILE_CALIBER = "caliber"
const val COL_PROJECTILE_NUMBER = "number"
const val COL_PROJECTILE_ID = "id"

class  DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val tankTable = "CREATE TABLE " + TANK_TABLE + " ("+
                COL_TANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_TANK_NAME + " VARCHAR(256)," +
                COL_TANK_WEIGHT + " INTEGER," +
                COL_TANK_GUN + " INTEGER," +
                COL_TANK_COUNTRY + " VARCHAR(256)," +
                COL_TANK_NUMBER + " INTEGER)"

        val projectileTable = "CREATE TABLE " + PROJECTILE_TABLE + " ("+
                COL_PROJECTILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_PROJECTILE_NAME + " VARCHAR(256)," +
                COL_PROJECTILE_TYPE + " VARCHAR(256)," +
                COL_PROJECTILE_CALIBER + " INTEGER," +
                COL_PROJECTILE_NUMBER + " INTEGER)"

        db?.execSQL(tankTable)
        db?.execSQL(projectileTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertTank(tank: Tank)
    {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_TANK_NAME,tank.Name)
        cv.put(COL_TANK_WEIGHT,tank.Weight)
        cv.put(COL_TANK_GUN,tank.Gun)
        cv.put(COL_TANK_COUNTRY,tank.Country)
        cv.put(COL_TANK_NUMBER,tank.Number)
        val result =  db.insert(TANK_TABLE,null,cv)
        if (result == (-1).toLong())
            Toast.makeText(this.context , "Failed" , Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this.context , "Success" , Toast.LENGTH_LONG).show()
    }

    fun insertProjectile(projectiles: Projectiles)
    {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_PROJECTILE_NAME, projectiles.Name)
        cv.put(COL_PROJECTILE_TYPE,projectiles.Type)
        cv.put(COL_PROJECTILE_CALIBER,projectiles.Caliber)
        cv.put(COL_PROJECTILE_NUMBER, projectiles.Number)
        val result =  db.insert(PROJECTILE_TABLE,null,cv)
        if (result == (-1).toLong())
            Toast.makeText(this.context , "Failed" , Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this.context , "Success" , Toast.LENGTH_LONG).show()
    }

    fun readTanks(): MutableList<Tank>{
        val list:MutableList<Tank> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TANK_TABLE"
        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                val id: Int = result.getString(result.getColumnIndex(COL_TANK_ID)).toInt()
                val name: String = result.getString(result.getColumnIndex(COL_TANK_NAME))
                val weight:Int = result.getString(result.getColumnIndex(COL_TANK_WEIGHT)).toInt()
                val gun:Int = result.getString(result.getColumnIndex(COL_TANK_GUN)).toInt()
                val country:String = result.getString(result.getColumnIndex(COL_TANK_COUNTRY))
                val number:Int = result.getString(result.getColumnIndex(COL_TANK_NUMBER)).toInt()
                list.add(Tank(id,name,weight,gun,country,number))
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun readProjectiles() : MutableList<Projectiles> {
        val list:MutableList<Projectiles> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $PROJECTILE_TABLE"
        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                val id: Int = result.getString(result.getColumnIndex(COL_PROJECTILE_ID)).toInt()
                val name = result.getString(result.getColumnIndex(COL_PROJECTILE_NAME))
                val caliber = result.getString(result.getColumnIndex(COL_PROJECTILE_CALIBER)).toInt()
                val type = result.getString(result.getColumnIndex(COL_PROJECTILE_TYPE))
                val number = result.getString(result.getColumnIndex(COL_PROJECTILE_NUMBER)).toInt()
                list.add(Projectiles(id,name,type,caliber,number))
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun filterTank(country:String, weight:String, gun:String,number:String) : ArrayList<Tank>{
        val list = ArrayList<Tank>()
        val db = this.readableDatabase
        var query = "SELECT * FROM $TANK_TABLE"

        if (country != "")
            query += " WHERE $COL_TANK_COUNTRY = '$country'"

        if (gun != "")
            query += if (country != "")
                " AND ${gun.toInt()} = $COL_TANK_GUN"
            else
                " WHERE ${gun.toInt()} = $COL_TANK_GUN"

        if (weight != "")
            query += if (country != "" || gun != "")
                " AND ${weight.toInt()} <= $COL_TANK_WEIGHT"
            else
                " WHERE ${weight.toInt()} <= $COL_TANK_WEIGHT"

        if (number != "")
            query += if (country != "" || weight != "" || gun != "")
                " AND ${number.toInt()} <= $COL_TANK_NUMBER"
            else
                " WHERE ${number.toInt()} <= $COL_TANK_NUMBER"

        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                val Id: Int = result.getString(result.getColumnIndex(COL_TANK_ID)).toInt()
                val Name: String = result.getString(result.getColumnIndex(COL_TANK_NAME))
                val Weight:Int = result.getString(result.getColumnIndex(COL_TANK_WEIGHT)).toInt()
                val Gun:Int = result.getString(result.getColumnIndex(COL_TANK_GUN)).toInt()
                val Country:String = result.getString(result.getColumnIndex(COL_TANK_COUNTRY))
                val Number:Int = result.getString(result.getColumnIndex(COL_TANK_NUMBER)).toInt()
                list.add(Tank(Id,Name,Weight,Gun,Country,Number))
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun filterProjectiles(type:String, caliber:String, number: String) :ArrayList<Projectiles> {
        val list = ArrayList<Projectiles>()
        val db = this.readableDatabase
        var query = "SELECT * FROM $PROJECTILE_TABLE"

        if (type != "")
            query += " WHERE $COL_PROJECTILE_TYPE = '$type'"

        if (caliber != "")
            query += if (type != "")
                " AND $COL_PROJECTILE_CALIBER = ${caliber.toInt()}"
            else
                " WHERE $COL_PROJECTILE_CALIBER = ${caliber.toInt()}"

        if (number != "")
            query += if (type != "" || caliber != "")
                " AND ${number.toInt()} <= $COL_PROJECTILE_NUMBER"
            else
                " WHERE ${number.toInt()} <= $COL_PROJECTILE_NUMBER"

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val Id: Int = result.getString(result.getColumnIndex(COL_PROJECTILE_ID)).toInt()
                val Name = result.getString(result.getColumnIndex(COL_PROJECTILE_NAME))
                val Caliber = result.getString(result.getColumnIndex(COL_PROJECTILE_CALIBER)).toInt()
                val Type = result.getString(result.getColumnIndex(COL_PROJECTILE_TYPE))
                val Number = result.getString(result.getColumnIndex(COL_PROJECTILE_NUMBER)).toInt()
                list.add(Projectiles(Id,Name, Type, Caliber, Number))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun deleteTank(name: String)
    {
        val db = this.writableDatabase
        db.delete(TANK_TABLE, "$COL_TANK_NAME=?", arrayOf(name))
        db.close()
    }

    fun deleteProjectile(name:String)
    {
        val db = this.writableDatabase
        db.delete(PROJECTILE_TABLE, "$COL_PROJECTILE_NAME=?", arrayOf(name))
        db.close()
    }

    fun updateTank(name: String, country: String,weight: String,gun: String,number: String)
    {
        val db =this.writableDatabase
        val query = "Select * from $TANK_TABLE"
        val result = db.rawQuery(query,null)

        if (result.moveToFirst()) {
            do {
                val cv = ContentValues()
                if (weight != "")
                    cv.put(COL_TANK_WEIGHT, weight.toInt())
                if (gun != "")
                    cv.put(COL_TANK_GUN,gun.toInt())
                if (country != "")
                    cv.put(COL_TANK_COUNTRY, country)
                if (number != "")
                    cv.put(COL_TANK_NUMBER,number.toInt())
                db.update(TANK_TABLE,cv, "$COL_TANK_NAME=?", arrayOf(name))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }

    fun updateProjectile(name: String,type: String ,caliber: String,number: String)
    {
        val db =this.writableDatabase
        val query = "Select * from $PROJECTILE_TABLE"
        val result = db.rawQuery(query,null)

        if (result.moveToFirst()) {
            do {
                val  cv = ContentValues()
                if (type != "")
                    cv.put(COL_PROJECTILE_TYPE, type)
                if (caliber != "")
                    cv.put(COL_PROJECTILE_CALIBER,caliber.toInt())
                if (number != "")
                    cv.put(COL_PROJECTILE_NUMBER,number.toInt())
                db.update(PROJECTILE_TABLE,cv, "$COL_PROJECTILE_NAME=?", arrayOf(name))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
    }
}