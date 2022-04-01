package io.netty.wms.api.wms;

import io.netty.wms.api.common.Operation;
import io.netty.wms.api.common.OperationResult;
import lombok.Data;
import lombok.extern.java.Log;

import java.util.Date;

/**
 * 处理业务逻辑类
 */
@Data
@Log
public class WcsOperation extends Operation {

    private int messageId;
    private String jobCode;
    private String rfid;
    private int executeStatus;
    private Date executeTime;
    private Date endTime;

    public WcsOperation(int messageId, String jobCode, String rfid, int executeStatus,
                        Date executeTime, Date endTime) {
        this.messageId = messageId;
        this.jobCode = jobCode;
        this.rfid = rfid;
        this.executeStatus = executeStatus;
        this.executeTime = executeTime;
        this.endTime = endTime;
    }

    @Override
    public OperationResult execute() {
        StringBuilder s = new StringBuilder();
        s.append("接收到指令：");
        s.append(jobCode).append("-").append(rfid).append("-").append(executeStatus)
                .append("-").append(executeTime).append("-").append(endTime);
        log.info(s.toString());
        //execute order logic
//        log.info("完成指令!");
        return new WmsOperationResult(messageId, jobCode,true);
    }
}