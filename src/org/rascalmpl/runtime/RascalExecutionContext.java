package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.PrintWriter;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.library.Prelude;
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

//		parsingTools = new ParsingTools(vf);
	}

	public PrintWriter getStdErr() { return stderr; }

	public PrintWriter getStdOut() { return stdout; }
	
	public PathConfig getPathConfig() { return pcfg; }

	public String getFullModuleName(){ return currentModuleName; }

	public String getFullModuleNameAsPath() { return currentModuleName.replaceAll("::",  "/") + ".rsc"; }

	public void setFullModuleName(String moduleName) { currentModuleName = moduleName; }
	

	public int endJob(boolean succeeded) {
		return ideServices.endJob(succeeded);
	}

	public void event(int inc) {
		ideServices.event(inc);
	}

	public void event(String name, int inc) {
		ideServices.event(name, inc);
	}

	public void event(String name) {
		ideServices.event(name);
	}

	public void startJob(String name, int workShare, int totalWork) {
		ideServices.startJob(name, workShare, totalWork);
	}

	public void startJob(String name, int totalWork) {
		ideServices.startJob(name, totalWork);
	}

	public void startJob(String name) {
		ideServices.startJob(name);
	}

	public void todo(int work) {
		ideServices.todo(work);
	}

	@Override
	public boolean isCanceled() {
		return ideServices.isCanceled();
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		ideServices.warning(message,  src);;
	}

	public Prelude getParsingTools() {
		// TODO Auto-generated method stub
		return null;
	}
}
