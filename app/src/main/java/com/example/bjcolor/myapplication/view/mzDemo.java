package com.example.bjcolor.myapplication.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.bjcolor.myapplication.Bact;
import com.example.bjcolor.myapplication.MyListView;
import com.example.bjcolor.myapplication.MyScAdapter;
import com.example.bjcolor.myapplication.R;
import com.example.bjcolor.myapplication.myservice;
import com.example.bjcolor.myapplication.presenter.mzPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class mzDemo extends Activity implements ImzDemo {

    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.fsv)
    ScrollView fsv;
    @Bind(R.id.tv_onclick)
    TextView tvOnclick;
    @Bind(R.id.lv)
    MyListView lv;
    private TextView tv;
    private com.example.bjcolor.myapplication.presenter.mzPresenter mzPresenter;
    private String str = "";
    private ArrayList strings;
    private int count;
    private com.example.bjcolor.myapplication.myservice myservice;
    private com.example.bjcolor.myapplication.myservice.MyBinder myBinder;
    public static MyHandler handler;
    private MyScAdapter myScAdapter;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mz_demo);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        strings = new ArrayList<>();
        for (int x = 1; x < 100; x++) {
            strings.add(x + "");
        }
        handler = new MyHandler();
        horizontal_layout();
        myScAdapter = new MyScAdapter(this, strings);
        gv.setAdapter(new MyScAdapter(this, strings));
        lv.setAdapter(myScAdapter);
//        lv.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                lv.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
        lv.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
            }

            @Override
            public void onLoadingMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    }
                }).start();
            }
        });


        mzPresenter = new mzPresenter(this);
        tvOnclick.setText("main");
        tvOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mzDemo.this, Bact.class);
                startActivity(intent);
            }
        });


        Intent intent = new Intent(this, myservice.class);

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (com.example.bjcolor.myapplication.myservice.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        if (myBinder != null) {
            myBinder.setDate(tvOnclick, new myservice.UpdateData() {
                @Override
                public void update(TextView tv, int data) {
                    tv.setText(data + "");
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("log", "Main-onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("log", "Main-onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("log", "Main-onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("log", "Main-onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("log", "Main-onRestart");
    }

    //gridview横向布局方法
    public void horizontal_layout() {
        int size = strings.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) (110 * size * density);
        int itemWidth = (int) (100 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv.setLayoutParams(params);// 设置GirdView布局参数
        gv.setColumnWidth(itemWidth);// 列表项宽
        gv.setHorizontalSpacing(10);// 列表项水平间距
        gv.setStretchMode(GridView.NO_STRETCH);
        gv.setNumColumns(size);//总长度

    }


    @Override
    public void setName(String name) {
        str += name;
    }

    @Override
    public void setAge(int age) {
        str += age;
    }

    @Override
    public void setSex(String sex) {
        str += sex;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

        }
    }

}
