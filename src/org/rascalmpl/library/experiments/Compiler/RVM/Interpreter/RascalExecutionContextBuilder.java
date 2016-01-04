package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.TypeStore;

public class RascalExecutionContextBuilder {

    private boolean build = false;

    private final IValueFactory vf;
	private final PrintWriter stderr;
	private final PrintWriter stdout;
	private String moduleName;

	private boolean testsuite = false;
	private ITestResultListener testResultListener = null;
	
	private boolean debug = false;
	private boolean debugRVM = false;
	private boolean profile = false;
	private boolean trackCalls = false;
	private boolean coverage = false;
	private boolean jvm = false;
	private TypeStore typeStore = null;

	private IMap symbolDefinitions = null ;
	private IMap moduleTags = null;
	private IFrameObserver frameObserver = null;
	private ISourceLocation logLocation = null;
	private RascalSearchPath rascalSearchPath = null;
	
	
	private RascalExecutionContextBuilder(IValueFactory vf, PrintWriter stdout, PrintWriter stderr) {
	    this.vf = vf;
	    this.stderr = stderr;
	    this.stdout = stdout;
	}
	
	public static RascalExecutionContextBuilder normalContext(IValueFactory vf, PrintWriter stdout, PrintWriter stderr) {
	    return new RascalExecutionContextBuilder(vf, stdout, stderr);
	}

	public static RascalExecutionContextBuilder normalContext(IValueFactory vf, PrintStream stdout, PrintStream stderr) {
	    return new RascalExecutionContextBuilder(vf, new PrintWriter(stdout), new PrintWriter(stderr, true));
	}

	/**
	 * Setup the rascal execution context for test suites
	 * @param resultListener a specific listener instance or null which will enable the DefaultTestResultListener
	 */
	public static RascalExecutionContextBuilder testSuiteContext(IValueFactory vf, ITestResultListener resultListener, PrintWriter stdout, PrintWriter stderr) {
	    RascalExecutionContextBuilder result = normalContext(vf, stdout, stderr);
	    result.testsuite = true;
	    result.testResultListener = resultListener;
	    return result;
	}
	
	public RascalExecutionContext build() {
	    this.build = true;
	    RascalExecutionContext result = new RascalExecutionContext(vf, stdout, stderr, moduleTags, symbolDefinitions, typeStore, debug, debugRVM, testsuite, profile, trackCalls, coverage, jvm, testResultListener, frameObserver, rascalSearchPath);
	    if (this.moduleName != null) {
	        result.setCurrentModuleName(moduleName);
	    }
	    if(this.logLocation != null){
	    	result.setLogLocation(logLocation);
	    }
	    return result;
	}
	
	public RascalExecutionContextBuilder setDebugging(boolean debug) {
	    assert !build;
	    this.debug = debug;
	    return this;
	}
	public RascalExecutionContextBuilder setDebuggingRVM(boolean debug) {
	    assert !build;
	    this.debugRVM = debug;
	    return this;
	}

	public RascalExecutionContextBuilder setProfiling(boolean profile) {
	    assert !build;
        this.profile = profile;
        return this;
    }
	
	public RascalExecutionContextBuilder setTrackCalls(boolean trackCalls) {
	    assert !build;
        this.trackCalls = trackCalls;
        return this;
    }
	
	public RascalExecutionContextBuilder setCoverage(boolean coverage) {
	    assert !build;
        this.coverage = coverage;
        return this;
    }
	
	public RascalExecutionContextBuilder setJVM(boolean jvm) {
	    assert !build;
        this.jvm = jvm;
        return this;
    }
	
	/**
	 * short hand for .setDebugging(true)
	 */
	public RascalExecutionContextBuilder debugging() {
	    assert !build;
	    return setDebugging(true);
    }
	
	
	/**
	 *  short hand for .setProfiling(true)
	 */
	public RascalExecutionContextBuilder profiling() {
	    assert !build;
	    return setProfiling(true);
	}

	/**
	 *  short hand for .setCoverage(true)
	 */
	public RascalExecutionContextBuilder measuringCodeCoverage() {
	    assert !build;
	    return setCoverage(true);
	}
	
	/**
	 * short hand for .setTrackCalls(true)
	 */
	public RascalExecutionContextBuilder callTracing() {
	    assert !build;
	    return setTrackCalls(true);
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
	
	public RascalExecutionContextBuilder withExisitingTypeStore(TypeStore typeStore) {
	    assert !build;
        this.typeStore = typeStore;
        return this;
    }
	
	public RascalExecutionContextBuilder observedBy(IFrameObserver obs) {
	    assert !build;
	    this.frameObserver = obs;
	    return this;
	}
	
	public RascalExecutionContextBuilder customSearchPath(RascalSearchPath rascalSearchPath) {
	    assert !build;
        this.rascalSearchPath = rascalSearchPath;
	    return this;
    }
	
	public RascalExecutionContextBuilder forModule(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }
	
	/**
	 *  short hand for .setJVM(true)
	 */
	public RascalExecutionContextBuilder runningInJVM() {
	    assert !build;
	    return setJVM(true);
	}
}
