package io.netty.example.study.wms;

import io.netty.example.study.common.OperationResult;
import lombok.Data;

@Data
public class WmsOperationResult extends OperationResult {

    private final int messageId;
    private final String agvMsg;
    private final boolean complete;
}