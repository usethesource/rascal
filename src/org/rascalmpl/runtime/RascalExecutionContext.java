package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.PrintWriter;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.library.util.PathConfig;

import io.usethesource.vallang.ISourceLocation;

public class RascalExecutionContext implements IRascalMonitor {
	private String currentModuleName;
	private final PrintWriter stdout;
	private final PrintWriter stderr;
	private final PathConfig pcfg;
	private IDEServices ideServices;

	public RascalExecutionContext(
			PrintWriter stdout,
			PrintWriter stderr, 
			PathConfig pcfg, 
			IDEServices ideServices
			){

		this.pcfg = pcfg == null ? new PathConfig() : pcfg;

		currentModuleName = "UNDEFINED";

		this.ideServices = ideServices == null ? new BasicIDEServices(stderr) : ideServices;
		this.stdout = stdout;
		this.stderr = stderr;
	}

	public PrintWriter getStdErr() { return stderr; }

	public PrintWriter getStdOut() { return stdout; }
	
	public PathConfig getPathConfig() { return pcfg; }

	public String getFullModuleName(){ return currentModuleName; }

	public String getFullModuleNameAsPath() { return currentModuleName.replaceAll("::",  "/") + ".rsc"; }

	public void setFullModuleName(String moduleName) { currentModuleName = moduleName; }
	

	@Override
	public int jobEnd(String name, boolean succeeded) {
		return ideServices.jobEnd(name, succeeded);
	}

	@Override
	public void jobStep(String name, String message, int worked) {
		ideServices.jobStep(name, message, worked);
	}

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
		ideServices.jobStart(name, workShare, totalWork);
	}


	@Override
	public void jobTodo(String name, int work) {
		ideServices.jobTodo(name, work);
	}

	@Override
	public boolean jobIsCanceled(String name) {
		return ideServices.jobIsCanceled(name);
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		ideServices.warning(message,  src);;
	}
}
