package com.maweida.client;

import com.maweida.PrinterPrx;
import com.maweida.PrinterPrxHelper;

public class Client {
	
	public static void main(String[] args) {
		System.out.println("客户端开始访问");
		int status = 0;
		Ice.Communicator ic = null;
		try {
			//初始化通信器
			ic = Ice.Util.initialize(args);
			//传入远程服务单元的名称、网络协议、IP及端口，获取Printer的远程代理，这里使用的是stringToProxy方式
			Ice.ObjectPrx base = ic.stringToProxy("SimplePrinter:default -h 127.0.0.1 -p 10001");
			//通过checkedCast向下转换，获取Printer接口的远程，并同时检测根据传入的名称获取的服务单元是否Printer的代理接口，如果不是则返回null对象
			PrinterPrx printer  = PrinterPrxHelper.checkedCast(base);
			if(printer != null){
				printer.printString("Hello World");
			}
		} catch (Ice.LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			e.printStackTrace();
			status = 1;
		} finally{
			if(ic != null){
				ic.destroy();
			}
		}
		System.exit(status);
	}
}
