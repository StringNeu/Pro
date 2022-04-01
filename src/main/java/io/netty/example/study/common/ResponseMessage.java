package io.netty.example.study.common;

public class ResponseMessage extends Message<OperationResult> {

    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpcode(opcode).getOperationResultClazz();
    }
}