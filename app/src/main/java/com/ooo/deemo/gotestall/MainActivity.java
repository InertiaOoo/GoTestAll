package com.ooo.deemo.gotestall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_golocal;
    private Button bt_goonline;
    private Button bt_gosound;
    private Button bt_goblue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }


    private void initView() {
        bt_golocal = findViewById(R.id.bt_golocal);
        bt_goonline = findViewById(R.id.bt_goonline);
        bt_gosound = findViewById(R.id.bt_gosound);
        bt_goblue = findViewById(R.id.bt_goblue);

        bt_golocal.setOnClickListener(this);
        bt_goonline.setOnClickListener(this);
        bt_gosound.setOnClickListener(this);
        bt_goblue.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_golocal:
                Intent intent_local = new Intent(MainActivity.this, LocalMusicTest.class);
                startActivity(intent_local);
                break;
            case R.id.bt_goonline:
                Intent intent_online = new Intent(MainActivity.this, OnlineMusicTest.class);
                startActivity(intent_online);
                break;
            case R.id.bt_gosound:
                Intent intent_sound = new Intent(MainActivity.this, SoundContentTest.class);
                startActivity(intent_sound);
                break;
            case R.id.bt_goblue:
                Intent intent_blue = new Intent(MainActivity.this, BlueToothTest.class);
                startActivity(intent_blue);
                break;
            default:
                break;
        }
    }
}
