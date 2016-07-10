package br.felipepedroso.mqttandroidtest.eventBusMessages;

/**
 * Created by FelipeAugusto on 10/07/2016.
 */
public class OperationSucceedEvent {
    private final OperationType operationType;

    public OperationSucceedEvent(OperationType operationType){
        this.operationType = operationType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        return String.format("OperationSucceed { operationType:\"%s\" }", operationType.toString());
    }
}
