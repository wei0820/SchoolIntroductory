package com.jackpan.schoolintroductory

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {
    lateinit var mButton: Button
    lateinit var mButton2: Button

    lateinit var mButton3: Button
    lateinit var mButton4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        initlayout()
    }
    fun initlayout(){
        mButton = findViewById(R.id.button1)
        mButton2 = findViewById(R.id.button2)
        mButton3 = findViewById(R.id.button3)
        mButton4 = findViewById(R.id.button4)
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

    }
}
