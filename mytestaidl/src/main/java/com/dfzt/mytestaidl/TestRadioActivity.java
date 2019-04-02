package com.dfzt.mytestaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dfzt.dfzt_radio.RadioAidl;
import com.dfzt.dfzt_radio.callback.IESTRadioServiceCallBack;
import com.dfzt.olinemusic.IOnlineMusicAidl;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class TestRadioActivity extends AppCompatActivity {
    private Intent musicIntent = new Intent();
    private RadioAidl radioAidl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_radio);
        connectAidl();
    }

    private void connectAidl() {
        musicIntent.setAction("com.dfzt.audio_content.service");
        musicIntent.setPackage("com.dfzt.dfzt_radio");
        startService(musicIntent);
        bindService(musicIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            radioAidl = RadioAidl.Stub.asInterface(service);
            Log.e("TAG", "连接成功");
            //，连接成功 注册callBack
            try {
                radioAidl.registerSearchListener(iestRadioServiceCallBack);
                radioAidl.searchAlbumByName("3","小说",1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IESTRadioServiceCallBack iestRadioServiceCallBack = new IESTRadioServiceCallBack.Stub() {
        @Override
        public void onXmlySearchByAlbumName(boolean success, List<Album> albums, int totalPage, int currentPage) throws RemoteException {
                Log.e("TAG"," onXmlySearchByAlbumName");
        }

        @Override
        public void onXmlySearchExactlyCompleted(boolean success, List<Track> tracks) throws RemoteException {
            Log.e("TAG"," onXmlySearchExactlyCompleted");
        }

        @Override
        public void onXmlySearchTag(boolean success, String category, String tag) throws RemoteException {
            Log.e("TAG"," onXmlySearchTag");
        }

        @Override
        public void onXmlyError(int errorCode, String errorString) throws RemoteException {
            Log.e("TAG"," onXmlyError");
        }

        @Override
        public void getNextIndexTrackList(List<Track> list) throws RemoteException {
            Log.e("TAG"," getNextIndexTrackList");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            radioAidl.unRegisterSearchListener(iestRadioServiceCallBack);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(connection);
    }
}
