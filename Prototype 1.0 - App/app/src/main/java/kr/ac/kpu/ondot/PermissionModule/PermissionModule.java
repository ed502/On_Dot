package kr.ac.kpu.ondot.PermissionModule;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import static android.content.Context.MODE_PRIVATE;

public class PermissionModule {

    private final String DEBUG_TYPE = "type";

    public static final int PERMISSION_ALLOWED = 0;
    public static final int PERMISSION_NOT_ALLOWED = 1;
    public static final int PERMISSION_NOT_CHECKED = 2;

    private Context context;
    private PermissionCancelListener permissionCancelListener;

    public PermissionModule(Context context) {
        this.context = context;

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
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
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
        SharedPreferences pref = context.getSharedPreferences("Check", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PERMISSION_CHECK","TRUE");
        editor.commit();
    }

    /**
     * application 권한 설정 화면으로 이동하는 함수
     */
    public void permissionSettingMove(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 권한 사용 여부 안내 화면 중지 함수
     */
    public void stopPermissionGuide(){
        Log.d(DEBUG_TYPE,"PermissionModule - stopPermissionGuide ");
    }


    /**
     * 권한 사용 여부 안내 화면 취소 함수
     */
    public void cancelPermissionGuide(){
        Log.d(DEBUG_TYPE,"PermissionModule - cancelPermissionGuide ");
    }

    /**
     * permission check 종료여부 확인을 위한 Listener 등록 함수
     */
    public void setPermissionCancelListener(PermissionCancelListener permissionCancelListener){
        this.permissionCancelListener = permissionCancelListener;
    }
}
