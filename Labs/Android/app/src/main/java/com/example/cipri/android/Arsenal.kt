package com.example.cipri.android

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.cipri.android.Adapters.ProjectileAdapter
import com.example.cipri.android.Adapters.TankAdapter
import com.example.cipri.android.Crud.*
import com.example.cipri.android.DBLocal.DataBaseHandler
import com.example.cipri.android.Model.Projectiles
import com.example.cipri.android.Model.Tank
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_arsenal.*
import kotlinx.android.synthetic.main.add_projectile.view.*
import kotlinx.android.synthetic.main.add_tank.view.*
import kotlinx.android.synthetic.main.app_bar_arsenal.*
import kotlinx.android.synthetic.main.filter_projectile.view.*
import kotlinx.android.synthetic.main.filter_tank.view.*
import kotlinx.android.synthetic.main.settings.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.ArrayList

class Arsenal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var tanks : ArrayList<Tank>
    private lateinit var projectiles: ArrayList<Projectiles>
    private lateinit var tankAdapter : TankAdapter
    private lateinit var projectileAdapter: ProjectileAdapter
    private lateinit var addTank: AddTank
    private lateinit var addProjectile: AddProjectile
    private lateinit var localdb: DataBaseHandler
    private lateinit var readData : ReadData
    var context: Context = this@Arsenal
    var animationIndex: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arsenal)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val it = findViewById<View>(R.id.action_settings)
        localdb = DataBaseHandler(context)
        readData = ReadData()
        setTankUI()
        setUpdateBtnTank()
        setFilterBtnTank()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.arsenal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val main = findViewById<LinearLayout>(R.id.line1)
                val stDialogView = LayoutInflater.from(this).inflate(R.layout.settings,null)
                val builder : AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setView(stDialogView)
                val stAlertDialog = builder.show()


                stDialogView.exit_btn.setOnClickListener { stAlertDialog.dismiss() }
                stDialogView.holo_blue_bright_btn.setOnClickListener {
                    main.setBackgroundResource(android.R.color.holo_blue_bright) }
                stDialogView.holo_orange_light_btn.setOnClickListener {
                    main.setBackgroundResource(android.R.color.holo_orange_light) }
                stDialogView.holo_blue_dark_btn.setOnClickListener {
                    main.setBackgroundResource(android.R.color.holo_blue_dark) }
                stDialogView.holo_orange_dark_btn.setOnClickListener {
                    main.setBackgroundResource(android.R.color.holo_orange_dark) }
                stDialogView.default_btn.setOnClickListener {
                    main.setBackgroundResource(android.R.color.white) }
                stDialogView.fade_in_btn.setOnClickListener {
                    this.tankAdapter.setAnimation(1)
                    try{
                        this.projectileAdapter.setAnimation(1)
                    }catch (e: Exception){}
                    this.animationIndex = 1
                }
                stDialogView.slide_left_btn.setOnClickListener {
                    this.tankAdapter.setAnimation(2)
                    try{
                        this.projectileAdapter.setAnimation(2)
                    }catch (e: Exception){}
                    this.animationIndex = 2
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_tanks ->{
                setTankUI()
                setUpdateBtnTank()
                setFilterBtnTank()
            }
            R.id.nav_projectiles ->{
                setProjectileUI()
                setUpdateBtnProj()
                setFilterBtnProjectile()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setFilterBtnProjectile()
    {
        val btn = findViewById<Button>(R.id.FilterBtn)
        btn.setOnClickListener {
            val filDialogView = LayoutInflater.from(this).inflate(R.layout.filter_projectile,null)
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(filDialogView)
            val filAlertDialog = builder.show()

            filDialogView.CancelFilterProjectileBtn.setOnClickListener {
                filAlertDialog.dismiss()
            }
            filDialogView.FilterProjectileBtn.setOnClickListener {
                val type = filDialogView.filterType.text.toString()
                val caliber = filDialogView.filterCaliber.text.toString()
                val number = filDialogView.filterNumberProjectile.text.toString()

                val newList:ArrayList<Projectiles>
                newList = localdb.filterProjectiles(type,caliber,number)

                filAlertDialog.dismiss()
                val projectileList = findViewById<ListView>(R.id.ListId)
                projectileAdapter = ProjectileAdapter(this, newList,localdb)
                projectileAdapter.setAnimation(this.animationIndex)
                projectileList.adapter = projectileAdapter
            }
        }
    }

    private fun setFilterBtnTank()
    {
        val btn = findViewById<Button>(R.id.FilterBtn)
        btn.setOnClickListener {
            val filDialogView = LayoutInflater.from(this).inflate(R.layout.filter_tank, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(filDialogView)
            val filAlertDialog = builder.show()

            filDialogView.CancelFilterTankBtn.setOnClickListener {
                filAlertDialog.dismiss()
            }

            filDialogView.FilterTankBtn.setOnClickListener {
                val gun = filDialogView.filterGun.text.toString()
                val weight = filDialogView.filterWeight.text.toString()
                val country = filDialogView.filterCountry.text.toString()
                val number = filDialogView.filterNumber.text.toString()

                val newList:ArrayList<Tank>
                newList = localdb.filterTank(country,weight,gun,number)

                filAlertDialog.dismiss()
                val tankList = findViewById<ListView>(R.id.ListId)
                tankAdapter = TankAdapter(this, newList,localdb)
                tankAdapter.setAnimation(this.animationIndex)
                tankList.adapter = tankAdapter
            }
        }
    }

    private  fun setUpdateBtnProj()
    {
        val btn = findViewById<Button>(R.id.UpdateBtn)
        btn.setOnClickListener {
            val upDialogView = LayoutInflater.from(this).inflate(R.layout.add_projectile,null)
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(upDialogView)
            val upAlertDialog = builder.show()
            upDialogView.ProjectileBtn.text = "Update"

            upDialogView.CancelProjectileBtn.setOnClickListener {
                upAlertDialog.dismiss()
            }

            upDialogView.ProjectileBtn.setOnClickListener {
                val name = upDialogView.NameBoxProj.text.toString()
                val type = upDialogView.TypeBox.text.toString()
                val caliber = upDialogView.CaliberBox.text.toString()
                val number = upDialogView.NumberBoxProj.text.toString()
                val updateProjectile = UpdateProjectile(name,type,caliber,number)

                val client = OkHttpClient()
                client.newCall(updateProjectile.update(projectiles)).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {runOnUiThread {
                        Toast.makeText(context, "No server connection", Toast.LENGTH_LONG).show()}}
                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful)
                            runOnUiThread {
                                updateProjectile.update(localdb)
                                readProj()
                            }
                        else
                            runOnUiThread {
                                Toast.makeText(context, "No server connection", Toast.LENGTH_LONG).show()}
                    }})
                upAlertDialog.dismiss()
            }
        }
    }

    private fun setUpdateBtnTank()
    {
        val btn = findViewById<Button>(R.id.UpdateBtn)
        btn.setOnClickListener {

            val upDialogView = LayoutInflater.from(this).inflate(R.layout.add_tank, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(upDialogView)
            val addAlertDialog = builder.show()
            upDialogView.TankBtn.text = "Update"

            upDialogView.CancelTankBtn.setOnClickListener {
                addAlertDialog.dismiss()
            }

            upDialogView.TankBtn.setOnClickListener {
                val name = upDialogView.NameBox.text.toString()
                val gun = upDialogView.GunBox.text.toString()
                val weight = upDialogView.WeightsBox.text.toString()
                val country = upDialogView.CountryBox.text.toString()
                val number = upDialogView.NumberBox.text.toString()
                val updateTank = UpdateTank(name, country, gun, weight, number)

                val client = OkHttpClient()
                client.newCall(updateTank.update(tanks)).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {runOnUiThread {
                        Toast.makeText(context, "No server connection", Toast.LENGTH_LONG).show()
                    }}
                    override fun onResponse(call: Call, response: Response)  {
                        if (response.isSuccessful)
                            runOnUiThread {
                                updateTank.update(localdb)
                                readTank()
                            }
                        else
                            runOnUiThread {
                                Toast.makeText(context, "No server connection", Toast.LENGTH_LONG).show()}
                    }})
                addAlertDialog.dismiss()
            }
        }
    }

    private fun setTankUI(){
        val btn = findViewById<Button>(R.id.AddButton)
        btn.text = "Add Tank"
        btn.visibility = View.VISIBLE
        btn.setOnClickListener {
            val addDialogView = LayoutInflater.from(this).inflate(R.layout.add_tank,null)
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(addDialogView)
            val addAlertDialog = builder.show()
            addDialogView.TankBtn.text = "Add"

            addDialogView.CancelTankBtn.setOnClickListener {
                addAlertDialog.dismiss()
            }

            addDialogView.TankBtn.setOnClickListener {
                val name = addDialogView.NameBox.text.toString()
                val gun = addDialogView.GunBox.text.toString()
                val weight = addDialogView.WeightsBox.text.toString()
                val country = addDialogView.CountryBox.text.toString()
                val number = addDialogView.NumberBox.text.toString()

                addTank = AddTank(name,country,gun,weight,number)
                if (addTank.check()) {
                    val client = OkHttpClient()
                    client.newCall(addTank.addServer()).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException){}
                        override fun onResponse(call: Call, response: Response){} })
                    addTank.addLocal(this@Arsenal.localdb)
                    readTank()
                }
                else
                    Toast.makeText(this, "Invalid tank", Toast.LENGTH_LONG).show()
                addAlertDialog.dismiss()
            }
        }
        readTanks()
    }


    private fun setProjectileUI() {
        val button = findViewById<Button>(R.id.AddButton)
        button.text = "Add Projectile"
        button.visibility = View.VISIBLE
        button.setOnClickListener {
            val addDialogView = LayoutInflater.from(this).inflate(R.layout.add_projectile,null)
            val builder : AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setView(addDialogView)
            val addAlertDialog = builder.show()
            addDialogView.ProjectileBtn.text = "Add"

            addDialogView.CancelProjectileBtn.setOnClickListener {
                addAlertDialog.dismiss()
            }

            addDialogView.ProjectileBtn.setOnClickListener {
                val name = addDialogView.NameBoxProj.text.toString()
                val type = addDialogView.TypeBox.text.toString()
                val caliber = addDialogView.CaliberBox.text.toString()
                val number = addDialogView.NumberBoxProj.text.toString()

                addProjectile = AddProjectile(name,type,caliber,number)
                if (addProjectile.check()){
                    val client = OkHttpClient()
                    client.newCall(addProjectile.addServer()).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {} })
                    addProjectile.addLocal(this@Arsenal.localdb)
                    readProj()
                }
                else
                    Toast.makeText(this, "Invalid projectile", Toast.LENGTH_LONG).show()
                addAlertDialog.dismiss()
            }
        }
        readProjectiles()
    }

    private fun readTank(){
        val client = OkHttpClient()
        client.newCall(readData.readTanks()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { runOnUiThread {
                tanks = localdb.readTanks() as ArrayList<Tank>
                tankAdapter.setList(tanks)
                tankAdapter.notifyDataSetChanged() }}

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val collectionType = object: TypeToken<ArrayList<Tank>>() {}.type
                    val tankfeed = gson.fromJson<ArrayList<Tank>>(body, collectionType)
                    runOnUiThread {
                        tankAdapter.setList(tankfeed)
                        tankAdapter.notifyDataSetChanged() }
                }else{ runOnUiThread {
                    tanks = localdb.readTanks() as ArrayList<Tank>
                    tankAdapter.setList(tanks)
                    tankAdapter.notifyDataSetChanged() }}
            }
        })
    }

    private fun readProj(){
        val client = OkHttpClient()
        client.newCall(readData.readProjectiles()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { runOnUiThread {
                projectiles = localdb.readProjectiles() as ArrayList<Projectiles>
                projectileAdapter.setList(projectiles)
                projectileAdapter.notifyDataSetChanged()
            }}

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val collectionType = object: TypeToken<ArrayList<Projectiles>>() {}.type
                    val projectilefeed = gson.fromJson<ArrayList<Projectiles>>(body, collectionType)
                    runOnUiThread {
                        projectileAdapter.setList(projectilefeed)
                        projectileAdapter.notifyDataSetChanged() }
                } else { runOnUiThread {
                    projectiles = localdb.readProjectiles() as ArrayList<Projectiles>
                    projectileAdapter.setList(projectiles)
                    projectileAdapter.notifyDataSetChanged() }}
            }
        })
    }

    private fun readProjectiles() {
        val client = OkHttpClient()
        client.newCall(readData.readProjectiles()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { runOnUiThread {
                projectiles = localdb.readProjectiles() as ArrayList<Projectiles>
                val projectileList = findViewById<ListView>(R.id.ListId)
                projectileAdapter = ProjectileAdapter(context, projectiles,localdb)
                projectileAdapter.setAnimation(animationIndex)
                projectileList.adapter = projectileAdapter}}

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val collectionType = object: TypeToken<ArrayList<Projectiles>>() {}.type
                    val projectilefeed = gson.fromJson<ArrayList<Projectiles>>(body, collectionType)
                    runOnUiThread {
                        projectiles = syncProjectiles(projectilefeed)
                        val projectileList = findViewById<ListView>(R.id.ListId)
                        projectileAdapter = ProjectileAdapter(context, projectiles,localdb)
                        projectileAdapter.setAnimation(animationIndex)
                        projectileList.adapter = projectileAdapter }
                }else{
                    runOnUiThread{
                        projectiles = localdb.readProjectiles() as ArrayList<Projectiles>
                        val projectileList = findViewById<ListView>(R.id.ListId)
                        projectileAdapter = ProjectileAdapter(context, projectiles,localdb)
                        projectileAdapter.setAnimation(animationIndex)
                        projectileList.adapter = projectileAdapter}}
            }
        })
    }

    private fun readTanks() {
        val client = OkHttpClient()

        client.newCall(readData.readTanks()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { runOnUiThread {
                this@Arsenal.tanks = localdb.readTanks() as ArrayList<Tank>
                val tankList = findViewById<ListView>(R.id.ListId)
                tankAdapter = TankAdapter(context, tanks, localdb)
                tankAdapter.setAnimation(animationIndex)
                tankList.adapter = tankAdapter}}

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val collectionType = object: TypeToken<ArrayList<Tank>>() {}.type
                    val tankfeed = gson.fromJson<ArrayList<Tank>>(body, collectionType)
                    runOnUiThread {
                        tanks = syncTanks(tankfeed)
                        val tankList = findViewById<ListView>(R.id.ListId)
                        tankAdapter = TankAdapter(context, tanks, localdb)
                        tankAdapter.setAnimation(animationIndex)
                        tankList.adapter = tankAdapter }
                }else{
                    runOnUiThread {
                        this@Arsenal.tanks = localdb.readTanks() as ArrayList<Tank>
                        val tankList = findViewById<ListView>(R.id.ListId)
                        tankAdapter = TankAdapter(context, tanks, localdb)
                        tankAdapter.setAnimation(animationIndex)
                        tankList.adapter = tankAdapter}}
            }
        })
    }

    private fun syncTanks(tankList: ArrayList<Tank>): ArrayList<Tank>{
        val localtanks = this@Arsenal.localdb.readTanks() as ArrayList<Tank>
        val client = OkHttpClient()

        if (localtanks.size > tankList.size) {
            var i = tankList.size
            while (i < localtanks.size) {
                val addtank = AddTank(localtanks[i])
                client.newCall(addtank.addServer()).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {}})
                i++
            }
        }
        else {
            var i = localtanks.size
            while (i < tankList.size){
                val addtank = AddTank(tankList[i])
                addtank.addLocal(localdb)
                i++
            }
            return tankList
        }
        return localtanks
    }

    private fun syncProjectiles(projectileList: ArrayList<Projectiles>) : ArrayList<Projectiles>{
        val localProjectiles = this@Arsenal.localdb.readProjectiles() as ArrayList<Projectiles>
        val client = OkHttpClient()

        if (localProjectiles.size > projectileList.size) {
            var i = projectileList.size
            while (i < localProjectiles.size) {
                val addprojectile = AddProjectile(localProjectiles[i])
                client.newCall(addprojectile.addServer()).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {}})
                i++
            }
        }
        else{
            var i = localProjectiles.size
            while (i < projectileList.size){
                val addprojectile = AddProjectile(projectileList[i])
                addprojectile.addLocal(localdb)
                i++
            }
            return projectileList
        }
        return localProjectiles
    }
}







