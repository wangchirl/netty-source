package com.shadow.netty.chapter09.demo03;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class NettyClient01Handler extends SimpleChannelInboundHandler<MessageProtocol> {

	private int count;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
		int length = msg.getLength();
		byte[] content = msg.getContent();

		System.out.println("客户端接收到消息：");
		System.out.println("长度：" + length);
		System.out.println("内容：" + new String(content,Charset.forName("utf-8")));

		System.out.println("客户端接收到消息数量：" + (++count));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			String msg = "send from client";
			byte[] content = msg.getBytes("utf-8");
			int length = content.length;

			MessageProtocol messageProtocol = new MessageProtocol();
			messageProtocol.setLength(length);
			messageProtocol.setContent(content);
			ctx.writeAndFlush(messageProtocol);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
