package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.DefaultTestResultListener;
import org.rascalmpl.interpreter.IEvaluatorContext;  // TODO: remove import? NOT YET: Only used as argument of reflective library function
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;

public class Execute {

	private IValueFactory vf;
	
	private static ITestResultListener testResultListener;

	public Execute(IValueFactory vf) {
		this.vf = vf;
	}
	
	static void setTestResultListener(ITestResultListener trl){
		testResultListener = trl;
	}
	
	// Library function to execute a RVM program from Rascal
	
	public ITuple executeProgram(ISourceLocation rvmExecutable,
			 					 IList argumentsAsList,
			 					 IBool debug, 
			 					 IBool testsuite, 
			 					 IBool profile, 
			 					 IBool trackCalls, 
			 					 IBool coverage,
			 					 IBool useByteCode,
			 					 IEvaluatorContext ctx) {
		
		RVMExecutable executable = RVMExecutable.read(rvmExecutable);
		
		return executeProgram(executable,  
				  			  argumentsAsList,
				  			  debug, 
				  			  testsuite, 
				  			  profile, 
				  			  trackCalls, 
				  			  coverage,
				  			  useByteCode,
				  			  ctx);
	}
	
	// Library function to execute a RVM program from Rascal

	public ITuple executeProgram(ISourceLocation rvmExecutable,
								 IConstructor program,
								 IMap imported_module_tags,
								 IMap imported_types,
								 IList imported_functions,
								 IList imported_overloaded_functions,
								 IMap imported_overloading_resolvers,
								 IList argumentsAsList,
								 IBool debug, 
								 IBool testsuite, 
								 IBool profile, 
								 IBool trackCalls, 
								 IBool coverage,
								 IBool useByteCode,
								 IEvaluatorContext ctx) {
		
		TypeStore typeStore = new TypeStore(); // new TypeStore(Factory.getStore());
		
		RascalLinker linker = new RascalLinker(vf, typeStore);
		
		RVMExecutable executable = linker.link(program,
								 imported_module_tags,
								 imported_types,
								 imported_functions,
								 imported_overloaded_functions,
								 imported_overloading_resolvers,
								 argumentsAsList, 
								 useByteCode.getValue());
		/*** Serialization  */
		
		RVMExecutable executable2 = null;
	
		executable.write(rvmExecutable);
				
		/*** Consistency checking after read: TODO: REMOVE THIS WHEN STABLE*/
		executable2 = RVMExecutable.read(rvmExecutable);
		if(!executable.comparable(executable2)){
			System.err.println("RVMExecutables differ");
		}
		
		/*** Start execution */
		
//		 TODO: Decide here to use the orignal executable or the serialized version.
		executable = executable2;
		
		return executeProgram(executable,  
							  argumentsAsList,
							  debug, 
							  testsuite, 
							  profile, 
							  trackCalls, 
							  coverage,
							  useByteCode,
							  ctx);
	}
		
	public ITuple executeProgram(RVMExecutable executable,  
								 IList argumentsAsList,
								 IBool debug, 
								 IBool testsuite, 
								 IBool profile, 
								 IBool trackCalls, 
								 IBool coverage,
								 IBool useByteCode,
								 IEvaluatorContext ctx){
		
		PrintWriter stdout = ctx.getStdOut();
		PrintWriter stderr = ctx.getStdErr();
		
		if(testResultListener == null){
			testResultListener = (ITestResultListener) new DefaultTestResultListener(stderr);
		}
		
		RascalExecutionContext rex = 
				new RascalExecutionContext(vf, 
										   executable.moduleTags, 
										   executable.symbol_definitions, 
										   new TypeStore(), 
										   debug.getValue(),
										   profile.getValue(), 
										   trackCalls.getValue(), 
										   coverage.getValue(), 
										   useByteCode.getValue(), 
										   ctx, testResultListener);
		
		RVM rvm = useByteCode.getValue() ? new RVMJVM(executable, rex) : new RVM(executable, rex);
		
		IValue[] arguments = new IValue[argumentsAsList.length()];
		for(int i = 0; i < argumentsAsList.length(); i++){
			arguments[i] = argumentsAsList.get(i);
		}
		
		ProfileLocationCollector profilingCollector = null;
		CoverageLocationCollector coverageCollector = null;
		
		if(profile.getValue()){
			profilingCollector = new ProfileLocationCollector();
			rvm.setLocationCollector(profilingCollector);
			profilingCollector.start();
	
		} else if(coverage.getValue()){
			coverageCollector = new CoverageLocationCollector();
			rvm.setLocationCollector(coverageCollector);
		}
		
		// Execute initializers of imported modules
		for(String initializer: executable.initializers){
			rvm.executeProgram("UNDEFINED", initializer, arguments);
		}
		
		if(executable.uid_module_init.equals("")) {
			// TODO remove collector
			throw new CompilerError("No module_init function found when loading RVM code!");
		}
		
		try {
			long start = Timing.getCpuTime();
			IValue result = null;
			if(testsuite.getValue()){
				/*
				 * Execute as testsuite
				 */
				rvm.executeProgram("TESTSUITE", executable.uid_module_init, arguments);

				IListWriter w = vf.listWriter();
				for(String uid_testsuite: executable.testsuites){
					RascalPrimitive.reset();
					IList test_results = (IList)rvm.executeProgram("TESTSUITE", uid_testsuite, arguments);
					w.insertAll(test_results);
				}
				result = w.done();
			} else {
				/*
				 * Standard execution of main function
				 */
				if(executable.uid_module_main.equals("")) {
					throw RascalRuntimeException.noMainFunction(null);
				}
				String moduleName = executable.module_name;
				rvm.executeProgram(moduleName, executable.uid_module_init, arguments);
				result = rvm.executeProgram(moduleName, executable.uid_module_main, arguments);
			}
			long now = Timing.getCpuTime();
			MuPrimitive.exit();
			RascalPrimitive.exit();
			Opcode.exit();
			if(profile.getValue()){
				profilingCollector.report(rvm.getStdOut());
			} else if(coverage.getValue()){
				coverageCollector.report(rvm.getStdOut());
			}
			
			return vf.tuple((IValue) result, vf.integer((now - start)/1000000));
			
		} catch(Thrown e) {
			e.printStackTrace(stdout);
			return vf.tuple(vf.string("Runtime exception: " + e.value), vf.integer(0));
		}
	}
}
