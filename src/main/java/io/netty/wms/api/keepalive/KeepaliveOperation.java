package io.netty.wms.api.keepalive;

import io.netty.wms.api.common.Operation;
import io.netty.wms.api.common.OperationResult;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * 检验在活
 */
@Data
@Log
public class KeepaliveOperation extends Operation {

    private long time;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }


    @Override
    public OperationResult execute() {
        return new KeepaliveOperationResult(time);
    }
}