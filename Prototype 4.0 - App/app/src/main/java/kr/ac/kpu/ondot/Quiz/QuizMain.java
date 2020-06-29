package kr.ac.kpu.ondot.Quiz;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
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
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class QuizMain extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";

    private CircleIndicator circleIndicator;

    private ViewPager mViewpager;
    private QuizPagerAdapter mAdapter;
    private int maxPage;
    private int currentView;

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;

    private Vibrator vibrator;
    private VibratorPattern pattern;

    private VoicePlayerModuleManager voicePlayerModuleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
        circleIndicator = findViewById(R.id.quiz_circleIndicator);

        linearLayout = findViewById(R.id.quiz_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (customTouchConnectListener != null) {
                    customTouchConnectListener.touchEvent(motionEvent);
                }
                return true;
            }
        });

        initDisplaySize();
        initTouchEvent();
        initVoicePlayer();

        mViewpager = findViewById(R.id.quiz_viewpager);
        mAdapter = new QuizPagerAdapter(getSupportFragmentManager());
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

    private void initVoicePlayer() {
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
    }

    private void menuVoice(int currentView) {
        voicePlayerModuleManager.stop();
        // 메뉴 이름 음성 출력
        switch (currentView) {
            case 0:
                // 1단계
                voicePlayerModuleManager.start(R.raw.quiz_1);
                break;
            case 1:
                // 2단계
                voicePlayerModuleManager.start(R.raw.quiz_2);
                break;
            case 2:
                // 3단계
                voicePlayerModuleManager.start(R.raw.quiz_3);
                break;

        }
    }

    public void activitySwitch(int currentView) {
        Intent intent;
        switch (currentView) {
            case 0:
                intent = new Intent(QuizMain.this, QuizFirst.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(QuizMain.this, QuizSecond.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(QuizMain.this, QuizThird.class);
                startActivity(intent);
                break;
        }
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
                if (fingerFunctionType == FingerFunctionType.RIGHT) { //오른쪽에서 왼쪽으로 스크롤
                    if (currentView < maxPage) {
                        mViewpager.setCurrentItem(currentView + 1);
                        vibrator.vibrate(pattern.getVibrateNormalPattern(),-1);
                    } else {
                        mViewpager.setCurrentItem(currentView);
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                    }
                    voicePlayerModuleManager.start(fingerFunctionType);
                    menuVoice(currentView);
                } else if (fingerFunctionType == FingerFunctionType.LEFT) { //왼쪽에서 오른쪽으로 스크롤
                    if (currentView > 0) {
                        mViewpager.setCurrentItem(currentView - 1);
                        vibrator.vibrate(pattern.getVibrateNormalPattern(),-1);
                    } else {
                        mViewpager.setCurrentItem(currentView);
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                    }
                    voicePlayerModuleManager.start(fingerFunctionType);
                    menuVoice(currentView);
                }else if (fingerFunctionType == FingerFunctionType.ENTER) {
                    vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                    voicePlayerModuleManager.start(fingerFunctionType);
                    activitySwitch(currentView);
                }else if (fingerFunctionType == FingerFunctionType.NONE) {
                    voicePlayerModuleManager.start(fingerFunctionType);
                }
            }
        });
    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                onBackPressed();
                break;
            case SPECIAL:
                break;
            case NONE:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        menuVoice(currentView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPermissionUseAgree() {

    }

    @Override
    public void onPermissionUseDisagree() {

    }
}