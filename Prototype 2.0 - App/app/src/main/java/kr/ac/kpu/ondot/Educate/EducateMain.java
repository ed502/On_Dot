package kr.ac.kpu.ondot.Educate;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import kr.ac.kpu.ondot.CircleIndicator;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class EducateMain extends AppCompatActivity implements CustomTouchEventListener {
    private final String DEBUG_TYPE = "type";

    private CircleIndicator circleIndicator;

    private ViewPager mViewpager;
    private EduPagerAdapter mAdapter;
    private int maxPage;
    private int currentView;

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.educate_main);

        circleIndicator = findViewById(R.id.edu_circleIndicator);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        linearLayout = findViewById(R.id.edu_layout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(customTouchConnectListener != null){
                    customTouchConnectListener.touchEvent(motionEvent);
                }
                return true;
            }
        });

        initDisplaySize();
        initTouchEvent();


        mViewpager = findViewById(R.id.edu_viewpager);
        mAdapter = new EduPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        mViewpager.setClipToPadding(false);
        maxPage = mAdapter.getCount() - 1;
        circleIndicator.setItemMargin(5);
        circleIndicator.createDotPanel(mAdapter.getCount(), R.drawable.indicator_dot_off, R.drawable.indicator_dot_on);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentView = position;
                circleIndicator.selectDot(position);
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

    private void initTouchEvent() {
        customTouchConnectListener = new CustomTouchEvent(this, this);
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
    public void onOneFingerFunction(final FingerFunctionType fingerFunctionType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fingerFunctionType == FingerFunctionType.RIGHT){ //오른쪽에서 왼쪽으로 스크롤
                    if(currentView<maxPage)
                        mViewpager.setCurrentItem(currentView+1);
                    else
                        mViewpager.setCurrentItem(currentView);
                }
                else if(fingerFunctionType == FingerFunctionType.LEFT){ //왼쪽에서 오른쪽으로 스크롤
                    if(currentView>0)
                        mViewpager.setCurrentItem(currentView-1);
                    else
                        mViewpager.setCurrentItem(currentView);
                }
            }
        });

        Log.d(DEBUG_TYPE,"MainActivity - fingerFunctionType : " + fingerFunctionType);
        if(fingerFunctionType == FingerFunctionType.ENTER){
            activitySwitch(currentView);

            Log.d(DEBUG_TYPE,"MainActivity - fingerFunctionType : " + fingerFunctionType);
        }
    }

    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType){
            case BACK:
                onBackPressed();
                break;
            case SPECIAL:
                Toast.makeText(this,"SPECIAL",Toast.LENGTH_SHORT).show();
                break;
            case NONE:
                Toast.makeText(this,"NONE",Toast.LENGTH_SHORT).show();
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
}