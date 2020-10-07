package com.shadow.netty.chapter09.demo01;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
//public class MyNettyServer01BusinessHandler extends SimpleChannelInboundHandler<Long> {
public class MyNettyServer01BusinessHandler extends SimpleChannelInboundHandler<String> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("msg = " + msg);
		System.out.println("msg type = " + msg.getClass());
		ctx.channel().writeAndFlush(500000L);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
