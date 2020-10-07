package com.shadow.netty.chapter09.demo02;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class MyNettyServer01Initializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new MyNettyServer01Handler());
	}
}
