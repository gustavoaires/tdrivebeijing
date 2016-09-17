package br.ufc.data.mining.reader;

public class MainData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileReader fileReader1 = new FileReader();
		FileReader fileReader2 = new FileReader();
		FileReader fileReader3 = new FileReader();
		FileReader fileReader4 = new FileReader();
		FileReader fileReader5 = new FileReader();
		FileReader fileReader6 = new FileReader();
		FileReader fileReader7 = new FileReader();
		FileReader fileReader8 = new FileReader();
		FileReader fileReader9 = new FileReader();
		FileReader fileReader10 = new FileReader();		
		
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
		Thread t6 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader6.readFile("tdrive6");	
			}
		});
		t6.start();
		System.out.println("Iniciou as threads");
		Thread t7 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader7.readFile("tdrive7");	
			}
		});
		t7.start();
		Thread t8 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader8.readFile("tdrive8");	
			}
		});
		t8.start();
		Thread t9 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader9.readFile("tdrive9");	
			}
		});
		t9.start();
		Thread t10 = new Thread(new Runnable() {
			@Override
			public void run() {
				fileReader10.readFile("tdrive10");	
			}
		});
		t10.start();
		System.out.println("Iniciou as threads");
	}

}
