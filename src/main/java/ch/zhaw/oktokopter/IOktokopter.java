package ch.zhaw.oktokopter;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import ch.zhaw.token.IToken;

public interface IOktokopter {
	public IOktokopter getLeader();
	public void setLeader(IOktokopter leader);
	
	public void setNext(IOktokopter next);
	public IOktokopter getNext();
	
	public void setPrev(IOktokopter next);
	public IOktokopter getPrev();
	
	public boolean isLeader();
	public void start();
	
	public boolean receiveToken(IToken token);
	
	public float getForceUp();
	
	public float getForceLeftRight();
	public float getForceAngle();
	public boolean isRunning();

	public void setBody(Body body);
	public Body getBody();
	
	public BodyDef getBodyDef();
	public FixtureDef getFixtureDef();
}
