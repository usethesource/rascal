package org.rascalmpl.interpreter;

public class NullRascalMonitor implements IRascalMonitor {
	public int endJob(boolean succeeded) {
		return 0;
	}

	public void event(String name) {
	}

	public void event(String name, int inc) {
	}

	public void event(int inc) {
	}

	public void startJob(String name, int totalWork) {
	}
	
	public void startJob(String name) {
	}
	
	public void startJob(String name, int workShare, int totalWork) {
	}
	
	public void todo(int work) {
	}
}
