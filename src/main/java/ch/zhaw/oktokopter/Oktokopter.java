package ch.zhaw.oktokopter;

import ch.zhaw.Logger;
import ch.zhaw.exeption.NextNotFoundExeption;
import ch.zhaw.token.DefineLeaderToken;
import ch.zhaw.token.ETokenTyp;
import ch.zhaw.token.IToken;
import ch.zhaw.token.TokenManager;
import ch.zhaw.token.TokenStack;


public class Oktokopter implements IOktokopter, IReceiveToken {

	
	private TokenManager tokenManager;
	private TokenStack tokenStack;
	private Oktokopter leader;
	private Oktokopter prev;
	private Oktokopter next;
	private String name;
	private long buildTime;
	private int akku;
	private boolean isWaitForToken;
	
	public Oktokopter(String name, int akku) {
		setName(name);
		setAkku(akku);
		setBuildTime(System.currentTimeMillis());
		tokenManager = new TokenManager();
		tokenStack = new TokenStack();
		setWaitForToken(false);
		setLeader(null);
		Logger.info("Oktokopter "+toString()+" erstellt");
	}
	
	public boolean kill() {
		prev.setNext(null);
		next.setPrev(null);
		return true;
	}
	
	public void start() {
		Logger.info(next.toString());
		next.receiveToken(tokenManager.createDefineLeaderToken(this));
		setWaitForToken(true);
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
		buildTime.substring(5, buildTime.length());
		return Integer.parseInt(buildTime);
	}

	@Override
	public boolean receiveToken(IToken token) {
		Logger.info(toString()+" | receiveToken() -token="+token.toString());
		ETokenTyp tokenTyp = tokenManager.decodeToken(token);
		
		if (tokenTyp == ETokenTyp.DEFINELEADERTOKEN) {
			try {
				operationDefineLeaderToken((DefineLeaderToken) token);
			}
			catch (NextNotFoundExeption e) {
				e.printStackTrace();
				tokenStack.push(token);
			}
			return true;
		}
		return false;
	}
	
	private void operationDefineLeaderToken(DefineLeaderToken token) throws NextNotFoundExeption {
		if (next == null) {
			throw new NextNotFoundExeption(toString());
		}
		if (token.isSet()) { // Ist der TokenInhalt festgesetzt?
			if (token.getSenderOktokopter().equals(this)) {
				setLeader(token.getLeader());
				Logger.info("Neuer Leader ist "+token.getLeader().toString());
			}
			else {
				setLeader(token.getLeader());
				next.receiveToken(token);
			}
		}
		else if (token.getSenderOktokopter().equals(this)) { // Festsetzen des TokenInhalts
			token.setSet(true);
			next.receiveToken(token);
		}
		else {
			if (token.getLeader().getAkku() < getAkku()) {
				token.setLeader(this);
				setLeader(this);
			}
			next.receiveToken(token);
		}
	}

	//--------------------------------
	// Getter & SETTER
	// -->
	
	public Oktokopter getLeader() {
		return leader;
	}

	public void setLeader(Oktokopter leader) {
		this.leader = leader;
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

	public int getAkku() {
		return akku;
	}

	public void setAkku(int akku) {
		this.akku = akku;
	}
	
	public boolean isWaitForToken() {
		return isWaitForToken;
	}

	public void setWaitForToken(boolean isWaitForToken) {
		this.isWaitForToken = isWaitForToken;
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
		return answer+" "+getName()+" "+getAkku();
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

}
