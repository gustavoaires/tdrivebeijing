package br.ufc.data.mining.reader;

import java.util.concurrent.atomic.AtomicInteger;

public class MainData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final AtomicInteger value = new AtomicInteger(0);
		for (int i = 1; i <= 20; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					new FileReader().readFile("tdrive" + Integer.toString(value.incrementAndGet()));	
				}
			}).start();
		}
		System.out.println("Iniciou as threads");
	}

}
