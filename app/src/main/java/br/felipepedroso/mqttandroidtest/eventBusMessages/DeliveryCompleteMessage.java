package br.felipepedroso.mqttandroidtest.eventBusMessages;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

/**
 * Created by FelipeAugusto on 10/07/2016.
 */
public class DeliveryCompleteMessage {
    private final IMqttDeliveryToken token;

    public DeliveryCompleteMessage(IMqttDeliveryToken token) {
        this.token = token;
    }

    public IMqttDeliveryToken getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token.toString();
    }
}
