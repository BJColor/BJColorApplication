package com.example.bjcolor.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by BJColor on 2017/9/23.
 */

public class Bact extends Activity {
    @Bind(R.id.tv_onclick)
    TextView tvOnclick;
    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.fsv)
    ScrollView fsv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mz_demo);
        ButterKnife.bind(this);
        tvOnclick.setText("b");
        tvOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bact.this,Cact.class);
                startActivity(intent);
            }
        });

        Log.i("log","B-onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("log","B-onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("log","B-onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("log","B-onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("log","B-onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("log","B-onRestart");
    }

}
