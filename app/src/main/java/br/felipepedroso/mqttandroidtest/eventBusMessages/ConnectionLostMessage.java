package br.felipepedroso.mqttandroidtest.eventBusMessages;

/**
 * Created by FelipeAugusto on 10/07/2016.
 */
public class ConnectionLostMessage {
    private final Throwable cause;

    public ConnectionLostMessage(Throwable cause) {
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return cause.toString();
    }
}
