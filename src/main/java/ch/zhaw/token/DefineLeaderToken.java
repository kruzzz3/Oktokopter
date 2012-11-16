package ch.zhaw.token;

import ch.zhaw.oktokopter.Oktokopter;

public class DefineLeaderToken implements IToken {

	private Oktokopter leader;
	private Oktokopter senderOktokopter;
	private boolean set;
	private int tokenId;
	
	public DefineLeaderToken() {
		setSet(false);
		setTokenId(Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(6)));
	}

	@Override
	public String getTokenDescription() {
		return "DefineLeaderToken gesendet von "+senderOktokopter.toString();
	}

	public Oktokopter getLeader() {
		return leader;
	}

	public void setLeader(Oktokopter leader) {
		this.leader = leader;
	}

	public Oktokopter getSenderOktokopter() {
		return senderOktokopter;
	}

	public void setSenderOktokopter(Oktokopter senderOktokopter) {
		this.senderOktokopter = senderOktokopter;
	}
	
	@Override
	public String toString() {
		return "DefineLeaderToken | id="+getTokenId()+" | von='"+getSenderOktokopter().toString()+"' | Aktueller Leader="+getLeader().toString();
	}

	public boolean isSet() {
		return set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	@Override
	public int getTokenId() {
		return tokenId;
	}
	
	@Override
	public void setTokenId(int id) {
		tokenId = id;
	}

	@Override
	public boolean sendToNext() {
		return true;
	}


}
