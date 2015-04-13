package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.DefaultTestResultListener;
import org.rascalmpl.interpreter.IEvaluatorContext;  // TODO: remove import? NOT YET: Only used as argument of reflective library function
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.Factory;

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
			 					 IEvaluatorContext ctx) {
		
		try {
			ISourceLocation x = URIResolverRegistry.getInstance().logicalToPhysical(rvmExecutable);
			RVMExecutable executable = RVMExecutable.read(x.getPath());
			
			return executeProgram(executable,  
					  			  argumentsAsList,
					  			  debug, 
					  			  testsuite, 
					  			  profile, 
					  			  trackCalls, 
					  			  coverage,
					  			  ctx);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
		}
	}
	
	// Library function to execute a RVM program from Rascal

	public ITuple executeProgram(ISourceLocation rvmExecutable,
								 IConstructor program,
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
								 IEvaluatorContext ctx) {
		
		TypeStore typeStore = new TypeStore(); // new TypeStore(Factory.getStore());
		
		RascalLinker linker = new RascalLinker(vf, typeStore);
		
		RVMExecutable executable = linker.link(program,
								 imported_types,
								 imported_functions,
								 imported_overloaded_functions,
								 imported_overloading_resolvers,
								 argumentsAsList);
		/*** Serialization  */
		
		RVMExecutable executable2 = null;
		
		long before = Timing.getCpuTime();
		try {
			ISourceLocation x = URIResolverRegistry.getInstance().logicalToPhysical(rvmExecutable);
			executable.write(x.getPath());
			long afterWrite = Timing.getCpuTime();
			executable2 = RVMExecutable.read(x.getPath());
			long afterRead = Timing.getCpuTime();
			
			System.out.println("write " + ((IString) program.get("name")).getValue() + ": " + (afterWrite - before)/1000000 + " msec; read " + (afterRead - afterWrite)/1000000 + " msec");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		/*** Consistency checking after read */
		if(!executable.comparable(executable2)){
			System.err.println("RVMExecutables differ");
		}
		
		/*** Start execution */
		
		// TODO: Decide here to use the orignal executable or the serialized version.
		executable = executable2;
		
		return executeProgram(executable,  
							  argumentsAsList,
							  debug, 
							  testsuite, 
							  profile, 
							  trackCalls, 
							  coverage,
							  ctx);
	}
		
	public ITuple executeProgram(RVMExecutable executable,  
								 IList argumentsAsList,
								 IBool debug, 
								 IBool testsuite, 
								 IBool profile, 
								 IBool trackCalls, 
								 IBool coverage,
								 IEvaluatorContext ctx){
		
		PrintWriter stdout = ctx.getStdOut();
		PrintWriter stderr = ctx.getStdErr();
		
		if(testResultListener == null){
			testResultListener = (ITestResultListener) new DefaultTestResultListener(stderr);
		}
		
		IMap symbol_definitions = executable.symbol_definitions; //(IMap) program.get("symbol_definitions");
		
		RascalExecutionContext rex = 
				new RascalExecutionContext(vf, symbol_definitions, new TypeStore(), 
										   debug.getValue(), profile.getValue(), trackCalls.getValue(), coverage.getValue(), 
										   ctx, testResultListener);
		
		RVM rvm = new RVM(executable, rex);
		
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
				String moduleName = executable.module_name; //((IString) program.get("name")).getValue();
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
