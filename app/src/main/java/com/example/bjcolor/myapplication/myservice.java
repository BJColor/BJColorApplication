package com.example.bjcolor.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;

public class myservice extends Service {
    private int data=1;
    private Handler handler;
    private boolean isStart;
    private boolean startUpdate;
    MyBinder binder= new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        isStart = true;
        new Thread(new MyThread()).start();
        return binder;
    }

    public class MyBinder extends Binder {
        public void setDate(final TextView tv, final UpdateData updata) {
            startUpdate = true;
            Message msg = new Message();

            handler = new Handler() {
                public void handleMessage(Message msg) {
                    updata.update(tv, data);
                }
            };
        }
    }

    public class MyThread implements Runnable {

        @Override
        public void run() {
            while (isStart) {
                if (startUpdate) {
                    data++;
                    Message msg = handler.obtainMessage();
                    msg.arg1 = data;
                    handler.sendMessage(msg);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public interface UpdateData {
        public void update(TextView tv, int data);

    }


    synchronized void  method1(){

    }

}