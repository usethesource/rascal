package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse.Traverse;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.ISourceLocation;

public class RascalExecutionContext implements IRascalMonitor {
	private String currentModuleName;
	private $RascalModule module;
	private final IRascalValueFactory $RVF;
	private final InputStream instream;
	private final PrintStream outstream;
	private final PrintWriter outwriter;
	private final PrintStream errstream;
	private final PrintWriter errwriter;
	private final PathConfig pcfg;
	private final IDEServices ideServices;
	private final Traverse $TRAVERSE;

	public RascalExecutionContext(
			InputStream instream,
			PrintStream outstream,
			PrintStream errstream, 
			PathConfig pcfg, 
			IDEServices ideServices
			){
		
		currentModuleName = "UNDEFINED";
		
		this.instream = instream;
		this.outstream = outstream;
		this.outwriter = new PrintWriter(outstream);
		this.errstream = errstream;
		this.errwriter = new PrintWriter(errstream);
		
		this.pcfg = pcfg == null ? new PathConfig() : pcfg;
		this.ideServices = ideServices == null ? new BasicIDEServices(errwriter) : ideServices;
		$RVF = new RascalRuntimeValueFactory(this);
		$TRAVERSE = new Traverse($RVF);
	}
	
	IRascalValueFactory getRascalRuntimeValueFactory() { return $RVF; }
	
	public Traverse getTraverse() { return $TRAVERSE; }
	
	public InputStream getInStream() { return instream; }
	
	public PrintWriter getOutWriter() { return outwriter; }
	
	public PrintStream getOutStream() { return outstream; }

	public PrintWriter getErrWriter() { return errwriter; }
	
	public PrintStream getErrStream() { return errstream; }

	public PathConfig getPathConfig() { return pcfg; }
	
	public void setModule($RascalModule module) { this.module = module; }
	
	public $RascalModule getModule() { return module; }

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
