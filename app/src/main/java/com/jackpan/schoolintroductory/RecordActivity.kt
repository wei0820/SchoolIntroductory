package com.jackpan.schoolintroductory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        MySharedPrefernces.getList(this).forEach {
        }
    }

}
