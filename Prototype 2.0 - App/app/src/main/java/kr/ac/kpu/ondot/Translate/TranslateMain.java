package kr.ac.kpu.ondot.Translate;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Data.DotVO;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.Quiz.QuizFirst;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;


public class TranslateMain extends AppCompatActivity implements CustomTouchEventListener, SensorEventListener {
    private final String DEBUG_TYPE = "type";
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static int LONG_PRESS_TIME = 500;

    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private long mShakeTime;

    private LinearLayout linearLayout;
    private LinearLayout[] circle;
    private CustomTouchConnectListener customTouchConnectListener;
    private int scrollCount, dataCount = 0;
    private String data = "", result="";

    private VoicePlayerModuleManager voicePlayerModuleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_main);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        circle = new LinearLayout[12];
        scrollCount = 0;
        circle[0] = findViewById(R.id.trans_circle1);
        circle[1] = findViewById(R.id.trans_circle2);
        circle[2] = findViewById(R.id.trans_circle3);
        circle[3] = findViewById(R.id.trans_circle4);
        circle[4] = findViewById(R.id.trans_circle5);
        circle[5] = findViewById(R.id.trans_circle6);
        circle[6] = findViewById(R.id.trans_circle7);
        circle[7] = findViewById(R.id.trans_circle8);
        circle[8] = findViewById(R.id.trans_circle9);
        circle[9] = findViewById(R.id.trans_circle10);
        circle[10] = findViewById(R.id.trans_circle11);
        circle[11] = findViewById(R.id.trans_circle12);


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

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        initDisplaySize();
        initTouchEvent();
        initVoicePlayer();
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
    public void onOneFingerFunction(final FingerFunctionType fingerFunctionType) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                voicePlayerModuleManager.start(fingerFunctionType);
                if (dataCount == 8) {
                    Toast.makeText(getApplicationContext(), "더 이상 입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (fingerFunctionType == FingerFunctionType.UP || fingerFunctionType == FingerFunctionType.DOWN) {
                        int i = scrollCount % 12;
                        if (fingerFunctionType == FingerFunctionType.UP) {
                            switch (i) {
                                case 0:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 1:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 2:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 3:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 4:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 5:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 6:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 7:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 8:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 9:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 10:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 11:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), scrollCount+1 + "번째 입력되었습니다(업)", Toast.LENGTH_SHORT).show();
                            scrollCount++;
                        }
                        else if (fingerFunctionType == FingerFunctionType.DOWN) {
                            switch (i) {
                                case 0:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 1:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 2:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 3:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 4:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 5:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 6:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 7:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 8:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 9:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 10:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 11:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), scrollCount + "번째 입력되었습니다(다운)", Toast.LENGTH_SHORT).show();
                            scrollCount++;
                        }
                        if (scrollCount % 12 == 0) {
                            for (int j = 0; j < 12; j++) {
                                circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                            }
                            dataCount++;
                            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                        }

                    } else if (fingerFunctionType != FingerFunctionType.UP && fingerFunctionType != FingerFunctionType.DOWN) {
                        Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if(fingerFunctionType==FingerFunctionType.ENTER){
                    if(scrollCount%6==0 && scrollCount >1) {
                        getData();
                        Intent intent = new Intent(getApplicationContext(), TranslateResult.class);
                        intent.putExtra("data", result);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "점자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
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
                if(scrollCount%12==0) {
                    if(scrollCount==0){
                        data = data + "111111";
                    }
                    scrollCount = scrollCount + 6;
                    Toast.makeText(this, "6개 빈칸", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                }
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
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
            double squaredD = Math.sqrt(f.doubleValue());
            float gForce = (float) squaredD;
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                long currentTime = System.currentTimeMillis();
                if (mShakeTime + SHAKE_SKIP_TIME > currentTime) {
                    return;
                }
                mShakeTime = currentTime;
                scrollCount = 0;
                dataCount = 0;
                data = "";
                for (int j = 0; j < 6; j++) {
                    circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                }
                Toast.makeText(getApplicationContext(), "초기화", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    public void onPermissionUseAgree() {

    }

    @Override
    public void onPermissionUseDisagree() {

    }

    public void getData() {
        StrictMode.enableDefaults();
        boolean bId = false, bWord = false, bDot = false, bRaw_id = false, bType = false;
        try {
            URL url = new URL("http://15.165.135.160/dotCombine?dot="+data);

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dot")) {
                            bDot = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bDot) {
                            result = (parser.getText());
                            bDot = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {

        }
    }
}