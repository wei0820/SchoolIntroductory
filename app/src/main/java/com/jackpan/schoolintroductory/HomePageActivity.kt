package com.jackpan.schoolintroductory

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast

class HomePageActivity : AppCompatActivity() {
    lateinit var mButton: Button
    lateinit var mButton2: Button

    lateinit var mButton3: Button
    lateinit var mButton4: Button
    val MY_PERMISSIONS_REQUEST_LOCATION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        checkPermission()
        initlayout()
    }
    fun initlayout(){
        mButton = findViewById(R.id.button1)
        mButton2 = findViewById(R.id.button2)
        mButton3 = findViewById(R.id.button3)
        mButton4 = findViewById(R.id.button4)
        mButton3.setOnClickListener {
            val intent  =Intent()
            intent.setClass(this,SearchActivity::class.java)
            startActivity(intent)
        }
        mButton.setOnClickListener {
            val intent=Intent()
            intent.setClass(this,MapsActivity::class.java)
            startActivity(intent)
        }
        mButton4.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,MessageActivity::class.java)
            startActivity(intent)
        }
        mButton2.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,RecordActivity::class.java)
            startActivity(intent)
        }

    }
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "需要定位功能,才能使用喔", Toast.LENGTH_SHORT).show()
                return
            }
        }
    }
}
