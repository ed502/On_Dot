package kr.ac.kpu.ondot.Intro;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.kpu.ondot.BluetoothModule.BluetoothManager;
import kr.ac.kpu.ondot.BluetoothModule.ConnectionInfo;
import kr.ac.kpu.ondot.BluetoothModule.Constants;
import kr.ac.kpu.ondot.BluetoothModule.DeviceList;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchConnectListener;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEvent;
import kr.ac.kpu.ondot.CustomTouch.CustomTouchEventListener;
import kr.ac.kpu.ondot.CustomTouch.FingerFunctionType;
import kr.ac.kpu.ondot.Educate.EducateMain;
import kr.ac.kpu.ondot.EnumData.MenuType;
import kr.ac.kpu.ondot.Main.MainActivity;
import kr.ac.kpu.ondot.R;
import kr.ac.kpu.ondot.Screen;
import kr.ac.kpu.ondot.VoiceModule.VoicePlayerModuleManager;

public class EduIntro extends AppCompatActivity implements CustomTouchEventListener {

    private static final String TAG = "EducateIntro";

    private CustomTouchConnectListener customTouchConnectListener;
    private LinearLayout linearLayout;

    private MenuType menuType = MenuType.EDUCATE;
    private VoicePlayerModuleManager voicePlayerModuleManager;

    /**
     * Bluetooth setting
     */
    private Context mContext;
    private BluetoothManager mBtManager;
    private BluetoothAdapter mBtAdapter;
    private BtHandler mBtHandler;
    private ConnectionInfo mConnectionInfo;
    private int mBtStatus = BluetoothManager.STATE_NONE;
    private String mBtStatusString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edu_intro);

        mContext = getApplicationContext();

        //액티비티 전환 애니메이션 제거
        overridePendingTransition(0, 0);

        linearLayout = findViewById(R.id.edu_intro_layout);
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
        initVoicePlayer();
        initBlue();
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

    // tts 초기화
    private void initVoicePlayer() {
        voicePlayerModuleManager = new VoicePlayerModuleManager(getApplicationContext());
    }

    @Override
    public void onOneFingerFunction(final FingerFunctionType fingerFunctionType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fingerFunctionType == FingerFunctionType.ENTER && mBtManager.getState() != BluetoothManager.STATE_CONNECTED){
                    Toast.makeText(getApplicationContext(),"블루투스 연결요망",Toast.LENGTH_SHORT).show();
                    //안내 음성파일
                }
                else if (fingerFunctionType == FingerFunctionType.ENTER && mBtStatus==BluetoothManager.STATE_CONNECTED) { // 블루투스 연결이 되어있는 상태
                    startActivity(new Intent(getApplicationContext(), EducateMain.class));
                    finish();
                } else if (fingerFunctionType == FingerFunctionType.LONG) {
                    doScan();
                }
            }
        });
    }

    @Override
    public void onTwoFingerFunction(final FingerFunctionType fingerFunctionType) {
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
    public void onPermissionUseAgree() {

    }

    @Override
    public void onPermissionUseDisagree() {

    }

    private void initBlue() {
        // Make instances
        mBtHandler = new BtHandler();
        mConnectionInfo = ConnectionInfo.getInstance(mContext);

        // Get Bluetooth adapter
        if (mBtManager != null) {
            mBtAdapter = mBtManager.getAdapter();
        } else {
            mBtManager = BluetoothManager.getInstance(this, mBtHandler);
            mBtAdapter = mBtManager.getAdapter();
        }

        // If the adapter is null, then Bluetooth is not supported
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
        }

    }

    public void doScan() {
        mBtAdapter = mBtManager.getAdapter();
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
        } else {
            setupBT();
        }
    }

    public void setupBT() {
        // Initialize the BluetoothManager to perform bluetooth connections
        if (mBtManager == null)
            mBtManager = BluetoothManager.getInstance(this, mBtHandler);
        Intent intent = new Intent(EduIntro.this, DeviceList.class);
        startActivityForResult(intent, Constants.REQUEST_CONNECT_DEVICE);
    }

    public void finalize() {
        // Stop the bluetooth session
        if (mBtManager != null) {
            mBtManager.stop();
            mBtManager.setHandler(null);
        }
        mBtManager = null;
        mContext = null;
        mConnectionInfo = null;
    }

    private void showBtStatus() {
        switch (mBtStatus) {
            case BluetoothManager.STATE_NONE:
                mBtStatusString = "none";
                break;
            case BluetoothManager.STATE_LISTEN:
                mBtStatusString = "listening";
                break;
            case BluetoothManager.STATE_CONNECTING:
                mBtStatusString = "connecting";
                break;
            case BluetoothManager.STATE_CONNECTED:
                mBtStatusString = "connected";
                break;
        }

        Log.d(TAG, "mBtStatus : " + mBtStatusString);
    }

    /**
     * Initiate a connection to a remote device.
     *
     * @param address Device's MAC address to connect
     */
    private void connectDevice(String address) {
        Log.d(TAG, "Service - connect to " + address);

        if (mBtAdapter != null) {
            BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
            if (device != null && mBtManager != null) {
                mBtManager.connect(device);
            }
        }
    }

    /**
     * Send message to remote device using Bluetooth
     */
    private void sendMessageToRemote(String message) {
        sendMessageToDevice(message);
    }

    /**
     * Send message to device.
     *
     * @param message message to send
     */
    private void sendMessageToDevice(String message) {
        if (message == null || message.length() < 1)
            return;

        /*TransactionBuilder.Transaction transaction = mTransactionBuilder.makeTransaction();
        transaction.begin();
        transaction.setMessage(message);
        transaction.settingFinished();
        transaction.sendTransaction();*/

        // 보내기
        //mBtManager.write(message.getBytes());
        Log.d(TAG, "Message : " + message.getBytes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        voicePlayerModuleManager.start(menuType);

        if (mBtManager != null) {
            mBtStatus = mBtManager.getState();
            if (mBtHandler != null)
                mBtManager.setHandler(mBtHandler);
        }
        showBtStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    /*****************************************************
     *	Inner classes  ,  Handler , activity Result
     ******************************************************/

    /**
     * Receives result from external activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case Constants.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.e(TAG, "BT is not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceList.EXTRA_DEVICE_ADDRESS);
                    // Attempt to connect to the device
                    if (address != null) {
                        if (mConnectionInfo != null)
                            mConnectionInfo.setDeviceAddress(address);
                        connectDevice(address);
                    }
                }
                break;
        }    // End of switch(requestCode)
    }


    /**
     * Receives messages from bluetooth manager
     */
    class BtHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // Bluetooth state changed
                case BluetoothManager.MESSAGE_STATE_CHANGE:
                    // Bluetooth state Changed
                    Log.d(TAG, "Service - MESSAGE_STATE_CHANGE: " + msg.arg1);

                    switch (msg.arg1) {
                        case BluetoothManager.STATE_NONE:
                            mBtStatus = BluetoothManager.STATE_NONE;
                            showBtStatus();
                            break;
                        case BluetoothManager.STATE_LISTEN:
                            mBtStatus = BluetoothManager.STATE_LISTEN;
                            showBtStatus();
                            break;
                        case BluetoothManager.STATE_CONNECTING:
                            mBtStatus = BluetoothManager.STATE_CONNECTING;
                            showBtStatus();
                            break;
                        case BluetoothManager.STATE_CONNECTED:
                            mBtStatus = BluetoothManager.STATE_CONNECTED;
                            showBtStatus();
                            break;
                    }
                    break;

                // If you want to send data to remote
                case BluetoothManager.MESSAGE_WRITE:
                    break;

                // Received packets from remote
                case BluetoothManager.MESSAGE_READ:
                    /*Log.d(TAG, "BT - MESSAGE_READ: ");
                    byte[] readBuf = (byte[]) msg.obj;
                    int readCount = msg.arg1;
                    if(msg.arg1 > 0) {
                        String strMsg = new String(readBuf, 0, msg.arg1);
                        // parse string
                        if(strMsg.contains("b")) {
                            Intent intent = new Intent(MainActivity.this, VRDefenseActivity.class);
                            startActivity(intent);
                        } else if(strMsg.contains("c")) {
                        }
                    }*/
                    break;

                case BluetoothManager.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME: ");

                    // save connected device's name and notify using toast
                    String deviceAddress = msg.getData().getString(Constants.SERVICE_HANDLER_MSG_KEY_DEVICE_ADDRESS);
                    String deviceName = msg.getData().getString(Constants.SERVICE_HANDLER_MSG_KEY_DEVICE_NAME);

                    if (deviceName != null && deviceAddress != null) {
                        // Remember device's address and name
                        mConnectionInfo.setDeviceAddress(deviceAddress);
                        mConnectionInfo.setDeviceName(deviceName);

                        Toast.makeText(mContext,
                                "Connected to " + deviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case BluetoothManager.MESSAGE_TOAST:
                    Log.d(TAG, "BT - MESSAGE_TOAST: ");

                    Toast.makeText(mContext,
                            msg.getData().getString(Constants.SERVICE_HANDLER_MSG_KEY_TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;

            }    // End of switch(msg.what)

            super.handleMessage(msg);
        }
    }    // End of class MainHandler
}
