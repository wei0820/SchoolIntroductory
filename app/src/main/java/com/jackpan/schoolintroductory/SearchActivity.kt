package com.jackpan.schoolintroductory

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_search.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {
    lateinit var mListView: ListView
    var TitleArray =ArrayList<String>()
    var latlonArray = ArrayList<String>()
    var arrayList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mListView = findViewById(R.id.listview)
        TitleArray = arrayListOf(
                "汽車停車場-中正大樓 B1、B2",
                "汽車停車場-中商大樓 B1、B2",
                "汽車停車場-中技大樓 1F、B1",
                "汽車停車場-民生校區校園廣場",
                "機車停車場-三民路側門平面停車場",
                "機車停車場-錦平街平面停車場",
                "機車停車場-資訊管 B1",
                "機車停車場-中技大樓1F",
                "機車停車場-民生校區校園廣場")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, TitleArray)
        mListView.adapter = adapter
        setTime()
    }
    fun setTime(){
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val curDate = Date(System.currentTimeMillis()) // 獲取當前時間

        val str = formatter.format(curDate)
        if (MySharedPrefernces.getList(this).size==0){
            arrayList = MySharedPrefernces.getList(this)
        }
        arrayList.add(str)
        MySharedPrefernces.setList(this,arrayList)
    }

}
