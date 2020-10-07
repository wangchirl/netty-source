package com.shadow.netty.chapter09.demo01;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class MyLongToStringDecoder extends MessageToMessageDecoder<Long> {

	@Override
	protected void decode(ChannelHandlerContext ctx, Long msg, List<Object> out) throws Exception {
		System.out.println("message to message invoked...");
		out.add(String.valueOf(msg));
	}
}
