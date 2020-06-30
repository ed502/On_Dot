package kr.ac.kpu.ondot.Educate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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
import kr.ac.kpu.ondot.Data.VibratorPattern;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

//줄임말
public class EduFifth extends AppCompatActivity implements CustomTouchEventListener {

    private static final String TAG = "EduFifth";

    private final String DEBUG_TYPE = "type";
    private LinearLayout linearLayout;
    private CustomTouchConnectListener customTouchConnectListener;
    private TextView textData;
    private LinearLayout[] circle;
    private ArrayList<DotVO> list;
    private DotVO data;
    private ArrayList<Integer> id, type;
    private ArrayList<String> word, dot, raw_id;
    private int currentLocation = 0;

    private String rawId = "";

    private VoicePlayerModuleManager voicePlayerModuleManager;

    private Vibrator vibrator;
    private VibratorPattern pattern;
    // Bluetooth

    private Context mContext;

    private BluetoothManager mBtManager = null;
    private BluetoothAdapter mBtAdapter = null;
    private ConnectionInfo mConnectionInfo = null;        // Remembers connection info when BT connection is made


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edu_fifth);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
        mContext = getApplicationContext();
        initBlue();

        initVoicePlayer();

        overridePendingTransition(0, 0);

        getData();
        setDot();
        checkData();

    }

    private void initVoicePlayer() {
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
    }

    private void initTouchEvent() {
        customTouchConnectListener = new CustomTouchEvent(this, this);
    }

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
                if ((fingerFunctionType == FingerFunctionType.RIGHT) && currentLocation < list.size() - 1) { //오른쪽에서 왼쪽으로 스크롤
                    currentLocation++;
                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                    checkData();
                } else if ((fingerFunctionType == FingerFunctionType.LEFT) && currentLocation > 0) { //왼쪽에서 오른쪽으로 스크롤
                    currentLocation--;
                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                    checkData();
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
                voicePlayerModuleManager.allStop();
                voicePlayerModuleManager.start(rawId);
                break;
            case NONE:
                Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setDot() {
        linearLayout = findViewById(R.id.edu_fifth_layout);
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
        textData = findViewById(R.id.edu5_text);

        circle = new LinearLayout[12];

        circle[0] = findViewById(R.id.edu5_circle1);
        circle[1] = findViewById(R.id.edu5_circle2);
        circle[2] = findViewById(R.id.edu5_circle3);
        circle[3] = findViewById(R.id.edu5_circle4);
        circle[4] = findViewById(R.id.edu5_circle5);
        circle[5] = findViewById(R.id.edu5_circle6);
        circle[6] = findViewById(R.id.edu5_circle7);
        circle[7] = findViewById(R.id.edu5_circle8);
        circle[8] = findViewById(R.id.edu5_circle9);
        circle[9] = findViewById(R.id.edu5_circle10);
        circle[10] = findViewById(R.id.edu5_circle11);
        circle[11] = findViewById(R.id.edu5_circle12);
    }

    // 서버에서 데이터 파싱해서 ArrayList에 저장
    public void getData() {
        StrictMode.enableDefaults();
        id = new ArrayList<Integer>();
        type = new ArrayList<Integer>();
        word = new ArrayList<String>();
        dot = new ArrayList<String>();
        raw_id = new ArrayList<>();

        list = new ArrayList<DotVO>();
        boolean bId = false, bWord = false, bDot = false, bRaw_id = false, bType = false;
        try {
            URL url = new URL("http://15.165.135.160/DotAbbrev");

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("id")) {
                            bId = true;
                        }
                        if (parser.getName().equals("word")) {
                            bWord = true;
                        }
                        if (parser.getName().equals("dot")) {
                            bDot = true;
                        }
                        if (parser.getName().equals("raw_id")) {
                            bRaw_id = true;
                        }
                        if (parser.getName().equals("type")) {
                            bType = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (bId) {
                            id.add(Integer.parseInt(parser.getText()));
                            bId = false;
                        }
                        if (bWord) {
                            word.add(parser.getText());
                            bWord = false;
                        }
                        if (bDot) {
                            dot.add(parser.getText());
                            bDot = false;
                        }
                        if (bRaw_id) {
                            raw_id.add(parser.getText());
                            bRaw_id = false;
                        }
                        if (bType) {
                            type.add(Integer.parseInt(parser.getText()));
                            bType = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < id.size(); i++) {
            data = new DotVO();
            data.setId(id.get(i));
            data.setWord(word.get(i));
            data.setDot(dot.get(i));
            data.setRaw_id(raw_id.get(i));
            data.setType(type.get(i));
            list.add(data);
        }
    }

    /*
    checkData()
    문제점1. 점자가 6개에서 12개로 변할 때 layout을 변경해야함
    문제점2. layout이 변경되면서 초기화했던 id를 다시 초기화해야하는 상황 발생

    해결책1. 점자가 6개, 12개가 있는 layout 2개를 만듦
    해결책2. setContentView를 이용하여 액티비티 전체를 교체하면서 setDot() 함수를 호출하여 각 layout에 있는 id를 전부 초기화
    해결책2 과정에서 스크롤 할 때마다 id를 초기화하는 문제가 발생
     */
    public void checkData() {
        String dotData = list.get(currentLocation).getDot();
        if (list.get(currentLocation).getDot().length() > 6) {
            setContentView(R.layout.edu_fifth1);
            setDot();
            textData.setText(list.get(currentLocation).getWord());
            for (int i = 0; i < dotData.length(); i++) {
                if ((int) dotData.charAt(i) == (int) '1') {
                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                } else if ((int) dotData.charAt(i) == (int) '2') {
                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                }
            }
        } else {
            setContentView(R.layout.edu_fifth);
            setDot();
            textData.setText(list.get(currentLocation).getWord());
            for (int i = 0; i < dotData.length(); i++) {
                if ((int) dotData.charAt(i) == (int) '1') {
                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                } else if ((int) dotData.charAt(i) == (int) '2') {
                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                }
            }
        }
        sendData(dotData);
        rawId = list.get(currentLocation).getRaw_id();
        voicePlayerModuleManager.allStop();
        voicePlayerModuleManager.start(rawId);

    }

    public void sendData(String str) {
        Log.d(TAG, "strrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr : " + str);
        mBtManager.write(str.getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendData("222222222222222222222222222222222222222222222222");
        //finalize();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //finalize();
    }

    private void initBlue() {
        mConnectionInfo = ConnectionInfo.getInstance(mContext);

        mBtManager = BluetoothManager.getInstance(mContext, null);

        // Get  Bluetooth adapter
        mBtAdapter = mBtManager.getAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(mContext, "Connected to " + mConnectionInfo.getDeviceName(), Toast.LENGTH_SHORT).show();
    }

    public void finalize() {
        // Stop the bluetooth session
        if (mBtManager != null) {
            //mBtManager.stop();
            mBtManager.setHandler(null);
        }
        mBtManager = null;
        mContext = null;
        mConnectionInfo = null;
    }

}