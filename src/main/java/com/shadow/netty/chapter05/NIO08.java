package com.shadow.netty.chapter05;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO08 {
	public static void main(String[] args) throws Exception{
		FileInputStream fileInputStream = new FileInputStream("NIO04in.txt");
		FileOutputStream fileOutputStream = new FileOutputStream("NIO04out.txt");

		FileChannel inputStreamChannel = fileInputStream.getChannel();
		FileChannel outputStreamChannel = fileOutputStream.getChannel();

		// ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		// 堆外内存 - 零拷贝
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);

		while (true) {
			// 这里需要注意
			byteBuffer.clear(); // position = 0,limit=capacity

			int read = inputStreamChannel.read(byteBuffer);
			System.out.println("read: " + read);
			if(-1 == read) {
				break;
			}

			byteBuffer.flip();

			outputStreamChannel.write(byteBuffer);
		}

		inputStreamChannel.close();
		outputStreamChannel.close();
	}
}
