package ch.zhaw.LogImpl;

import ch.zhaw.ILog;

public class ErrorLog implements ILog {

	@Override
	public void info(String text) {}

	@Override
	public void error(String text) {
		System.err.println(text);
	}
}
