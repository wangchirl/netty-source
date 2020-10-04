package com.shadow.netty.chapter05;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author shadow
 * @create 2020-10-03
 * @description
 */
public class NIOServer01 {

	private static Map<String,SocketChannel> clients = new HashMap<>();

	public static void main(String[] args) throws Exception{
		// 样板式代码
		// 1、ServerSockeChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(8899));

		// 2、Selector
		Selector selector = Selector.open();
		// TODO ① 将 serverSocketChannel 注册到 selector 对象上
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 关注 ACCEPT 事件

		while (true) {
			try {
				// selector 关注的事件数
				selector.select();

				Set<SelectionKey> selectionKeys = selector.selectedKeys();

				selectionKeys.forEach(selectionKey -> {
					final SocketChannel client;
					try {
						if(selectionKey.isAcceptable()) { // 连接事件处理
							// TODO ① 这里一定是 ServerSocketChannel
							ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
							client = server.accept();
							client.configureBlocking(false);
							// TODO ② 将 SocketChannel 注册到 selector 对象上
							client.register(selector, SelectionKey.OP_READ); // 关注 READ 事件

							// 记录客户端 sockeChannel
							String key = UUID.randomUUID().toString();
							clients.put(key, client);
						}else if(selectionKey.isReadable()) {// 可读事件处理
							// TODO ② 这里一定是 SocketChannel
							client = (SocketChannel) selectionKey.channel();
							client.configureBlocking(false);
							// 3、Buffer
							ByteBuffer readBuffer = ByteBuffer.allocate(1024);

							int count = client.read(readBuffer);

							if(count > 0) {
								readBuffer.flip();

								Charset charset = Charset.forName("utf-8");
								String receivedMsg = String.valueOf(charset.decode(readBuffer).array());

								System.out.println(client + " : " + receivedMsg);

								// 找到事件触发的 client
								String senderKey = null;
								for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
									if(client == entry.getValue()) {
										senderKey = entry.getKey();
										break;
									}
								}
								// 群发
								for (Map.Entry<String, SocketChannel> entry : clients.entrySet()) {
									SocketChannel value = entry.getValue();

									ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

									writeBuffer.put((senderKey + " : " + receivedMsg).getBytes());
									writeBuffer.flip();

									value.write(writeBuffer);
								}
							}
						}

						// TODO 一定要移除
						// selectionKeys.clear();
						selectionKeys.remove(selectionKey);
					} catch (Exception e){
						e.printStackTrace();
					}
				});
			} catch (Exception e){
				e.printStackTrace();
			}
		}

	}
}
