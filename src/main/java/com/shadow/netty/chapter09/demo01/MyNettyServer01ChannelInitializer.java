package com.shadow.netty.chapter09.demo01;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class MyNettyServer01ChannelInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline
//				.addLast(new MyByteToLongDecoder())
				.addLast(new MyByteToLongDecoder2()) // 这里和MyLongToStringDecoder注意顺序
				.addLast(new MyLongToStringDecoder())
				.addLast(new MyLongToByteEncoder())
				.addLast(new MyNettyServer01BusinessHandler());

	}
}
