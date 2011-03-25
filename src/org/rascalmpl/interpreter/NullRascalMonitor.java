package org.rascalmpl.interpreter;

public class NullRascalMonitor implements IRascalMonitor {
	@Override
	public void endJob(boolean succeeded) {
	}

	@Override
	public void event(String name) {
	}

	@Override
	public void event(String name, int inc) {
	}

	@Override
	public void event(int inc) {
	}

	@Override
	public void startJob(String name, int totalWork) {
	}
}
