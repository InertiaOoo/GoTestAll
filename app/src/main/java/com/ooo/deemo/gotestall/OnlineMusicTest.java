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


//    private static Button bt_stopser;
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
//        bt_stopser = findViewById(R.id.bt_stopser);
//        bt_testonline = findViewById(R.id.bt_testonline);
        startTestOnline();
//        bt_testonline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.olinemusic");
//                if (intent_tolocal != null) {
//                    startActivity(intent_tolocal);
//                } else {
//                    // 未安装应用时
//                    Toast.makeText(getApplicationContext(), "未安装在线音乐APP", Toast.LENGTH_LONG).show();
//                }
//
//                startOnlineService();
//            }
//        });

//        bt_stopser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    iOnlineMusicAidl.unRegisterMusicStateListener(iPlayMusicStateListener);
//
//                    iOnlineMusicAidl.unRegisterSearchListener(iSearchMusicCallBack);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//
//                unbindService(onlinemusicConnect);
//            }
//        });
    }

    private void startTestOnline(){
        Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.olinemusic");
        if (intent_tolocal != null) {
            startActivity(intent_tolocal);
        } else {
            // 未安装应用时
            Toast.makeText(getApplicationContext(), "未安装在线音乐APP", Toast.LENGTH_LONG).show();
        }

        startOnlineService();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            iOnlineMusicAidl.unRegisterMusicStateListener(iPlayMusicStateListener);

            iOnlineMusicAidl.unRegisterSearchListener(iSearchMusicCallBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        unbindService(onlinemusicConnect);

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
            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "播放"));
            iOnlineMusicAidl.play();
            tl_List.add(new TestLog("-after play:isPlaying:", "" + iOnlineMusicAidl.isPlaying()));
//            rAdapter.notifyItemChanged(tl_List.size()-1);
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "播放模式"));
            tl_List.add(new TestLog("-after getPlayModel:", "" + iOnlineMusicAidl.getPlayModel()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "下一首"));
            iOnlineMusicAidl.next();
            tl_List.add(new TestLog("-after next:getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "当前播放"));
            tl_List.add(new TestLog("-after getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "搜索"));
            iOnlineMusicAidl.searchOnLineMusic("温柔");
            tl_List.add(new TestLog("-after searchOnLineMusic:getCurrentMusic:", ""+iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "播放状态"));
            tl_List.add(new TestLog("-after isPlaying:", "" + iOnlineMusicAidl.isPlaying()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "当前播放"));
            tl_List.add(new TestLog("-after getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "总时长"));
            tl_List.add(new TestLog("-after getDurationTime:", "" + iOnlineMusicAidl.getDurationTime()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "移动seekbar"));
            int random = (int) (Math.random()*iOnlineMusicAidl.getDurationTime());
            iOnlineMusicAidl.seekTo(random);
            tl_List.add(new TestLog("-after seekTo:"+random+"：isPlaying：", "" + iOnlineMusicAidl.isPlaying()));
            Thread.sleep(5*MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "下一首"));
            iOnlineMusicAidl.next();
            tl_List.add(new TestLog("-after next:getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "暂停"));
            iOnlineMusicAidl.pause();
            tl_List.add(new TestLog("-after pause:isPlaying:", "" + iOnlineMusicAidl.isPlaying()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "播放或暂停"));
            iOnlineMusicAidl.doPlayOrPause();
            tl_List.add(new TestLog("-after doPlayOrPause:isPlaying:", "" + iOnlineMusicAidl.isPlaying()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "列表播放"));
            iOnlineMusicAidl.setPlayModel(0);
            tl_List.add(new TestLog("-after setPlayModel(0):getPlayModel:", "" + iOnlineMusicAidl.getPlayModel()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "下一首"));
            iOnlineMusicAidl.next();
            tl_List.add(new TestLog("-after next:getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "随机播放"));
            iOnlineMusicAidl.setPlayModel(1);
            tl_List.add(new TestLog("-after setPlayModel(1):getPlayModel:", "" + iOnlineMusicAidl.getPlayModel()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "下一首"));
            iOnlineMusicAidl.next();
            tl_List.add(new TestLog("-after next:getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "单曲播放"));
            iOnlineMusicAidl.setPlayModel(2);
            tl_List.add(new TestLog("-after setPlayModel(2):getPlayModel:", "" + iOnlineMusicAidl.getPlayModel()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(ONLINEMUSICTAG + order++ + "——", "下一首"));
            iOnlineMusicAidl.next();
            tl_List.add(new TestLog("-after next:getCurrentMusic:", "" + iOnlineMusicAidl.getCurrentMusic()));
            Thread.sleep(MILLIS);
//

            rAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
