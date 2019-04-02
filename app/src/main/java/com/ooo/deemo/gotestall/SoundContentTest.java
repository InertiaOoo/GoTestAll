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

import com.dfzt.dfzt_radio.RadioAidl;
import com.dfzt.dfzt_radio.callback.IESTRadioServiceCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class SoundContentTest extends AppCompatActivity {
    static final private String SOUNDCONTTAG = "有声内容";
    static final private int MILLIS = 1000;
    public static List<TestLog> tl_List = new ArrayList<>();
    public static RecycleAdapter rAdapter;

    private RecyclerView rv_log;

    private RadioAidl radioAidl;

    private List<Album> albumList = new ArrayList<>();

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
            radioAidl.unRegisterSearchListener(iestRadioServiceCallBack);


        } catch (RemoteException e) {
            e.printStackTrace();
        }

        unbindService(soundcontConnect);

    }


    //初始化RecycleView内容
    private void initList() {
        tl_List.clear();
        TestLog tlog1 = new TestLog("location:", "action");
        tl_List.add(tlog1);
    }

    private IESTRadioServiceCallBack iestRadioServiceCallBack = new IESTRadioServiceCallBack() {
        @Override
        public void onXmlySearchByAlbumName(boolean success, List<Album> albums, int totalPage, int currentPage) throws RemoteException {

        }

        @Override
        public void onXmlySearchExactlyCompleted(boolean success, List<Track> tracks) throws RemoteException {

        }

        @Override
        public void onXmlySearchTag(boolean success, String category, String tag) throws RemoteException {

        }

        @Override
        public void onXmlyError(int errorCode, String errorString) throws RemoteException {

        }

        @Override
        public void getNextIndexTrackList(List<Track> list) throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    private void startTestSound() {
        Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.dfzt_radio");
        if (intent_tolocal != null) {
            startActivity(intent_tolocal);
        } else {
            // 未安装应用时
            Toast.makeText(getApplicationContext(), "未安装有声内容", Toast.LENGTH_LONG).show();
        }

        startSoundService();
    }

    private ServiceConnection soundcontConnect = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 从连接中获取Stub对象
            radioAidl = RadioAidl.Stub.asInterface(iBinder);
            try {
                radioAidl.registerSearchListener(iestRadioServiceCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            soundcontTestcase();    //执行有声内容测试用例
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开连接
            radioAidl = null;

        }
    };


    //有声内容服务
    private void startSoundService() {
        final Intent soundcontIntent = new Intent();
        soundcontIntent.setAction("com.dfzt.audio_content.service");
        soundcontIntent.setPackage("com.dfzt.dfzt_radio");
        bindService(soundcontIntent, soundcontConnect, Context.BIND_AUTO_CREATE);

    }


    private void soundcontTestcase() {
        try {
            int order = 1;          //步骤数

            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(SOUNDCONTTAG + order++ + "——", "搜索"));
            albumList = radioAidl.searchAudioContentAlbum("大自然的下雨声");
            Thread.sleep(MILLIS);
            radioAidl.playAudioContentVoice(albumList.get(0));
            tl_List.add(new TestLog("-after playAudioContentVoice:getCurrentAlbumId:", radioAidl.getCurrentAlbumId()));
//            rAdapter.notifyItemChanged(tl_List.size()-1);
            Thread.sleep(MILLIS);


            rAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
