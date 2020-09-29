package com.shadow.netty.chapter01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;

/**
 * @author shadow
 * @create 2020-09-29
 * @description
 */
public class NettyClient02 {
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class) // 类比 Socket
				.handler(new MyNettyClient02ChannelInitializer());
		try {
			ChannelFuture channelFuture = bootstrap.connect("localhost", 8090).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	static class MyNettyClient02ChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
					.addLast(new LengthFieldPrepender(4))
					.addLast(new StringEncoder(CharsetUtil.UTF_8))
					.addLast(new StringDecoder(CharsetUtil.UTF_8))
					.addLast("myHandler",new MyNettyClient02ChannelInBoundHandler());
		}
	}

	static class MyNettyClient02ChannelInBoundHandler extends SimpleChannelInboundHandler<String> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			System.out.println(ctx.channel().remoteAddress());
			System.out.println("client received msg: " + msg);
			ctx.writeAndFlush("from client: " + LocalDateTime.now());
		}

		// 异常
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		// 1.客户端连接后发送数据 - 这里首先触发，不然客户端服务端都不会发送数据，等待触发
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			ctx.writeAndFlush("active");
		}
	}
}
