package org.czc.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;

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
        boolean useEpoll = false;
        bootstrap = new ServerBootstrap();
        EventLoopGroup boss = useEpoll ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
        EventLoopGroup worker = useEpoll ? new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2) : new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        bootstrap.group(boss, worker);
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
                                .addLast(new NettyServerHandler());
                    }
                });
        bootstrap.bind(9999).syncUninterruptibly();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }
}
