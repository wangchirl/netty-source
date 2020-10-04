package com.shadow.netty.chapter05;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO12 {
	public static void main(String[] args) throws Exception{

		int[] ports = new int[5];
		ports[0] = 5000;
		ports[1] = 5001;
		ports[2] = 5002;
		ports[3] = 5003;
		ports[4] = 5004;

		// 1、Selector
		Selector selector = Selector.open();

		// 2、Channel
		for (int i = 0; i < ports.length; i++) {
			ServerSocketChannel socketChannel = ServerSocketChannel.open();
			socketChannel.configureBlocking(false); // TODO 非阻塞
			socketChannel.socket().bind(new InetSocketAddress(ports[i]));

			socketChannel.register(selector,SelectionKey.OP_ACCEPT); // TODO 关注连接事件
			System.out.println("服务监听端口：" + ports[i]);
		}

		while (true){
			int numbers = selector.select();
			System.out.println("关注事件的 numbers = " + numbers);

			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				if(selectionKey.isAcceptable()) { // TODO 连接事件
					// TODO ServerSocketChannel
					ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);// TODO 非阻塞
					socketChannel.register(selector,SelectionKey.OP_READ); // TODO 关注读事件
					// TODO 一定要移除
					iterator.remove();
					System.out.println("客户端连接：" + socketChannel);
				}else if(selectionKey.isReadable()) { // TODO 读取事件
					// TODO SocketChannel
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

					int bytesRead = 0;
					while (true){
						// 3、Buffer
						ByteBuffer byteBuffer = ByteBuffer.allocate(512);

						byteBuffer.clear();

						int read = socketChannel.read(byteBuffer);

						if(read <= 0) {
							break;
						}

						byteBuffer.flip();

						socketChannel.write(byteBuffer);

						bytesRead += read;
					}

					System.out.println("读取：" + bytesRead + "来自于：" + socketChannel);
					// TODO 一定要移除
					iterator.remove();
				}
			}
		}
	}
}
