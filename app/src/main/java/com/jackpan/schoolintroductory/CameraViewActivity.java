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
		Toast.makeText(this,"latitude: "+location.getLatitude()+" longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
		updateDescription();
		soundPool2.play(alertId2, 1.0F, 1.0F, 0, 0, 1.0F);

	}

	@Override
	public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
		mAzimuthReal = azimuthChangedTo;
		mAzimuthTeoretical = calculateTeoreticalAzimuth();

		pointerIcon = (ImageView) findViewById(R.id.icon);
		//動畫路徑設定(x1,x2,y1,y2)
		Animation am = new TranslateAnimation(10,200,10,500);

		//動畫開始到結束的時間，2秒
		am.setDuration( 2000 );

		// 動畫重覆次數 (-1表示一直重覆，0表示不重覆執行，所以只會執行一次)
		am.setRepeatCount(-1);

		//將動畫寫入ImageView
		pointerIcon.setAnimation(am);
		//開始動畫
		am.startNow();

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
				soundPool.play(alertId, 1.0F, 1.0F, 0, 0, 1.0F);

			}
		});
		updateDescription();

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

	private void setupLayout() {
		mTimeText =findViewById(R.id.time);
		new CountDownTimer(20000,1000){

			@Override
			public void onFinish() {
				mTimeText.setText("Done!");
			}

			@Override
			public void onTick(long millisUntilFinished) {
				mTimeText.setText(""+millisUntilFinished/1000);
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
