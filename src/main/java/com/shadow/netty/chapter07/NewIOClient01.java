package com.shadow.netty.chapter07;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author shadow
 * @create 2020-10-04
 * @description
 */
public class NewIOClient01 {
	public static void main(String[] args) throws Exception{

		SocketChannel socketChannel = SocketChannel.open();

		socketChannel.connect(new InetSocketAddress("localhost",8899));

		socketChannel.configureBlocking(true);

		String fileName = "F:\\pycharm2018破解版\\pycharm2018破解版.rar";

		FileChannel fileChannel = new FileInputStream(fileName).getChannel();

		long startTime = System.currentTimeMillis();
		// TODO 这里 Windows 系统下有点问题
		long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
		System.out.println("发送总字节数：" + count + ",耗时：" + (System.currentTimeMillis() - startTime));

		fileChannel.close();
	}
}
