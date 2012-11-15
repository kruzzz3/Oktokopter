package ch.zhaw.LogImpl;

import ch.zhaw.ILog;

public class SimpleLog implements ILog {

	@Override
	public void info(String text) {
		System.out.println(text);
	}

	@Override
	public void error(String text) {
		System.err.println(text);
	}

}
