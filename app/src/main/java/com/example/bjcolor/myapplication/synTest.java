package com.example.bjcolor.myapplication;

import android.util.Log;

/**
 * Created by BJColor on 2017/9/24.
 */

public class synTest implements Runnable {


    @Override
    public void run() {
        if (flag) {
            flag = false;
            testSync1();
        } else {
            flag = true;
            testSync2();
        }
    }

    private static boolean flag = true;

    private void testSync1() {
        synchronized (synTest.class) {
            for (int i = 0; i < 100; i++) {
                Log.i("testSyncObject: " , i+"");
            }
        }
    }

    private void testSync2() {
        synchronized (synTest.class) {
            for (int i = 0; i < 100; i++) {
                Log.i("testSyncClass:" , i+"");
            }
        }
    }
}
