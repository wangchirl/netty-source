package com.shadow.netty.chapter09.demo01;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 * 入站处理器
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("byte to long decoder ByteToMessageDecoder invoked...");

		System.out.println(in.readableBytes());

		if(in.readableBytes() >= 8) {
			out.add(in.readLong());
		}
	}
}
