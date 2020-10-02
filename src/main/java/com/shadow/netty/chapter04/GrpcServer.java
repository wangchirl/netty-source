package com.shadow.netty.chapter04;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 *	- rpc SayHello(HelloRequest) returns (HelloResponse);
 *	- rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);
 *	- rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
 *	- rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
 */
public class GrpcServer {

	private Server server;

	public static void main(String[] args) throws InterruptedException, IOException {
		GrpcServer grpcServer = new GrpcServer();

		grpcServer.start();
		grpcServer.awaitTerminated();

	}

	private void start() throws IOException {
		this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();
		System.out.println("server started");

		// 异步方法
		Runtime.getRuntime().addShutdownHook(new Thread(() ->{
			System.out.println("shutdown jvm");
			GrpcServer.this.stop();
		}));
	}

	private void stop() {
		if(null != this.server) this.server.shutdown();
	}


	private void awaitTerminated() throws InterruptedException {
		if(null != this.server) this.server.awaitTermination();
	}
}
