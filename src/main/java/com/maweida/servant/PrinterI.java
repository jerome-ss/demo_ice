package com.maweida.servant;

import com.maweida._PrinterDisp;

import Ice.Current;

public class PrinterI extends _PrinterDisp{

	private static final long serialVersionUID = 1L;

	@Override
	public void printString(String s, Current __current) {
		System.out.println("PrinterI打印：" + s);
	}

}
