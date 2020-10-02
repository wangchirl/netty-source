package com.shadow.netty.chapter04;

import com.shadow.netty.chapter04.generate.*;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 */
public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

	// 1、rpc SayHello(HelloRequest) returns (HelloResponse);
	@Override
	public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
		System.out.println("接收到客户端消息：" + request.getUsername());

		// 返回数据
		responseObserver.onNext(MyResponse.newBuilder().setRealname("shadow").build());
		responseObserver.onCompleted();
	}

	// 2、 rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);
	@Override
	public void getStudentByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
		System.out.println("接收到客户端信息：" + request.getAge());

		responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(20).setCity("深圳").build());
		responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(19).setCity("北京").build());
		responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(18).setCity("上海").build());
		responseObserver.onCompleted();
	}

	// 3、rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
	@Override
	public StreamObserver<StudentRequest> getStudentWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
		return new StreamObserver<StudentRequest>() {
			@Override
			public void onNext(StudentRequest value) {
				System.out.println("onNext：" + value.getAge());
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				StudentResponse studentResponse1 = StudentResponse.newBuilder().setName("jack").setAge(30).setCity("深圳").build();
				StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("rose").setAge(26).setCity("张家界").build();

				StudentResponseList studentResponseList = StudentResponseList.newBuilder().addStudentResponse(studentResponse1).addStudentResponse(studentResponse2).build();

				responseObserver.onNext(studentResponseList);
				responseObserver.onCompleted();
			}
		};
	}

	// 4、rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
	@Override
	public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
		return new StreamObserver<StreamRequest>() {
			@Override
			public void onNext(StreamRequest value) {
				System.out.println(value.getRequestInfo());

				responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				responseObserver.onCompleted();
			}
		};
	}
}
