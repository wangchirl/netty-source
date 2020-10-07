package com.shadow.netty.chapter09.demo01;

import com.google.common.primitives.Chars;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class MyNettyClient01BusinessHandler extends SimpleChannelInboundHandler<Long> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
		System.out.println(ctx.channel().remoteAddress());
		System.out.println("client output : " + msg);
		// 非Long 的消息被丢弃掉
		ctx.writeAndFlush("from client：" + LocalDateTime.now());
		ctx.writeAndFlush(100L);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(1234567L);
		ctx.writeAndFlush(1L);
		ctx.writeAndFlush(2L);
		ctx.writeAndFlush(3L);
		ctx.writeAndFlush(4L);
		ctx.writeAndFlush(5L);
//		ctx.writeAndFlush(Unpooled.copiedBuffer("helloworld",Charset.forName("utf-8")));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
