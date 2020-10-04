package com.shadow.netty.chapter05;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO09 {
	public static void main(String[] args) throws IOException {
		RandomAccessFile randomAccessFile = new RandomAccessFile("NIO09.txt", "rw");
		FileChannel channel = randomAccessFile.getChannel();

		MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

		mappedByteBuffer.put(0, (byte) 'a');
		mappedByteBuffer.put(3, (byte) 'b');

		randomAccessFile.close();
	}
}
