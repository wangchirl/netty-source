package com.shadow.netty.chapter09.demo02;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class MyNettyServer01Handler extends SimpleChannelInboundHandler<ByteBuf> {

	private int count;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[] buffer = new byte[msg.readableBytes()];
		msg.readBytes(buffer);

		String message = new String(buffer, Charset.forName("utf-8"));
		System.out.println("服务端接收到消息内容：" + message);
		System.out.println("服务端接收到的消息数量：" + (++this.count));

		ByteBuf res = Unpooled.copiedBuffer(UUID.randomUUID().toString(), Charset.forName("utf-8"));

		ctx.writeAndFlush(res);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
