package ch.zhaw.exeption;

import ch.zhaw.Logger;

public class NextNotFoundExeption extends Exception {

	private static final long serialVersionUID = 7416882807661682198L;
	private String thrower;
	
	public NextNotFoundExeption(String thrower) {
		this.thrower = thrower;
	}
	
	@Override
	public void printStackTrace() {
		Logger.error("NextNotFoundExeption | "+thrower);
	}

}
