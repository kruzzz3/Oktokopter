package ch.zhaw.logimpl;

import ch.zhaw.log.ILog;

public class NoLog implements ILog{

	@Override
	public void info(String text) {}

	@Override
	public void error(String text) {}

}
