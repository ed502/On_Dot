package kr.ac.kpu.ondot.Translate;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

import kr.ac.kpu.ondot.BluetoothModule.BluetoothManager;
import kr.ac.kpu.ondot.BluetoothModule.ConnectionInfo;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Data.DotVO;
import kr.ac.kpu.ondot.Data.TransDataVO;
import kr.ac.kpu.ondot.Data.VibratorPattern;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class TranslateResult extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type111";

    private LinearLayout linearLayout;
    private CustomTouchConnectListener customTouchConnectListener;
    private TextView transData;
    private String sData;
    private ArrayList<DotVO> list;
    private ArrayList<TransDataVO> dotData;
    private TransDataVO dataVO;
    private DotVO data;
    private ArrayList<Integer> id, type;
    private ArrayList<String> word, dot, raw_id;

    private Vibrator vibrator;
    private VibratorPattern pattern;

    private VoicePlayerModuleManager voicePlayerModuleManager;

    // Bluetooth
    private Context mContext;
    private BluetoothManager mBtManager = null;
    private BluetoothAdapter mBtAdapter = null;
    private ConnectionInfo mConnectionInfo = null;

    private String ttsText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_result);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        mContext = getApplicationContext();
        initBlue();
        initVoicePlayer();


        linearLayout = findViewById(R.id.trans_result_layout);
        transData = findViewById(R.id.trans_data);

        Intent intent = getIntent();
        sData = intent.getExtras().getString("data");
        transData.setText(sData);

        Log.d(DEBUG_TYPE, "텍스트 : " + sData);

        voicePlayerModuleManager.allStop();
        voicePlayerModuleManager.start(sData);
        storeData();

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
    public void onOneFingerFunction(FingerFunctionType fingerFunctionType) {

    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK:
                vibrator.vibrate(pattern.getVibrateEnterPattern(),-1);
                voicePlayerModuleManager.start(fingerFunctionType);
                onBackPressed();
                break;
            case SPECIAL:
                voicePlayerModuleManager.allStop();
                voicePlayerModuleManager.start(sData);
                //Toast.makeText(this, "SPECIAL", Toast.LENGTH_SHORT).show();
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
    public void onPermissionUseAgree() {

    }

    @Override
    public void onPermissionUseDisagree() {

    }

    public void storeData(){

        for(int i=0; i<sData.length()/6; i++){
            dataVO = new TransDataVO();
            dataVO.setDot(sData.substring(i*6,(i+1)*6));

        }
    }

    public void sendData(String str) {
        mBtManager.write(str.getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBtManager != null){
            sendData("2222222222222222222222222222222222222222222222222");
        }

        //finalize();
    }

    public void transData(){

    }

    private void initBlue() {
        mConnectionInfo = ConnectionInfo.getInstance(mContext);

        mBtManager = BluetoothManager.getInstance(mContext, null);

        // Get  Bluetooth adapter
        mBtAdapter = mBtManager.getAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            //Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }

       //Toast.makeText(mContext, "Connected to " + mConnectionInfo.getDeviceName(), Toast.LENGTH_SHORT).show();
    }
}
