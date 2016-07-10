package br.felipepedroso.mqttandroidtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import br.felipepedroso.mqttandroidtest.eventBusMessages.ConnectionLostMessage;
import br.felipepedroso.mqttandroidtest.eventBusMessages.DeliveryCompleteMessage;
import br.felipepedroso.mqttandroidtest.eventBusMessages.OperationFailedEvent;
import br.felipepedroso.mqttandroidtest.eventBusMessages.OperationSucceedEvent;
import br.felipepedroso.mqttandroidtest.eventBusMessages.OperationType;
import br.felipepedroso.mqttandroidtest.eventBusMessages.NewMqttMessageEvent;

public class MqttConnectionService extends Service implements MqttCallback {
    private final String LOG_TAG = MqttConnectionService.class.getName();

    private final IBinder binder = new MqttConnectionBinder();

    private String clientId = MqttClient.generateClientId();
    private MqttAndroidClient mqttClient;

    public boolean isConnectedToBroker() {
        return mqttClient != null && mqttClient.isConnected();
    }

    public void connectToBroker() {
        if (mqttClient != null) {
            return;
        }

        String serverUri = Utility.getServerUri(this.getApplicationContext());

        mqttClient = new MqttAndroidClient(this.getApplicationContext(), serverUri, clientId);
        mqttClient.setCallback(MqttConnectionService.this);

        try {
            IMqttToken token = mqttClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOG_TAG, "Connected!");

                    EventBus.getDefault().post(new OperationSucceedEvent(OperationType.Connect));
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(LOG_TAG, "Failed to connect.");
                    EventBus.getDefault().post(new OperationFailedEvent(OperationType.Connect, exception));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace(); // TODO: handle the exception properly
        }
    }

    public void disconnectFromBroker() {
        if (isConnectedToBroker()) {
            try {
                IMqttToken token = mqttClient.disconnect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d(LOG_TAG, "The client was disconnected successfully!");
                        EventBus.getDefault().post(new OperationSucceedEvent(OperationType.Disconnect));
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(LOG_TAG, "Failed to disconnect.");
                        EventBus.getDefault().post(new OperationFailedEvent(OperationType.Disconnect, exception));
                    }
                });

            } catch (MqttException e) {
                e.printStackTrace(); // TODO: handle the exception properly
            }
        }
    }

    private void publish(String topic, byte[] payload) {
        if (isConnectedToBroker()) {
            MqttMessage message = new MqttMessage(payload);
            message.setRetained(true);

            try {
                mqttClient.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new OperationFailedEvent(OperationType.Publish, e));
            }
        }
    }

    public void publish(String topic, String payload) {
        this.publish(topic, Utility.encodeToUtf8(payload));
    }

    public void subscribe(String topic, int qos) {
        try {
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new OperationFailedEvent(OperationType.Subscribe, e));
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        EventBus.getDefault().post(new ConnectionLostMessage(cause));
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        EventBus.getDefault().post(new NewMqttMessageEvent(topic, message));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        EventBus.getDefault().post(new DeliveryCompleteMessage(token));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MqttConnectionBinder extends Binder {
        MqttConnectionService getService() {
            return MqttConnectionService.this;
        }
    }
}
