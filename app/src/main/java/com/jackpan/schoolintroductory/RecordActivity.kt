package com.jackpan.schoolintroductory

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_record.*
import java.text.SimpleDateFormat
import java.util.*

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        MySharedPrefernces.getList(this).forEach {
            Log.d("Jack",it)
        }
    }

}
