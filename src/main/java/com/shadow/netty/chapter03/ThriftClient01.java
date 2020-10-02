package com.shadow.netty.chapter03;

import com.shadow.netty.thrift.gegerted.Person;
import com.shadow.netty.thrift.gegerted.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 */
public class ThriftClient01 {
	public static void main(String[] args) {
		TTransport transport = new TFramedTransport(new TSocket("localhost",8888),600);
		TProtocol protocol = new TCompactProtocol(transport);
		PersonService.Client client = new PersonService.Client(protocol);

		try {
			transport.open();

			Person person = client.getPersonByUsername("张三");

			System.out.println(person.getUsername());
			System.out.println(person.getAge());
			System.out.println(person.isMarried());

			Person p = new Person();
			p.setUsername("李四").setAge(30).setMarried(true);
			client.savePerson(p);
		} catch (Exception e){
			e.printStackTrace();
		} finally {
		 transport.close();
		}
	}
}
