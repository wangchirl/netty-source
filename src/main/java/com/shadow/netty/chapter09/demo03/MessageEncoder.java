package com.shadow.netty.chapter09.demo03;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
		System.out.println("MessageEncoder invoked...");
		out.writeInt(msg.getLength());
		out.writeBytes(msg.getContent());
	}
}
