package com.shadow.netty.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadow
 * @create 2020-09-29
 * @description
 *
 * 简易聊天程序：
 *
 * 测试：
 * 服务器启动 -> 客户端1建立连接、客户端2建立连接....
 * 客户端上线通知其他客户端xxx上线
 * 某一个客户端发送消息 -> 其余客户端收到xxx的消息，自己显示[自己] ->群发
 *
 */
public class NettyServer03 {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new MyNettyServer03ChannelInitializer());
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

	static class MyNettyServer03ChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new DelimiterBasedFrameDecoder(4096,Delimiters.lineDelimiter()))
					.addLast(new StringDecoder(CharsetUtil.UTF_8))
					.addLast(new StringEncoder(CharsetUtil.UTF_8))
					.addLast(new MyNettyServer03ChannelInBoundHandler());
		}
	}

	static class MyNettyServer03ChannelInBoundHandler extends SimpleChannelInboundHandler<String> {
		// 存储所有客户端
		private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

		// 客户端建立连接后 -> 广播xxx加入消息
		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			// 通知其他客户端
			channelGroup.writeAndFlush("【服务器】- "+ channel.remoteAddress() + " 加入\n");
			// 添加自己
			channelGroup.add(channel);
		}

		// 接收到客户端的消息 -> 广播发送消息
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			Channel channel = ctx.channel();
			channelGroup.forEach(ch -> {
				if(channel != ch) {
					ch.writeAndFlush(channel.remoteAddress() + " 发送的消息：" + msg + "\n");
				}else {
					ch.writeAndFlush("【自己】 " + msg + "\n");
				}
			});
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			System.out.println(channel.remoteAddress() + " 上线");
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			System.out.println(channel.remoteAddress() + " 下线");
		}

		// 客户端移除 -> 广播消息xxx离开
		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			Channel channel = ctx.channel();
			// channelGroup.remove(channel); 这里 ChannelGroup 会自动移除，调用当然也没问题
			System.out.println(channelGroup.size()); // 验证自动移除
			channelGroup.writeAndFlush("【服务器】- " + channel.remoteAddress() + "离开\n");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
		}
	}
}
