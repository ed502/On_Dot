package kr.ac.kpu.ondot.Quiz;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
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
import kr.ac.kpu.ondot.Translate.TranslateResult;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class QuizThird extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";
    private ArrayList<DotVO> list;
    private DotVO data;
    private ArrayList<Integer> id, type;
    private ArrayList<String> word, dot, raw_id;
    private LinearLayout linearLayout;
    private int[] random;
    private int dataCount = 0, scrollCount = 0, answerCheck = 0, randomIndex = 0, answerCount = 0, wronganswerCount = 0;
    private String answer = "";
    private String voiceRaw_id = "";
    private CustomTouchConnectListener customTouchConnectListener;

    private VoicePlayerModuleManager voicePlayerModuleManager;
    private Vibrator vibrator;
    private VibratorPattern pattern;

    private TextView textData;
    private LinearLayout[] circle;
    private int currentLocation = 0, quizLocation = 0;

    // Bluetooth
    private Context mContext;
    private BluetoothManager mBtManager = null;
    private BluetoothAdapter mBtAdapter = null;
    private ConnectionInfo mConnectionInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_third);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        pattern = new VibratorPattern();
        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        initDisplaySize();
        initTouchEvent();
        initVoicePlayer();

        mContext = getApplicationContext();
        initBlue();

        random = new int[10];

        getData();
        for (int i = 0; i < 10; i++) {
            random[i] = (int) (Math.random() * list.size());
            for (int j = 0; j < i; j++) {
                if (random[j] == random[i]) {
                    i--;
                    break;
                }
            }
            Log.i("randomValue", "" + random[i]);
        }
        setDot();
        checkData(random[randomIndex]);

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
                if (dataCount == 8) {
                    voicePlayerModuleManager.start(R.raw.not_insert_dot);
                    Toast.makeText(getApplicationContext(), "더 이상 입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (fingerFunctionType == FingerFunctionType.UP || fingerFunctionType == FingerFunctionType.DOWN || fingerFunctionType == FingerFunctionType.RIGHT) {
                        int i = scrollCount % 6;
                        if (fingerFunctionType == FingerFunctionType.UP) {
                            switch (i) {
                                case 0:
                                    voicePlayerModuleManager.start(R.raw.dot_1);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 1:
                                    voicePlayerModuleManager.start(R.raw.dot_2);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 2:
                                    voicePlayerModuleManager.start(R.raw.dot_3);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 3:
                                    voicePlayerModuleManager.start(R.raw.dot_4);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 4:
                                    voicePlayerModuleManager.start(R.raw.dot_5);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 5:
                                    voicePlayerModuleManager.start(R.raw.dot_6);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), scrollCount + 1 + "번째 입력되었습니다(업)", Toast.LENGTH_SHORT).show();
                            vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                            scrollCount++;
                        } else if (fingerFunctionType == FingerFunctionType.DOWN) {
                            switch (i) {
                                case 0:
                                    voicePlayerModuleManager.start(R.raw.dot_1);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 1:
                                    voicePlayerModuleManager.start(R.raw.dot_2);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 2:
                                    voicePlayerModuleManager.start(R.raw.dot_3);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 3:
                                    voicePlayerModuleManager.start(R.raw.dot_4);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 4:
                                    voicePlayerModuleManager.start(R.raw.dot_5);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 5:
                                    voicePlayerModuleManager.start(R.raw.dot_6);
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                            }
                            Toast.makeText(getApplicationContext(), scrollCount + "번째 입력되었습니다(다운)", Toast.LENGTH_SHORT).show();
                            vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                            scrollCount++;
                        } else if (fingerFunctionType == FingerFunctionType.RIGHT) {
                            if (scrollCount > 0) {
                                if (scrollCount % 6 == 0) {
                                    scrollCount--;
                                    dataCount--;
                                    voicePlayerModuleManager.start(R.raw.delete);
                                    answer = answer.substring(0, scrollCount);
                                    Toast.makeText(getApplicationContext(), scrollCount + 1 + "번째 취소되었습니다(왼쪽)", Toast.LENGTH_SHORT).show();
                                    for (int j = 0; j < 5; j++) {
                                        if (answer.substring((scrollCount / 6) * 6 + j, (scrollCount / 6) * 6 + j + 1).equals("1")) {
                                            circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                        } else if (answer.substring((scrollCount / 6) * 6 + j, (scrollCount / 6) * 6 + j + 1).equals("2")) {
                                            circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                        }
                                    }
                                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                                } else {
                                    scrollCount--;
                                    voicePlayerModuleManager.start(R.raw.delete);
                                    Toast.makeText(getApplicationContext(), scrollCount + 1 + "번째 취소되었습니다(왼쪽)", Toast.LENGTH_SHORT).show();
                                    i = scrollCount % 6;
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                                    answer = answer.substring(0, scrollCount);
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
                            Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_SHORT).show();
                        }

                    }else if (fingerFunctionType != FingerFunctionType.UP && fingerFunctionType != FingerFunctionType.DOWN && fingerFunctionType != FingerFunctionType.RIGHT && fingerFunctionType != FingerFunctionType.ENTER) {
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                        voicePlayerModuleManager.start(R.raw.reinput);
                        Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                if (fingerFunctionType == FingerFunctionType.ENTER) {
                    if (scrollCount % 6 == 0 && scrollCount > 1) {
                        voicePlayerModuleManager.start(R.raw.submit);
                        answerCheckFunc();
                        vibrator.vibrate(pattern.getVibrateEnterPattern(), -1);
                    } else {
                        vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                        voicePlayerModuleManager.start(R.raw.not_dot_input);
                        Toast.makeText(getApplicationContext(), "점자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }/*else if(fingerFunctionType != FingerFunctionType.LONG){
                    vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                    voicePlayerModuleManager.start(voiceRaw_id);
                }*/
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
                /*if (scrollCount % 6 == 0 && dataCount<8) {
                    answer = answer + "111111";
                    scrollCount = scrollCount + 6;
                    vibrator.vibrate(pattern.getVibrateSpecialPattern(), -1);
                    Toast.makeText(this, "6개 빈칸", Toast.LENGTH_SHORT).show();
                } else {
                    vibrator.vibrate(pattern.getVibrateErrorPattern(), -1);
                    Toast.makeText(this, "입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                }*/
                vibrator.vibrate(pattern.getVibrateNormalPattern(), -1);
                voicePlayerModuleManager.start(voiceRaw_id);
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
            URL url = new URL("http://15.165.135.160/Quiz3");

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

    private void setDot() {
        linearLayout = findViewById(R.id.quiz_third_layout);
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
        textData = findViewById(R.id.quiz3_text);

        circle = new LinearLayout[6];

        circle[0] = findViewById(R.id.quiz3_circle1);
        circle[1] = findViewById(R.id.quiz3_circle2);
        circle[2] = findViewById(R.id.quiz3_circle3);
        circle[3] = findViewById(R.id.quiz3_circle4);
        circle[4] = findViewById(R.id.quiz3_circle5);
        circle[5] = findViewById(R.id.quiz3_circle6);
    }

    public void checkData(int randomIndex) {
        setContentView(R.layout.quiz_third);
        setDot();
        textData.setText(list.get(randomIndex).getWord());

        voiceRaw_id = list.get(randomIndex).getWord();

        /*if(voicePlayerModuleManager.checkTTSPlaying() == false){
            //voicePlayerModuleManager.allStop();
            voicePlayerModuleManager.start(voiceRaw_id);
        }*/
        voicePlayerModuleManager.allStop();
        voicePlayerModuleManager.start(voiceRaw_id);
    }

    public void answerCheckFunc() {
        answerCheck = 1;
        if (list.get(random[randomIndex]).getDot().equals(answer)) {
            answer = "정답입니다";
            voicePlayerModuleManager.start(R.raw.correct);
            answerCount++;
        } else {
            answer = "오답입니다";
            voicePlayerModuleManager.start(R.raw.wrong);
            Log.d(DEBUG_TYPE, "answer : "+ list.get(random[randomIndex]).getDot());
            sendData(list.get(random[randomIndex]).getDot());

            String url = "http://15.165.135.160/test.jsp";
            NetworkTask networkTask = new NetworkTask(url, null);
            networkTask.execute();
            wronganswerCount++;
        }
        Intent intent = new Intent(getApplicationContext(), TranslateResult.class);
        intent.putExtra("data", answer);
        startActivity(intent);
        answer = "";
        if (randomIndex < 9)
            randomIndex++;
        checkData(random[randomIndex]);
        scrollCount = 0;
        dataCount = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //output.setText
        }
    }

    public class RequestHttpURLConnection {


        public String request(String _url, ContentValues _params) {
            HttpURLConnection urlConn = null;
            StringBuffer sbParams = new StringBuffer();

            if (_params == null)
                sbParams.append("");
            else {
                boolean isAnd = false;
                String key;
                String value;

                for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                    key = parameter.getKey();
                    value = parameter.getValue().toString();

                    if (isAnd)
                        sbParams.append("&");
                    sbParams.append(key).append("=").append(value);

                    if (!isAnd)
                        if (_params.size() >= 2)
                            isAnd = true;
                }
            }
            try {
                URL url = new URL(_url);
                urlConn = (HttpURLConnection) url.openConnection();

                urlConn.setDefaultUseCaches(false);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Accept-Charset", "UTF-8");
                urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                StringBuffer buffer = new StringBuffer();
                buffer.append("id").append("=").append(list.get(random[randomIndex]).getId());

                Log.i("boardWrite", buffer.toString());

                OutputStreamWriter os = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(os);
                writer.write(buffer.toString());
                writer.flush();

                if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return null;

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

                String line;
                String page = "";

                while ((line = reader.readLine()) != null) {
                    page += line + "\n";
                }
                return page;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }
            return null;
        }
    }

    public void sendData(String str) {
        mBtManager.write(str.getBytes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendData("2222222222222222222222222222222222222222222222222");
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

        Toast.makeText(mContext,"Connected to " + mConnectionInfo.getDeviceName(), Toast.LENGTH_SHORT).show();
    }
}