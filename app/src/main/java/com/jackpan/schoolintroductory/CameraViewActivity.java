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
	long hp ,mhp;
	long attack; ;
	long mattck= 1;
	long time;
	long m_time ;
	private ImageView mFireImg;
	private TextView HpTextView,mFractiText;
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

		if (minAngle > maxAngle) {
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
		soundPool2.play(alertId2, 1.0F, 1.0F, 0, 0, 1.0F);

	}

	@Override
	public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
		mAzimuthReal = azimuthChangedTo;
		mAzimuthTeoretical = calculateTeoreticalAzimuth();

		double minAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(0);
		double maxAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(1);
		if (isBetween(minAngle, maxAngle, mAzimuthReal)) {
			pointerIcon.setVisibility(View.VISIBLE);

		} else {
			pointerIcon.setVisibility(View.INVISIBLE);
		}

		int fire = (int)(Math.random()* 49+1);
		Log.d(TAG, "onAzimuthChanged: "+fire);
		int fireran = fire%20;
		Log.d(TAG, "onAzimuthChanged: "+fireran);

		if (fireran==0){
			mFireImg.setVisibility(View.VISIBLE);
			mhp = mhp - mattck;
            HpTextView.setText("HP:"+mhp);
            if (mhp==0){
				HpTextView.setText("HP:0");
				MySharedPreferncesHelp.saveIsFraction(CameraViewActivity.this,mhp);
				Toast.makeText(CameraViewActivity.this,"您輸了！！",Toast.LENGTH_SHORT).show();
				finish();
			}

		}else {
			mFireImg.setVisibility(View.GONE);

		}
		pointerIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAttackImg.setVisibility(View.VISIBLE);
				hp = hp - attack;
				mhp = mhp + attack;
				mHPTextView.setText("HP:"+hp);
				soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);
				if (hp==0){
					mHPTextView.setText("怪物已死亡");
					Toast.makeText(CameraViewActivity.this,"擊敗怪物！！",Toast.LENGTH_SHORT).show();
					try {
						new Thread().sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					MySharedPreferncesHelp.saveIsFraction(CameraViewActivity.this,mhp);
					finish();
				}else {

				}

			}
		});
		mAttackImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAttackImg.setVisibility(View.INVISIBLE);
			}
		});

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
		mFireImg = findViewById(R.id.fireimg);
		mTimeText =findViewById(R.id.time);
		pointerIcon = (ImageView) findViewById(R.id.icon);
		HpTextView = findViewById(R.id.mhp);
		mFractiText = findViewById(R.id.fa);
		mAttackImg = findViewById(R.id.attack);
		int mo = (int)(Math.random()*2);
		int i;
		switch (mo){
			case 0:
				hp = 30;
				m_time = 30000;
				attack = 1;
				i = (int)(Math.random()* mMonsterprimary.length);
				pointerIcon.setImageResource(mMonsterprimary[i]);
				break;
			case 1:
				hp = 40;
				m_time = 20000;
				attack = 5;
				i = (int)(Math.random()* mMonsterintermediate.length);
				pointerIcon.setImageResource(mMonsterintermediate[i]);


				break;
			case 2:
				hp = 50;
				m_time = 10000;
				attack = 10;
				i = (int)(Math.random()* mMonsterBoss.length);
				pointerIcon.setImageResource(mMonsterBoss[i]);
				break;
			default:
				hp = 30;
				m_time = 30000;

				i = (int)(Math.random()* mMonsterprimary.length);
				pointerIcon.setImageResource(mMonsterprimary[i]);
				break;
		}
		mAttackImg.setVisibility(View.INVISIBLE);

		mHPTextView = findViewById(R.id.hp);
		mhp = MySharedPreferncesHelp.getIsFraction(CameraViewActivity.this);
		mHPTextView.setText("HP:"+hp);
		HpTextView.setText("HP:"+mhp);
		mAnimation = AnimationUtils.loadAnimation(this,R.anim. balloonscale);
		pointerIcon.setAnimation(mAnimation );
		mAnimation.start();
		new CountDownTimer(m_time,1000){

			@Override
			public void onFinish() {
				mTimeText.setText("時間結束!");
				if(hp>0){
					Toast.makeText(CameraViewActivity.this,"沒擊敗怪物 您輸了！！",Toast.LENGTH_SHORT).show();
					finish();
				}

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
