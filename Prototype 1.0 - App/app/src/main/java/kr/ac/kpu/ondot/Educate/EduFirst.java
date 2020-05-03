package kr.ac.kpu.ondot.Educate;

import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
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

import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Data.DotVO;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;

public class EduFirst extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";
    private LinearLayout linearLayout;
    private CustomTouchConnectListener customTouchConnectListener;
    private TextView textData;
    private LinearLayout[] circle;
    private ArrayList<DotVO> list;
    private DotVO data;
    private ArrayList<Integer> id, type;
    private ArrayList<String> word, dot;
    private int currentLocation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edu_first);

        linearLayout = findViewById(R.id.edu_first_layout);
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

        getData();

        circle = new LinearLayout[6];
        circle[0] = findViewById(R.id.edu1_circle1);
        circle[1] = findViewById(R.id.edu1_circle2);
        circle[2] = findViewById(R.id.edu1_circle3);
        circle[3] = findViewById(R.id.edu1_circle4);
        circle[4] = findViewById(R.id.edu1_circle5);
        circle[5] = findViewById(R.id.edu1_circle6);
        textData = findViewById(R.id.edu1_text);
        checkData();


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
                if ((fingerFunctionType == FingerFunctionType.RIGHT) && currentLocation < 13) { //오른쪽에서 왼쪽으로 스크롤
                    currentLocation++;
                    checkData();
                } else if ((fingerFunctionType == FingerFunctionType.LEFT) && currentLocation > 0) { //왼쪽에서 오른쪽으로 스크롤
                    currentLocation--;
                    checkData();
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

        list = new ArrayList<DotVO>();
        boolean bId = false, bWord = false, bDot = false, bType = false;
        try {
            URL url = new URL("http://15.165.135.160/DotXml");

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
            data.setType(type.get(i));
            list.add(data);
        }
    }

    public void checkData() {
        String dotData = list.get(currentLocation).getDot();
        textData.setText(list.get(currentLocation).getWord());
        for (int i = 0; i < dotData.length(); i++) {
            if ((int) dotData.charAt(i) == (int) '1') {
                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle));
            } else if ((int) dotData.charAt(i) == (int) '2') {
                circle[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.stroke_circle2));
            }
        }

    }
}
