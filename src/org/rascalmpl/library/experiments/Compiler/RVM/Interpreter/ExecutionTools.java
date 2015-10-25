package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.HashMap;

import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

public class ExecutionTools {

	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private static ITestResultListener testResultListener;

	static void setTestResultListener(ITestResultListener trl){
		testResultListener = trl;
	}
	
	public static RascalExecutionContext makeRex(
					RVMExecutable rvmExecutable,
					IBool debug, 
					IBool testsuite, 
					IBool profile, 
					IBool trackCalls, 
					IBool coverage, 
					IBool useJVM, RascalSearchPath rascalSearchPath
	) {
		return new RascalExecutionContext(
					vf, 
				   	new PrintWriter(System.out), 
				   	new PrintWriter(System.err), 
				   	rvmExecutable.getModuleTags(), 
				   	rvmExecutable.getSymbolDefinitions(),
				   	new TypeStore(), 
				   	debug.getValue(), 
				   	testsuite.getValue(), 
				   	profile.getValue(), 
				   	trackCalls.getValue(), 
				   	coverage.getValue(), 
				   	useJVM.getValue(), 
				   	null, 
				   	rascalSearchPath);
	}
	
	
	public static RVMExecutable loadProgram(
					 ISourceLocation rvmProgramLoc,
					 IConstructor rvmProgram,
					 IBool useJVM	
    ) {
		
		return load(
					rvmProgramLoc,
				    rvmProgram, 
				    useJVM,
				    vf.bool(false)
				    );
	}
	
	// Read a RVMExecutable from file
	
	public static RVMExecutable load(ISourceLocation rvmExecutableLoc) {
		return RVMExecutable.read(rvmExecutableLoc);
	}
	
	// Create an RVMExecutable given an RVMProgram
	
	public static RVMExecutable load(
			 	ISourceLocation rvmProgramLoc,
			 	IConstructor rvmProgram,
			 	IBool useJVM, 
			 	IBool serialize
	) {

		TypeStore typeStore = new TypeStore();
		RVMLoader loader = new RVMLoader(vf, typeStore);
		RVMExecutable executable = loader.load(rvmProgram,	useJVM.getValue());
		
		if(serialize.getValue()){
			executable.write(rvmProgramLoc);			

//			/*** Consistency checking after read: TODO: REMOVE THIS WHEN STABLE*/
//			RVMLinked executable2 = RVMLinked.read(linkedRVM);
//			if(!executable.comparable(executable2)){
//				System.err.println("RVMExecutables differ");
//			}
//
//			//TODO: Use the serialized version for testing purposes only
//			executable = executable2;
		}
		return executable;
	}
		
	public static IValue executeProgram(RVMExecutable executable, IMap keywordArguments, RascalExecutionContext rex){
		RVM rvm = rex.getUseJVM() ? new RVMJVM(executable, rex) : new RVM(executable, rex);
		
		rvm = initializedRVM(executable, rex);
		
		return executeProgram(rvm, executable, keywordArguments, rex);
	}
	
	/**
	 * @param executable		RVM exectable
	 * @param keywordArguments	map of actual keyword parameters
	 * @param rex				Execution context
	 * @return					Result of executing program with given parameters in given context
	 */
	public static IValue executeProgram(RVM rvm, RVMExecutable executable, IMap keywordArguments, RascalExecutionContext rex){
		
		IValue[] arguments = new IValue[0];
		HashMap<String, IValue> hmKeywordArguments = new HashMap<>();
		
		for(IValue key : keywordArguments){
			String keyString = ((IString) key).getValue();
			hmKeywordArguments.put(keyString, keywordArguments.get(key));
		}

		try {
			//long start = Timing.getCpuTime();
			IValue result = null;
			if(rex.getTestSuite()){
				/*
				 * Execute as testsuite
				 */
				rvm.executeProgram("TESTSUITE", executable.getUidModuleInit(), arguments, hmKeywordArguments);

				IListWriter w = vf.listWriter();
				int n = 0;
				for(String uid_testsuite: executable.getTestSuites()){
					rex.clearCaches();
					//System.out.println("Testsuite: " + uid_testsuite);
					IList test_results = (IList)rvm.executeProgram("TESTSUITE" + n++, uid_testsuite, arguments, hmKeywordArguments);
					w.insertAll(test_results);
				}
				result = w.done();
			} else {
				/*
				 * Standard execution of main function
				 */
				if(executable.getUidModuleMain().equals("")) {
					throw RascalRuntimeException.noMainFunction(null);
				}
				String moduleName = executable.getModuleName();
				rvm.executeProgram(moduleName, executable.getUidModuleInit(), arguments, hmKeywordArguments);
				//System.out.println("Initializing: " + (Timing.getCpuTime() - start)/1000000 + "ms");
				result = rvm.executeProgram(moduleName, executable.getUidModuleMain(), arguments, hmKeywordArguments);
			}
			//long now = Timing.getCpuTime();
			MuPrimitive.exit(rvm.getStdOut());
			RascalPrimitive.exit(rex);
			Opcode.exit();
			if(rex.getProfile()){
				((ProfileLocationCollector) rvm.getLocationCollector()).report(rvm.getStdOut());
			} else if(rex.getCoverage()){
				((CoverageLocationCollector) rvm.getLocationCollector()).report(rvm.getStdOut());
			}
			
			//System.out.println("Executing: " + (now - start)/1000000 + "ms");
			return (IValue) result;
			
		} catch(Thrown e) {
			e.printStackTrace(rex.getStdOut());
			return vf.tuple(vf.string("Runtime exception: " + e.value), vf.integer(0));
		}
	}
	
	/**
	 * @param executable	RVM exectable
	 * @return				an initialized RVM instance
	 */
	 public static RVM initializedRVM(RVMExecutable executable, RascalExecutionContext rex){
		
		RVM rvm = rex.getUseJVM() ? new RVMJVM(executable, rex) : new RVM(executable, rex);
		
		ProfileLocationCollector profilingCollector = null;
		CoverageLocationCollector coverageCollector = null;
		
		if(rex.getProfile()){
			profilingCollector = new ProfileLocationCollector();
			rvm.setLocationCollector(profilingCollector);
			profilingCollector.start();
	
		} else if(rex.getCoverage()){
			coverageCollector = new CoverageLocationCollector();
			rvm.setLocationCollector(coverageCollector);
		}
		
		// Execute initializers of imported modules
		for(String initializer: executable.getInitializers()){
			rvm.executeProgram("UNDEFINED", initializer, new IValue[0], null);
		}
		
		if(executable.getUidModuleInit().equals("")) {
			// TODO remove collector
			throw new CompilerError("No module_init function found when loading RVM code!");
		}
		
		return rvm;
	}
}
