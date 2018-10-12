package com.jackpan.schoolintroductory

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import java.lang.reflect.Array

class MessageActivity : AppCompatActivity() {
    lateinit var mListView: ListView
    var TitleArray =ArrayList<String>()
    var UrlArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        mListView = findViewById(R.id.listview)
        TitleArray.add("國立台中科技大學")
        TitleArray.add("國立台中科技大學-資訊管理系")
        UrlArray = arrayListOf("https://www.nutc.edu.tw/bin/home.php","https://im.nutc.edu.tw/bin/home.php")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, TitleArray)
        mListView.adapter = adapter
        mListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString("Url",UrlArray[position].toString())
            intent.putExtras(bundle)
            intent.setClass(this,WebViewActivity::class.java)
            startActivity(intent)
        }
    }

}
