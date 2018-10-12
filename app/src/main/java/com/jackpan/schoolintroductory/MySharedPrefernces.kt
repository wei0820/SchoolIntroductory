package com.jackpan.schoolintroductory

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.R.id.edit
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.util.Log


object MySharedPrefernces {
    fun setList(context: Context,list :ArrayList<String>){
        val editor = context.getSharedPreferences("EnvironDataList", MODE_PRIVATE).edit()
        editor.putInt("EnvironNums", list.size)
        Log.d("Jack",list.size.toString())
        for (i in list.indices) {
            editor.putString("item_$i", list[i])
        }
        editor.commit()
    }
    fun  getList(context: Context):ArrayList<String>{
        val environmentList = ArrayList<String>()
        val preferDataList = context.getSharedPreferences("EnvironDataList", MODE_PRIVATE)
        val environNums = preferDataList.getInt("EnvironNums", 0)
        for (i in 0 until environNums) {
            val environItem = preferDataList.getString("item_$i", null)
            environmentList.add(environItem)
        }
        Log.d("Jack",environmentList.size.toString())
        return  environmentList
    }
}
