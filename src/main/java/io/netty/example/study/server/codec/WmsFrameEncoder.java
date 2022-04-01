package io.netty.example.study.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 为客户端接受解决ByteBuf半包、粘包问题
 * 告诉构造器使用两个字节保存信息长度
 */
public class WmsFrameEncoder extends LengthFieldPrepender {

    public WmsFrameEncoder() {
        super(2);
    }
}