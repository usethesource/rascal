package org.rascalmpl.core.ide;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.osgi.framework.FrameworkUtil;
import org.rascalmpl.eclipse.Activator;
import org.rascalmpl.eclipse.editor.IDESummaryService;
import org.rascalmpl.eclipse.nature.ProjectEvaluatorFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.lang.rascal.boot.IKernel;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IString;

public class RascalCodeIDESummary implements IDESummaryService {
	
	private final Future<Evaluator> singleEvaluator;
    public RascalCodeIDESummary() {
    	// this constructor is run on the main thread, and so are the callbacks
    	// so we need to construct the evaluator on a seperate thread, to try and avoid freezing the main thread
    	singleEvaluator = new FutureTask<>(() -> {
    		try {
    			Evaluator eval = ProjectEvaluatorFactory.getInstance().getBundleEvaluator(FrameworkUtil.getBundle(RascalCodeIDESummary.class));
    			eval.doImport(null, "lang::rascalcore::check::ADTSummary");
    			return eval;
    		}
    		catch (Throwable e) {
    			Activator.log("Cannot initialize rascal-core type checker", e);
    			return null;
    		}
    	});
    	// schedule the init on a thread that runs once and finishes after initializing the evaluator
    	Thread lazyInit = new Thread((FutureTask<?>)singleEvaluator);
    	lazyInit.setDaemon(true);
    	lazyInit.setName("Background initializer for evaluator");
    	lazyInit.start();
    }
	
	private Evaluator getEvaluator() {
		try {
			return singleEvaluator.get();
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}
	}
	


	@Override
	public IConstructor calculate(IKernel kernel, IString moduleName, IConstructor pcfg) {
		final Evaluator eval = getEvaluator();
		if (eval == null) {
			return null;
		}
		synchronized (eval) {
			return (IConstructor) eval.call("calculateSummary", moduleName, pcfg);
		}
	}

	@Override
	public INode getOutline(IKernel kernel, IConstructor moduleTree) {
		final Evaluator eval = getEvaluator();
		if (eval == null) {
			return null;
		}
		synchronized (eval) {
			return (INode) eval.call("getOutline", moduleTree);
		}
	}

}
