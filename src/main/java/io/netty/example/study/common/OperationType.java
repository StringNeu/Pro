package io.netty.example.study.common;

import io.netty.example.study.auth.AuthOperation;
import io.netty.example.study.auth.AuthOperationResult;
import io.netty.example.study.keepalive.KeepaliveOperation;
import io.netty.example.study.keepalive.KeepaliveOperationResult;
import io.netty.example.study.wms.WmsOperation;
import io.netty.example.study.wms.WmsOperationResult;

/**
 * 信息操作类，规范操作类的对应关系
 */
public enum OperationType {

    AUTH(1, AuthOperation.class, AuthOperationResult.class),
    KEEPALIVE(2, KeepaliveOperation.class, KeepaliveOperationResult.class),
    ORDER(3, WmsOperation.class, WmsOperationResult.class);

    public int opCode;
    public Class<? extends Operation> operationClazz;
    public Class<? extends OperationResult> operationResultClazz;

    OperationType(int opCode, Class<? extends Operation> operationClazz, Class<? extends OperationResult> operationResultClazz) {
        this.opCode = opCode;
        this.operationClazz = operationClazz;
        this.operationResultClazz = operationResultClazz;
    }

    public int getOpCode() {
        return opCode;
    }

    public Class<? extends Operation> getOperationClazz() {
        return operationClazz;
    }

    public Class<? extends OperationResult> getOperationResultClazz() {
        return operationResultClazz;
    }

    public static OperationType fromOperation(Operation operation) {

        if (operation == null) {
            return null;
        }

        OperationType result = null;
        Class clazz = operation.getClass();
        for (OperationType operationType : OperationType.values()) {
            if (clazz == operationType.getOperationClazz()) {
                result = operationType;
            }
        }
        return result;
    }

    public static OperationType fromOpcode(int opcode) {
        OperationType result = null;
        for (OperationType operationType : OperationType.values()) {
            if (opcode == operationType.getOpCode()) {
                result = operationType;
            }
        }
        return result;
    }
}
