package com.shadow.netty.chapter09.demo01;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 * 出站处理器
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
		System.out.println("long to byte encoder MessageToByteEncoder invoked...");
		System.out.println(msg);
		out.writeLong(msg);
	}
}
