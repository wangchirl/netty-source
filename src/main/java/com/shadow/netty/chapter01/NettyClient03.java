package com.shadow.netty.chapter01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author shadow
 * @create 2020-09-29
 * @description 简易聊天客户端
 */
public class NettyClient03 {
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new MyNettyClient03ChannelInitializer());
		try {
			ChannelFuture channelFuture = bootstrap.connect("localhost", 8090).sync();

			// 读取用户输入
			Channel channel = channelFuture.channel();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			for(;;){
				channel.writeAndFlush(br.readLine() + "\r\n");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	static class MyNettyClient03ChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new DelimiterBasedFrameDecoder(4096,Delimiters.lineDelimiter()))
					.addLast(new StringDecoder(CharsetUtil.UTF_8))
					.addLast(new StringEncoder(CharsetUtil.UTF_8))
					.addLast(new MyNettyClient03ChannelInBoundHandler());
		}
	}

	static class MyNettyClient03ChannelInBoundHandler extends SimpleChannelInboundHandler<String> {

		// 接收发送的消息进行打印
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
			System.out.println(msg);
		}
	}
}
