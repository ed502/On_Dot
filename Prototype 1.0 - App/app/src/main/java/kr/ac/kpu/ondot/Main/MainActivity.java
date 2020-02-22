package kr.ac.kpu.ondot.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import kr.ac.kpu.ondot.Board.BoardMain;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Educate.EducateMain;
import kr.ac.kpu.ondot.Quiz.QuizMain;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.Translate.TranslateMain;

public class MainActivity extends AppCompatActivity implements CustomTouchEventListener, TextToSpeechListener {
    private TextToSpeechClient ttsClient;

    private final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 1111;

    private final String DEBUG_TYPE = "type";

    private ViewPager mViewpager;
    private MainPagerAdapter mAdapter;
    private int currentView;

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
            } else {
                // 유저가 거부하면서 다시 묻지 않기를 클릭.. 권한이 없다고 유저에게 직접 알림.
            }
        } else {

        }


        initDisplaySize();
        initTouchEvent();

        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());

        String speechMode = TextToSpeechClient.NEWTONE_TALK_1;
        String voiceType = TextToSpeechClient.VOICE_MAN_READ_CALM;
        double speechSpeed = 1.0D;
        int sampleRate = 16000;

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(speechMode)
                .setSpeechSpeed(speechSpeed)
                .setSpeechVoice(voiceType)
                .setListener(this)
                .setSampleRate(sampleRate)
                .build();




        mViewpager = findViewById(R.id.main_viewpager);
        mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setClipToPadding(false);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentView = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String strText = "안녕하세요 제발 해주세요 제발 제발 제발 제발 제발";
        ttsClient.play(strText);
    }

    public void activitySwitch(int currentView) {
        Intent intent;
        switch (currentView) {
            case 0:
                intent =  new Intent(MainActivity.this, EducateMain.class);
                startActivity(intent);
                break;
            case 1:
                intent =  new Intent(MainActivity.this, QuizMain.class);
                startActivity(intent);
                break;
            case 2:
                intent =  new Intent(MainActivity.this, TranslateMain.class);
                startActivity(intent);
                break;
            case 3:
                intent =  new Intent(MainActivity.this, BoardMain.class);
                startActivity(intent);
                break;
        }
    }

    private void initTouchEvent() {
        customTouchConnectListener = new CustomTouchEvent(this, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(customTouchConnectListener != null){
            customTouchConnectListener.touchEvent(event);
        }

        return false;


        /*switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                activitySwitch(currentView);
                break;
        }

        return false;*/
    }

    // 해상도 구하기
    private void initDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);




        Screen.displayX = size.x;
        Screen.displayY = size.y;
    }

    @Override
    public void onOneFingerFunction(FingerFunctionType fingerFunctionType) {

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fingerFunctionType == FingerFunctionType.ENTER){
                    Toast.makeText(getApplicationContext(),"ENTER",Toast.LENGTH_SHORT).show();
                }else if(fingerFunctionType == FingerFunctionType.LEFT){
                    Toast.makeText(getApplicationContext(),"LEFT",Toast.LENGTH_SHORT).show();
                }else if(fingerFunctionType == FingerFunctionType.RIGHT){
                    Toast.makeText(getApplicationContext(),"RIGHT",Toast.LENGTH_SHORT).show();
                }else if(fingerFunctionType == FingerFunctionType.UP){
                    Toast.makeText(getApplicationContext(),"UP",Toast.LENGTH_SHORT).show();
                }else if(fingerFunctionType == FingerFunctionType.DOWN){
                    Toast.makeText(getApplicationContext(),"DOWN",Toast.LENGTH_SHORT).show();
                }else if(fingerFunctionType == FingerFunctionType.LONG){
                    Toast.makeText(getApplicationContext(),"LONG",Toast.LENGTH_SHORT).show();
                }else if(fingerFunctionType == FingerFunctionType.NONE){
                    Toast.makeText(getApplicationContext(),"NONE",Toast.LENGTH_SHORT).show();
                }

            }
        });*/


        Log.d(DEBUG_TYPE,"MainActivity - fingerFunctionType : " + fingerFunctionType);
        if(fingerFunctionType == FingerFunctionType.ENTER){
            activitySwitch(currentView);

            Log.d(DEBUG_TYPE,"MainActivity - fingerFunctionType : " + fingerFunctionType);
        }
    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                Toast.makeText(this, "BACK", Toast.LENGTH_SHORT).show();
                break;
            case SPECIAL:
                Toast.makeText(this, "SPECIAL", Toast.LENGTH_SHORT).show();
                break;
            case NONE:
                Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    // --------------- 음성 api 연습중 --------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onFinished() {
        int intSentSize = ttsClient.getSentDataSize();      //세션 중에 전송한 데이터 사이즈
        int intRecvSize = ttsClient.getReceivedDataSize();  //세션 중에 전송받은 데이터 사이즈

        final String strInacctiveText = "handleFinished() SentSize : " + intSentSize + "  RecvSize : " + intRecvSize;

        Log.i(DEBUG_TYPE, strInacctiveText);
    }

    @Override
    public void onError(int code, String message) {
        handleError(code);
    }

    private void handleError(int errorCode) {
        String errorText;
        switch (errorCode) {
            case TextToSpeechClient.ERROR_NETWORK:
                errorText = "네트워크 오류";
                break;
            case TextToSpeechClient.ERROR_NETWORK_TIMEOUT:
                errorText = "네트워크 지연";
                break;
            case TextToSpeechClient.ERROR_CLIENT_INETRNAL:
                errorText = "음성합성 클라이언트 내부 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_INTERNAL:
                errorText = "음성합성 서버 내부 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_TIMEOUT:
                errorText = "음성합성 서버 최대 접속시간 초과";
                break;
            case TextToSpeechClient.ERROR_SERVER_AUTHENTICATION:
                errorText = "음성합성 인증 실패";
                break;
            case TextToSpeechClient.ERROR_SERVER_SPEECH_TEXT_BAD:
                errorText = "음성합성 텍스트 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_SPEECH_TEXT_EXCESS:
                errorText = "음성합성 텍스트 허용 길이 초과";
                break;
            case TextToSpeechClient.ERROR_SERVER_UNSUPPORTED_SERVICE:
                errorText = "음성합성 서비스 모드 오류";
                break;
            case TextToSpeechClient.ERROR_SERVER_ALLOWED_REQUESTS_EXCESS:
                errorText = "허용 횟수 초과";
                break;
            default:
                errorText = "정의하지 않은 오류";
                break;
        }

        final String statusMessage = errorText + " (" + errorCode + ")";

        Log.i(DEBUG_TYPE, statusMessage);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(DEBUG_TYPE,"MainActivity - 성공?? ");
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
