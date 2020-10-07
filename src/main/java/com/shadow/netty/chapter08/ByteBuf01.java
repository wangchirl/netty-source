package com.shadow.netty.chapter08;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class ByteBuf01 {
	public static void main(String[] args) {
		ByteBuf byteBuf = Unpooled.buffer(10);

		for (int i = 0; i < 10; i++) {
			byteBuf.writeByte(i);
		}

		for (int i = 0; i < byteBuf.capacity(); i++) {
			System.out.println(byteBuf.getByte(i));
		}
		// readerIndex未改变
		System.out.println("readerIndex = " + byteBuf.readerIndex());
		for (int i = 0; i < byteBuf.capacity(); i++) {
			System.out.println(byteBuf.readByte());
		}
		// readerIndex被改变
		System.out.println("readerIndex = " + byteBuf.readerIndex());
	}
}
