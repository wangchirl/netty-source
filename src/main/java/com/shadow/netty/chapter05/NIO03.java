package com.shadow.netty.chapter05;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 *
 * 写入
 */
public class NIO03 {
	public static void main(String[] args) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream("NIO03.txt");
		FileChannel fileChannel = fileOutputStream.getChannel();

		ByteBuffer byteBuffer = ByteBuffer.allocate(512);

		byte[] bytes = "hello shadow ~".getBytes();

		for (int i = 0; i < bytes.length; i++) {
			byteBuffer.put(bytes[i]);
		}
		// 反转
		byteBuffer.flip();

		fileChannel.write(byteBuffer);

		fileOutputStream.close();

	}
}
