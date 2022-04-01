package io.netty.wms.api.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.wms.api.common.Operation;
import io.netty.wms.api.common.OperationResult;
import io.netty.wms.api.common.RequestMessage;
import io.netty.wms.api.common.ResponseMessage;

/**
 * 消息处理器
 * SimpleChannelInboundHandler会自动释放我们的channel等，我们只需要关心最核心的业务处理部分
 */
public class WmsServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestMessage requestMessage) throws Exception {
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        channelHandlerContext.writeAndFlush(responseMessage);
    }
}