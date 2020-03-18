package kr.ac.kpu.ondot.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import kr.ac.kpu.ondot.R;

public class QuizMain extends AppCompatActivity {

    private ViewPager mViewpager;
    private QuizPagerAdapter mAdapter;
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main);

        mViewpager = findViewById(R.id.quiz_viewpager);
        mAdapter = new QuizPagerAdapter(getSupportFragmentManager());
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

    public void activitySwitch(int currentView) {
        Intent intent;
        switch (currentView) {
            case 0:
                intent =  new Intent(QuizMain.this, QuizFirst.class);
                startActivity(intent);
                break;
            case 1:
                intent =  new Intent(QuizMain.this, QuizSecond.class);
                startActivity(intent);
                break;
            case 2:
                intent =  new Intent(QuizMain.this, QuizThird.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                activitySwitch(currentView);
                break;
        }

        return false;
    }
}