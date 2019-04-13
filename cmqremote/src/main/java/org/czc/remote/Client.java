package org.czc.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {

    private Bootstrap bootstrap;

    private Channel channel;

    public void init() {
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_SNDBUF, 64 * 1024)
                .option(ChannelOption.SO_RCVBUF, 64 * 1024)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MsgCodec())
                                .addLast(new NettyClientHandler());
                    }
                });
        channel = bootstrap.connect("127.0.0.1",9999).channel();
    }

    public Channel getChannel() {
        return channel;
    }

    public static void main(String[] args) {
        Client server = new Client();
        server.init();
    }
}
