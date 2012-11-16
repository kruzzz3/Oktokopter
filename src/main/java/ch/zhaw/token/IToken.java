package ch.zhaw.token;

public interface IToken {

	public int getTokenId();
	
	public void setTokenId(int id);
	
	public String getTokenDescription();
	
	public boolean sendToNext();
	
}
