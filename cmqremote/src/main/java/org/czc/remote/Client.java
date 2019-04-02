package org.czc.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {

    private Bootstrap bootstrap;
    private Channel channel;

    public void init() {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_SNDBUF, 64 * 1024)
                .option(ChannelOption.SO_RCVBUF, 64 * 1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        channel.pipeline().addLast(new MsgCodec())
                                .addLast(new NettyClientHandler());
                    }
                });
//                .handler(new ChannelInitializer<Channel>() {
//                    @Override
//                    protected void initChannel(Channel channel) throws Exception {
//                        ChannelConfig channelConfig = channel.config();
//                        channelConfig.setConnectTimeoutMillis(5000)
//                                .setOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
//                        channelConfig.setOption(ChannelOption.SO_SNDBUF, 64 * 1024);
//                        channelConfig.setOption(ChannelOption.SO_RCVBUF, 64 * 1024);
//                        channelConfig.setOption(ChannelOption.SO_KEEPALIVE, false);
//                        channel.pipeline().addLast(new MsgCodec())
//                                .addLast(new NettyClientHandler());
//                    }
//                });
        channel = bootstrap.connect("127.0.0.1",9999).channel();
    }
}
