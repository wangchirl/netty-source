package com.shadow.netty.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author shadow
 * @create 2020-09-28
 * @description
 *
 *  HTTP 服务器
 *
 * 1、curl "http://localhost:8080"
 */
public class NettyServer {
	public static void main(String[] args) {
		// 事件循环组
		// bossGroup 接收连接不处理请求，将请求转发给 workGroup
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

		// 服务端启动器
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new MyChannelInitializer()); // 通道初始化器

		try {
			// 绑定ip端口
			ChannelFuture channelFuture = serverBootstrap.bind("localhost", 8080).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			// 优雅关闭
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	static class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
						// HttpServerCodec 编解码通道处理器 HttpRequestDecoder、HttpResponseEncoder的合并
			pipeline.addLast("httpServerCodec",new HttpServerCodec())
						// StringEncoder、StringDecoder字符串编解码处理器
					.addLast("stringDec",new StringDecoder())
					.addLast(new StringEncoder())
						// 业务处理 handler
					.addLast("httpHandler",new MyChannelInBuoundHandler());
		}
	}

	// ChannelInboundHandlerAdapter 子类  SimpleChannelInboundHandler
	static class MyChannelInBuoundHandler extends SimpleChannelInboundHandler<HttpObject> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
			// msg 类型
			System.out.println(msg.getClass());
			// 远程通道地址
			System.out.println(ctx.channel().remoteAddress());
			// 可进行查看 remote address
			Thread.sleep(8000);

			if(msg instanceof HttpRequest) {
				HttpRequest httpRequest = (HttpRequest)msg;

				System.out.println("请求方法名：" + httpRequest.method().name());

				URI uri = new URI(httpRequest.uri());
				if("/favicon.ico".equals(uri.getPath())) {
					System.out.println(uri.getPath());
				}
				ByteBuf content = Unpooled.copiedBuffer("Hello World ~",CharsetUtil.UTF_8);
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1
						, HttpResponseStatus.OK, content);
				response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
				// 写回响应
				ctx.writeAndFlush(response);
				// 关闭通道 - 服务端主动关闭（可根据连接的保持时间来定时关闭）
				ctx.channel().close();
			}
		}

		// 3.active
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channel active");
			super.channelActive(ctx);
		}

		// 2.registered
		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channel registered");
			super.channelRegistered(ctx);
		}

		// 1.added
		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			System.out.println("handler added");
			super.handlerAdded(ctx);
		}

		// 4.inActive
		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channel inActive");
			super.channelInactive(ctx);
		}

		// 5.unRegistered
		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channel unRegistered");
			super.channelUnregistered(ctx);
		}
	}
}
