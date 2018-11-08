package com.jackpan.schoolintroductory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import android.Manifest.permission
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.Gravity
import com.livinglifetechway.quickpermissions.annotations.OnPermissionsDenied
import com.livinglifetechway.quickpermissions.annotations.OnPermissionsPermanentlyDenied
import com.livinglifetechway.quickpermissions.annotations.OnShowRationale
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import com.livinglifetechway.quickpermissions.util.QuickPermissionsRequest



// 首頁
class HomePageActivity : AppCompatActivity() {
    lateinit var mButton: Button
    lateinit var mButton2: Button
    lateinit var mButton3: Button
    lateinit var mButton4: Button
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    val MY_PERMISSIONS_REQUEST_CAMERA = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        initlayout()
        methodRequiresPermissions()

    }
    //設定 元件
    fun initlayout(){
        mButton = findViewById(R.id.button1)
        mButton2 = findViewById(R.id.button2)
        mButton3 = findViewById(R.id.button3)
        mButton4 = findViewById(R.id.button4)
        mButton3.setOnClickListener {
            // 跳轉至搜尋頁面
            val intent  =Intent()
            intent.setClass(this,SearchActivity::class.java)
            startActivity(intent)
        }
        mButton.setOnClickListener {
            // 跳轉至地圖
            val intent=Intent()
            intent.setClass(this,MapsActivity::class.java)
            startActivity(intent)
        }
        mButton4.setOnClickListener {
            // 跳轉至訊息頁面
            val intent = Intent()
            intent.setClass(this,MessageActivity::class.java)
            startActivity(intent)
        }
        mButton2.setOnClickListener {
            // 跳轉至足跡
            val intent = Intent()
            intent.setClass(this,RecordActivity::class.java)
            startActivity(intent)
        }

    }




    @WithPermissions(permissions = [(Manifest.permission.CAMERA), (Manifest.permission.ACCESS_FINE_LOCATION)])
    private fun methodRequiresPermissions() {
        val toast = Toast.makeText(this, "給予相機與位置權限", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    @OnShowRationale
    fun rationaleCallback(req: QuickPermissionsRequest) {
        // this will be called when permission is denied once or more time. Handle it your way
        AlertDialog.Builder(this)
                .setTitle("權限被拒絕")
                .setMessage("請允許我們繼續“+”再次請求權限，或結束權限流程。")
                .setPositiveButton("允許") { dialog, which -> req.proceed() }
                .setNegativeButton("結束") { dialog, which -> req.cancel() }
                .setCancelable(false)
                .show()
    }

    @OnPermissionsPermanentlyDenied
    fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        // this will be called when some/all permissions required by the method are permanently
        // denied. Handle it your way.
        AlertDialog.Builder(this)
                .setTitle("權限被拒絕")
                .setMessage("請打開應用設置以打開允許權限的應用設置，”+\n" +
                        "                         “或取消以結束許可流程。")
                .setPositiveButton("設定") { dialog, which -> req.openAppSettings() }
                .setNegativeButton("結束") { dialog, which -> req.cancel() }
                .setCancelable(false)
                .show()
    }


    @OnPermissionsDenied
    fun whenPermAreDenied(req: QuickPermissionsRequest) {
        // handle something when permissions are not granted and the request method cannot be called
        val toast = Toast.makeText(this, req.deniedPermissions.size.toString() + " permission(s) denied. This feature will not work.", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}
