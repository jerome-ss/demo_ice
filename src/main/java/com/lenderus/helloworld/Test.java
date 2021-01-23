package com.lenderus.helloworld;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Test {
	public static void main(String[] args) throws Exception {
		// Test tes = new Test();
		// tes.testName();
		InputStream in = Test.class.getResourceAsStream("config.properties");
		Properties p = new Properties();
		try {
			p.load(in);
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("ip:" + p.getProperty("--Ice.Default.Locator"));

	}

}
