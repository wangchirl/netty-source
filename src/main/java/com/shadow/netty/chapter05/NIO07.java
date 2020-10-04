package com.shadow.netty.chapter05;

import java.nio.ByteBuffer;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 *
 * 只读 Buffer，我们可以随时将一个普通额Buffer调用asReadOnlyBuffer方法返回一个只读Buffer
 * 但是不同将一个只读Buffer转换为读写Buffer
 */
public class NIO07 {
	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);

		System.out.println(byteBuffer.getClass());

		for (int i = 0; i < byteBuffer.capacity(); i++) {
			byteBuffer.put((byte) i);
		}

		ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

		System.out.println(readOnlyBuffer.getClass());
		readOnlyBuffer.position(0);
		// readOnlyBuffer.put((byte) 2);

	}
}
