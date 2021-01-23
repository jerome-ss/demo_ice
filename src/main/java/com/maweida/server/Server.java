package com.maweida.server;

import com.maweida.servant.PrinterI;

public class Server {

	public static void main(String[] args) {
		System.out.println("服务端开始启动");
		int status = 0;
		Ice.Communicator ic = null;
		try{
			//初始化连接
			ic = Ice.Util.initialize(args);
			//创建名为SimplePrinterAdapter的适配器，并要求适配器使用缺省的协议(TCP/IP侦听端口为10000的请求) 
			Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints("SimplePrinterAdapter", "default -h 127.0.0.1 -p 10001");
			//实例化一个PrinterI对象，为Printer接口创建一个服务对象
			Ice.Object object = new PrinterI();
			//将服务单元增加到适配器中，并给服务对象指定名称为SimplePrinter，该名称用于唯一确定一个服务单元     
			adapter.add(object, Ice.Util.stringToIdentity("SimplePrinter"));
			//激活适配器
			adapter.activate();
			//让服务在退出之前，一直持续对请求的监听
			ic.waitForShutdown();
		}catch(Ice.LocalException e){
			e.printStackTrace();
			status = 1;
		}catch(Exception e){
			e.printStackTrace();
			status = 1;
		}finally{
			if(ic != null){
				ic.destroy();
			}
		}
		System.exit(status);
	}
}
