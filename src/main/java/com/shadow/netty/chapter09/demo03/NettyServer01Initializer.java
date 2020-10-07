package com.shadow.netty.chapter09.demo03;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author shadow
 * @create 2020-10-07
 * @description
 */
public class NettyServer01Initializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new MessageDecoder()) // TODO 添加自定义的编解码器
				.addLast(new MessageEncoder()) // TODO 添加自定义的编解码器
				.addLast(new NettyServer01Handler());
	}
}
