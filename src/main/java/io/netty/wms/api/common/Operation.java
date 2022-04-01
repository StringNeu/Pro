package io.netty.wms.api.common;

public abstract class Operation extends MessageBody {

    public abstract OperationResult execute();
}