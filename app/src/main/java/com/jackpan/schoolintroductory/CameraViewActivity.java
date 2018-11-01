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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
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
	private SoundPool soundPool,soundPool2;
	private int alertId,alertId2;
	int[] mMonsterprimary = new int[]{
			R.mipmap.monsterprimary_1,
			R.mipmap.monsterprimary_2,
			R.mipmap.monsterprimary_3,
			R.mipmap.monsterprimary_4,
			R.mipmap.monsterprimary_5,
			R.mipmap.monsterprimary_6,
			R.mipmap.monsterprimary_7,
			R.mipmap.monsterprimary_8,
			R.mipmap.monsterprimary_9,
			R.mipmap.monsterprimary10,
			R.mipmap.monsterprimary_20,
			R.mipmap.monsterprimary_21,
			R.mipmap.monsterprimary_22,
			R.mipmap.monsterprimary_23,
			R.mipmap.monsterprimary_24,
			R.mipmap.monsterprimary_25,
			R.mipmap.monsterprimary_26,
			R.mipmap.monsterprimary_27,
			R.mipmap.monsterprimary_28,
			R.mipmap.monsterprimary_29,
			R.mipmap.monsterprimary_30};

	int[] mMonsterintermediate = new int[]{
			R.mipmap.monsterintermediate_1,
			R.mipmap.monsterintermediate_2,
			R.mipmap.monsterintermediate_3,
			R.mipmap.monsterintermediate_4,
			R.mipmap.monsterintermediate_5,
			R.mipmap.monsterintermediate_6,
			R.mipmap.monsterintermediate_7};
	int[] mMonsterBoss = new int[]{
			R.mipmap.monsterboss_1,
			R.mipmap.monsterboss_2};
	private TextView mHPTextView;
	long hp ;
	long attack = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_view);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		soundPool2 = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
		alertId2 = soundPool2.load(this, R.raw.monster_roaning2, 1);
		setupListeners();
		setupLayout();
		setAugmentedRealityPoint();

	}

	private void setAugmentedRealityPoint() {
		mPoi = new AugmentedPOI(
				"Kościół Marciacki",
				"Kościół Marciacki w Krakowie",
				25.062179, 121.537813

		);

	}

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

	private boolean isBetween(double minAngle, double maxAngle, double azimuth) {
		//  minAngle = 73.34841807969889
		//  maxAngle = 83.34841807969889
		//  azimuth = 140.0

		if (minAngle > maxAngle) {
			Log.d(TAG, "Jack: "+"minAngle > maxAngle");
			Log.d(TAG, "Jack: "+minAngle);
			Log.d(TAG, "Jack: "+maxAngle);
			Log.d(TAG, "Jack: "+azimuth);

			if (isBetween(0, maxAngle, azimuth) && isBetween(minAngle, 360, azimuth))
				return true;
		} else {

			if (azimuth > minAngle && azimuth > maxAngle)
				return true;
		}
		return false;
	}

	private void updateDescription() {
		descriptionTextView.setText(mPoi.getPoiName() + " azimuthTeoretical "
				+ mAzimuthTeoretical + " azimuthReal " + mAzimuthReal + " latitude "
				+ mMyLatitude + " longitude " + mMyLongitude);
	}

	private static final String TAG = "CameraViewActivity";
	@Override
	public void onLocationChanged(Location location) {
		mMyLatitude = location.getLatitude();
		mMyLongitude = location.getLongitude();
		mAzimuthTeoretical = calculateTeoreticalAzimuth();
//		Toast.makeText(this,"latitude: "+location.getLatitude()+" longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
//		updateDescription();
		soundPool2.play(alertId2, 1.0F, 1.0F, 0, 0, 1.0F);

	}

	@Override
	public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
		mAzimuthReal = azimuthChangedTo;
		mAzimuthTeoretical = calculateTeoreticalAzimuth();

		double minAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(0);
		double maxAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(1);
		Log.d(TAG, "onAzimuthChanged: "+minAngle);
		Log.d(TAG, "onAzimuthChanged: "+maxAngle);
		Log.d(TAG, "onAzimuthChanged: "+mAzimuthReal);
		Log.d(TAG, "onAzimuthChanged: "+(isBetween(minAngle, maxAngle, mAzimuthReal)));
		if (isBetween(minAngle, maxAngle, mAzimuthReal)) {
			pointerIcon.setVisibility(View.VISIBLE);

		} else {
			pointerIcon.setVisibility(View.INVISIBLE);
		}
		pointerIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				hp = hp - attack;
				Log.d(TAG, "onClick: "+attack);

				Log.d(TAG, "onClick: "+hp);
				mHPTextView.setText("HP:"+hp);
				soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
				if (hp==0){
					mHPTextView.setText("怪物已死亡");

					Toast.makeText(CameraViewActivity.this,"擊敗怪物！！",Toast.LENGTH_SHORT).show();
					try {
						new Thread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finish();
				}else {

				}

			}
		});
//		updateDescription();

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
	Animation mAnimation = null ;

	private void setupLayout() {
		mTimeText =findViewById(R.id.time);
		pointerIcon = (ImageView) findViewById(R.id.icon);
		int i = (int)(Math.random()* mMonsterprimary.length);
		pointerIcon.setImageResource(mMonsterprimary[i]);
		mHPTextView = findViewById(R.id.hp);
		hp = 30;
		mHPTextView.setText("HP:"+hp);
		mAnimation = AnimationUtils.loadAnimation(this,R.anim. balloonscale);
		pointerIcon.setAnimation(mAnimation );
		mAnimation.start();

//		TranslateAnimation animation = new TranslateAnimation(0,800,0,300);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
//		animation.setDuration(1000);  // animation duration
////		animation.setRepeatCount(5);  // animation repeat count
//		animation.setRepeatCount(-1);   // repeat animation (left to right, right to left )
//		//移動效果
//		Animation amTranslate = new TranslateAnimation(0.0f, 100.0f, 0.0f, 100.0f);
//		//setDuration (long durationMillis) 設定動畫開始到結束的執行時間
//		amTranslate.setDuration(2000);
//		//setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
//		amTranslate.setRepeatCount(-1);
//
//		//旋轉效果
//		Animation amRotate = new RotateAnimation(0.0f, 360.0f, 0.0f, 100.0f);
//		//setDuration (long durationMillis) 設定動畫開始到結束的執行時間
//		amRotate.setDuration(2000);
//		//setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
//		amRotate.setRepeatCount(-1);
//
//		//放大縮小效果
//		Animation amScale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
//		//setDuration (long durationMillis) 設定動畫開始到結束的執行時間
//		amRotate.setDuration(2000);
//		//setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
//		amRotate.setRepeatCount(-1);
//
//		//淡進淡出效果
//		Animation amAlpha = new AlphaAnimation(1.0f, 0.0f);
//		//setDuration (long durationMillis) 設定動畫開始到結束的執行時間
//		amAlpha.setDuration(2000);
//		//setRepeatCount (int repeatCount) 設定重複次數 -1為無限次數 0
//		amAlpha.setRepeatCount(-1);
//
//		//特效組合
//		AnimationSet amSet = new AnimationSet(false);
//		amSet.addAnimation(animation);
////		amSet.addAnimation(amRotate);
//		amSet.addAnimation(amScale);
//		amSet.addAnimation(amAlpha);

		//將動畫參數設定到圖片並開始執行動畫
//		pointerIcon.startAnimation(amSet);
		new CountDownTimer(20000,1000){

			@Override
			public void onFinish() {
				mTimeText.setText("Done!");
			}

			@Override
			public void onTick(long millisUntilFinished) {
				mTimeText.setText("00:"+millisUntilFinished/1000);
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
