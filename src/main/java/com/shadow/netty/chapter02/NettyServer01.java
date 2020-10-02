package com.shadow.netty.chapter02;

import com.google.protobuf.MessageLite;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 *
 * Protobuf 的使用
 * Protobuf 编解码器：（注意addLast顺序）
 *  - ProtobufVarint32FrameDecoder
 *  - ProtobufDecoder
 *  - ProtobufVarint32LengthFieldPrepender
 *  - ProtobufEncoder
 *
 * 如何使其支持所有的数据类型进行处理？
 * 1、自定义协议（Netty官方示例）
 * 2、规范IDL消息定义方式（消息枚举）
 *
 */
public class NettyServer01 {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new MyNettyServer01Initializer());
		try {
			ChannelFuture channelFuture = serverBootstrap.bind("localhost", 8090).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	static class MyNettyServer01Initializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
					// 解码器 - 参数 MessageLite - 要转换类的实例 protoc生成的文件
			pipeline.addLast(new ProtobufVarint32FrameDecoder())
//					.addLast(new ProtobufDecoder(DataInfo.Student.getDefaultInstance()))
					.addLast(new ProtobufDecoder(GeneralMessage.Message.getDefaultInstance()))
					.addLast(new ProtobufVarint32LengthFieldPrepender())
					.addLast(new ProtobufEncoder())
					.addLast(new MyNettyServer01ChannelInBoundHandler()); // 自己的
		}
	}
	// 这里的泛型就是解码器中的类型 MessageLite
//	static class MyNettyServer01ChannelInBoundHandler extends SimpleChannelInboundHandler<DataInfo.Student> {
	static class MyNettyServer01ChannelInBoundHandler extends SimpleChannelInboundHandler<GeneralMessage.Message> {
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, GeneralMessage.Message msg) throws Exception {
			// 版本 1
			/*System.out.println(msg.getName());
			System.out.println(msg.getAge());
			System.out.println(msg.getAddress());*/

			// 版本 2
			GeneralMessage.Message.DataType dateType = msg.getDateType();
			if(dateType == GeneralMessage.Message.DataType.PersonType) {
				GeneralMessage.Person person = msg.getPerson();
				System.out.println(person.getName());
				System.out.println(person.getAge());
				System.out.println(person.getAddress());
			}else if(dateType == GeneralMessage.Message.DataType.DogType){
				GeneralMessage.Dog dog = msg.getDog();
				System.out.println(dog.getName());
				System.out.println(dog.getAge());
			}else {
				GeneralMessage.Cat cat = msg.getCat();
				System.out.println(cat.getName());
				System.out.println(cat.getCity());
			}
		}
	}
}
