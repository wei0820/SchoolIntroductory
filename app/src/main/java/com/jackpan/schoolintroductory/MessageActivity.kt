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
    var TitleArray = ArrayList<String>()
    var UrlArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        mListView = findViewById(R.id.listview)
        TitleArray = arrayListOf("國立台中科技大學", "國立台中科技大學-資訊管理系"
                , "台中科技大學-進修部"
                , "台中科技大學-進修學院",
                "台中科技大學-空中學院",
                "教務處",
                "總務處",
                "人事室",
                "圖書館",
                "學生事務處",
                "軍訓室",
                "體育室",
                "語言中心",
                "通識教育中心",
                "商學院網站",
                "設計學院網站",
                "資訊與流通學院",
                "中護健康學院",
                "語文學院")
        UrlArray = arrayListOf("https://www.nutc.edu.tw/bin/home.php",
                "https://im.nutc.edu.tw/bin/home.php",
                "https://nd.nutc.edu.tw/bin/home.php",
                "https://continue.nutc.edu.tw/bin/home.php",
                "https://air.nutc.edu.tw/bin/home.php",
                "https://aca.nutc.edu.tw/bin/home.php",
                "https://ga.nutc.edu.tw/bin/home.php",
                "https://person.nutc.edu.tw/bin/home.php",
                "https://lib.nutc.edu.tw/bin/home.php",
                "https://student.nutc.edu.tw/bin/home.php",
                "https://military.nutc.edu.tw/bin/home.php",
                "https://pe.nutc.edu.tw/bin/home.php",
                "https://language.nutc.edu.tw/bin/home.php",
                "https://ge.nutc.edu.tw/bin/home.php",
                "https://commerce.nutc.edu.tw/bin/home.php",
                "https://codesign.nutc.edu.tw/bin/home.php",
                "http://cids.nutc.edu.tw/",
                "https://ntcnc.nutc.edu.tw/bin/home.php",
                "https://colanguage.nutc.edu.tw/bin/home.php")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, TitleArray)
        mListView.adapter = adapter
        mListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString("Url", UrlArray[position].toString())
            intent.putExtras(bundle)
            intent.setClass(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }

}
