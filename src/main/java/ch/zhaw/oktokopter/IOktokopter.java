package ch.zhaw.oktokopter;

public interface IOktokopter {
	
	public Oktokopter getLeader();

	public void setLeader(Oktokopter leader);

	public Oktokopter getNext();

	public void setNext(Oktokopter next);
	
	public boolean isLeader();
	
	public void start();
	
}
