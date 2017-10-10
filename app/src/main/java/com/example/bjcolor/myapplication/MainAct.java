package com.example.bjcolor.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by BJColor on 2017/9/24.
 */

public class MainAct extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mz_demo);
        ExecutorService exec= Executors.newFixedThreadPool(2);
        synTest synTest = new synTest();
        synTest synTest2 = new synTest();
        exec.execute(synTest);
        exec.execute(synTest2);
        exec.shutdown();

    }

}