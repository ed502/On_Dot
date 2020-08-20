package kr.ac.kpu.ondot.Main;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import kr.ac.kpu.ondot.BluetoothModule.BluetoothManager;
import kr.ac.kpu.ondot.BluetoothModule.ConnectionInfo;
import kr.ac.kpu.ondot.CircleIndicator;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.CustomTouch.TouchType;
import kr.ac.kpu.ondot.Data.DotVO;
import kr.ac.kpu.ondot.Data.JsonModule;
import kr.ac.kpu.ondot.Data.VibratorPattern;
import kr.ac.kpu.ondot.Educate.EducateMain;
import kr.ac.kpu.ondot.Intro.EduIntro;
import kr.ac.kpu.ondot.Intro.QuizIntro;
import kr.ac.kpu.ondot.Intro.TranslateIntro;
import kr.ac.kpu.ondot.PermissionModule.PermissionCancelListener;
import kr.ac.kpu.ondot.PermissionModule.PermissionModule;
import kr.ac.kpu.ondot.Quiz.QuizMain;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.Translate.TranslateMain;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class MainActivity extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";
    private CircleIndicator circleIndicator;

    private ViewPager mViewpager;
    private MainPagerAdapter mAdapter;
    private int maxPage;
    private int currentView = 0;

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;

    private long first_time, second_time;

    // 음성 TTS 테스트
    private VoicePlayerModuleManager voicePlayerModuleManager;

    // 퍼미션 허가 테스트
    private PermissionModule permissionModule;

    private Vibrator vibrator;
    private VibratorPattern pattern;

    // Bluetooth
    private Context mContext;
    private BluetoothManager mBtManager = null;
    private BluetoothAdapter mBtAdapter = null;
    private ConnectionInfo mConnectionInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
        circleIndicator = findViewById(R.id.main_circleIndicator);

        linearLayout = findViewById(R.id.main_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (customTouchConnectListener != null) {
                    customTouchConnectListener.touchEvent(motionEvent);
                }
                return true;
            }
        });

        mContext = getApplicationContext();
        //initBlue();

        initDisplaySize();
        initTouchEvent();

        initPermission();
        initVoicePlayer();

        // hashkey 얻기
        getHashKey();

        mViewpager = findViewById(R.id.main_viewpager);
        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setClipToPadding(false);
        maxPage = mAdapter.getCount() - 1;
        circleIndicator.setItemMargin(5);
        circleIndicator.createDotPanel(mAdapter.getCount(), R.drawable.indicator_dot_off, R.drawable.indicator_dot_on);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentView = position;
                circleIndicator.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void menuVoice(int currentView) {
        //voicePlayerModuleManager.stop();
        // 메뉴 이름 음성 출력
        switch (currentView) {
            case 1:
                // 교육
                voicePlayerModuleManager.start(R.raw.education);
                break;
            case 2:
                // 퀴즈
                voicePlayerModuleManager.start(R.raw.quiz);
                break;
            case 3:
                // 번역
                voicePlayerModuleManager.start(R.raw.translate);
                break;
            case 0:
                // 메뉴얼
                voicePlayerModuleManager.start(R.raw.menual);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(mContext, "Connected to " + mConnectionInfo.getDeviceName(), Toast.LENGTH_SHORT).show();
        voicePlayerModuleManager.stop();
        menuVoice(currentView);
    }

    private void initTouchEvent() {
        customTouchConnectListener = new CustomTouchEvent(this, this);
    }

    // 해상도 구하기
    private void initDisplaySize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Screen.displayX = size.x;
        Screen.displayY = size.y;
    }

    // tts 초기화
    private void initVoicePlayer() {
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
    }

    // 퍼미션 모듈
    private void initPermission() {
        permissionModule = new PermissionModule(this, linearLayout, this);
    }

    // 메뉴에 들어가기 전 퍼미션 체크
    private void checkPermission() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int checkPermissionResult = permissionModule.checkPermission();
                if (checkPermissionResult == PermissionModule.PERMISSION_NOT_CHECKED) {
                    customTouchConnectListener.setTouchType(TouchType.PERMISSION_CHECK_TYPE);

                    permissionModule.startPermissionGuide(checkPermissionResult);
                    permissionModule.setPermissionCancelListener(new PermissionCancelListener() {
                        @Override
                        public void permissionCancel() {
                            customTouchConnectListener.setTouchType(TouchType.NONE_TYPE);
                            permissionModule.cancelPermissionGuide();
                        }
                    });
                } else {
                    activitySwitch(currentView);
                }
            }
        });

    }

    public void activitySwitch(int currentView) {
        Intent intent;
        switch (currentView) {
            case 0:
                intent = new Intent(MainActivity.this, Menual.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(MainActivity.this, EduIntro.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(MainActivity.this, QuizIntro.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(MainActivity.this, TranslateIntro.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onOneFingerFunction(final FingerFunctionType fingerFunctionType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*if(fingerFunctionType != FingerFunctionType.UP || fingerFunctionType != FingerFunctionType.DOWN){
                    Log.d(DEBUG_TYPE, "MainActivity - fingerFunctionType : " + fingerFunctionType);
                    voicePlayerModuleManager.start(fingerFunctionType);
                }*/
                Log.d(DEBUG_TYPE, "MainActivity - fingerFunctionType : " + fingerFunctionType);
                if (fingerFunctionType == FingerFunctionType.RIGHT) { //오른쪽에서 왼쪽으로 스크롤

                    if (currentView < maxPage) {
                        mViewpager.setCurrentItem(currentView + 1);
                        vibrator.vibrate(pattern.getVibrateNormalPattern(),-1);
                        voicePlayerModuleManager.start(fingerFunctionType);
                        voicePlayerModuleManager.stop();
                        menuVoice(currentView);
                    } else {
                        mViewpager.setCurrentItem(currentView);
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                        voicePlayerModuleManager.start(R.raw.last_menu);
                        menuVoice(currentView);
                    }
                } else if (fingerFunctionType == FingerFunctionType.LEFT) { //왼쪽에서 오른쪽으로 스크롤
                    if (currentView > 0) {
                        mViewpager.setCurrentItem(currentView - 1);
                        vibrator.vibrate(pattern.getVibrateNormalPattern(),-1);
                        voicePlayerModuleManager.start(fingerFunctionType);
                        voicePlayerModuleManager.stop();
                        menuVoice(currentView);
                    } else {
                        mViewpager.setCurrentItem(currentView);
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                        voicePlayerModuleManager.start(R.raw.first_menu);
                        menuVoice(currentView);
                    }
                } else if (fingerFunctionType == FingerFunctionType.ENTER) {
                    //activitySwitch(currentView);
                    vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                    checkPermission();
                    voicePlayerModuleManager.start(fingerFunctionType);
                    //menuVoice(currentView);
                    Log.d(DEBUG_TYPE, "MainActivity - fingerFunctionType : " + fingerFunctionType);
                } else if (fingerFunctionType == FingerFunctionType.NONE) {
                    //Log.d(DEBUG_TYPE, "MainActivity - fingerFunctionType : " + fingerFunctionType);

                    voicePlayerModuleManager.start(fingerFunctionType);
                    menuVoice(currentView);
                }
            }
        });

    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                voicePlayerModuleManager.stop();
                voicePlayerModuleManager.start(R.raw.back_exit);
                onBackPressed();
                break;
            case SPECIAL:
                Toast.makeText(this, "SPECIAL", Toast.LENGTH_SHORT).show();
                break;
            case NONE:
                Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, "종료하시겠습니까?", Toast.LENGTH_SHORT).show();
        if (second_time - first_time < 2000) {
            super.onBackPressed();
            finishAffinity();
        }
        first_time = System.currentTimeMillis();
    }

    // 권한 설정 동의
    @Override
    public void onPermissionUseAgree() {
        customTouchConnectListener.setTouchType(TouchType.NONE_TYPE);
        permissionModule.allowedPermission();

        activitySwitch(currentView);

    }

    // 권한 설정 동의 거부
    @Override
    public void onPermissionUseDisagree() {
        permissionModule.cancelPermissionGuide();
    }

    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        permissionModule.stopPermissionGuide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        voicePlayerModuleManager.allStop();
    }

    private void initBlue() {
        mConnectionInfo = ConnectionInfo.getInstance(mContext);

        mBtManager = BluetoothManager.getInstance(mContext, null);

        // Get  Bluetooth adapter
        mBtAdapter = mBtManager.getAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(mContext, "Connected to " + mConnectionInfo.getDeviceName(), Toast.LENGTH_SHORT).show();
    }
}