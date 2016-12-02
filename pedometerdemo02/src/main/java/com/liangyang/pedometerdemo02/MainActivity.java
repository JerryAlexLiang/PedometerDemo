package com.liangyang.pedometerdemo02;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
    private EditText mEditText;
    private String mEditStep;
    private int mPlanStep;

    /**
     * 定义Handler接收消息,接收从服务端回调的步数
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (StepService.flag) {
                countStep = StepDetector.CURRENT_SETP;
                mTextView.setText(countStep);
//                //弹出框判断
//                if (countStep==mPlanStep){
//                    showCommonDialog();
//                }
                handler.sendEmptyMessageDelayed(0,100);
            }else {
                handler.removeMessages(0);
            }

        }
    };



    /**
     * 当运动步数等于设置的计划步数时弹出Dialog
     */
    private void showCommonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("运动提醒")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("恭喜你完成今日运动计划，继续加油！")
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "你按了取消按钮", Toast.LENGTH_SHORT).show();
//                    }
//                })
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "你点击了确定按钮", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        // 创建对话框
        AlertDialog dialog = builder.create();
        // 显示对话框
        dialog.show();

    }


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
        //mPlanStep = Integer.valueOf(mEditText.getText().toString()).intValue();
//        mEditStep = mEditText.getText().toString();
//        if ("".equals(mEditStep)){
//            Toast.makeText(MainActivity.this, "请填写计划", Toast.LENGTH_SHORT).show();
//        }else {
//            mPlanStep = Integer.parseInt(mEditStep);
//        }
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

    /**
     * 连按两次退出应用
     */
    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) > waitTime) {
                Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else if (KeyEvent.KEYCODE_HOME == keyCode){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
