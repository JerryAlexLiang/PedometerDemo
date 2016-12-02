package com.liangyang.pedometerdemo02;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

/**
 * 计步器的服务
 */
public class StepService extends Service {

    public static Boolean flag = false;
    //实现了信号监听的记步的类
    private StepDetector stepDetector;
    //获取传感器管理器的实例
    private SensorManager sensorManager;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //这里开启了一个线程，因为后台服务也是在主线程中进行，这样可以安全点，防止主线程阻塞
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启计步器
                startStepDetector();
            }
        }).start();
    }

    /**
     * 开启计步器
     */
    private void startStepDetector() {
        flag = true;
        //实现信号监听的记步的类
        stepDetector = new StepDetector(this);
        //获取传感器管理器的实例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //获得传感器的类型，这里获得的类型是加速度传感器
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //此方法用来注册，只有注册过才会生效
        //参数：SensorEventListener的实例，Sensor的实例，更新速率
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        if (stepDetector != null) {
            sensorManager.unregisterListener(stepDetector);
        }
    }

}
