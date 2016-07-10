package br.felipepedroso.mqttandroidtest.eventBusMessages;

/**
 * Created by FelipeAugusto on 10/07/2016.
 */
public class OperationFailedEvent {

    private final OperationType operationType;
    private final Throwable cause;

    public OperationFailedEvent(OperationType operationType, Throwable cause){
        this.operationType = operationType;
        this.cause = cause;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return String.format("OperationFailed { operationType:\"%s\", cause:\"%s\" }", operationType.toString(), cause.toString());
    }
}
