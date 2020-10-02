package com.shadow.netty.chapter02;

import com.google.protobuf.MessageLite;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.util.Random;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 */
public class NettyClient01 {
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new MyNettyClient01Initializer());
		try {
			ChannelFuture channelFuture = bootstrap.connect("localhost", 8090).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
	static class MyNettyClient01Initializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new ProtobufVarint32FrameDecoder())
//					.addLast(new ProtobufDecoder(DataInfo.Student.getDefaultInstance()))
					.addLast(new ProtobufDecoder(GeneralMessage.Message.getDefaultInstance()))
					.addLast(new ProtobufVarint32LengthFieldPrepender())
					.addLast(new ProtobufEncoder())
					.addLast(new MyNettyClient01ChannleInBoundHandler());
		}
	}

//	static class MyNettyClient01ChannleInBoundHandler extends SimpleChannelInboundHandler<DataInfo.Student> {
	static class MyNettyClient01ChannleInBoundHandler extends SimpleChannelInboundHandler<GeneralMessage.Message> {
		@Override
//		protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {
		protected void channelRead0(ChannelHandlerContext ctx, GeneralMessage.Message msg) throws Exception {

		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			// 版本 1 - 只支持特定的类型
			/*DataInfo.Student student = DataInfo.Student.newBuilder()
					.setName("李四")
					.setAge(18)
					.setAddress("张家界").build();
			ctx.writeAndFlush(student);*/

			// 版本 2 - 支持多种类型
			GeneralMessage.Message message = null;
			int random = new Random().nextInt(3);
			if(0 == random) {
				message = GeneralMessage.Message.newBuilder()
						.setDateType(GeneralMessage.Message.DataType.PersonType)
						.setPerson(GeneralMessage.Person.newBuilder().setName("张三").setAge(20).setAddress("张家界").build())
						.build();
			} else if(1 == random) {
				message = GeneralMessage.Message.newBuilder()
						.setDateType(GeneralMessage.Message.DataType.DogType)
						.setDog(GeneralMessage.Dog.newBuilder().setName("小黑").setAge(2).build())
						.build();
			}else {
				message = GeneralMessage.Message.newBuilder()
						.setDateType(GeneralMessage.Message.DataType.CatType)
						.setCat(GeneralMessage.Cat.newBuilder().setName("花花").setCity("深圳").build())
						.build();
			}
			ctx.writeAndFlush(message);
		}
	}
}
