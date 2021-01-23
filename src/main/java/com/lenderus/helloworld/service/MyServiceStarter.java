package com.lenderus.helloworld.service;

public class MyServiceStarter {
	public static void main(String[] args) {
		int status = 0;
		Ice.Communicator ic = null;
		try {
			// 初始化Communicator对象,args可以传一些初始化参数,如连接超时,初始化客户端连接池的数量等
			ic = Ice.Util.initialize(args);
			// 创建名为MyServiceAdapter的ObjectAdapter,使用缺省的通信协议(TCP/IP端口为10000的请求)
			Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints("MyServiceAdapter", "default -p 10001");
			// 实例化一个MyService服务对象(Servant)
			MyServiceImpl servant = new MyServiceImpl();
			// 将Servant增加到ObjectAdapter中,并将Servant关联到ID为MyService的Ice Object
			adapter.add(servant, Ice.Util.stringToIdentity("MyService"));
			// 激活ObjectAdapter
			adapter.activate();
			// 让服务在退出之前,一直持续对请求的监听
			System.out.println("server is started ... ");
			ic.waitForShutdown();
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
