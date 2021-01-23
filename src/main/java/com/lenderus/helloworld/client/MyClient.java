package com.lenderus.helloworld.client;

import com.hqu.animal.EatPrx;
import com.hqu.animal.EatPrxHelper;
import com.hqu.animal.Student;

public class MyClient {
	public static void main(String[] args) {
		int status = 0;
		Ice.Communicator ic = null;
		try {
			// 初始化通信容器
			ic = Ice.Util.initialize(args);
			// 传入远程服务单元的名称、网络协议、IP及端口，构造一个Proxy对象
			Ice.ObjectPrx base = ic.stringToProxy("MyService:default -p 10001");
			// 通过checkCast向下转型，获取MyService接口的远程，并同时检测根据传入的名称获取服务单元是否OnlineBook的代理接口
			EatPrx prxy = EatPrxHelper.checkedCast(base);
//			EatPrx prxy = (EatPrx)IceClientUtil.getServicePrx(EatPrx.class);
			if (prxy == null) {
				throw new Error("Invalid proxy");
			}
			// 调用服务方法
			//普通调用传入的实体不会被修改
			System.out.println("br");
			Student stu = new Student();
			stu.name = "jerome";
			System.out.println("client student name is :" + stu.name);
			Student s = prxy.eatOrange(stu);
			System.out.println("server return student name is :" + s.name);
			
		} catch (Exception e) {
			e.printStackTrace();
			status = 1;
		} finally {
			if (ic != null) {
				ic.destroy();
			}
		}
		System.exit(status);
	}
}
