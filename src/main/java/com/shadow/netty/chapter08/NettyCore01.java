package com.shadow.netty.chapter08;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author shadow
 * @create 2020-10-04
 * @description
 */
public class NettyCore01 {
	public static void main(String[] args) throws Exception{
		t2();
	}


	public static void t2() throws Exception{

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();


		ServerBootstrap serverBootstrap = new ServerBootstrap();

		serverBootstrap.group(bossGroup,workGroup)
				.handler(new LoggingHandler(LogLevel.INFO))
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new StringEncoder())
								.addLast();
					}
				});

		ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
		channelFuture.channel().closeFuture().sync();

		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();

	}



	public static void t1(){
		int max = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
		System.out.println(max);
	}
}
