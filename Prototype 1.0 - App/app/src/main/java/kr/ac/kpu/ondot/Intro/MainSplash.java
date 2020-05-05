package kr.ac.kpu.ondot.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.kpu.ondot.R;

public class MainSplash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash);

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        startLoading();

    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainIntro.class));
                finish();
            }
        },3000);
    }

}
