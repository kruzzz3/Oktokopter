package ch.zhaw.LogImpl;

import ch.zhaw.ILog;

public class NoLog implements ILog{

	@Override
	public void info(String text) {}

	@Override
	public void error(String text) {}

}
