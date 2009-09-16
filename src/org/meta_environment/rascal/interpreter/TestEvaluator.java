package org.meta_environment.rascal.interpreter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Test;
import org.meta_environment.rascal.ast.Test.Labeled;
import org.meta_environment.rascal.ast.Test.Unlabeled;
import org.meta_environment.rascal.interpreter.control_exceptions.FailedTestError;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

public class TestEvaluator {
	private Visitor visitor;
	private Evaluator eval;

	public TestEvaluator(Evaluator eval) {
		this.eval = eval;
		this.visitor = new Visitor(eval);
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
