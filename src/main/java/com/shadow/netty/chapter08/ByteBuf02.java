package com.shadow.netty.chapter08;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class ByteBuf02 {
	public static void main(String[] args) {
		// UTF-8 一个中文占 三个字节
		ByteBuf byteBuf = Unpooled.copiedBuffer("钦hello world",Charset.forName("utf-8"));
		// byteBuf.hasArray() == true 表示为 堆上缓冲
		if(byteBuf.hasArray()) {
			byte[] content = byteBuf.array();
			System.out.println(new String(content,Charset.forName("utf-8")));
		}
		System.out.println(byteBuf);

		System.out.println("arrayOffset = " + byteBuf.arrayOffset());
		System.out.println("readerIndex = " + byteBuf.readerIndex());
		System.out.println("writerIndex = " + byteBuf.writerIndex());
		System.out.println("capacity = " + byteBuf.capacity());

		int readableBytes = byteBuf.readableBytes();
		System.out.println("readableBytes = " + readableBytes);

		int writableBytes = byteBuf.writableBytes();
		System.out.println("writableBytes = " + writableBytes);

		for (int i = 0; i < byteBuf.readableBytes(); i++) {
			System.out.println((char) byteBuf.getByte(i));
		}

		System.out.println(byteBuf.getCharSequence(0,4,Charset.forName("utf-8")));
		System.out.println(byteBuf.getCharSequence(4,6,Charset.forName("utf-8")));

	}
}
