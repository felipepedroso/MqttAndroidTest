package br.felipepedroso.mqttandroidtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private boolean isBoundToService;
    private MqttConnectionService mqttConnectionService;
    private Button testButton;

    @Override
    protected void onResume() {
        super.onResume();

        testButton = (Button)findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoundToService){
                    mqttConnectionService.connectToBroker();
                    mqttConnectionService.publish("TestingAndroid", "TestMessage");
                }
            }
        });

        // Binding to service
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
            testButton.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBoundToService = false;
            testButton.setEnabled(false);
        }
    };
}
