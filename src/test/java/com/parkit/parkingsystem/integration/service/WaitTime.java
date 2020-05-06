package com.parkit.parkingsystem.integration.service;

public class WaitTime extends Thread {

	private long wait;

	public WaitTime(long wait) {
		this.wait = wait;
	}

	public void run() {
		try {
			sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
