package io.netty.wms.api.wms;

import io.netty.wms.api.common.OperationResult;
import lombok.Data;

@Data
public class WcsOperationResult extends OperationResult {

    private final int messageId;
    private final String agvMsg;
    private final boolean complete;
}