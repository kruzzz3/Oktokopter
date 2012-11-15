package ch.zhaw.token;

import java.util.ArrayList;

public class TokenStack {

	private ArrayList<IToken> tokenStack;
	
	public TokenStack() {
		tokenStack = new ArrayList<IToken>();
	}
	
	public void push(IToken token) {
		tokenStack.add(token);
	}
	
	public IToken pop() {
		if (!isEmpty()) {
			int i = tokenStack.size()-1;
			IToken token = tokenStack.get(i);
			tokenStack.remove(i);
			return token;
		}
		return null;
	}
	
	public boolean isEmpty() {
		if (tokenStack.size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	

}
