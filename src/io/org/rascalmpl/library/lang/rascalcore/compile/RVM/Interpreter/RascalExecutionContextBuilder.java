package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.util.PathConfig;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IValue;

/**
 * Builder for constructing RascalExecutionContexts.
 *
 */
public class RascalExecutionContextBuilder {

    private boolean build = false;

    private final PathConfig pcfg;
	private final PrintWriter stderr;
	private final PrintWriter stdout;
	private String moduleName;
	
	private IDEServices ideServices;

	private boolean coverage = false;
	private boolean debug = false;
	private boolean debugRVM = false;
	private boolean jvm = true;
	private boolean profile = false;
	private boolean testsuite = false;
	private boolean trace = false;
	private boolean verbose = false;
	
	private IMap symbolDefinitions = null ;
	private IMap moduleTags = null;
	private Map<IValue,IValue> moduleVariables;
	private IFrameObserver frameObserver = null;
	
	private RascalExecutionContextBuilder(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr) {
	    this.stderr = stderr;
	    this.stdout = stdout;
	    this.pcfg = pcfg;
	}
	
	public static RascalExecutionContextBuilder normalContext(PathConfig pcfg) {
	    return new RascalExecutionContextBuilder(pcfg, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
	}
	
	public static RascalExecutionContextBuilder normalContext(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr) {
	    return new RascalExecutionContextBuilder(pcfg, stdout, stderr);
	}

	public static RascalExecutionContextBuilder normalContext(PathConfig pcfg, PrintStream stdout, PrintStream stderr) {
	    return new RascalExecutionContextBuilder(pcfg, new PrintWriter(stdout), new PrintWriter(stderr, true));
	}

	/**
	 * Setup the rascal execution context for test suites
	 * @param pcfg TODO
	 */
	public static RascalExecutionContextBuilder testSuiteContext(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr) {
	    RascalExecutionContextBuilder result = normalContext(pcfg, stdout, stderr);
	    result.testsuite = true;
	    return result;
	}
	
	public RascalExecutionContext build() {
	    this.build = true;
	    RascalExecutionContext result = new RascalExecutionContext(pcfg, 
	                                                               stdout, 
	                                                               stderr, 
	                                                               moduleTags, 
	                                                               symbolDefinitions, 
	                                                               frameObserver, 
	                                                               ideServices, 
	                                                               coverage, 
	                                                               debug, 
	                                                               debugRVM, 
	                                                               jvm, 
	                                                               profile, 
	                                                               testsuite, 
	                                                               trace, 
	                                                               verbose);
	    
	    
	    if (this.moduleName != null) {
	        result.setFullModuleName(moduleName);
	    }
	    if(this.moduleVariables != null){
	    	result.setModuleVariables(moduleVariables);
	    }
	    return result;
	}
	
	public RascalExecutionContextBuilder coverage(boolean coverage) {
        assert !build;
        this.coverage = coverage;
        return this;
    }
	
	public RascalExecutionContextBuilder debug(boolean debug) {
	    assert !build;
	    this.debug = debug;
	    return this;
	}
	
	public RascalExecutionContextBuilder debugRVM(boolean debug) {
	    assert !build;
	    this.debugRVM = debug;
	    return this;
	}
	
	public RascalExecutionContextBuilder jvm(boolean jvm) {
        assert !build;
        this.jvm = jvm;
        return this;
    }

	public RascalExecutionContextBuilder profile(boolean profile) {
	    assert !build;
        this.profile = profile;
        return this;
    }
	
	public RascalExecutionContextBuilder trace(boolean trace) {
	    assert !build;
        this.trace = trace;
        return this;
    }
	
	public RascalExecutionContextBuilder testsuite(boolean testsuite) {
	    assert !build;
        this.testsuite = testsuite;
        return this;
    }
	
	
	public RascalExecutionContextBuilder verbose(boolean verbose) {
	    assert !build;
        this.verbose = verbose;
        return this;
    }
	
	public RascalExecutionContextBuilder withIDEServices(IDEServices ideServices){
	  assert !build;
	  this.ideServices = ideServices;
	  return this;
	}
	
	public RascalExecutionContextBuilder withSymbolDefinitions(IMap symbols) {
	    assert !build;
	    this.symbolDefinitions = symbols;
	    return this;
	}
	
	public RascalExecutionContextBuilder withModuleTags(IMap tags) {
	    assert !build;
	    this.moduleTags = tags;
	    return this;
	}
	
	public RascalExecutionContextBuilder withModuleVariables(Map<IValue,IValue> moduleVariables) {
	    assert !build;
	    this.moduleVariables = moduleVariables;
	    return this;
	}
	
	public RascalExecutionContextBuilder observedBy(IFrameObserver obs) {
	    assert !build;
	    this.frameObserver = obs;
	    return this;
	}
	
	public RascalExecutionContextBuilder forModule(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }
	
	public RascalExecutionContextBuilder verbose() {
	    return verbose(true);
	}
	public RascalExecutionContextBuilder quiet() {
	    return verbose(true);
	}
}
