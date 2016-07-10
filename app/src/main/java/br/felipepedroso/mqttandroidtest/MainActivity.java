package br.felipepedroso.mqttandroidtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.felipepedroso.mqttandroidtest.eventBusMessages.NewMqttMessageEvent;
import br.felipepedroso.mqttandroidtest.eventBusMessages.OperationSucceedEvent;
import br.felipepedroso.mqttandroidtest.eventBusMessages.OperationType;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getName();

    private boolean isBoundToService;
    private MqttConnectionService mqttConnectionService;
    private Button testButton;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        testButton = (Button)findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoundToService){
                    mqttConnectionService.connectToBroker();
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

    @Subscribe
    public void handleNewMqttMessage(NewMqttMessageEvent event){
        Log.d(LOG_TAG, event.toString());
    }

    @Subscribe
    public void handleOperationSucceed(OperationSucceedEvent event){
        if (event.getOperationType() == OperationType.Connect){
            mqttConnectionService.publish("TestingAndroid", "TestMessage");
            mqttConnectionService.subscribe("TestingAndroid", 1);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            MqttConnectionService.MqttConnectionBinder mqttConnectionBinder = (MqttConnectionService.MqttConnectionBinder) binder;
            mqttConnectionService = mqttConnectionBinder.getService();
            testButton.setEnabled(true);
            isBoundToService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBoundToService = false;
            testButton.setEnabled(false);
        }
    };
}
