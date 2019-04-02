package com.ooo.deemo.gotestall;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
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
import com.dfzt.music.aidl.Music;
import com.dfzt.music.aidl.callback.IPlayMusicStateListener;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicTest extends AppCompatActivity {
    static final private String LOCALMUSICTAG = "本地音乐";
    static final private int MILLIS=1000;
    public static List<TestLog> tl_List = new ArrayList<>();
    public static RecycleAdapter rAdapter;
    private static IPlayerProvider iPlayerProvider;


    private RecyclerView rv_log;
//    private Button bt_testlocal;
//private Button bt_stser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_test);

        initList();
        rv_log = findViewById(R.id.rv_log);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_log.setLayoutManager(layoutManager);
        rv_log.setFocusableInTouchMode(true);
        rAdapter = new RecycleAdapter(tl_List);

        rv_log.setAdapter(rAdapter);

//bt_stser = findViewById(R.id.bt_stser);
//        bt_testlocal = findViewById(R.id.bt_testlocal);
startLocalTest();
//        bt_testlocal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.newmyplayer");
//                if (intent_tolocal != null) {
//                    startActivity(intent_tolocal);
//                } else {
//                    // 未安装应用时
//                    Toast.makeText(getApplicationContext(), "未安装本地音乐APP", Toast.LENGTH_LONG).show();
//                }
//                startLocalService();
//            }
//        });

//        bt_stser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                unbindService(localmusicConnect);
//            }
//        });
    }

    private void startLocalTest(){
        Intent intent_tolocal = getPackageManager().getLaunchIntentForPackage("com.dfzt.newmyplayer");
        if (intent_tolocal != null) {
            startActivity(intent_tolocal);
        } else {
            // 未安装应用时
            Toast.makeText(getApplicationContext(), "未安装本地音乐APP", Toast.LENGTH_LONG).show();
        }
        startLocalService();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            iPlayerProvider.unRegisterMusicStateListener(iPlayMusicStateListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(localmusicConnect);

    }

    //初始化RecycleView内容
    private void initList(){
        tl_List.clear();
        TestLog tlog1 = new TestLog("location:","action");
        tl_List.add(tlog1);
    }



    private static ServiceConnection localmusicConnect = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 从连接中获取Stub对象
            iPlayerProvider = IPlayerProvider.Stub.asInterface(iBinder);
            try {
                iPlayerProvider.registerMusicStateListener(iPlayMusicStateListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            localmusicTestcase();    //开始执行本地音乐测试用例
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开连接
            iPlayerProvider = null;

        }
    };

    private static IPlayMusicStateListener iPlayMusicStateListener = new IPlayMusicStateListener.Stub() {
        @Override
        public void currentTime(long time) throws RemoteException {
            Log.e("currentTime",time+"！！！！！！！！！！！");

        }

        @Override
        public void durationTime(long time) throws RemoteException {
            Log.e("durationTime",time+"！！！！！！！！！！！");
        }

        @Override
        public void onMusicChange(Music music) throws RemoteException {
            Log.e("onMusicChange",music.toString()+"！！！！！！！！！！！");
        }

        @Override
        public void onMusicPlayFinish() throws RemoteException {
            Log.e("onMusicPlayFinish","！！！！！！！！！！！");
        }

        @Override
        public void onPlayerStart() throws RemoteException {
            Log.e("onPlayerStart","！！！！！！！！！！！");
        }

        @Override
        public void onPlayerPause() throws RemoteException {
            Log.e("onPlayerPause","！！！！！！！！！！！");
        }

        @Override
        public void onPlayModeChange(int model) throws RemoteException {
            Log.e("onPlayModeChange",model+"！！！！！！！！！！！");
        }

        @Override
        public void onRootChange(int root) throws RemoteException {
            Log.e("onRootChange",root+"！！！！！！！！！！！");
        }

        @Override
        public void scanFinish() throws RemoteException {
            Log.e("scanFinish","！！！！！！！！！！！");
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };

    private static void localmusicTestcase(){
        try {



            int order = 1;          //步骤数

            Thread.sleep(MILLIS);
            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","打开本地setroot1"));
            iPlayerProvider.setRoot(1);
            tl_List.add(new TestLog("-after setroot 1:getRoot(1-本地，2-sd卡，3-U盘):",""+iPlayerProvider.getRoot()));
//            rAdapter.notifyItemChanged(tl_List.size()-1);
            Thread.sleep(MILLIS);



            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","打开SD路径setroot2"));
            iPlayerProvider.setRoot(2);
            tl_List.add(new TestLog("-after setroot 2:getRoot(1-本地，2-sd卡，3-U盘):",""+iPlayerProvider.getRoot()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","打开U盘路径setroot3"));
            iPlayerProvider.setRoot(3);
            tl_List.add(new TestLog("-after setroot 3:getRoot(1-本地，2-sd卡，3-U盘):",""+iPlayerProvider.getRoot()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","打开本地setroot1"));
            iPlayerProvider.setRoot(1);
            tl_List.add(new TestLog("-after setroot 1:getRoot(1-本地，2-sd卡，3-U盘):",""+iPlayerProvider.getRoot()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","播放play"));
            iPlayerProvider.play();

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","正在播放的曲名getPlayingName:"+iPlayerProvider.getPlayingName()));

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","播放时长getCurrent:"+iPlayerProvider.getCurrent()));

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","getCurrentPlayPosition:"+iPlayerProvider.getCurrentPlayPosition()));
            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","歌曲总时长getDuration:"+iPlayerProvider.getDuration()));
            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","isMusicPlayVolFocus:"+String.valueOf(iPlayerProvider.isMusicPlayVolFocus())));
            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","播放状态getPlayerState(1-play,2-pause,3-stop):"+iPlayerProvider.getPlayerState()));
            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","getMusics:"+String.valueOf(iPlayerProvider.getMusics())));
            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","getPlayerState(1-play,2-pause,3-stop):"+iPlayerProvider.getPlayerState()));

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","暂停pause"));
            iPlayerProvider.pause();
            tl_List.add(new TestLog("-after pause:getPlayerState(1-play,2-pause,3-stop):",""+iPlayerProvider.getPlayerState()));
            Thread.sleep(200);

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","下一首next"));
            iPlayerProvider.next();
            tl_List.add(new TestLog("-after next:getCurrentPlayPosition:"
                    ,"" + iPlayerProvider.getCurrentPlayPosition()+"\tgetPlayingName:"+iPlayerProvider.getPlayingName()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","列表循环setMode(0)"));
            iPlayerProvider.setMode(0);
            tl_List.add(new TestLog("-after setMode 0:getRoot(0-列表循环，1-单曲循环，2-随机播放):getMode:",""+iPlayerProvider.getMode()));
            Thread.sleep(MILLIS);

            for(int count = 0;count<10;count++) {
                tl_List.add(new TestLog(LOCALMUSICTAG + order++ + "——", "下一首next"));
                iPlayerProvider.next();
                tl_List.add(new TestLog("-after next:getCurrentPlayPosition:"
                        , "" + iPlayerProvider.getCurrentPlayPosition() + "\tgetPlayingName:" + iPlayerProvider.getPlayingName()));
                Thread.sleep(MILLIS);
            }

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","单曲循环setMode(1)"));
            iPlayerProvider.setMode(1);
            tl_List.add(new TestLog("-after setMode 1:getRoot(0-列表循环，1-单曲循环，2-随机播放):getMode:",""+iPlayerProvider.getMode()));

            for(int count = 0;count<10;count++) {
                tl_List.add(new TestLog(LOCALMUSICTAG + order++ + "——", "下一首next"));
                iPlayerProvider.next();
                tl_List.add(new TestLog("-after next:getCurrentPlayPosition:"
                        , "" + iPlayerProvider.getCurrentPlayPosition() + "\tgetPlayingName:" + iPlayerProvider.getPlayingName()));
                Thread.sleep(MILLIS);
            }

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","随机播放setMode(2)"));
            iPlayerProvider.setMode(2);
            tl_List.add(new TestLog("-after setMode 2:getRoot(0-列表循环，1-单曲循环，2-随机播放):getMode:",""+iPlayerProvider.getMode()));
            Thread.sleep(MILLIS);

            for(int count = 0;count<10;count++) {
                tl_List.add(new TestLog(LOCALMUSICTAG + order++ + "——", "下一首next"));
                iPlayerProvider.next();
                tl_List.add(new TestLog("-after next:getCurrentPlayPosition:"
                        , "" + iPlayerProvider.getCurrentPlayPosition() + "\tgetPlayingName:" + iPlayerProvider.getPlayingName()));
                Thread.sleep(MILLIS);
            }

            for(int count = 0;count<10;count++) {
                tl_List.add(new TestLog(LOCALMUSICTAG + order++ + "——", "上一首prevoius"));
                iPlayerProvider.prevoius();
                tl_List.add(new TestLog("-after prevoius:getCurrentPlayPosition:"
                        , "" + iPlayerProvider.getCurrentPlayPosition() + "\tgetPlayingName:" + iPlayerProvider.getPlayingName()));
                Thread.sleep(MILLIS);
            }

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","当前位置getCurrent:"+iPlayerProvider.getCurrent()));
            tl_List.add(new TestLog("getDuration:",""+iPlayerProvider.getDuration()));
            int random = (int)(Math.random()*(iPlayerProvider.getDuration()));
            iPlayerProvider.setProgress(random);
            Thread.sleep(MILLIS);
            tl_List.add(new TestLog("random number:"+random,":getCurrent:"+iPlayerProvider.getCurrent()));
            Thread.sleep(MILLIS);

            tl_List.add(new TestLog(LOCALMUSICTAG+order+++"——","getCurrentPlayPosition:"+iPlayerProvider.getCurrentPlayPosition()));
            iPlayerProvider.playByPosition(2);
            tl_List.add(new TestLog("-after playByPosition(2):getCurrentPlayPosition:"
                    ,"" +iPlayerProvider.getCurrentPlayPosition()+"\tgetPlayingName:"+iPlayerProvider.getPlayingName()));

            rAdapter.notifyDataSetChanged();

            new Thread(new Runnable() {
                @Override
                public void run() {
//                    setKeyCode(KeyEvent.KEYCODE_BACK);
                }
            }).start();




//            Intent intent_back = new Intent(MainActivity.this,MainActivity.class);
//            startActivity(intent_back);

            /*
            unuse method******************
            void playByPath(String path);
            boolean getBootPlay();
            void setBootPlay(boolean falg);
            void setPartitionPlay(boolean isOpened);
            boolean isMusicPlayVolFocus();
            boolean deleteFile(String path);
            void  setMusicPlayVolFocus(boolean isVolFocus);
            int getScanState();
            void setNeedPlayVolBeLarger(boolean isNeed);

             */
/*
error or useless code *****************
 */

//      setProgress(random)

//      setRoot(4)
//            Thread.sleep(1000);
//            tl_List.add(new TestLog(LOCALMUSICTAG+"errortest-1","setroot4"));
//            iPlayerProvider.setRoot(4);
//            tl_List.add(new TestLog("-after setroot 4:getRoot(1-本地，2-sd卡，3-U盘):",""+iPlayerProvider.getRoot()));
//            rAdapter.notifyItemChanged(tl_List.size()-1);
//            Thread.sleep(1000);

 /*
 crash code *****************
  */
            //     setMode(3)
//            tl_List.add(new TestLog(LOCALMUSICTAG+"15","setMode(3)"));  //接口说明里3是顺序播放，实际没有这功能
//            iPlayerProvider.setMode(3);
//            tl_List.add(new TestLog("-after setMode 2:getRoot(0-列表循环，1-单曲循环，2-随机播放):getMode:",""+iPlayerProvider.getMode()));
//            rAdapter.notifyItemInserted(tl_List.size()-1);

//跳转到到新testlog页面
//            Intent intent_tolocalteslog = new Intent(MainActivity.this,LocalMusicTestlog.class);
//            startActivity(intent_tolocalteslog);
            //

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //将自订log保存为文件
    private void logToFile(){
//        tl_List
    }


    //模拟点击系统按键
    private static void setKeyCode(final int code){
        new Thread(){
            @Override
            public void run() {
                super.run();
                new Instrumentation().sendKeyDownUpSync(code);
            }
        }.start();
    }



    //本地音乐服务
    private void startLocalService() {
        final Intent localmusicIntent = new Intent();
        localmusicIntent.setAction("com.dfzt.music_player.aidl");
        localmusicIntent.setPackage("com.dfzt.newmyplayer");
        bindService(localmusicIntent, localmusicConnect, Context.BIND_AUTO_CREATE);

    }


}
