package ch.zhaw;

import ch.zhaw.LogImpl.SimpleLog;

public class Init {
	
	public static void main(String[] args) {
		Logger.setLog(new SimpleLog());
		new Simulation();
	}

}
