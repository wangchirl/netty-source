package com.shadow.netty.chapter09.demo03;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 * 解码器
 */
public class MessageDecoder extends ReplayingDecoder<Void> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("MessageDecoder decode invoked....");
		int length = in.readInt();
		byte[] content = new byte[length];
		in.readBytes(content);

		MessageProtocol messageProtocol = new MessageProtocol();
		messageProtocol.setLength(length);
		messageProtocol.setContent(content);

		out.add(messageProtocol);
	}
}
