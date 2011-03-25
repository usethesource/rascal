package org.rascalmpl.interpreter;

public class NullRascalMonitor implements IRascalMonitor {
	public void endJob(boolean succeeded) {
	}

	public void event(String name) {
	}

	public void event(String name, int inc) {
	}

	public void event(int inc) {
	}

	public void startJob(String name, int totalWork) {
	}
}
