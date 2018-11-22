package com.jackpan.schoolintroductory

import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// 搜尋
class SearchActivity : AppCompatActivity() {
    lateinit var mListView: ListView
    var TitleArray =ArrayList<String>()
    var latlonArray = ArrayList<String>()
    var arrayList = ArrayList<String>()
    private var locationManager: LocationManager? = null
     var mNowAdd :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
        mListView = findViewById(R.id.listview)
        // 設定 資訊

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
        // 資訊經緯度

        latlonArray = arrayListOf(
                "24.149652,120.682987",
                "24.151357,120.682503"
                ,"24.152269,120.683773",
                "24.150164,120.683574",
                "24.149222,120.682825"
                ,"24.151532,120.684423"
                ,"24.149648,120.683715"
                ,"24.152250,120.683778"
                ,"24.150164,120.683574")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, TitleArray)
        mListView.adapter = adapter
        mListView.setOnItemClickListener { parent, view, position, id ->
            goToMap(mNowAdd,latlonArray.get(position).toString())
            setTime(TitleArray.get(position).toString())
        }
    }
    // 設定時間

    fun setTime(name:String){
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val curDate = Date(System.currentTimeMillis()) // 獲取當前時間
        val str =name +"\n\n"+"搜尋時間："+ formatter.format(curDate)
        arrayList = MySharedPrefernces.getList(this)
        arrayList.add(str)
        MySharedPrefernces.setList(this,arrayList)
    }
    // 跳轉地圖

    fun goToMap(nowAdd :String,GotoAdd:String){
        val vDirectionUrl = ("https://maps.google.com/maps?f=d"
                + "&saddr=" + nowAdd
                + "&daddr=" + GotoAdd
                + "&hl=tw")
        // 在 Google 地圖 App 顯示導航
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(vDirectionUrl))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)

    }
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            mNowAdd = location.latitude.toString() + "," + location.longitude.toString()
//            locationTextView.text = "${location.latitude} - ${location.longitude}"


        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}
