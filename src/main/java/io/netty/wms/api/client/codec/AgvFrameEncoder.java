package io.netty.wms.api.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 为客户端接受解决ByteBuf半包、粘包问题
 * 告诉构造器使用两个字节保存信息长度
 */
public class AgvFrameEncoder extends LengthFieldPrepender {

    public AgvFrameEncoder() {
        super(2);
    }
}