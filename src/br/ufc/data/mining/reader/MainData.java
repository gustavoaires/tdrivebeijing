package br.ufc.data.mining.reader;

public class MainData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileReader fileReader1 = new FileReader();
		FileReader fileReader2 = new FileReader();
		FileReader fileReader3 = new FileReader();
		FileReader fileReader4 = new FileReader();
		FileReader fileReader5 = new FileReader();
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader1.readFile("tdrive1");	
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader2.readFile("tdrive2");	
			}
		});
		t2.start();
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader3.readFile("tdrive3");	
			}
		});
		t3.start();
		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader4.readFile("tdrive4");	
			}
		});
		t4.start();
		Thread t5 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader5.readFile("tdrive5");	
			}
		});
		t5.start();
		System.out.println("Iniciou as threads");
	}

}
