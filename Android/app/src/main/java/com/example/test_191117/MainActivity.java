package com.example.test_191117;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.test_191117.R;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private static int MAX_X;
    private static int MAX_Y;

    private LinearLayout layout;

    private static final int SWIPE_MIN_DISTANCE_Y=120;
    private static final int SWIPE_MIN_DISTANCE_X=120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.layout);
        //MAX_X = getScreenSize(this).x;
        //MAX_Y = getScreenSize(this).y;
        gestureScanner = new GestureDetector(this);
    }

    //액티비티 화면의 사이즈를 구하기 위해서 호출하는 함수
    //onCreate에서 x,y 값을 구할 수 없음
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        MAX_X = layout.getWidth();
        MAX_Y = layout.getHeight();
        Toast.makeText(getApplication(), MAX_X + "   " + MAX_Y, Toast.LENGTH_SHORT).show();
    }

    //스마트폰 전체 화면의 크기를 구하기 위해서 사용
    //size.x, size,y로 값을 가져올 수 있음
    /*public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureScanner.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(getApplication(), "싱글 터치", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(getApplication(), "롱 터치", Toast.LENGTH_SHORT).show();
        //진동 넣기
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float percentageX, percentageY, changeX, changeY, distanceX, distanceY;
        changeX = Math.abs(e1.getX() - e2.getX());
        changeY = Math.abs(e1.getY() - e2.getY());
        percentageX = (Math.abs(changeX) / MAX_X);
        percentageY = (Math.abs(changeY) / MAX_Y);
        distanceX = Math.abs(velocityX);
        distanceY = Math.abs(velocityY);

        try {
            if ( (changeX / changeY > 0.8 && changeX / changeY < 1.2) ) {
                Toast.makeText(getApplication(), "다시 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                //위로 드래그
                if ((e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE_Y) && (distanceY > SWIPE_THRESHOLD_VELOCITY) && (percentageX < percentageY)) {
                    Toast.makeText(getApplication(), "위로 드래그  " + percentageX + "  " + percentageY, Toast.LENGTH_SHORT).show();
                }

                //아래로 드래그
                if ((e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE_Y) && (distanceY > SWIPE_THRESHOLD_VELOCITY) && (percentageX < percentageY)) {
                    Toast.makeText(getApplication(), "아래로 드래그  " + percentageX + "  " + percentageY, Toast.LENGTH_SHORT).show();
                }

                //왼쪽으로 드래그
                if ((e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE_X) && (distanceX > SWIPE_THRESHOLD_VELOCITY) && (percentageX > percentageY)) {
                    Toast.makeText(getApplication(), "왼쪽으로 드래그  " + percentageX + "  " + percentageY, Toast.LENGTH_SHORT).show();
                }

                //오른쪽으로 드래그
                if ((e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE_X) && (distanceX > SWIPE_THRESHOLD_VELOCITY) && (percentageX > percentageY)) {
                    Toast.makeText(getApplication(), "오른쪽으로 드래그  " + percentageX + "  " + percentageY, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {

        }
        return true;
    }
}