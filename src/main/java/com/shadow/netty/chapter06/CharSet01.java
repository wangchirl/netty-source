package com.shadow.netty.chapter06;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.SortedMap;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class CharSet01 {
	public static void main(String[] args) throws Exception{
		String inputFile = "CharSet_In.txt";
		String outputFile = "CharSet_Out.txt";

		RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
		RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

		long inputLength = new File(inputFile).length();

		FileChannel inputRandomAccessFileChannel = inputRandomAccessFile.getChannel();
		FileChannel outputRandomAccessFileChannel = outputRandomAccessFile.getChannel();

		MappedByteBuffer buffer = inputRandomAccessFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);


		System.out.println("-------------charset-----------------");

		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		charsets.forEach((k,v) ->{
			System.out.println("k: " + k + "| v: " + v);
		});

		System.out.println("-------------charset-----------------");


		//Charset charset = Charset.forName("utf-8");
		Charset charset = Charset.forName("iso-8859-1");
		CharsetDecoder decoder = charset.newDecoder();
		CharsetEncoder encoder = charset.newEncoder();

		CharBuffer charBuffer = decoder.decode(buffer);

		// UTF-8 三个字节表示一个汉字 iso-8859-1 一个字节来解码，过程中出现乱码，但最终是不会乱码的，不会存在字节的丢失
		System.out.println(charBuffer.get(14));

		ByteBuffer byteBuffer = encoder.encode(charBuffer);

		outputRandomAccessFileChannel.write(byteBuffer);

		inputRandomAccessFile.close();
		outputRandomAccessFile.close();
	}
}
