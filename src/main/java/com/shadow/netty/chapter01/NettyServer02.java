package com.shadow.netty.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @author shadow
 * @create 2020-09-29
 * @description
 *
 * Socket 的支持
 * 类比记忆：
 * NioServerSocketChannel -> ServerSocket
 * NioSocketChannel -> Socket
 * 测试：启动服务器与客户端
 *
 */
public class NettyServer02 {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class) // 类比 ServerSocket
				.handler(new LoggingHandler()) // bossGroup 的 handler
				.childHandler(new MyNettyServer02ChannelInitializer());
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

	static class MyNettyServer02ChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
					.addLast(new LengthFieldPrepender(4))
					.addLast(new StringEncoder(CharsetUtil.UTF_8))
					.addLast(new StringDecoder(CharsetUtil.UTF_8))
					.addLast("myHandler",new MyNettyServer02ChannelInBoundHandler());
		}
	}

	static class MyNettyServer02ChannelInBoundHandler extends SimpleChannelInboundHandler<String> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			// 接收到客户端发送的数据
			System.out.println(ctx.channel().remoteAddress() + "," + msg);
			// 返回客户端数据
			ctx.writeAndFlush("from server : " + UUID.randomUUID());
		}

		// 异常处理
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}
	}
}
