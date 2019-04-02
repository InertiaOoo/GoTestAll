package com.ooo.deemo.gotestall;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dfzt.bluetooth.IBluetoothService;
import com.dfzt.bluetooth.callback.IBluetoothStateCallback;


import java.util.ArrayList;
import java.util.List;

public class BlueToothTest extends AppCompatActivity {

    static final private String SOUNDCONTTAG = "蓝牙";
    static final private int MILLIS = 1000;
    public static List<TestLog> tl_List = new ArrayList<>();
    public static RecycleAdapter rAdapter;


    private RecyclerView rv_log;

    private IBluetoothService iBluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_content_test);
        initList();
        rv_log = findViewById(R.id.rv_log);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_log.setLayoutManager(layoutManager);
        rv_log.setFocusableInTouchMode(true);
        rAdapter = new RecycleAdapter(tl_List);

        rv_log.setAdapter(rAdapter);

        startTestSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            iBluetoothService.unRegisterBluetoothStateCallback(iBluetoothStateCallback);


        } catch (RemoteException e) {
            e.printStackTrace();
        }

        unbindService(bluetoothConnect);

    }


    //初始化RecycleView内容
    private void initList() {
        tl_List.clear();
        TestLog tlog1 = new TestLog("location:", "action");
        tl_List.add(tlog1);
    }

    private IBluetoothStateCallback iBluetoothStateCallback = new IBluetoothStateCallback() {
        @Override
        public void onBluetoothPlayState(boolean isPlaying) throws RemoteException {

        }

        @Override
        public void onBluetoothConnectState(int state, String name) throws RemoteException {

        }

        @Override
        public void onBluetoothOpenState(boolean opened) throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };


    private void startTestSound() {
        Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.bluetooth");
        if (intent_tolocal != null) {
            startActivity(intent_tolocal);
        } else {
            // 未安装应用时
            Toast.makeText(getApplicationContext(), "未安装蓝牙", Toast.LENGTH_LONG).show();
        }

        startBlueService();
    }


    private ServiceConnection bluetoothConnect = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 从连接中获取Stub对象
            iBluetoothService = IBluetoothService.Stub.asInterface(iBinder);
            try {
                iBluetoothService.registerBluetoothStateCallback(iBluetoothStateCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            bluetoothTestcase();    //执行蓝牙测试用例
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开连接
            iBluetoothService = null;

        }
    };


    //蓝牙服务
    private void startBlueService() {
        final Intent soundcontIntent = new Intent();
        soundcontIntent.setAction("com.dfzt.blutooth.service");
        soundcontIntent.setPackage("com.dfzt.bluetooth");
        bindService(soundcontIntent, bluetoothConnect, Context.BIND_AUTO_CREATE);

    }


    private void bluetoothTestcase() {
        try {
            int order = 1;          //步骤数

            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(SOUNDCONTTAG + order++ + "——", "搜索"));
            iBluetoothService.play();
            tl_List.add(new TestLog("-after play:isPlaying:", "" + ""));

            Thread.sleep(MILLIS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}