package com.shadow.netty.chapter05;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIO10 {

	public static void main(String[] args) throws Exception {
		RandomAccessFile accessFile = new RandomAccessFile("NIO10.txt", "rw");
		FileChannel fileChannel = accessFile.getChannel();

		FileLock fileLock = fileChannel.lock(3, 6, true);

		System.out.println("valid: " + fileLock.isValid());
		System.out.println("lock type : " + fileLock.isShared());

		fileLock.release();
		accessFile.close();
	}
}
