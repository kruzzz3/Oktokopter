package ch.zhaw;

import java.math.MathContext;
import java.util.ArrayList;

import ch.zhaw.oktokopter.Oktokopter;

public class Simulation {

	private Oktokopter oktokopter1;
	private Oktokopter oktokopter2;
	private Oktokopter oktokopter3;
	private Oktokopter oktokopter4;
	private Oktokopter oktokopter5;
	private Oktokopter oktokopter6;
	private Oktokopter oktokopter7;
	
	public Simulation() {
		createOktokopter();
	}
	
	private void createOktokopter() {
		oktokopter1 = new Oktokopter("Okto 1", (int)Math.random()*100);
		oktokopter2 = new Oktokopter("Okto 2", (int)Math.random()*100);
		oktokopter3 = new Oktokopter("Okto 3", (int)Math.random()*100);
		oktokopter4 = new Oktokopter("Okto 4", 98);
		oktokopter5 = new Oktokopter("Okto 5", (int)Math.random()*100);
		oktokopter6 = new Oktokopter("Okto 6", 99);
		oktokopter7 = new Oktokopter("Okto 7", (int)Math.random()*100);
		oktokopter1.setNext(oktokopter2); oktokopter1.setPrev(oktokopter7);
		oktokopter2.setNext(oktokopter3); oktokopter2.setPrev(oktokopter1);
		oktokopter3.setNext(oktokopter4); oktokopter3.setPrev(oktokopter2);
		oktokopter4.setNext(oktokopter5); oktokopter4.setPrev(oktokopter3);
		oktokopter5.setNext(oktokopter6); oktokopter5.setPrev(oktokopter4);
		oktokopter6.setNext(oktokopter7); oktokopter6.setPrev(oktokopter5);
		oktokopter7.setNext(oktokopter1); oktokopter7.setPrev(oktokopter6);
		oktokopter5.kill();
		oktokopter1.start();
	}

}
