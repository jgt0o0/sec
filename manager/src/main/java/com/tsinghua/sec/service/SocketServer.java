package com.tsinghua.sec.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by ji on 16-5-20.
 */
public class SocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);

    public void init() {

        /*EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast("LoginAuthResponse", new LoginAuthResponseHandler());
                            ch.pipeline().addLast(new MessageHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap
                    .bind(new InetSocketAddress(ServerConstant.SERVER_SOCKET, ServerConstant.SERVER_SOCKET_PORT))
                    .sync();

            LOGGER.info("SOCKET SERVER STARTED AT PORT:{}", ServerConstant.SERVER_SOCKET_PORT);
        } catch (Exception e) {
            LOGGER.error("SOCKET SERVER START ERROR", e);
            throw new RuntimeException(e);
        }*/
    }
}
