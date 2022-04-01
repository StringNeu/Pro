package io.netty.example.study.wms;

import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;
import lombok.Data;
import lombok.extern.java.Log;

import java.util.Date;

/**
 * 处理业务逻辑类
 */
@Data
@Log
public class WmsOperation extends Operation {

    private int messageId;
    private String agvMsg;
    private String jobCode;
    private String wareHouseCode;
    private String receiveAddress;
    private String roadwayCode;
    private String trayBarCode;
    private String deliveryAddress;
    private int type;
    private String billCode;
    private String mesNo;
    private Date createTime;

    public WmsOperation(int messageId, String agvMsg,String jobCode,String wareHouseCode,
                        String receiveAddress,String roadwayCode,String trayBarCode,String deliveryAddress,
                        int type,String billCode,String mesNo,Date createTime) {
        this.messageId = messageId;
        this.agvMsg = agvMsg;
        this.jobCode = jobCode;
        this.wareHouseCode = wareHouseCode;
        this.receiveAddress = receiveAddress;
        this.roadwayCode = roadwayCode;
        this.trayBarCode = trayBarCode;
        this.deliveryAddress = deliveryAddress;
        this.type = type;
        this.billCode = billCode;
        this.mesNo = mesNo;
        this.createTime = createTime;
    }

    @Override
    public OperationResult execute() {
        StringBuilder s = new StringBuilder();
        s.append("接收到指令：");
        s.append(agvMsg).append("-").append(jobCode).append("-").append(wareHouseCode)
                .append("-").append(receiveAddress).append("-").append(roadwayCode).append("-").append(trayBarCode)
                .append("-").append(deliveryAddress).append("-").append(type).append("-").append(billCode).append("-").append(mesNo)
                .append("-").append(createTime);
        log.info(s.toString());
        //execute order logic
//        log.info("完成指令!");
        return new WmsOperationResult(messageId, agvMsg,true);
    }
}