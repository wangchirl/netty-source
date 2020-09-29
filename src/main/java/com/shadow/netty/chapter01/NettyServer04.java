package com.shadow.netty.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author shadow
 * @create 2020-09-29
 * @description
 *
 * 心跳机制 - IdleStateHandler
 */
public class NettyServer04 {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO)) // bossGroup 的 handler
				.childHandler(new MyNettyServer04ChannelInitializer());
		try {
			ChannelFuture channelFuture = serverBootstrap.bind("localhost", 8090).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	static class MyNettyServer04ChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new IdleStateHandler(5,7,10,TimeUnit.SECONDS)) //(读时间,写时间,读写时间) 空闲检测 handler(没有读写的情况下触发事件)
					.addLast(new MyNettyServer04ChannelInBoundHandler());
		}
	}

	static class MyNettyServer04ChannelInBoundHandler extends ChannelInboundHandlerAdapter {
		// 事件触发
		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if(evt instanceof IdleStateEvent) {
				IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

				String eventType = "";

				switch (idleStateEvent.state()) {
					case READER_IDLE:
						eventType = "读空闲";
						break;
					case WRITER_IDLE:
						eventType = "写空闲";
						break;
					case ALL_IDLE:
						eventType = "读写空闲";
						break;
				}
				System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
				ctx.close();
			}
		}
	}
}
