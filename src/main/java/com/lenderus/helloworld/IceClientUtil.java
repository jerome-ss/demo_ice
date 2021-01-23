package com.lenderus.helloworld;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import Ice.ObjectPrx;

/**
 * 未调通,暂时没时间研究
 * 工具类说明 http://pan.baidu.com/s/1i4a76xn
 * @author JeromeThinkPad
 *
 */
public class IceClientUtil {
	private static volatile Ice.Communicator ic = null;
	private static Map<Class<Object>, ObjectPrx> cls2PrxMap = new HashMap<Class<Object>, ObjectPrx>();
	private static volatile long lastAccessTimestamp;
	private static volatile MonitorThread monitorThread;
	private static long idleTimeOutSeconds = 0;
	private static String iceLocator = null;
	private static final String locatorKey = "--Ice.Default.Locator";

	// 延迟加载Communicator
	public static Ice.Communicator getIceCommunictor() {
		if (ic == null) {
			synchronized (IceClientUtil.class) {
				if (ic == null) {
					if (iceLocator == null) {
						InputStream in = Test.class.getResourceAsStream("config.properties");
						Properties p = new Properties();
						try {
							p.load(in);
							in.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
//						System.out.println("ip:" + p.getProperty("--Ice.Default.Locator"));
//						ResourceBundle rb = ResourceBundle.getBundle("config.properties", Locale.ENGLISH); //读取配置文件config.properties
						iceLocator = p.getProperty(locatorKey);
						idleTimeOutSeconds = Integer.parseInt(p.getProperty("idleTimeOutSeconds"));
//						idleTimeOutSeconds = Integer.parseInt(rb.getString("idleTimeOutSeconds"));
						System.out.println("ice client's locator is " + iceLocator + ",proxy cache time out seconds:"
								+ idleTimeOutSeconds);
					}
					String[] initParams = new String[] { locatorKey + "=" + iceLocator };
					ic = Ice.Util.initialize(initParams);
					// 创建守护进程
					createMonitorThread();
				}
			}
		}
		lastAccessTimestamp = System.currentTimeMillis();
		return ic;
	}

	public static void createMonitorThread() {
		monitorThread = new MonitorThread();
		monitorThread.setDaemon(true);
		monitorThread.start();
	}

	// 关闭Communicator,释放资源
	public static void closeCommunicator(boolean removeServiceCache) {
		synchronized (IceClientUtil.class) {
			safeShutdown();
			monitorThread.interrupt();
			if (removeServiceCache && !cls2PrxMap.isEmpty()) {
				try {
					cls2PrxMap.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void safeShutdown() {
		try {
			ic.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ic.destroy();
			ic = null;
		}
	}

	// 用反射方式创建爱你Object Proxy
	private static ObjectPrx createIceProxy(Ice.Communicator communicator, Class<Object> serviceCls) {
		ObjectPrx proxy = null;
		String clsName = serviceCls.getName();
		String serviceName = serviceCls.getSimpleName();
		int pos = serviceName.lastIndexOf("Prx");
		if (pos <= 0) {
			throw new IllegalArgumentException("invalid objectPrx class, class name must end whih Prx!");
		}
		String realSvName = serviceName.substring(0, pos);
		try {
			Ice.ObjectPrx base = communicator.stringToProxy(realSvName);
			proxy = (ObjectPrx) Class.forName(clsName + "Helper").newInstance();
			Method ml = proxy.getClass().getDeclaredMethod("uncheckedCast", ObjectPrx.class);
			proxy = (ObjectPrx) ml.invoke(proxy, base);
			return proxy;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用于客户端API获取Ice服务实例的场景
	 * 
	 * @param serviceCls
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ObjectPrx getServicePrx(Class serviceCls) {
		ObjectPrx proxy = cls2PrxMap.get(serviceCls);
		if (proxy != null) {
			lastAccessTimestamp = System.currentTimeMillis();
			return proxy;
		}

		proxy = createIceProxy(getIceCommunictor(), serviceCls);
		cls2PrxMap.put(serviceCls, proxy);
		lastAccessTimestamp = System.currentTimeMillis();
		return proxy;
	}

	static class MonitorThread extends Thread {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(5000L);
					if (lastAccessTimestamp + idleTimeOutSeconds * 1000L < System.currentTimeMillis()) {
						closeCommunicator(true);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}
}
