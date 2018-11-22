package com.jackpan.schoolintroductory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by krzysztofjackowski on 24/09/15.
 */

public class AugmentedPOI {
	private int mId;
	private String mName;
	private String mDescription;
	private double mLatitude;
	private double mLongitude;
	//  設定 怪物 顯示方式 路徑 經緯度 名稱
	public AugmentedPOI(String newName, String newDescription,
                        double newLatitude, double newLongitude) {
		this.mName = newName;
        this.mDescription = newDescription;
        this.mLatitude = newLatitude;
        this.mLongitude = newLongitude;
	}
	//取名字
	public String getPoiName() {
		return mName;
	}
	//取經緯度
	public double getPoiLatitude() {
		return mLatitude;
	}
	public double getPoiLongitude() {
		return mLongitude;
	}


}
