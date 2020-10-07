package com.shadow.netty.chapter09.demo02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class MyNettyClient01Handler extends SimpleChannelInboundHandler<ByteBuf> {

	private int count;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[] bytes = new byte[msg.readableBytes()];
		msg.readBytes(bytes);

		String message = new String(bytes, Charset.forName("utf-8"));
		System.out.println("客户端收到消息内容：" + message);
		System.out.println("客户端收到消息的数量：" + (++count));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ByteBuf byteBuf = Unpooled.copiedBuffer("send from client ", Charset.forName("utf-8"));
			ctx.writeAndFlush(byteBuf);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
