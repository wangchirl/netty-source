package com.shadow.netty.chapter05;

import java.nio.ByteBuffer;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO06 {
	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);

		for (int i = 0; i < byteBuffer.capacity(); i++) {
			byteBuffer.put((byte) i);
		}

		byteBuffer.position(2);
		byteBuffer.limit(6);

		ByteBuffer sliceBuffer = byteBuffer.slice();

		for (int i = 0; i < sliceBuffer.capacity(); i++) {
			byte b = sliceBuffer.get();
			b *= b;
			sliceBuffer.put(i,b);
		}

		byteBuffer.position(0);
		byteBuffer.limit(byteBuffer.capacity());

		while (byteBuffer.hasRemaining()) {
			System.out.println(byteBuffer.get());
		}

	}
}
