package kr.ac.kpu.ondot.Educate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import kr.ac.kpu.ondot.R;

public class EducateMain extends AppCompatActivity {

    private ViewPager mViewpager;
    private EduPagerAdapter mAdapter;
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.educate_main);

        mViewpager = findViewById(R.id.edu_viewpager);
        mAdapter = new EduPagerAdapter(getSupportFragmentManager());
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
                intent =  new Intent(EducateMain.this, EduFirst.class);
                startActivity(intent);
                break;
            case 1:
                intent =  new Intent(EducateMain.this, EduSecond.class);
                startActivity(intent);
                break;
            case 2:
                intent =  new Intent(EducateMain.this, EduThird.class);
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