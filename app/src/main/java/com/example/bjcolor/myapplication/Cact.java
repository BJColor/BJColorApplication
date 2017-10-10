package com.example.bjcolor.myapplication;

import android.app.Activity;
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

public class Cact extends Activity {
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
        tvOnclick.setText("c");
        tvOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Log.i("log","C-onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("log","C-onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("log","C-onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("log","C-onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("log","C-onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("log","C-onRestart");
    }
}
