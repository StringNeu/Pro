package io.netty.example.study.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 接受信息时，处理粘包、半包的解码器
 * LengthFieldBasedFrameDecoder是最通用的解决tcp协议粘包问题的方案
 */
public class WmsFrameDecoder extends LengthFieldBasedFrameDecoder {
    //信息的最大长度
    private static int maxFrameLength = Integer.MAX_VALUE;
    //长度的位移
    private static int lengthFieldOffset = 0;
    //需要调整长度
    private static int lengthAdjustment = 0;
    //用信息头的两个字节，表示信息的长度
    private static int lengthFieldLength = 2;
    //解码后，把信息头的两个字节去掉，只保留后面内容
    private static int initialBytesToStrip = 2;

    public WmsFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2,
                0, 2);
    }
}