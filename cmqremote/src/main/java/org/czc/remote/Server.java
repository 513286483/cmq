package org.czc.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;

import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

public class Server {

    private ServerBootstrap bootstrap;

    public void init() {
        boolean useEpoll = true;
        bootstrap = new ServerBootstrap();
        bootstrap.group(new EpollEventLoopGroup(1), new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2));
        bootstrap.channel(useEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, 64*1024)
                .childOption(ChannelOption.SO_RCVBUF, 64*1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MsgCodec())
                                .addLast(new NettyClientHandler());
                    }
                });
//        bootstrap.option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_BACKLOG, 1024)
//                .option(ChannelOption.SO_SNDBUF, 64 * 1024)
//                .option(ChannelOption.SO_SNDBUF, 64 * 1024)
//                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
//        bootstrap.handler(new ChannelInitializer<Channel>() {
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
        bootstrap.bind(9999).syncUninterruptibly();
    }
}
