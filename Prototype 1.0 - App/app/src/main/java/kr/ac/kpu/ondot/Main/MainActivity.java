package kr.ac.kpu.ondot.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class MainActivity extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type111";
    private final String TAG = "DB11111";

    private ViewPager mViewpager;
    private MainPagerAdapter mAdapter;
    private int currentView;

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;

    // 음성 TTS 테스트
    private VoicePlayerModuleManager voicePlayerModuleManager;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDisplaySize();
        initTouchEvent();


        Button btn1 = (Button) findViewById(R.id.buttonTest);
        Button connectBtn = (Button) findViewById(R.id.DBConnecBtn);
        textView = (TextView) findViewById(R.id.textViewTest);

        final Customtask task = new Customtask();

        /*btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.execute("6");
            }
        });*/
        //task.execute("6");



        // 음성 TTS 테스트
        voicePlayerModuleManager = new VoicePlayerModuleManager(this);

        //checkPermission();

        // hashkey 얻기
        getHashKey();


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



    }

    class Customtask extends AsyncTask<String, Void, String>{
        String sendMsg, recvMsg;

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG,"들어옴?");
            try{
                String str;
                URL url = new URL("http://15.165.135.160/translateRankTest.jsp");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "id=6";

                osw.write(sendMsg);
                osw.flush();
                Log.d(TAG,"보냊ㅁ???");
/*

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();

                    while((str = reader.readLine()) != null){
                        buffer.append(str);
                    }

                    recvMsg = buffer.toString();
                    Log.d(TAG,recvMsg);

                }else{
                    Log.d(TAG,conn.getResponseCode() + "에러");
                }
*/


            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            return recvMsg;
        }
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
            //activitySwitch(currentView);

            // 음성 TTS 테스트
            voicePlayerModuleManager.start(fingerFunctionType);

            Log.d(DEBUG_TYPE,"MainActivity - fingerFunctionType 수행 됨");

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

    private void checkPermission(){

    }

    private void getHashKey(){
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



}
