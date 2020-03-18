package kr.ac.kpu.ondot.Translate;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;


public class TranslateMain extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";
    private LinearLayout linearLayout;
    private LinearLayout[] circle;
    private CustomTouchConnectListener customTouchConnectListener;
    private int scrollCount;
    private String data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_main);
        circle = new LinearLayout[6];
        scrollCount = 0;
        circle[0] = findViewById(R.id.circle1);
        circle[1] = findViewById(R.id.circle2);
        circle[2] = findViewById(R.id.circle3);
        circle[3] = findViewById(R.id.circle4);
        circle[4] = findViewById(R.id.circle5);
        circle[5] = findViewById(R.id.circle6);

        linearLayout = findViewById(R.id.translate_layout);
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
                int i=scrollCount%6;
                if(fingerFunctionType == FingerFunctionType.DOWN || fingerFunctionType == FingerFunctionType.UP){
                    if (fingerFunctionType == FingerFunctionType.UP) { //오른쪽에서 왼쪽으로 스크롤
                        switch (i) {
                            case 0:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                data = data+"2";
                                break;
                            case 1:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                data = data+"2";
                                break;
                            case 2:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                data = data+"2";
                                break;
                            case 3:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                data = data+"2";
                                break;
                            case 4:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                data = data+"2";
                                break;
                            case 5:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                data = data+"2";
                                break;
                        }
                        scrollCount++;
                        Toast.makeText(getApplicationContext(),scrollCount+"번째 입력되었습니다(업)",Toast.LENGTH_SHORT).show();

                    } else if (fingerFunctionType == FingerFunctionType.DOWN) {
                        switch (i) {
                            case 0:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                data = data+"1";
                                break;
                            case 1:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                data = data+"1";
                                break;
                            case 2:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                data = data+"1";
                                break;
                            case 3:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                data = data+"1";
                                break;
                            case 4:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                data = data+"1";
                                break;
                            case 5:
                                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                data = data+"1";
                                break;
                        }
                        scrollCount++;
                        Toast.makeText(getApplicationContext(),scrollCount+"번째 입력되었습니다(다운)",Toast.LENGTH_SHORT).show();
                    }
                    if(scrollCount>5 && scrollCount%6==0){
                        for(int j = 0 ;j<6;j++) {
                            circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                        }
                        data = data + "  ";
                        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"다시 입력해주세요",Toast.LENGTH_SHORT).show();
                }



            }
        });
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
}
