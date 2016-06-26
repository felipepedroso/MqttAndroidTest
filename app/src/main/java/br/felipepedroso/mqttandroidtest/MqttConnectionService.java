package br.felipepedroso.mqttandroidtest;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.io.UnsupportedEncodingException;

public class MqttConnectionService extends Service {
    private final String LOG_TAG = MqttConnectionService.class.getName();

    private final IBinder binder = new MqttConnectionBinder();

    private String clientId;
    private MqttAndroidClient mqttClient;

    public boolean isConnectedToBroker() {
        return mqttClient != null && mqttClient.isConnected();
    }

    public void connectToBroker() {
        if (mqttClient != null) {
            return;
        }

        if (TextUtils.isEmpty(clientId)) {
            clientId = MqttClient.generateClientId();
        }

        String serverUri = Utility.getServerUri(this.getApplicationContext());

        mqttClient = new MqttAndroidClient(this.getApplicationContext(), serverUri, clientId);

        try {
            IMqttToken token = mqttClient.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(LOG_TAG, "Connected!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(LOG_TAG, "Failed to connect.");
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
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d(LOG_TAG, "Failed to disconnect.");
                    }
                });

            } catch (MqttException e) {
                e.printStackTrace(); // TODO: handle the exception properly
            }
        }
    }

    public void publish(String topic, String payload) {
        if (isConnectedToBroker()) {

            byte[] encodedPayload = new byte[0];

            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                message.setRetained(true);
                mqttClient.publish(topic, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }
        }
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
