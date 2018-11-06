package com.jackpan.schoolintroductory

import android.content.Context
import android.content.Context.MODE_PRIVATE


object  MySharedPrefernces {
    fun setList(context: Context,list :ArrayList<String>){
        val editor = context.getSharedPreferences("EnvironDataList", MODE_PRIVATE).edit()
        editor.putInt("EnvironNums", list.size)
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
        return  environmentList
    }
}
