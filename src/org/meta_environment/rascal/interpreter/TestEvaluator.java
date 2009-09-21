package org.meta_environment.rascal.interpreter;

import java.io.OutputStream;
import java.io.PrintStream;
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

	private final TestResultListener testResultListener;

	public TestEvaluator(Evaluator eval, OutputStream errorStream){
		this.eval = eval;

		this.testResultListener = new TestResultListener(errorStream);
	}

	private static class TestResultListener{
		private final PrintStream err;

		public TestResultListener(OutputStream errorStream){
			super();

			this.err = new PrintStream(errorStream);
		}

		public void report(boolean successful, String test){
			synchronized (err) {
				err.print(successful ? "success : " : "failed  : ");
				if(test.length() <= 50){
					err.println(test);
				}else{
					err.println(test.substring(0, 47));
					err.println("...");
				}
			}
		}

		public void report(boolean successful, String test, Throwable t){
			synchronized(err){
				err.print(successful ? "success : " : "failed  : ");
				if(test.length() <= 50){
					err.println(test);
				}else{
					err.println(test.substring(0, 47));
					err.println("...");
				}
				t.printStackTrace(err);
			}
		}
	}
	
	public void test(){
		for(String module : eval.getCurrentEnvt().getImports()){
			List<Test> tests = eval.getHeap().getModule(module).getTests();
			runTests(tests);
		}

		runTests(((ModuleEnvironment) eval.getCurrentEnvt().getRoot()).getTests());
	}

	private void runTests(List<Test> tests){
		Visitor visitor = new Visitor();
		int size = tests.size();
		for(int i = 0; i < size; i++){
			tests.get(i).accept(visitor);
		}
	}

	private class Visitor extends NullASTVisitor<Result<IBool>>{
		
		public Visitor(){
			super();
		}
		
		public Result<IBool> visitTestLabeled(Labeled x){
			Result<IValue> result = ResultFactory.bool(true);

			try{
				result = x.getExpression().accept(eval);
			}catch(Throw e){
				testResultListener.report(result.isTrue(), x.toString(), e);
			}catch(Throwable e){
				testResultListener.report(result.isTrue(), x.toString(), e);
			}

			testResultListener.report(result.isTrue(), x.toString());

			return ResultFactory.bool(true);
		}
		
		public Result<IBool> visitTestUnlabeled(Unlabeled x){
			Result<IValue> result = ResultFactory.bool(true);

			try{
				result = x.getExpression().accept(eval);
			}catch(Throw e){
				testResultListener.report(result.isTrue(), x.toString(), e);
			}catch(Throwable e){
				testResultListener.report(result.isTrue(), x.toString(), e);
			}

			testResultListener.report(result.isTrue(), x.toString());

			return ResultFactory.bool(true);
		}
	}

}
