package ch.zhaw;

public class Logger {

	private static ILog logger;
	
	public static void setLog(ILog log) {
		logger = log;
	}
	
	public static void info(String text) {
		logger.info(text);
	}
	
	public static void error(String text) {
		logger.error(text);
	}
	
}
