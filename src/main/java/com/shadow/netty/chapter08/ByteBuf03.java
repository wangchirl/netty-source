package com.shadow.netty.chapter08;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class ByteBuf03 {
	public static void main(String[] args) {
		// composite buffer
		CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();

		// heap buffer
		ByteBuf heapBuf = Unpooled.buffer(10);
		// direct buffer
		ByteBuf directBuffer = Unpooled.directBuffer(8);
		// 添加
		compositeByteBuf.addComponents(heapBuf, directBuffer);
		// 删除 heapBuf
		//compositeByteBuf.removeComponent(0);

		Iterator<ByteBuf> iterator = compositeByteBuf.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

	}
}
