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

	@Override
	public void startJob(String name) {
	}

	@Override
	public void startJob(String name, int workShare, int totalWork) {
	}

	@Override
	public void todo(int work) {
	}
}
