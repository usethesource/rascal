package org.rascalmpl.test.infrastructure;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.uri.JarURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;

public class RascalJUnitTestRunner extends Runner {
	private static Evaluator evaluator;
	private static GlobalEnvironment heap;
	private static ModuleEnvironment root;
	private static PrintWriter stderr;
	private static PrintWriter stdout;
	private String module;
	private Description desc;

	static{
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("___junit_test___", heap));
		
		stderr = new PrintWriter(System.err);
		stdout = new PrintWriter(System.out);
		evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), stderr, stdout,  root, heap);
		URIResolverRegistry resolverRegistry = evaluator.getResolverRegistry();
		
		resolverRegistry.registerInput(new JarURIResolver(TestFramework.class));
	}
	
	public RascalJUnitTestRunner(Class<?> clazz) {
		this(clazz.getAnnotation(RascalModuleTest.class).value()[0]);
	}
	
	public RascalJUnitTestRunner(String module) {
		this.module = module;
		evaluator.doImport(new NullRascalMonitor(), module);
	}
	
	@Override
	public Description getDescription() {
		Description desc = Description.createSuiteDescription(module);
		for (AbstractFunction f : heap.getModule(module).getTests()) {
			desc.addChild(Description.createTestDescription(getClass(), f.getName()));
		}
		this.desc = desc;
		return desc;
	}

	@Override
	public void run(final RunNotifier notifier) {
		notifier.fireTestRunStarted(Description.createTestDescription(getClass(), module));
		evaluator.doImport(new NullRascalMonitor(), module);

		evaluator.setTestResultListener(new ITestResultListener() {
			int current = 0;
			int count;
			
			@Override
			public void start(int count) {
				this.count = count;
				
				notifier.fireTestRunStarted(desc);
				if (current < count) {
				  notifier.fireTestStarted(desc.getChildren().get(current));
				}
			}
			
			@Override
			public void report(boolean successful, String test, ISourceLocation loc,
					String message) {
				if (!successful) {
					notifier.fireTestFailure(new Failure(desc.getChildren().get(current), new Exception(message)));
				}
				
				notifier.fireTestFinished(desc.getChildren().get(current));
				current++;
				if (current < count) {
					notifier.fireTestStarted(desc.getChildren().get(current));
				}
			}
			
			@Override
			public void done() {
				notifier.fireTestRunFinished(new Result());
			}
		});
		
		evaluator.runTests(new NullRascalMonitor());
	}
}
