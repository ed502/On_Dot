package kr.ac.kpu.ondot.Board;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class BoardMain extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";
    private LinearLayout linearLayout;
    private CustomTouchConnectListener customTouchConnectListener;

    private MenuType menuType = MenuType.BOARD;
    private VoicePlayerModuleManager voicePlayerModuleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_main);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        initDisplaySize();
        initTouchEvent();
        initVoicePlayer();

        linearLayout = findViewById(R.id.board_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (customTouchConnectListener != null) {
                    customTouchConnectListener.touchEvent(motionEvent);
                }
                return true;
            }
        });
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
    private void initVoicePlayer(){
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
    }


    @Override
    public void onOneFingerFunction(FingerFunctionType fingerFunctionType) {

    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
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
        super.onBackPressed();
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
        voicePlayerModuleManager.start(menuType);
    }
}
