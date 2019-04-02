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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bt_golocal.setOnClickListener(this);
        bt_goonline.setOnClickListener(this);
    }




    private void initView(){
        bt_golocal = findViewById(R.id.bt_golocal);
        bt_goonline = findViewById(R.id.bt_goonline);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_golocal:
                Intent intent_local = new Intent(MainActivity.this,LocalMusicTest.class);
                startActivity(intent_local);
                break;
            case R.id.bt_goonline:
                Intent intent_online = new Intent(MainActivity.this,OnlineMusicTest.class);
                startActivity(intent_online);
                break;
                default:
                    break;
        }
    }
}
