package ch.zhaw.exeption;

import ch.zhaw.Logger;

public class PrevNotFoundExeption extends Exception {

	private static final long serialVersionUID = 8981954144030297535L;
	private String thrower;
	
	public PrevNotFoundExeption(String thrower) {
		this.thrower = thrower;
	}
	
	@Override
	public void printStackTrace() {
		Logger.error("PrevNotFoundExeption | "+thrower);
	}

}
