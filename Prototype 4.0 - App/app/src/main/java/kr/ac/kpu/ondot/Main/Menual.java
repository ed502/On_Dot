package kr.ac.kpu.ondot.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.kpu.ondot.CircleIndicator;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Data.DotVO;
import kr.ac.kpu.ondot.Data.TransDataVO;
import kr.ac.kpu.ondot.Educate.EducateMain;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.Quiz.QuizFirst;
import kr.ac.kpu.ondot.Quiz.QuizMain;
import kr.ac.kpu.ondot.Quiz.QuizPagerAdapter;
import kr.ac.kpu.ondot.Quiz.QuizSecond;
import kr.ac.kpu.ondot.Quiz.QuizThird;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class Menual extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";

    private LinearLayout linearLayout;
    private CustomTouchConnectListener customTouchConnectListener;

    private MenuType menuType = MenuType.MAIN;
    private VoicePlayerModuleManager voicePlayerModuleManager;

    private Vibrator vibrator;
    private long[] vibrateErrorPattern = {50, 100, 50, 100};
    private long[] vibrateNormalPattern = {50, 100};
    private long[] vibrateEnterPattern = {50,300};
    private long[] vibrateSpecialPattern = {50, 100};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menual);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        linearLayout = findViewById(R.id.menual_layout);
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
        voicePlayerModuleManager.start(menuType);

        Log.d(DEBUG_TYPE, "voic : "+ voicePlayerModuleManager.getMediaPlaying());


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
                if (fingerFunctionType == FingerFunctionType.ENTER) { // 블루투스 연결이 되어있는 상태
                    voicePlayerModuleManager.allStop();
                    vibrator.vibrate(vibrateEnterPattern,-1);
                    //voicePlayerModuleManager.start(fingerFunctionType);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                vibrator.vibrate(vibrateEnterPattern,-1);
                onBackPressed();
                break;
            case SPECIAL:
                vibrator.vibrate(vibrateSpecialPattern,-1);
                voicePlayerModuleManager.allStop();
                voicePlayerModuleManager.start(menuType);
                break;
            case NONE:
                break;

        }
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
