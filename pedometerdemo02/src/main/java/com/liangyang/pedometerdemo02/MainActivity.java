package com.liangyang.pedometerdemo02;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Intent intent;
    private int countStep = 0;


    /**
     * 定义Handler接收消息,接收从服务端回调的步数
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (StepService.flag) {
                countStep = StepDetector.CURRENT_SETP;
                mTextView.setText(countStep + "步");
                System.out.println("=======  " + "步数："+countStep);
                handler.sendEmptyMessageDelayed(0, 100);
            } else {
                handler.removeMessages(0);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mTextView = (TextView) findViewById(R.id.textView);//记步数
    }

    /**
     * 在onCreate方法中初始化Handler，onStart方法中开启服务，
     * 以备退到后台，再到前台，会触发onStart方法，以此来开启service。
     */
    @Override
    protected void onStart() {
        super.onStart();
        setupService();
    }

    private void setupService() {
        intent = new Intent(this, StepService.class);
        //开启服务
        startService(intent);
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        startService(intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        moveTaskToBack(true);

    }
}
