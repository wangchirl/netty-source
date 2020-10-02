package com.shadow.netty.chapter02;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author shadow
 * @create 2020-10-01
 * @description
 */
public class ProtoBufTest {
	public static void main(String[] args) throws InvalidProtocolBufferException {

		DataInfo.Student student = DataInfo.Student.newBuilder()
				.setName("张三").setAge(18).setAddress("深圳").build();

		byte[] bytes = student.toByteArray();

		DataInfo.Student student1 = DataInfo.Student.parseFrom(bytes);

		System.out.println(student1.getName());
		System.out.println(student1.getAge());
		System.out.println(student1.getAddress());

	}
}
