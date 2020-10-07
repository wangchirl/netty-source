package com.shadow.netty.chapter07;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @author shadow
 * @create 2020-10-04
 * @description
 */
public class OldIOClient01 {
	public static void main(String[] args) throws Exception{

		Socket socket = new Socket("localhost", 8899);

		String fileName = "F:\\pycharm2018破解版\\pycharm2018破解版.rar";

		FileInputStream fileInputStream = new FileInputStream(fileName);

		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
		byte[] buffer = new byte[4096];
		long readCount;
		long total = 0;

		long startTime = System.currentTimeMillis();

		while ((readCount = fileInputStream.read(buffer)) >= 0) {
			total += readCount;
			dataOutputStream.write(buffer);
		}
		System.out.println("发送总字节数：" + total + ",耗时：" + (System.currentTimeMillis() - startTime));

		dataOutputStream.close();
		socket.close();
		fileInputStream.close();
	}
}
