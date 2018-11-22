package com.jackpan.schoolintroductory;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysztofjackowski on 24/09/15.
 */
public class CameraViewActivity extends Activity implements
		SurfaceHolder.Callback, OnLocationChangedListener, OnAzimuthChangedListener{

	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private boolean isCameraviewOn = false;
	private AugmentedPOI mPoi;

	private double mAzimuthReal = 0;
	private double mAzimuthTeoretical = 0;
	private static double AZIMUTH_ACCURACY = 5;
	private double mMyLatitude = 0;
	private double mMyLongitude = 0;

	private MyCurrentAzimuth myCurrentAzimuth;
	private MyCurrentLocation myCurrentLocation;

	TextView descriptionTextView;
	ImageView pointerIcon;
	TextView mTimeText;
	ImageView mAttackImg ;
	private SoundPool soundPool,soundPool2;
	private int alertId,alertId2;
	// 設定怪物 圖示 初級
	int[] mMonsterprimary = new int[]{
			R.mipmap.monsterprimary_1,
			R.mipmap.monsterprimary_2,
			R.mipmap.monsterprimary_3,
			R.mipmap.monsterprimary_4,
			R.mipmap.monsterprimary_5,
			R.mipmap.monsterprimary_6};
	// 設定怪物 圖示 中級

	int[] mMonsterintermediate = new int[]{
			R.mipmap.monsterintermediate_1,
			R.mipmap.monsterintermediate_2,
			R.mipmap.monsterintermediate_3,
			R.mipmap.monsterintermediate_4,
			R.mipmap.monsterintermediate_5};
	// 設定怪物 圖示 高級

	int[] mMonsterBoss = new int[]{
			R.mipmap.monsterboss_1,
			R.mipmap.monsterboss_2};
	private TextView mHPTextView;
	long hp ,mhp;
	long attack; ;
	long mattck= 1;
	long time;
	long m_time ;
	private ImageView mFireImg;
	private TextView HpTextView,mFractiText;
	private boolean isAppear = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_view);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setupListeners();
		setupLayout();
		setAugmentedRealityPoint();
	}

	 // 透過 地圖回傳的經緯度 設定怪物
	private void setAugmentedRealityPoint() {
		Double lat = getIntent().getExtras().getDouble("latitude");
		Double lon =  getIntent().getExtras().getDouble("longitude");
		if(lat!=null&&lat!=0&&lon!=null&&lon!=0){
			Toast.makeText(CameraViewActivity.this,"發現怪物 趕快去找出他吧",Toast.LENGTH_SHORT).show();
			mPoi = new AugmentedPOI(
					"",
					"",
					lat, lon

			);
		}else {
			Toast.makeText(CameraViewActivity.this,"",Toast.LENGTH_SHORT).show();
			Toast.makeText(CameraViewActivity.this,"未發現怪物",Toast.LENGTH_SHORT).show();

		}

	}

	// 設定 怪物路徑
	public double calculateTeoreticalAzimuth() {
		double dX = mPoi.getPoiLatitude() - mMyLatitude;
		double dY = mPoi.getPoiLongitude() - mMyLongitude;

		double phiAngle;
		double tanPhi;
		double azimuth = 0;

		tanPhi = Math.abs(dY / dX);
		phiAngle = Math.atan(tanPhi);
		phiAngle = Math.toDegrees(phiAngle);

		if (dX > 0 && dY > 0) { // I quater
			return azimuth = phiAngle;
		} else if (dX < 0 && dY > 0) { // II
			return azimuth = 180 - phiAngle;
		} else if (dX < 0 && dY < 0) { // III
			return azimuth = 180 + phiAngle;
		} else if (dX > 0 && dY < 0) { // IV
			return azimuth = 360 - phiAngle;
		}

		return phiAngle;
	}
	// 設定 怪物路徑  透過手機 搖晃
	private List<Double> calculateAzimuthAccuracy(double azimuth) {
		double minAngle = azimuth - AZIMUTH_ACCURACY;
		double maxAngle = azimuth + AZIMUTH_ACCURACY;
		List<Double> minMax = new ArrayList<Double>();

		if (minAngle < 0)
			minAngle += 360;

		if (maxAngle >= 360)
			maxAngle -= 360;

		minMax.clear();
		minMax.add(minAngle);
		minMax.add(maxAngle);

		return minMax;
	}
	// 計算怪物 出現的公式
	private boolean isBetween(double minAngle, double maxAngle, double azimuth) {
		if (minAngle > maxAngle) {
			if (isBetween(0, maxAngle, azimuth) && isBetween(minAngle, 360, azimuth))
				return true;
		} else {
			if (azimuth > minAngle && azimuth < maxAngle)
				return true;
		}
		return false;
	}



	private static final String TAG = "CameraViewActivity";
	// 經緯度 改變的時候會呼叫

	@Override
	public void onLocationChanged(Location location) {
		mMyLatitude = location.getLatitude();
		mMyLongitude = location.getLongitude();
		mAzimuthTeoretical = calculateTeoreticalAzimuth();
//		Toast.makeText(this,"latitude: "+location.getLatitude()+" longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
//		updateDescription();
	}

	@Override
	public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
		mAzimuthReal = azimuthChangedTo;
		mAzimuthTeoretical = calculateTeoreticalAzimuth();

		double minAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(0);
		double maxAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(1);
		if (isBetween(minAngle, maxAngle, mAzimuthReal)) {
			pointerIcon.setVisibility(View.VISIBLE);
			isAppear = true;

		} else {
			pointerIcon.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onStop() {
		myCurrentAzimuth.stop();
		myCurrentLocation.stop();
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		myCurrentAzimuth.start();
		myCurrentLocation.start();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        alertId = soundPool.load(this, R.raw.middle_punch1, 1);

    }

	private void setupListeners() {
		myCurrentLocation = new MyCurrentLocation(this);
		myCurrentLocation.buildGoogleApiClient(this);
		myCurrentLocation.start();

		myCurrentAzimuth = new MyCurrentAzimuth(this, this);
		myCurrentAzimuth.start();
	}
	// 設定元件 與及 怪物等級血量 與 時間

	private void setupLayout() {
		mhp = MySharedPreferncesHelp.getIsFraction(CameraViewActivity.this);
		mFireImg = findViewById(R.id.fireimg);
		mTimeText =findViewById(R.id.time);
		pointerIcon = (ImageView) findViewById(R.id.icon);
		HpTextView = findViewById(R.id.mhp);
		mFractiText = findViewById(R.id.fa);
		mAttackImg = findViewById(R.id.attack);
        mHPTextView = findViewById(R.id.hp);
		mHPTextView.setText("分數:"+mhp);
		int mo = getIntent().getExtras().getInt("num");
		int i;
		switch (mo){
			case 0:
				mFractiText.setText("初級怪物");
				hp = 30;
				m_time = 30000;
				i = (int)(Math.random()* mMonsterprimary.length);
				pointerIcon.setImageResource(mMonsterprimary[i]);
				break;
			case 1:
				mFractiText.setText("中級怪物");

				hp = 40;
				m_time = 20000;
				i = (int)(Math.random()* mMonsterintermediate.length);
				pointerIcon.setImageResource(mMonsterintermediate[i]);


				break;
			case 2:
				mFractiText.setText("BOSS級怪物");
				hp = 50;
				m_time = 10000;
				i = (int)(Math.random()* mMonsterBoss.length);
				pointerIcon.setImageResource(mMonsterBoss[i]);
				break;
			default:
				mFractiText.setText("初級怪物");
				hp = 30;
				m_time = 30000;
				i = (int)(Math.random()* mMonsterprimary.length);
				pointerIcon.setImageResource(mMonsterprimary[i]);
				break;
		}
		//倒數計時器 以等級去設定不同時間

		new CountDownTimer(m_time,1000){

			@Override
			public void onFinish() {
				mTimeText.setText("時間結束!");
				if (!isAppear){
					mhp = mhp-hp;
					MySharedPreferncesHelp.saveIsFraction(CameraViewActivity.this,mhp);
					Toast.makeText(CameraViewActivity.this,"沒發現怪物 您輸了！！",Toast.LENGTH_SHORT).show();
					finish();
				}else {
					mhp = mhp+hp;
					MySharedPreferncesHelp.saveIsFraction(CameraViewActivity.this,mhp);
					Toast.makeText(CameraViewActivity.this,"恭喜您找到怪物",Toast.LENGTH_SHORT).show();
					finish();
				}
				isAppear = false;

			}
			@Override
			public void onTick(long millisUntilFinished) {
				mTimeText.setText("00:"+millisUntilFinished/1000);
				time = millisUntilFinished/1000;
			}

		}.start();
		descriptionTextView = (TextView) findViewById(R.id.cameraTextView);
		descriptionTextView.setVisibility(View.GONE);
		getWindow().setFormat(PixelFormat.UNKNOWN);
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraview);
		mSurfaceHolder = surfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		if (isCameraviewOn) {
			mCamera.stopPreview();
			isCameraviewOn = false;
		}

		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
				mCamera.startPreview();
				isCameraviewOn = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		mCamera.setDisplayOrientation(90);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		isCameraviewOn = false;
	}
}