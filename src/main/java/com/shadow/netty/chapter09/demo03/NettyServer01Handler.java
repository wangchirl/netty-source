package com.shadow.netty.chapter09.demo03;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class NettyServer01Handler extends SimpleChannelInboundHandler<MessageProtocol> {

	private int count;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
		int length = msg.getLength();
		byte[] content = msg.getContent();

		System.out.println("服务器接收到数据:");
		System.out.println("长度：" + length);
		System.out.println("内容：" + new String(content,Charset.forName("utf-8")));
		System.out.println("服务端收到的消息数量：" + (++count));

		String resMsg = UUID.randomUUID().toString();
		byte[] resContent = resMsg.getBytes();
		int resLength = resContent.length;

		MessageProtocol res = new MessageProtocol();
		res.setLength(resLength);
		res.setContent(resContent);

		ctx.writeAndFlush(res);
	}
}
