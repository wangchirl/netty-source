package com.shadow.netty.chapter05;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO11 {
	public static void main(String[] args) throws Exception{
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		serverSocketChannel.socket().bind(new InetSocketAddress(8899));

		int messageLength = 2 + 3 + 4;

		ByteBuffer[] buffers = new ByteBuffer[3];
		buffers[0] = ByteBuffer.allocate(2);
		buffers[1] = ByteBuffer.allocate(3);
		buffers[2] = ByteBuffer.allocate(4);

		SocketChannel socketChannel = serverSocketChannel.accept();

		while (true) {
			int bytesRead = 0;

			while (bytesRead < messageLength) {
				long read = socketChannel.read(buffers);
				bytesRead += read;

				System.out.println("bytesRead = " + bytesRead);
				Arrays.asList(buffers).stream().map(buffer ->{
					return "position：" + buffer.position() + " limit：" + buffer.limit();
				}).forEach(System.out::println);
			}

			Arrays.asList(buffers).forEach(buffer ->{
				buffer.flip();
			});

			long bytesWritten = 0;
			while (bytesWritten < messageLength) {
				long write = socketChannel.write(buffers);
				bytesWritten += write;
			}

			Arrays.asList(buffers).forEach(buffer ->{
				buffer.clear();
			});

			System.out.println("bytesRead：" + bytesRead);
			System.out.println("bytesWritten：" + bytesWritten);
			System.out.println("messageLength：" + messageLength);
		}
	}
}
