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
import android.os.Vibrator;
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
import kr.ac.kpu.ondot.Data.VibratorPattern;
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
    private String data = "", result = "";

    private VoicePlayerModuleManager voicePlayerModuleManager;

    private Vibrator vibrator;
    private VibratorPattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_main);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
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
    private void initVoicePlayer() {
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
    }

    @Override
    public void onOneFingerFunction(final FingerFunctionType fingerFunctionType) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataCount == 100) {
                    voicePlayerModuleManager.start(R.raw.not_insert_dot);
                    //Toast.makeText(getApplicationContext(), "더 이상 입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (fingerFunctionType == FingerFunctionType.UP || fingerFunctionType == FingerFunctionType.DOWN || fingerFunctionType == FingerFunctionType.RIGHT) {
                        int i = scrollCount % 6;
                        if (fingerFunctionType == FingerFunctionType.UP) {
                            switch (i) {
                                case 0:
                                    voicePlayerModuleManager.start(R.raw.dot_1);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 1:
                                    voicePlayerModuleManager.start(R.raw.dot_2);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 2:
                                    voicePlayerModuleManager.start(R.raw.dot_3);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 3:
                                    voicePlayerModuleManager.start(R.raw.dot_4);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 4:
                                    voicePlayerModuleManager.start(R.raw.dot_5);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                                case 5:
                                    voicePlayerModuleManager.start(R.raw.dot_6);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    data = data + "2";
                                    break;
                            }
                            vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                            scrollCount++;
                        } else if (fingerFunctionType == FingerFunctionType.DOWN) {
                            switch (i) {
                                case 0:
                                    voicePlayerModuleManager.start(R.raw.dot_1);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 1:
                                    voicePlayerModuleManager.start(R.raw.dot_2);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 2:
                                    voicePlayerModuleManager.start(R.raw.dot_3);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 3:
                                    voicePlayerModuleManager.start(R.raw.dot_4);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 4:
                                    voicePlayerModuleManager.start(R.raw.dot_5);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                                case 5:
                                    voicePlayerModuleManager.start(R.raw.dot_6);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    data = data + "1";
                                    break;
                            }
                            vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                            scrollCount++;
                        } else if (fingerFunctionType == FingerFunctionType.RIGHT) {
                            if (scrollCount > 0) {
                                if (scrollCount % 6 == 0) {
                                    scrollCount--;
                                    dataCount--;
                                    voicePlayerModuleManager.start(R.raw.delete);
                                    data = data.substring(0, scrollCount);
                                    for (int j = 0; j < 5; j++) {
                                        if (data.substring(((scrollCount / 6) * 6) + j, ((scrollCount / 6) * 6) + j + 1).equals("1")) {
                                            circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                        } else if (data.substring(((scrollCount / 6) * 6) + j, ((scrollCount / 6) * 6) + j + 1).equals("2")) {
                                            circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                        }
                                    }
                                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                                } else {
                                    scrollCount--;
                                    voicePlayerModuleManager.start(R.raw.delete);
                                    i = scrollCount % 6;
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                                    data = data.substring(0, scrollCount);
                                }
                            } else if (scrollCount == 0) {
                                voicePlayerModuleManager.start(R.raw.not_delete);
                                vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                            }
                        }
                        if (scrollCount % 6 == 0) {
                            for (int j = 0; j < 6; j++) {
                                circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                            }
                            dataCount++;
                        }

                    } else if (fingerFunctionType != FingerFunctionType.UP && fingerFunctionType != FingerFunctionType.DOWN && fingerFunctionType != FingerFunctionType.RIGHT && fingerFunctionType != FingerFunctionType.ENTER ) {
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                        voicePlayerModuleManager.start(R.raw.reinput);
                        //Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if (fingerFunctionType == FingerFunctionType.ENTER) {
                    if (scrollCount % 6 == 0 && scrollCount > 1) {
                        getData();
                        if (result.equals("")) {
                            result = "번역할 수 없습니다";
                        }
                        vibrator.vibrate(pattern.getVibrateEnterPattern(), -1);
                        Intent intent = new Intent(getApplicationContext(), TranslateResult.class);
                        intent.putExtra("data", result);
                        startActivity(intent);
                        finish();
                    } else {
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                        voicePlayerModuleManager.start(R.raw.not_dot_input);
                        //Toast.makeText(getApplicationContext(), "점자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                vibrator.vibrate(pattern.getVibrateEnterPattern(), -1);
                voicePlayerModuleManager.start(fingerFunctionType);
                onBackPressed();
                break;
            case SPECIAL:
                /*if (scrollCount % 12 == 0) {
                    scrollCount = scrollCount + 6;
                    data = data + "111111";
                    vibrator.vibrate(pattern.getVibrateSpecialPattern(), -1);
                    Toast.makeText(this, "6개 빈칸", Toast.LENGTH_SHORT).show();
                } else {
                    vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                    Toast.makeText(this, "입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                }*/

                break;
            case NONE:
                //Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
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
                for (int j = 0; j < 12; j++) {
                    circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                }
                vibrator.vibrate(pattern.getVibrateShakePattern(), -1);
                //Toast.makeText(getApplicationContext(), "초기화", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPermissionUseAgree() {

    }

    @Override
    public void onPermissionUseDisagree() {

    }

    public void getData() {
        StrictMode.enableDefaults();
        boolean bDot = false;
        try {
            URL url = new URL("http://15.165.135.160/DotCombine?dot=" + data);

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