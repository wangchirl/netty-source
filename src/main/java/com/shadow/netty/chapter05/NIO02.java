package com.shadow.netty.chapter05;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 *
 * java.io -> java.nio
 *
 * 读取
 */
public class NIO02 {
	public static void main(String[] args) throws IOException {

		FileInputStream fileInputStream = new FileInputStream("NIO02.txt");

		FileChannel fileChannel = fileInputStream.getChannel();

		ByteBuffer byteBuffer = ByteBuffer.allocate(512);
		fileChannel.read(byteBuffer);
		// 反转
		byteBuffer.flip();

		while (byteBuffer.remaining() > 0) {
			byte b = byteBuffer.get();
			System.out.println("character：" + (char)b);
		}

		fileInputStream.close();
	}
}
