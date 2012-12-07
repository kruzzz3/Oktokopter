package ch.zhaw.oktokopter;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public interface ISystemOktokopter {
	public Oktokopter getLeader();
	public void setLeader(Oktokopter leader);
	public Oktokopter getNext();
	public void setNext(Oktokopter next);
	public boolean isLeader();
	public void start();
	
	public void setForceUp(float forceUp);
	
	public float getForceUp();
	
	public float getForceLeftRight();

	public void setForceLeftRight(float forceLeftRight);
	
	public BodyDef getBodyDef();
	public FixtureDef getFixtureDef();
}
