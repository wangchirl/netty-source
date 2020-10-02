package com.shadow.netty.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.time.LocalDateTime;

/**
 * @author shadow
 * @create 2020-09-29
 * @description
 *
 * 对 WebSocket 的支持
 * 基于 HTTP，需要http编解码器
 *	- HttpServerCodec
 *  - ChunkedWriteHandler  块
 *  - HttpObjectAggregator 聚合
 * websocket的编解码器
 *  - WebSocketServerProtocolHandler
 *
 * websocket的消息类型：WebSocketFrame
 * 	- BinaryWebSocketFrame
 * 	- TextWebSocketFrame
 * 	- ContinuationWebSocketFrame
 * 	- PingWebSocketFrame
 * 	- PongWebSocketFrame
 * 	- CloseWebSocketFrame
 */
public class NettyServer05 {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new MyNettyServer05Initializer());
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

	static class MyNettyServer05Initializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpServerCodec())
					.addLast(new ChunkedWriteHandler())
					.addLast(new HttpObjectAggregator(8192))
					.addLast(new WebSocketServerProtocolHandler("/ws"))
					.addLast(new MyNettyServer05ChannelInBoundHandler());
		}
	}

	static class MyNettyServer05ChannelInBoundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
			// 打印消息
			System.out.println("收到消息:" + msg.text());
			// 写回消息
			ctx.writeAndFlush(new TextWebSocketFrame("websocket message：服务器时间" + LocalDateTime.now()));
		}

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			System.out.println("handler added: " + ctx.channel().id().asLongText());
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			System.out.println("handler removed: " + ctx.channel().id().asLongText());
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}
}
