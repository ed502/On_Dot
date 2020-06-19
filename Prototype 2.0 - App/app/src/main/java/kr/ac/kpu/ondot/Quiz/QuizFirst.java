package kr.ac.kpu.ondot.Quiz;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Data.DotVO;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.Translate.TranslateResult;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;


public class QuizFirst extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";
    private ArrayList<DotVO> list;
    private DotVO data;
    private ArrayList<Integer> id, type;
    private ArrayList<String> word, dot, raw_id;
    private LinearLayout linearLayout;
    private int[] random;
    private int dataCount = 0, scrollCount = 0, answerCheck = 0, randomIndex=0, answerCount=0, wronganswerCount=0;
    private String answer = "";
    private String voiceRaw_id="";
    private CustomTouchConnectListener customTouchConnectListener;

    private VoicePlayerModuleManager voicePlayerModuleManager;

    private TextView textData;
    private LinearLayout[] circle;
    private int currentLocation = 0, quizLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_first);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        initDisplaySize();
        initTouchEvent();
        initVoicePlayer();

        random = new int[10];

        getData();
        for(int i=0; i<10; i++){
            random[i]=(int)(Math.random()*list.size());
            for(int j=0; j<i; j++){
                if(random[j]==random[i]){
                    i--;
                    break;
                }
            }
            Log.i("randomValue",""+random[i]);
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
                voicePlayerModuleManager.start(fingerFunctionType);
                if (dataCount == 8) {
                    Toast.makeText(getApplicationContext(), "더 이상 입력할 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (fingerFunctionType == FingerFunctionType.UP || fingerFunctionType == FingerFunctionType.DOWN) {
                        int i = scrollCount % 6;
                        if (fingerFunctionType == FingerFunctionType.UP) {
                            switch (i) {
                                case 0:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 1:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 2:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 3:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 4:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
                                case 5:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer + "2";
                                    break;
/*                                case 6:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer+"2";
                                    break;
                                case 7:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer+"2";
                                    break;
                                case 8:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer+"2";
                                    break;
                                case 9:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer+"2";
                                    break;
                                case 10:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer+"2";
                                    break;
                                case 11:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
                                    answer = answer+"2";
                                    break;*/
                            }
                            Toast.makeText(getApplicationContext(), scrollCount + 1 + "번째 입력되었습니다(업)", Toast.LENGTH_SHORT).show();
                            scrollCount++;
                        } else if (fingerFunctionType == FingerFunctionType.DOWN) {
                            switch (i) {
                                case 0:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 1:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 2:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 3:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 4:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
                                case 5:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer + "1";
                                    break;
/*                                case 6:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer+"1";
                                    break;
                                case 7:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer+"1";
                                    break;
                                case 8:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer+"1";
                                    break;
                                case 9:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer+"1";
                                    break;
                                case 10:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer+"1";
                                    break;
                                case 11:
                                    circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                                    answer = answer+"1";
                                    break;*/
                            }
                            Toast.makeText(getApplicationContext(), scrollCount + 1 + "번째 입력되었습니다(다운)", Toast.LENGTH_SHORT).show();
                            scrollCount++;
                        }
                        if (scrollCount % 6 == 0) {
                            for (int j = 0; j < 6; j++) {
                                circle[j].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
                            }
                            dataCount++;
                            //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                        }

                    } else if (fingerFunctionType != FingerFunctionType.UP && fingerFunctionType != FingerFunctionType.DOWN) {
                        Toast.makeText(getApplicationContext(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }

                /*if ((fingerFunctionType == FingerFunctionType.RIGHT) && currentLocation < list.size() - 1 && answerCheck == 1) { //오른쪽에서 왼쪽으로 스크롤
                    answerCheck = 0;
                    currentLocation++;
                    checkData();
                }
                if ((fingerFunctionType == FingerFunctionType.LEFT) && currentLocation > 0 && answerCheck == 1) { //왼쪽에서 오른쪽으로 스크롤
                    answerCheck = 0;
                    currentLocation--;
                    checkData();
                }*/


                if (fingerFunctionType == FingerFunctionType.ENTER) {
                    if (scrollCount % 6 == 0 && scrollCount > 1) {
                        answerCheckFunc();
                    } else {
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
                if(scrollCount%6==0) {
                    if(scrollCount==0){
                        answer = answer + "111111";
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
            URL url = new URL("http://15.165.135.160/DotInitial");

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
        linearLayout = findViewById(R.id.quiz_first_layout);
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
        textData = findViewById(R.id.quiz1_text);

        circle = new LinearLayout[6];

        circle[0] = findViewById(R.id.quiz1_circle1);
        circle[1] = findViewById(R.id.quiz1_circle2);
        circle[2] = findViewById(R.id.quiz1_circle3);
        circle[3] = findViewById(R.id.quiz1_circle4);
        circle[4] = findViewById(R.id.quiz1_circle5);
        circle[5] = findViewById(R.id.quiz1_circle6);
    }

    public void checkData(int randomIndex) {
        setContentView(R.layout.quiz_first);
        setDot();
        textData.setText(list.get(randomIndex).getWord());

        voiceRaw_id = list.get(randomIndex).getRaw_id();

    }

    public void answerCheckFunc() {
        answerCheck = 1;
        if (list.get(random[randomIndex]).getDot().equals(answer)) {
            answer = "정답입니다";
            answerCount++;
        } else {
            answer = "오답입니다";
            wronganswerCount++;
        }
        Intent intent = new Intent(getApplicationContext(), TranslateResult.class);
        intent.putExtra("data", answer);
        startActivity(intent);
        answer = "";
        if(randomIndex<9)
            randomIndex++;
        checkData(random[randomIndex]);
        scrollCount = 0;
        dataCount = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        voicePlayerModuleManager.start(voiceRaw_id);
    }
}
