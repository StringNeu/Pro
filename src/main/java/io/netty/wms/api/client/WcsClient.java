package io.netty.wms.api.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.wms.api.client.codec.AgvFrameDecoder;
import io.netty.wms.api.client.codec.AgvFrameEncoder;
import io.netty.wms.api.client.codec.AgvProtocoDecoder;
import io.netty.wms.api.client.codec.AgvProtocoEncoder;
import io.netty.wms.api.common.RequestMessage;
import io.netty.wms.api.wms.WcsOperation;
import io.netty.wms.api.wms.WmsOperation;
import io.netty.wms.api.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class WcsClient {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(new NioEventLoopGroup());

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();
                pipeline.addLast(new AgvFrameDecoder());
                pipeline.addLast(new AgvFrameEncoder());
                pipeline.addLast(new AgvProtocoEncoder());
                pipeline.addLast(new AgvProtocoDecoder());
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture sync = bootstrap.connect("127.0.0.1",9090);
        sync.sync();
        for (int i = 10; i < 15; i++) {
            RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(),
                    new WcsOperation(i, "jobCode"+i,"rfid"+i,1,new Date(),new Date()));
            sync.channel().writeAndFlush(requestMessage);
        }

        sync.channel().closeFuture().get();
    }
}