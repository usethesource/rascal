package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

public class RascalMonitor implements IRascalMonitor {

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobStep(String name, String message, int workShare) {
		// TODO Auto-generated method stub

	}

	@Override
	public int jobEnd(String name, boolean succeeded) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean jobIsCanceled(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void jobTodo(String name, int work) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warning(String message, ISourceLocation src) {
		// TODO Auto-generated method stub

	}

}
