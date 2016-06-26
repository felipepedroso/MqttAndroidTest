package br.felipepedroso.mqttandroidtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private boolean isBoundToService;
    private MqttConnectionService mqttConnectionService;

    @Override
    protected void onResume() {
        super.onResume();

        // Bindind to service
        Intent intent = new Intent(this, MqttConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            MqttConnectionService.MqttConnectionBinder mqttConnectionBinder = (MqttConnectionService.MqttConnectionBinder) binder;
            mqttConnectionService = mqttConnectionBinder.getService();
            isBoundToService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBoundToService = false;
        }
    };
}
