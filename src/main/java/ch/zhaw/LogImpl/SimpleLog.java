package ch.zhaw.logimpl;

import ch.zhaw.log.ILog;

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
