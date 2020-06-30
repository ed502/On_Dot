package kr.ac.kpu.ondot.Intro;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import kr.ac.kpu.ondot.CircleIndicator;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Data.VibratorPattern;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.Main.MainPagerAdapter;
import kr.ac.kpu.ondot.MenualPagerAdapter.MainMenualAdapter;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class MainIntro extends AppCompatActivity implements CustomTouchEventListener {

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;

    private VoicePlayerModuleManager voicePlayerModuleManager;

    private ViewPager mViewpager;
    private MainMenualAdapter mAdapter;
    private CircleIndicator circleIndicator;
    private int maxPage, currentView=0;

    private MenuType menuType = MenuType.MAIN;
    private Vibrator vibrator;
    private VibratorPattern pattern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_intro);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
        linearLayout = findViewById(R.id.main_intro_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (customTouchConnectListener != null) {
                    customTouchConnectListener.touchEvent(motionEvent);
                }
                return true;
            }
        });

        mViewpager = findViewById(R.id.mainMenual_viewpager);
        circleIndicator = findViewById(R.id.mainMenual_circleIndicator);
        mAdapter = new MainMenualAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setClipToPadding(false);
        maxPage = mAdapter.getCount() - 1;
        circleIndicator.setItemMargin(5);
        circleIndicator.createDotPanel(mAdapter.getCount(), R.drawable.indicator_dot_off, R.drawable.indicator_dot_on);

        initDisplaySize();
        initTouchEvent();
        initVoicePlayer();

        voicePlayerModuleManager.start(menuType);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {     }

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

    private void initVoicePlayer() {
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
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

    @Override
    public void onOneFingerFunction(final FingerFunctionType fingerFunctionType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fingerFunctionType == FingerFunctionType.ENTER) {
                    vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                    voicePlayerModuleManager.allStop();
                    //voicePlayerModuleManager.start(fingerFunctionType);
                    startActivity(new Intent(getApplicationContext(), BluetoothActivity.class));
                    finish();
                }
                else if (fingerFunctionType == FingerFunctionType.RIGHT) { //오른쪽에서 왼쪽으로 스크롤

                    if (currentView < maxPage) {
                        mViewpager.setCurrentItem(currentView + 1);
                        vibrator.vibrate(pattern.getVibrateNormalPattern(),-1);
                    } else {
                        mViewpager.setCurrentItem(currentView);
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                    }
                } else if (fingerFunctionType == FingerFunctionType.LEFT) { //왼쪽에서 오른쪽으로 스크롤
                    if (currentView > 0) {
                        mViewpager.setCurrentItem(currentView - 1);
                        vibrator.vibrate(pattern.getVibrateNormalPattern(),-1);
                    } else {
                        mViewpager.setCurrentItem(currentView);
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                    }
                }
            }
        });
    }

    @Override
    public void onTwoFingerFunction(final FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                voicePlayerModuleManager.allStop();
                voicePlayerModuleManager.start(fingerFunctionType);
                onBackPressed();
                break;
        }
    }

    @Override
    public void onPermissionUseAgree() {

    }

    @Override
    public void onPermissionUseDisagree() {

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}