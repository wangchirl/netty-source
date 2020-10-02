package com.shadow.netty.chapter03;

import com.shadow.netty.thrift.gegerted.DataException;
import com.shadow.netty.thrift.gegerted.Person;
import com.shadow.netty.thrift.gegerted.PersonService;
import org.apache.thrift.TException;

/**
 * @author shadow
 * @create 2020-10-02
 * @description
 */
public class PersonServiceImpl implements PersonService.Iface {
	@Override
	public Person getPersonByUsername(String username) throws DataException, TException {
		System.out.println("getPersonByUsername Client Param = " + username);
		Person person = new Person();
		person.setUsername(username).setAge(20).setMarried(false);
		return person;
	}

	@Override
	public void savePerson(Person person) throws DataException, TException {
		System.out.println("savePerson Client Param = " + person);
		System.out.println(person.getUsername());
		System.out.println(person.getAge());
		System.out.println(person.isMarried());
	}
}
