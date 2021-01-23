[["java:package:com.hqu"]]
#include "model.ice" //引入其他模块
module animal{
	sequence<People> peoples; // 声明集合要先声明在外面
	sequence<peoples> peopless; // 集合的集合
	dictionary<int,int> dic; // Map类型
	
	/**
	 * 定义学生类(各种类型都实现)
	 */
	struct Student{
		//short grade; // 年级
		//int age;// 年纪
		//long bornTime; // 出生日期
		//float heitht; // 身高
		//double score = 100; //分数
		string name; //名字
		
		// 复合数据类型(注意：声明的变量不能和实体名一样)
		//Gender g = Man; //声明枚举
		//People p;
		//peoples ps; // 集合
		//peopless pss; // 集合的集合
		//dic d; // Map类型
	};
	
	/**
	 * 吃东西接口
	 */
	interface Eat{
		/**
		 * 吃橘子
		 */
		Student eatOrange(Student stu);
	};
	
	exception Error {} ; // 空异常是允许的
	exception RangeError{
		People errorPeople;
	};	
};