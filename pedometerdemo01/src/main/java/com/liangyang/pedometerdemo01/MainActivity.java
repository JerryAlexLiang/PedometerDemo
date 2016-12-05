package com.liangyang.pedometerdemo01;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 计步器Demo01
 * 主页面
 * 在onCreate方法中初始化Handler，onStart方法中开启服务，
 * 以备退到后台，再到前台，会触发onStart方法，以此来开启service。
 */
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
     * 设置监听事件，启动服务
     */
    public void toStart(View view) {
        intent = new Intent(this, StepService.class);
        //开启服务
        startService(intent);
        handler.sendEmptyMessage(0);
    }

    public void toStop(View view) {
        //关闭服务
        stopService(intent);
        handler.removeMessages(0);
    }

//    /**
//     * 连按两次退出应用
//     */
//    private long waitTime = 2000;
//    private long touchTime = 0;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
//            long currentTime = System.currentTimeMillis();
//            if ((currentTime - touchTime) > waitTime) {
//                Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
//                touchTime = currentTime;
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        } else if (KeyEvent.KEYCODE_HOME == keyCode) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
