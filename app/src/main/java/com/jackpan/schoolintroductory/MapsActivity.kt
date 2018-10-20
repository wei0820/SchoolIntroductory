package com.jackpan.schoolintroductory

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


// 地圖
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener {


    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveCanceled() {
    }

    override fun onCameraIdle() {
    }

    private lateinit var mMap: GoogleMap

    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private var locationManager: LocationManager? = null
    var mLat :Double = 0.0
    var mLon :Double = 0.0
    var mLatLngArray = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initLayout()
        mLatLngArray = arrayListOf(
                LatLng(24.150137, 120.682460),
                LatLng(24.150400, 120.683218),
                LatLng(24.149792, 120.683767),
                LatLng(24.150435, 120.683870),
                LatLng(24.149163, 120.682901),
                LatLng(24.149931, 120.683975),
                LatLng(24.151192, 120.682705),
                LatLng(24.151341, 120.683889),
                LatLng(24.151341, 120.683889),
                LatLng(24.149178, 120.683542)
                )

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMyLocationEnabled(true)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveCanceledListener(this)
        mMap.setOnCameraIdleListener(this)
        mMap.setMaxZoomPreference(20.0f)
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        mLatLngArray.forEach {
            addMarker(it,
                    "test",
                    "test")
        }


    }
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }

    }

    fun initLayout() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
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
    // 在地圖加入指定位置與標題的標記
    private fun addMarker(place: LatLng, title: String, context: String) {
        var icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_black)
        val markerOptions = MarkerOptions()
        markerOptions.position(place)
                .title(title)
                .snippet(context)
                .icon(icon)

        mMap.addMarker(markerOptions)
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("Location", location.latitude.toString())
            Log.d("Location", location.longitude.toString())
            mLat= location.latitude
            mLon =location.longitude
            var latlon: String = location.latitude.toString() + "," + location.longitude.toString()
//            locationTextView.text = "${location.latitude} - ${location.longitude}"
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 20f))


            mMap.setOnMarkerClickListener(gmapListener)


        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    // 按下標記觸發 OnMarkerClick 事件
    private val gmapListener = GoogleMap.OnMarkerClickListener { marker ->
        marker.showInfoWindow()
        // 用吐司顯示註解
//        Log.d("Jack",marker.position.latitude.toString())
//        Log.d("Jack",marker.position.longitude.toString())
//        Log.d("Jack",Distance(mLat,mLon,marker.position.latitude,marker.position.longitude).toString())
//        Toast.makeText(this@MapsActivity,
//                "距離"+Distance(mLat,mLon,marker.position.latitude,marker.position.longitude)+
//                        "公尺",Toast.LENGTH_SHORT).show()
       val  intent = Intent()
        intent.setClass(this@MapsActivity,CameraViewActivity::class.java)
        startActivity(intent)
        true
    }


    //帶入使用者及景點經緯度可計算出距離
    fun Distance(longitude1: Double, latitude1: Double, longitude2: Double, latitude2: Double): Double {
        val radLatitude1 = latitude1 * Math.PI / 180
        val radLatitude2 = latitude2 * Math.PI / 180
        val l = radLatitude1 - radLatitude2
        val p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180
        var distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2.0) + (Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2.0))))
        distance = distance * 6378137.0
        distance = (Math.round(distance * 10000) / 10000).toDouble()

        return distance
    }

}
