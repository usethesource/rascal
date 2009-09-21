package org.meta_environment.rascal.interpreter;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Test;
import org.meta_environment.rascal.ast.Test.Labeled;
import org.meta_environment.rascal.ast.Test.Unlabeled;
import org.meta_environment.rascal.interpreter.control_exceptions.FailedTestError;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

public class TestEvaluator{
	private final Evaluator eval;
	private final Visitor visitor;
	
	private final TestResultListener testResultListener;

	public TestEvaluator(Evaluator eval, OutputStream errorStream) {
		this.eval = eval;
		this.visitor = new Visitor(eval);
		
		this.testResultListener = new TestResultListener(errorStream);
	}
	
	private static class TestResultListener{
		private final PrintStream err;
		
		public TestResultListener(OutputStream errorStream){
			super();
			
			this.err = new PrintStream(errorStream);
		}
		
		public void report(boolean successful, Expression test){
			synchronized(err){
				err.print(successful ? "success : " : "failed  : ");
				String expr = test.toString();
				if(expr.length() <= 50){
					err.println(expr);
				}else{
					err.println(expr.substring(0, 47));
					err.println("...");
				}
			}
		}
	}
	
	public List<FailedTestError> test(String moduleName) {
		LinkedList<FailedTestError> report = new LinkedList<FailedTestError>();
		test(moduleName, report);
		return report;
	}

	private void test(String moduleName, List<FailedTestError> report) {
		List<Test> tests = eval.getHeap().getModule(moduleName).getTests();
		
		runTests(report, tests);
	}

	private void runTests(List<FailedTestError> report, List<Test> tests) {
		for (Test t : tests) {
			try {
				t.accept(visitor);
			}
			catch (FailedTestError e) {
				report.add(0, e);
			}
		
		}
	}
	
	public List<FailedTestError> test() {
		List<FailedTestError> report = new LinkedList<FailedTestError>();
		
		for (String module : eval.getCurrentEnvt().getImports()) {
			test(module, report);
		}
		
		runTests(report, ((ModuleEnvironment) eval.getCurrentEnvt().getRoot()).getTests());
		
		return report;
	}

	
	public String report(List<FailedTestError> report) {
		StringBuilder b = new StringBuilder();
		
		for (FailedTestError e : report) {
			b.append(e.getMessage());
			b.append('\n');
		}
		
		return b.toString();
	}
	
	private static class Visitor extends NullASTVisitor<Result<IBool>> {
		private Evaluator eval;
		
		public Visitor(Evaluator eval) {
			this.eval = eval;
		}
		
		@Override
		public Result<IBool> visitTestLabeled(Labeled x) {
			Result<IValue> result = ResultFactory.bool(true);
			
			try {
				result = x.getExpression().accept(eval);
			} 
			catch (Throw e) {
				throw new FailedTestError(x, e);
			}
			catch (Throwable e) {
				throw new FailedTestError(x, e);
			}
			
			if (!result.isTrue()) {
				throw new FailedTestError(x);
			}
			
			return ResultFactory.bool(true);
		}
		

		@Override
		public Result<IBool> visitTestUnlabeled(Unlabeled x) {
			Result<IValue> result = ResultFactory.bool(true);
			
			try {
				result = x.getExpression().accept(eval);
			} 
			catch (Throw e) {
				throw new FailedTestError(x, e);
			}
			catch (Throwable e) {
				throw new FailedTestError(x, e);
			}
			
			if (!result.isTrue()) {
				throw new FailedTestError(x);
			}
			
			return ResultFactory.bool(true);		}
	}

}
