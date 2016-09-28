package com.bit2016.network.thread;

public class DigitThread extends Thread {

	public static void main(String[] args) {

	}

	@Override
	public void run() {

		for (int i = 0; i <= 9; i++) {
			System.out.print(i);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
