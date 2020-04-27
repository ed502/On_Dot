package kr.ac.kpu.ondot.BluetoothModule;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import kr.ac.kpu.ondot.R;


/*
* 다시 수정 필요
* */
public class BluetoothModule {
    private Context context;

    private BluetoothSPP bt;

    public BluetoothModule(Context context) {
        this.context = context;
        bt = new BluetoothSPP(context);
    }


    // 연결 시도하는 ?
    public void btConnect(){
        if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
            bt.disconnect();
        } else {
            Intent intent = new Intent(context, DeviceList.class);
            //startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    // 데이터 전송
    public void btSend(String data){
        bt.send(data,true);
    }

    // 사용 가능 여부?
    public boolean btCheck(){
        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(context, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }

    public void onDestroy(){
        bt.stopService();
    }

    /*public void onStart(){
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {
        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("110010", true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }*/
}
