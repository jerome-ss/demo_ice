package com.lenderus.helloworld.service;

import com.hqu.animal.Student;
import com.hqu.animal._EatDisp;

import Ice.Current;

public class MyServiceImpl extends _EatDisp {

	private static final long serialVersionUID = 7114601588161119171L;

	/**
	 * 吃橘子
	 */
	@Override
	public Student eatOrange(Student stu, Current __current) {
		System.out.println("earOrange is wok and name is change ..");
		stu.name = "name is change ~";
		return stu;
	}

}
