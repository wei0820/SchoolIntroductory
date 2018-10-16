package com.jackpan.schoolintroductory

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

class RecordActivity : AppCompatActivity() {
    lateinit var mListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        mListView = findViewById(R.id.listview)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MySharedPrefernces.getList(this))
        mListView.adapter = adapter
//        MySharedPrefernces.getList(this).forEach {
//        }
    }

}
