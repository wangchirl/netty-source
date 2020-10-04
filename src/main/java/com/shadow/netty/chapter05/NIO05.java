package com.shadow.netty.chapter05;

import java.nio.ByteBuffer;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 *
 * ByteBuffer 类型化的 put 与 get
 */
public class NIO05 {
	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(64);

		byteBuffer.putInt(10);
		byteBuffer.putLong(2000000000L);
		byteBuffer.putDouble(3.1415926);
		byteBuffer.putChar('C');
		byteBuffer.putShort((short) 3);
		byteBuffer.putChar('钦');

		byteBuffer.flip();

		System.out.println(byteBuffer.getInt());
		System.out.println(byteBuffer.getLong());
		System.out.println(byteBuffer.getDouble());
		System.out.println(byteBuffer.getChar());
		System.out.println(byteBuffer.getShort());
		System.out.println(byteBuffer.getChar());

	}
}
