package com.shadow.netty.chapter05;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO01 {
	public static void main(String[] args) {

		IntBuffer buffer = IntBuffer.allocate(10);

		System.out.println("capacity = " + buffer.capacity());

		for (int i = 0; i < buffer.capacity(); i++) {
			int nextInt = new SecureRandom().nextInt(20);
			buffer.put(nextInt);
		}
		// 位置反转
		buffer.flip();

		while (buffer.hasRemaining()) {
			System.out.println(buffer.get());
		}
	}
}
