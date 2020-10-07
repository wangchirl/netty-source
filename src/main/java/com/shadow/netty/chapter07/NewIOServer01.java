package com.shadow.netty.chapter07;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author shadow
 * @create 2020-10-04
 * @description
 */
public class NewIOServer01 {
	public static void main(String[] args) throws Exception{

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		ServerSocket serverSocket = serverSocketChannel.socket();

		serverSocket.setReuseAddress(true);// 连接复用
		serverSocket.bind(new InetSocketAddress(8899));

		ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

		while (true) {
			SocketChannel socketChannel = serverSocketChannel.accept();

			socketChannel.configureBlocking(true); // 阻塞的

			int readCount = 0;
			while (-1 != readCount) {
				try {
					readCount = socketChannel.read(byteBuffer);
				} catch (Exception e) {

				}
				byteBuffer.rewind(); // position = 0
				//byteBuffer.position(0);
			}
		}
	}
}
