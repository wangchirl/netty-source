package com.shadow.netty.chapter09.demo01;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 * 入站处理器
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("byte to long decoder ReplayingDecoder invoked...");
		// 无需进行 in.readableBytes(); 判断
		out.add(in.readLong());
	}
}
