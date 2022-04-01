package io.netty.wms.api.keepalive;

import io.netty.wms.api.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {
    private final long time;
}