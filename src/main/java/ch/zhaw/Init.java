package ch.zhaw;

import java.util.ArrayList;

import ch.zhaw.LogImpl.SimpleLog;
import ch.zhaw.oktokopter.Oktokopter;

public class Init {

	private static ArrayList<Oktokopter> oktokopters;
	
	
	public static void main(String[] args) {
		Logger.setLog(new SimpleLog());
		createOktokopter();
	}
	
	private static void createOktokopter() {
		oktokopters = new ArrayList<Oktokopter>();
		oktokopters.add(new Oktokopter("Okto 1",30));
		oktokopters.add(new Oktokopter("Okto 2",50));
		oktokopters.add(new Oktokopter("Okto 3",90));
		oktokopters.add(new Oktokopter("Okto 4",100));
		oktokopters.add(new Oktokopter("Okto 5",88));
		oktokopters.add(new Oktokopter("Okto 6",90));
		oktokopters.add(new Oktokopter("Okto 7",99));
		for (int i = 0; i < oktokopters.size(); i++) {
			if (i < oktokopters.size()-1) {
				oktokopters.get(i).setNext(oktokopters.get(i+1));
			}
			else {
				oktokopters.get(i).setNext(oktokopters.get(0));
			}
		}
		oktokopters.get(0).setLeader(oktokopters.get(0));
		oktokopters.get(0).start();
	}

}
