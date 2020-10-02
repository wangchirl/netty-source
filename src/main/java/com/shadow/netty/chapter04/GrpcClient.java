package com.shadow.netty.chapter04;

import com.shadow.netty.chapter04.generate.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 */
public class GrpcClient {
	public static void main(String[] args) {

		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899)
				.usePlaintext().build();

		StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc
				.newBlockingStub(managedChannel);
		// 异步的方式 rpc GetStudentWrapperByAges(stream StudentRequest) returns (StudentResponseList) {}
		StudentServiceGrpc.StudentServiceStub stub = StudentServiceGrpc.newStub(managedChannel);

		// 1、rpc SayHello(HelloRequest) returns (HelloResponse);
		MyResponse response = blockingStub.getRealNameByUsername(MyRequest.newBuilder()
				.setUsername("李四").build());

		System.out.println(response.getRealname());

		// 2、 rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);
		System.out.println("-----------response stream start-----------");

		Iterator<StudentResponse> iterator = blockingStub.getStudentByAge(StudentRequest.newBuilder().setAge(20).build());

		while (iterator.hasNext()) {
			StudentResponse studentResponse = iterator.next();
			System.out.print(studentResponse.getName());
			System.out.print(studentResponse.getAge());
			System.out.println(studentResponse.getCity());
		}
		System.out.println("-----------response stream end-----------");

		// 3、rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
		System.out.println("-----------request stream start-----------");
		StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
			@Override
			public void onNext(StudentResponseList value) {
				value.getStudentResponseList().forEach(response ->{
					System.out.print(response.getName());
					System.out.print(response.getAge());
					System.out.println(response.getCity());
				});
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				System.out.println("completed");
			}
		};
		// 异步的 - stub
		StreamObserver<StudentRequest> studentRequestStreamObserver = stub.getStudentWrapperByAges(studentResponseListStreamObserver);

		studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(20).build());
		studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(30).build());
		studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(40).build());
		studentRequestStreamObserver.onCompleted();
		System.out.println("-----------request stream end-----------");


		// 4、rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
		System.out.println("-----------request response stream start-----------");

		StreamObserver<StreamRequest> streamRequestStreamObserver = stub.biTalk(new StreamObserver<StreamResponse>() {
			@Override
			public void onNext(StreamResponse value) {
				System.out.println(value.getResponseInfo());
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				System.out.println("onCompleted");
			}
		});

		for (int i = 0; i < 10; i++) {
			streamRequestStreamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
			try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
		}

		System.out.println("-----------request response stream end-----------");


		try {
			System.in.read(); // 阻塞住看执行结果
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
