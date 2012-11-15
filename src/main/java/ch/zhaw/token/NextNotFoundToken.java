package ch.zhaw.token;

import ch.zhaw.oktokopter.Oktokopter;

public class NextNotFoundToken implements IToken {

	private Oktokopter prev;
	private Oktokopter next;
	private int tokenId;
	
	public NextNotFoundToken() {
		setTokenId(Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(6)));
	}
	
	@Override
	public int getTokenId() {
		return tokenId;
	}

	@Override
	public void setTokenId(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTokenDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return "DefineLeaderToken | id="+getTokenId()+" | von='"+getPrev().toString()+"'";
	}

	public Oktokopter getPrev() {
		return prev;
	}

	public void setPrev(Oktokopter prev) {
		this.prev = prev;
	}

	public Oktokopter getNext() {
		return next;
	}

	public void setNext(Oktokopter next) {
		this.next = next;
	}


}
