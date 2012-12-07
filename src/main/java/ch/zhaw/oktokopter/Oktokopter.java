package ch.zhaw.oktokopter;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ch.zhaw.exeption.NextNotFoundExeption;
import ch.zhaw.log.Logger;
import ch.zhaw.token.DefineLeaderToken;
import ch.zhaw.token.ETokenTyp;
import ch.zhaw.token.IToken;
import ch.zhaw.token.NextNotFoundToken;
import ch.zhaw.token.PrevNotFoundToken;
import ch.zhaw.token.TokenManager;
import ch.zhaw.token.TokenStack;


public class Oktokopter implements IOktokopter, IReceiveToken {

	private TokenManager tokenManager;
	private TokenStack tokenStack;
	private IOktokopter leader;
	private IOktokopter prev;
	private IOktokopter next;
	private String name;
	private long buildTime;
	private float akku;
	private boolean isWaitForToken;
	
	private float maxPower = 500;
	
	private Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	
	private boolean isRunning = true;
	
	private float posX;
	private float posY;
	
	private float forceAngle = 0;
	private float forceUp = 0;
	private float forceLeftRight = 0; // Minus = Left
	
	public Oktokopter(String name, int akku, float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
		setName(name);
		setAkku(akku);
		setBuildTime(System.currentTimeMillis());
		tokenManager = new TokenManager();
		tokenStack = new TokenStack();
		setWaitForToken(false);
		setLeader(null);
		
		createFixtureDef();
		createBodyDef();
		
		Thread t = new Thread(new Correction());
		t.start();
		
		Logger.info("Oktokopter "+toString()+" erstellt");
	}
	
	private void createFixtureDef() {
		PolygonShape shape = new PolygonShape();
	    shape.setAsBox(1.5f, 1.0f);
	    
	    fixtureDef = new FixtureDef();
	    fixtureDef.shape = shape;
	    fixtureDef.density = 0.5f;
	    fixtureDef.friction = 0.99f;
	    fixtureDef.restitution = 0.1f;
	}
	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}
	
	private void createBodyDef() {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(posX, posY);
		posY = 35;
	}
	public BodyDef getBodyDef() {
		return bodyDef;
	}
	
	@Override
	public void setBody(Body body) {
		this.body = body;
	}

	@Override
	public Body getBody() {
		return body;
	}
	
	public boolean kill() {
		if (getPrev() != null) { getPrev().setNext(null); }
		if (getNext() != null) { getNext().setPrev(null); }
		forceAngle = 0;
		forceUp = 0;
		forceLeftRight = 0; // Minus = Left
		isRunning = false;
		return true;
	}
	
	public void start() {
		setWaitForToken(true);
		getNext().receiveToken(tokenManager.createDefineLeaderToken(this));
	}
	
	public boolean startLeaderOperation() {
		if(isLeader()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Oktokopter) {
			Oktokopter oktokopter = (Oktokopter) o;
			if (getBuildTime() == oktokopter.getBuildTime() && getName().equals(oktokopter.getName())) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		String buildTime = String.valueOf(getBuildTime());
		buildTime = buildTime.substring(5, buildTime.length());
		return Integer.parseInt(buildTime);
	}

	@Override
	public boolean receiveToken(IToken token) {
		Logger.info(toString()+" | receiveToken() -token="+token.toString());
		ETokenTyp tokenTyp = tokenManager.decodeToken(token);
		try {
			if (tokenTyp == ETokenTyp.DEFINELEADERTOKEN) {
				operationDefineLeaderToken((DefineLeaderToken) token);
				return true;
			}
			if (tokenTyp == ETokenTyp.NEXTNOTFOUNDTOKEN) {
				operationNextNotFoundToken((NextNotFoundToken) token);
				return true;
			}
			if (tokenTyp == ETokenTyp.PREVNOTFOUNDTOKEN) {
				operationPrevNotFoundToken((PrevNotFoundToken) token);
				return true;
			}
		}
		catch (NextNotFoundExeption e) {
			e.printStackTrace();
			tokenStack.push(token);
			setWaitForToken(true);
			getPrev().receiveToken(tokenManager.createNextNotFoundToken(this));
		}
		/*
		catch (PrevNotFoundExeption e) {
			e.printStackTrace();
			tokenStack.push(token);
			setWaitForToken(true);
			getNext().receiveToken(tokenManager.createPrevNotFoundToken(this));
		}
		*/
		
		return false;
	}
	
	private void checkStack() {
		if (isWaitForToken()) {
			if (!tokenStack.isEmpty()) {
				IToken token = tokenStack.pop();
				if(token.sendToNext()) {
					getNext().receiveToken(token);
				}
				else {
					getPrev().receiveToken(token);
				}
			}
			else {
				setWaitForToken(false);
			}
		}
	}
	
	private void operationDefineLeaderToken(DefineLeaderToken token) throws NextNotFoundExeption {
		if (getNext() == null) {
			throw new NextNotFoundExeption(toString());
		}
		if (token.isSet()) { // Ist der TokenInhalt festgesetzt?
			if (token.getSenderOktokopter().equals(this)) {
				setLeader(token.getLeader());
				Logger.info("Neuer Leader ist "+token.getLeader().toString());
				checkStack();
			}
			else {
				setLeader(token.getLeader());
				getNext().receiveToken(token);
			}
		}
		else if (token.getSenderOktokopter().equals(this)) { // Festsetzen des TokenInhalts
			token.setSet(true);
			getNext().receiveToken(token);
		}
		else {
			if (token.getLeader().getAkku() < getAkku()) {
				token.setLeader(this);
				setLeader(this);
			}
			getNext().receiveToken(token);
		}
	}
	
	private void operationPrevNotFoundToken(PrevNotFoundToken token) {
		if (token.getNext().equals(this)) {
			Logger.info("Neuer Prev ist "+getPrev().toString());
			checkStack();
		}
		else {
			if (getNext() == null) {
				setNext(token.getNext());
				token.getNext().setPrev(this);
				getNext().receiveToken(token);
			}
			else {
				getNext().receiveToken(token);
			}
		}
	}
	
	private void operationNextNotFoundToken(NextNotFoundToken token) {
		if (token.getPrev().equals(this)) {
			Logger.info("Neuer Next ist "+getNext().toString());
			checkStack();
		}
		else {
			if (getPrev() == null) {
				setPrev(token.getPrev());
				token.getPrev().setNext(this);
				getPrev().receiveToken(token);
			}
			else {
				getPrev().receiveToken(token);
			}
		}
	}

	//--------------------------------
	// Getter & SETTER
	// -->
	
	public IOktokopter getLeader() {
		return leader;
	}

	public void setLeader(IOktokopter leader) {
		this.leader = leader;
	}

	public IOktokopter getPrev() {
		return prev;
	}

	public void setPrev(IOktokopter prev) {
		this.prev = prev;
	}
	
	public IOktokopter getNext() {
		return next;
	}

	public void setNext(IOktokopter next) {
		this.next = next;
	}

	public long getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(long buildTime) {
		this.buildTime = buildTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getAkku() {
		return akku;
	}

	public void setAkku(float akku) {
		this.akku = akku;
	}
	
	public boolean isWaitForToken() {
		return isWaitForToken;
	}

	public void setWaitForToken(boolean isWaitForToken) {
		this.isWaitForToken = isWaitForToken;
	}
	
	public void setForceUp(float forceUp) {
		this.forceUp = forceUp;
	}
	
	public float getForceUp() {
		return forceUp;
	}
	
	public float getForceLeftRight() {
		return forceLeftRight;
	}

	public void setForceLeftRight(float forceLeftRight) {
		this.forceLeftRight = forceLeftRight;
	}
	
	@Override
	public float getForceAngle() {
		return forceAngle;
	}
	
	//--------------------------------
	// Getter & SETTER
	// <--
	
	@Override
	public String toString() {
		String answer = "";
		if (isLeader()) {
			answer = "Lead-Oktokopter";
		}
		else {
			answer = "Slave-Oktokopter";
		}
		return answer+" "+getName()+" | Akku="+getAkku();
	}
	
	@Override
	public boolean isLeader() {
		if (getLeader() == null) {
			return false;
		}
		else if (getLeader().equals(this)) {
			return true;
		}
		return false;
	
	}
	
	private void akkuDrain() {
		setAkku((float)(getAkku()-0.000005*forceUp));
		//Logger.error(""+getAkku());
		if (getAkku() <= 0) {
			Logger.error("AKKU LEER von "+toString());
			kill();
		}
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	class Correction implements Runnable {
		@Override
		public void run() {
			while (isRunning) {
				if (getBody() != null) {
					if (getBody().getPosition().x != posX) {
						forceLeftRight = posX-getBody().getPosition().x;
						if (forceLeftRight < 0) {
							forceLeftRight = 2 * forceLeftRight * forceLeftRight * -1;
						}
						else {
							forceLeftRight = 2 * forceLeftRight * forceLeftRight;
						}
					}
					if (getBody().getPosition().y < posY) {
						forceUp = forceUp + posY-getBody().getPosition().y;
						if (forceUp > maxPower) {
							forceUp = maxPower;
						}
					}
					else {
						forceUp = forceUp-20;
						if (forceUp < 0) {
							forceUp = 0;
						}
					}
					if (getBody().getAngle() > 0.01 || getBody().getAngle() < -0.01) {
						if (getBody().getAngle() < 0) {
							forceAngle = 1;
						}
						else {
							forceAngle = -1;
						}
					}
					else {
						forceAngle = 0;
					}
					akkuDrain();
				}
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println("forceUp="+forceUp);
			}
		}
	}

}
