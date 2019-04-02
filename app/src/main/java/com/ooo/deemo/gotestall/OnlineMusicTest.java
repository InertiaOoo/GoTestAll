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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dfzt.music.aidl.IPlayerProvider;
import com.dfzt.olinemusic.IOnlineMusicAidl;
import com.dfzt.olinemusic.callback.IPlayMusicStateListener;
import com.dfzt.olinemusic.callback.ISearchMusicCallBack;
import com.dfzt.olinemusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

public class OnlineMusicTest extends AppCompatActivity {
    static final private String ONLINEMUSICTAG = "在线音乐";
    static final private int MILLIS = 1000;
    public static List<TestLog> tl_List = new ArrayList<>();
    public static RecycleAdapter rAdapter;
    private static IOnlineMusicAidl iOnlineMusicAidl;
    private List<Music> musicList = new ArrayList<>();


    private static Button bt_stopser;
    private RecyclerView rv_log;
    private Button bt_testonline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music_test);
        initList();
        rv_log = findViewById(R.id.rv_log);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_log.setLayoutManager(layoutManager);
        rv_log.setFocusableInTouchMode(true);
        rAdapter = new RecycleAdapter(tl_List);

        rv_log.setAdapter(rAdapter);
        bt_stopser = findViewById(R.id.bt_stopser);
        bt_testonline = findViewById(R.id.bt_testonline);
        bt_testonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.olinemusic");
                if (intent_tolocal != null) {
                    startActivity(intent_tolocal);
                } else {
                    // 未安装应用时
                    Toast.makeText(getApplicationContext(), "未安装在线音乐APP", Toast.LENGTH_LONG).show();
                }

                startOnlineService();
            }
        });

        bt_stopser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    iOnlineMusicAidl.unRegisterMusicStateListener(iPlayMusicStateListener);

                    iOnlineMusicAidl.unRegisterSearchListener(iSearchMusicCallBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                unbindService(onlinemusicConnect);
            }
        });
    }


    //初始化RecycleView内容
    private void initList() {
        tl_List.clear();
        TestLog tlog1 = new TestLog("location:", "action");
        tl_List.add(tlog1);
    }


    private ISearchMusicCallBack iSearchMusicCallBack = new ISearchMusicCallBack() {
        @Override
        public void searchMusicSuccess(List<Music> list) throws RemoteException {
            musicList.clear();
            musicList.addAll(list);
            Log.e(ONLINEMUSICTAG, "searchMusicSuccess");
            Toast.makeText(OnlineMusicTest.this, "searchMusicSuccess", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void noSearchMusic() throws RemoteException {
            Log.e(ONLINEMUSICTAG, "noSearchMusic");
            Toast.makeText(OnlineMusicTest.this, "noSearchMusic", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void netWorkError() throws RemoteException {
            Log.e(ONLINEMUSICTAG, "netWorkError");
            Toast.makeText(OnlineMusicTest.this, "netWorkError", Toast.LENGTH_SHORT).show();
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    private IPlayMusicStateListener iPlayMusicStateListener = new IPlayMusicStateListener() {
        @Override
        public void currentTime(long time) throws RemoteException {

        }

        @Override
        public void durationTime(long time) throws RemoteException {

        }

        @Override
        public void onMusicChange(Music music) throws RemoteException {

        }

        @Override
        public void onMusicPlayFinish() throws RemoteException {

        }

        @Override
        public void onPlayerStart() throws RemoteException {

        }

        @Override
        public void onPlayerPause() throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    private ServiceConnection onlinemusicConnect = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 从连接中获取Stub对象
            iOnlineMusicAidl = IOnlineMusicAidl.Stub.asInterface(iBinder);
            try {
                iOnlineMusicAidl.registerSearchListener(iSearchMusicCallBack);
                iOnlineMusicAidl.registerMusicStateListener(iPlayMusicStateListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            onlinemusicTestcase();    //执行在线音乐测试用例
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开连接
            iOnlineMusicAidl = null;

        }
    };

    //在线音乐服务
    private void startOnlineService() {
        final Intent onlinemusicIntent = new Intent();
        onlinemusicIntent.setAction("com.dfzt.onlinemusic.service");
        onlinemusicIntent.setPackage("com.dfzt.olinemusic");
        bindService(onlinemusicIntent, onlinemusicConnect, Context.BIND_AUTO_CREATE);

    }

    //测试用例
    private void onlinemusicTestcase() {
        try {
            int order = 1;          //步骤数

            Thread.sleep(MILLIS);
            iOnlineMusicAidl.play();


            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "播放状态"));
            Toast.makeText(OnlineMusicTest.this, "iOnlineMusicAidl.isPlaying()" + iOnlineMusicAidl.isPlaying(), Toast.LENGTH_SHORT).show();
            tl_List.add(new TestLog("-after isPlaying:", "" + iOnlineMusicAidl.isPlaying()));
//            rAdapter.notifyItemChanged(tl_List.size()-1);
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "播放模式"));
            tl_List.add(new TestLog("-after getPlayModel:", "" + iOnlineMusicAidl.getPlayModel()));
            Toast.makeText(OnlineMusicTest.this, "iOnlineMusicAidl.getPlayModel()" + iOnlineMusicAidl.getPlayModel(), Toast.LENGTH_SHORT).show();
            Thread.sleep(MILLIS);

            iOnlineMusicAidl.next();

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "搜索"));
            iOnlineMusicAidl.searchOnLineMusic("温柔");
            tl_List.add(new TestLog("-after getPlayModel:", "" + ""));
            Thread.sleep(MILLIS);


            Thread.sleep(MILLIS);
//            iOnlineMusicAidl.playSearchMusic(musicList.get(0),true);

//

            rAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
