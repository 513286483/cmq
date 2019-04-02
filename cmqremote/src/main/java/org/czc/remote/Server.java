package org.czc.remote;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;

import io.netty.channel.nio.NioEventLoopGroup;

public class Server {

    private ServerBootstrap bootstrap;
    private Channel channel;

    public void init() {
        bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(1), new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2));
        bootstrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelConfig channelConfig = channel.config();
                        channelConfig.setConnectTimeoutMillis(5000)
                                .setOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                        channelConfig.setOption(ChannelOption.SO_SNDBUF, 64 * 1024);
                        channelConfig.setOption(ChannelOption.SO_RCVBUF, 64 * 1024);
                        channelConfig.setOption(ChannelOption.SO_KEEPALIVE, false);
                        channel.pipeline().addLast(new MsgCodec())
                                .addLast(new NettyClientHandler());
                    }
                });
        bootstrap.bind(9999).syncUninterruptibly();
    }
}
