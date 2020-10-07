package com.shadow.netty.chapter08;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author shadow
 * @create 2020-10-06
 * @description
 */
public class AtomicIntegerFieldUpdater01 {
	public static void main(String[] args) throws InterruptedException {
		Person p = new Person();
		for (int i = 1; i <=10 ; i++) {
		    new Thread(() -> {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(p.age++);
		    },String.valueOf(i)).start();
		}

		Thread.sleep(2000);

		AtomicIntegerFieldUpdater<Person> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater
				.newUpdater(Person.class,"age");
		Person person = new Person();

		for (int i = 1; i <=10 ; i++) {
		    new Thread(() -> {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(atomicIntegerFieldUpdater.getAndIncrement(person));
		    },String.valueOf(i)).start();
		}



	}



	static class Person {
		/*static*/ volatile /*Integer*/ int age = 1;
	}
}
