package br.felipepedroso.mqttandroidtest.eventBusMessages;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by FelipeAugusto on 10/07/2016.
 */
public class NewMqttMessageEvent {
    public final MqttMessage message;
    private final String topic;

    public NewMqttMessageEvent(String topic, MqttMessage message) {
        this.message = message;
        this.topic = topic;
    }

    public String getMessageString(){
        return message.toString();
    }

    public MqttMessage getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return String.format("NewMessage { topic:\"%s\", message:\"%s\" }", topic, getMessageString());
    }
}
