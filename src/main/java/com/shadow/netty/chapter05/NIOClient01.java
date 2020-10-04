package com.shadow.netty.chapter05;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIOClient01 {
	public static void main(String[] args) throws Exception{

		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);

		Selector selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT); // 客户端关注 连接事件

		socketChannel.connect(new InetSocketAddress("localhost",8899));

		while (true) {
			// 一定要调用
			selector.select();

			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			for (SelectionKey selectionKey : selectionKeys) {
				if(selectionKey.isConnectable()) { // 连接事件
					SocketChannel client = (SocketChannel) selectionKey.channel();
					if(client.isConnectionPending()) { // 连接中
						client.finishConnect(); // 完成连接

						ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

						writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
						writeBuffer.flip();

						client.write(writeBuffer);

						// 开启线程
						ExecutorService executorService = Executors.newSingleThreadExecutor();
						executorService.submit(() ->{
							while (true) {
								try {
									// 清理
									writeBuffer.clear();

									InputStreamReader reader = new InputStreamReader(System.in);
									BufferedReader br = new BufferedReader(reader);

									String msg = br.readLine();

									writeBuffer.put(msg.getBytes());
									writeBuffer.flip();
									client.write(writeBuffer);
								}catch (Exception e){
									e.printStackTrace();
								}
							}
						});
					}

					client.register(selector, SelectionKey.OP_READ); // 注册读事件

				}else if(selectionKey.isReadable()) { // 处理读事件
					SocketChannel client = (SocketChannel) selectionKey.channel();
					ByteBuffer readBuffer = ByteBuffer.allocate(1024);

					int read = client.read(readBuffer);

					if(read > 0) {
						String receivedMsg = new String(readBuffer.array(), 0, read);
						System.out.println(receivedMsg);
					}
				}
				// 移除
				//selectionKeys.clear();
				selectionKeys.remove(selectionKey);
			}
		}
	}
}
