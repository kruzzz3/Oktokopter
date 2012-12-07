package ch.zhaw.token;

import ch.zhaw.log.Logger;
import ch.zhaw.oktokopter.Oktokopter;

public class TokenManager {
	
	public ETokenTyp decodeToken(IToken token) {
		Logger.info("TokenManager | decodeToken() -token="+token.toString());
		if (token instanceof DefineLeaderToken) {return ETokenTyp.DEFINELEADERTOKEN;}
		if (token instanceof NextNotFoundToken) {return ETokenTyp.NEXTNOTFOUNDTOKEN;}
		if (token instanceof PrevNotFoundToken) {return ETokenTyp.PREVNOTFOUNDTOKEN;}
		Logger.error("TokenManager | decodeToken() MSG=Token konnte nicht dekodiert werden!");
		return ETokenTyp.EMPTY;
	}
	
	public IToken createDefineLeaderToken(Oktokopter senderOktokopter) {
		DefineLeaderToken token = new DefineLeaderToken();
		token.setSenderOktokopter(senderOktokopter);
		token.setLeader(senderOktokopter);
		return (IToken) token;
	}
	
	public IToken createNextNotFoundToken(Oktokopter senderOktokopter) {
		NextNotFoundToken token = new NextNotFoundToken();
		token.setPrev(senderOktokopter);
		return (IToken) token;
	}
	
	public IToken createPrevNotFoundToken(Oktokopter senderOktokopter) {
		PrevNotFoundToken token = new PrevNotFoundToken();
		token.setNext(senderOktokopter);
		return (IToken) token;
	}

}
