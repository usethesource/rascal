package org.meta_environment.rascal.interpreter;

import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Test;
import org.meta_environment.rascal.ast.Test.Labeled;
import org.meta_environment.rascal.ast.Test.Unlabeled;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

public class TestEvaluator{
	private final Evaluator eval;
	private final ITestResultListener testResultListener;
	
	public TestEvaluator(Evaluator eval, ITestResultListener testResultListener){
		super();
		
		this.eval = eval;
		this.testResultListener = testResultListener;
	}
	
	public void test(){
		for(String module : eval.getCurrentEnvt().getImports()){
			System.err.println("tests in module = " + module);
			List<Test> tests = eval.getHeap().getModule(module).getTests();
			runTests(tests);
		}
		System.err.println("tests in current module");
		runTests(((ModuleEnvironment) eval.getCurrentEnvt().getRoot()).getTests());
	}
	
	private void runTests(List<Test> tests){
		Visitor visitor = new Visitor();
		for(int i = tests.size() - 1; i >= 0; i--){
			tests.get(i).accept(visitor);
		}
	}
	
	private class Visitor extends NullASTVisitor<Result<IBool>>{
		
		public Visitor(){
			super();
		}
		
		public Result<IBool> visitTestLabeled(Labeled x){
			Result<IValue> result = ResultFactory.bool(true, eval);
			System.err.println("visitTestLabeled: " + x);
			
			try{
				result = x.getExpression().accept(eval);
			}catch(Throw e){
				testResultListener.report(false, x.toString(), e);
			}catch(Throwable e){
				testResultListener.report(false, x.toString(), e);
			}
			
			testResultListener.report(result.isTrue(), x.toString());
			
			return ResultFactory.bool(result.isTrue(), eval);
		}
		
		public Result<IBool> visitTestUnlabeled(Unlabeled x){
			Result<IValue> result = ResultFactory.bool(true, eval);
			System.err.println("visitTestUnlabeled: " + x);
			
			try{
				result = x.getExpression().accept(eval);
			}catch(Throw e){
				testResultListener.report(false, x.toString(), e);
			}catch(Throwable e){
				testResultListener.report(false, x.toString(), e);
			}
			
			testResultListener.report(result.isTrue(), x.toString());
			
			return ResultFactory.bool(result.isTrue(), eval);
		}
	}
}
