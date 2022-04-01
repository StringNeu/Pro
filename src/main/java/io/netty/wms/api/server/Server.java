package io.netty.wms.api.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.wms.api.server.codec.WmsFrameDecoder;
import io.netty.wms.api.server.codec.WmsFrameEncoder;
import io.netty.wms.api.server.codec.WmsProtocoDecoder;
import io.netty.wms.api.server.codec.WmsProtocoEncoder;
import io.netty.wms.api.server.handler.WmsServerProcessHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class Server {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        //在父group中增加log
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.group(new NioEventLoopGroup());

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();
                pipeline.addLast(new WmsFrameDecoder());
                pipeline.addLast(new WmsFrameEncoder());
                pipeline.addLast(new WmsProtocoEncoder());
                pipeline.addLast(new WmsProtocoDecoder());
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(new WmsServerProcessHandler());
            }
        });

        ChannelFuture sync = serverBootstrap.bind(9090).sync();
        sync.channel().closeFuture().get();
    }
}