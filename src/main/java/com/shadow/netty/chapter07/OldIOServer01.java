package com.shadow.netty.chapter07;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shadow
 * @create 2020-10-04
 * @description
 */
public class OldIOServer01 {
	public static void main(String[] args) throws Exception{

		ServerSocket serverSocket = new ServerSocket(8899);

		while (true) {
			Socket socket = serverSocket.accept();

			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

			byte[] bytes = new byte[4096];

			while (true) {
				int readCount = dataInputStream.read(bytes,0,bytes.length);
				if(-1 == readCount) {
					break;
				}
			}
		}
	}
}
