package kr.ac.kpu.ondot.PermissionModule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.INVISIBLE;

public class PermissionModule {

    private final String DEBUG_TYPE = "type";

    public static final int PERMISSION_ALLOWED = 0;
    public static final int PERMISSION_NOT_ALLOWED = 1;
    public static final int PERMISSION_NOT_CHECKED = 2;
    private static final int PERMISSIONS = 101;

    private Context context;
    private Activity activity;

    private PermissionCancelListener permissionCancelListener;

    private View view;
    private ImageView permissionGuideBackGround, permissionGuideImage;

    private TimerTask permissionCheckTimerTask;
    private Timer permissionCheckTimer;
    private int permissionCheckTime = 0;

    private Resources res;

    private String[] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    public PermissionModule(Context context, View view, Activity activity) {
        this.context = context;
        this.view = view;
        this.res = context.getResources();
        this.activity = activity;
    }




    private void checkPermissionView() {

        if(permissionGuideBackGround == null){
            permissionGuideBackGround = new ImageView(context);
            FrameLayout.LayoutParams backGroundLayoutParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            );

            permissionGuideBackGround.setLayoutParams(backGroundLayoutParam);
            permissionGuideBackGround.setBackgroundColor(Color.parseColor("#ee171a2d"));
            ((FrameLayout) view.getParent()).addView(permissionGuideBackGround);
            permissionGuideBackGround.setVisibility(INVISIBLE);
        }

        if(permissionGuideImage == null) {
            permissionGuideImage = new ImageView(context);
            FrameLayout.LayoutParams guideLayoutParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );

            guideLayoutParam.width = (int) (Screen.displayY * 0.65);
            guideLayoutParam.height = (int) (Screen.displayY * 0.65) * 2;
            permissionGuideImage.setLayoutParams(guideLayoutParam);
            ((FrameLayout) view.getParent()).addView(permissionGuideImage);
            permissionGuideImage.setVisibility(INVISIBLE);
        }
    }


    /**
     * permission check 함수
     * 음성인식을 활용하는 메뉴 접속 시 권한 사용 여부를 사용자에게 안내한적이 있는지 확인하는 함수
     */
    public int checkPermission() {
        SharedPreferences pref = context.getSharedPreferences("Check", MODE_PRIVATE);
        String checkPref = pref.getString("PERMISSION_CHECK", "FALSE");
        if(checkPref == "TRUE" || checkPref.equals("TRUE") ){
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context,Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){

                return PERMISSION_NOT_ALLOWED;
            }else
                return PERMISSION_ALLOWED;
        }else{
            return PERMISSION_NOT_CHECKED;
        }
    }



    /**
     * 권한설정을 허용하였을 때의 함수
     */
    public void allowedPermission(){
        stopPermissionGuide();

        SharedPreferences pref = context.getSharedPreferences("Check", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PERMISSION_CHECK","TRUE");
        editor.commit();
    }

    /**
     * application 권한 설정 화면으로 이동하는 함수
     */
    public void permissionSettingMove(){
        stopPermissionGuide();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 권한 사용 안내 화면 출력 함수
     * @param permissionCheckResult
     */
    public void startPermissionGuide(int permissionCheckResult){
        int drawableId = 0, rawid = 0;

        if(permissionCheckResult == PERMISSION_NOT_CHECKED){
            drawableId = R.drawable.permission_not_check;
            rawid = 1;
            Log.d(DEBUG_TYPE,"PermissionModule - 권한 체크 안됨 ");

        }else if(permissionCheckResult == PERMISSION_NOT_ALLOWED){
            drawableId = R.drawable.permission_not_allowed;
            rawid = 2;
            Log.d(DEBUG_TYPE,"PermissionModule - 권한 체크 거부 ");
        }

        // allow 한 경우
        if(drawableId != 0 && rawid != 0){
            checkPermissionView();
            recyclePermissionImage();
            permissionGuideBackGround.setVisibility(View.VISIBLE);
            permissionGuideImage.setImageDrawable(getDrawableImage(drawableId, permissionGuideImage.getLayoutParams().width, permissionGuideImage.getLayoutParams().height));
            permissionGuideImage.setVisibility(View.VISIBLE);
            ActivityCompat.requestPermissions(activity,permissions,PERMISSIONS);
            //  음성 들어갈 곳
            permissionCheckWaitThreadStart();

            Log.d(DEBUG_TYPE,"PermissionModule - 권한 체크 허용 ");
        }

    }



    /**
     * 권한 사용 여부 안내 화면 중지 함수
     */
    public void stopPermissionGuide(){
        Log.d(DEBUG_TYPE,"PermissionModule - stopPermissionGuide ");
        permissionCheckWaitThreadStop();
        //recyclePermissionImage();
        if(permissionGuideBackGround != null){
            permissionGuideBackGround.setVisibility(INVISIBLE);
        }
    }


    /**
     * 권한 사용 여부 안내 화면 취소 함수
     */
    public void cancelPermissionGuide(){
        stopPermissionGuide();

        Log.d(DEBUG_TYPE,"PermissionModule - cancelPermissionGuide ");
    }

    /**
     * permission check 종료여부 확인을 위한 Listener 등록 함수
     */
    public void setPermissionCancelListener(PermissionCancelListener permissionCancelListener){
        this.permissionCancelListener = permissionCancelListener;
    }

    /**
     * 이미지 메모리 해제 함수
     */
    private void recyclePermissionImage(){
        if(permissionGuideImage != null) {
            Drawable image = permissionGuideImage.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            permissionGuideImage.setImageDrawable(null);
        }
    }


    /**
     * 5초간의 대기 시간 안에 더블 탭이 발동되면 권한 사용 메뉴접속을 허용한다.
     * 5초간의 대기 시간 안에 더블 탭이 발동되지 않으면  실행하지 않는다.
     */
    private void permissionCheckWaitThreadStart(){
        if (permissionCheckTimerTask == null) {
            permissionCheckTimerTask = new TimerTask() {
                @Override
                public void run() {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (permissionCheckTime == 5) {
                                permissionCheckTime = 0;
                                permissionCheckWaitThreadStop();
                                cancelPermissionGuide();
                                if(permissionCancelListener != null)
                                    permissionCancelListener.permissionCancel();
                                permissionCancelListener = null;
                            } else {
                                permissionCheckTime++;
                                // mediaSoundManager.start(R.raw.clock);
                            }
                        }
                    });
                }
            };
            permissionCheckTimer = new Timer();
            permissionCheckTimer.schedule(permissionCheckTimerTask, 0, 1000);
        }
    }

    /**
     * 권한 확인을 위해 5초간의 대기를 하는 스레드를 초기화 하는 함수
     */
    private void permissionCheckWaitThreadStop(){
        permissionCheckTime = 0;

        if(permissionCheckTimerTask != null) {
            permissionCheckTimerTask.cancel();
            permissionCheckTimerTask = null;
        }

        if(permissionCheckTimer != null){
            permissionCheckTimer.cancel();
            permissionCheckTimer = null;
        }

        // mediaSoundManager.stop();
    }

    // Drawable 형태의 이미지를 얻는 메소드
    public Drawable getDrawableImage(int raw_id, int width, int height){
        return new BitmapDrawable(res, getBitmapImage(raw_id, width, height));
    }

    // Bitmap형태의 이미지를 resize하여 반환받는 메소드
    public Bitmap getBitmapImage(int res_id, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 실제로 이미지를 로드하지 않음
        BitmapFactory.decodeResource(res, res_id, options);
        options.inSampleSize = getInSampleSize(options, width, height);
        options.inJustDecodeBounds = false; // 이미지를 실제로 로드

        return BitmapFactory.decodeResource(res, res_id, options);
    }

    // 이미지 용량을 몇배로 줄일건지 결정하는 함수
    private int getInSampleSize(BitmapFactory.Options options, int real_width, int real_height){
        int inSampleSize = 1; // 따로 지정하지 않는다면 기본적으로 1
        int virtual_height = options.outHeight;
        int virtual_width = options.outWidth;

        if(virtual_height > real_height || virtual_width > real_width){
            while((virtual_height/inSampleSize)>real_height && (virtual_width/inSampleSize)>real_width){
                inSampleSize *= 2; //2의 지수만큼 해상도를 줄일 때 속도가 빠름
            }
        }

        return inSampleSize;
    }
}
