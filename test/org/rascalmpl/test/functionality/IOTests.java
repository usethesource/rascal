/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/

package org.rascalmpl.test.functionality;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.ATermReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;
import junit.framework.TestCase;

public class IOTests extends TestCase {
	private static TypeFactory tf = TypeFactory.getInstance();
	private static TypeStore ts = new TypeStore();
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private static Type Boolean = tf.abstractDataType(ts, "Boolean");
	
	private static Type Name = tf.abstractDataType(ts, "Name");
	private static Type True = tf.constructor(ts, Boolean, "true");
	private static Type False= tf.constructor(ts, Boolean, "false");
	private static Type And= tf.constructor(ts, Boolean, "and", Boolean, Boolean);
	private static Type Or= tf.constructor(ts, Boolean, "or", tf.listType(Boolean));
	private static Type Not= tf.constructor(ts, Boolean, "not", Boolean);
	private static Type TwoTups = tf.constructor(ts, Boolean, "twotups", tf.tupleType(Boolean, Boolean), tf.tupleType(Boolean, Boolean));
	private static Type NameNode  = tf.constructor(ts, Name, "name", tf.stringType());
	private static Type Friends = tf.constructor(ts, Boolean, "friends", tf.listType(Name));
	private static Type Couples = tf.constructor(ts, Boolean, "couples", tf.listType(tf.tupleType(Name, Name)));
	
	private IValue[] testValues = {
			vf.constructor(True).asWithKeywordParameters().setParameter("anno", vf.constructor(False)),
			vf.constructor(True).asWithKeywordParameters().setParameter("anno", vf.constructor(False)).asWithKeywordParameters().setParameter("banno", vf.constructor(False)),
			vf.constructor(True),
			vf.constructor(True),
			vf.constructor(And, vf.constructor(True), vf.constructor(False)),
			vf.constructor(Not, vf.constructor(And, vf.constructor(True), vf.constructor(False))),
			vf.constructor(And, vf.constructor(And, vf.constructor(True), vf.constructor(True)), vf.constructor(And, vf.constructor(True), vf.constructor(True))),
			vf.constructor(TwoTups, vf.tuple(vf.constructor(True), vf.constructor(False)),vf.tuple(vf.constructor(True), vf.constructor(False))),
			vf.constructor(Or, vf.list(vf.constructor(True), vf.constructor(False), vf.constructor(True))),
			vf.constructor(Friends, vf.list(name("Hans"), name("Bob"))),
			vf.constructor(Or, vf.list()),
			vf.constructor(Couples, vf.list(vf.tuple(name("A"), name("B")), vf.tuple(name("C"), name("D"))))
	};
	
	static {
		ts.declareKeywordParameter(Boolean, "anno", Boolean);
		ts.declareKeywordParameter(Boolean, "banno", Boolean);
	}
	
	private String[] testATerm = {
			"true{[\"anno\",false]}",
			"true{[\"banno\",false],[\"anno\",false]}",
			"true{}",
			"true",
			"and(true,false)",
		    "not(and(true,false))",
		    "!and(and(true,#A),#B)",
		    "twotups((true,false),(true,false))",
		    "or([true,false,true])",
		    "friends([name(\"Hans\"),name(\"Bob\")])",
		    "or([])",
		    "couples([(name(\"A\"),name(\"B\")),(name(\"C\"),name(\"D\"))])"
	    };

	private static IValue name(String n) {
		return vf.constructor(NameNode, vf.string(n));
	}
	
	public void testATermReader() {
		ATermReader testReader = new ATermReader();
		
		
		
		try {
			for (int i = 0; i < testATerm.length; i++) {
				IValue result = testReader.read(vf, ts, Boolean, new ByteArrayInputStream(testATerm[i].getBytes()));
				System.err.println(testATerm[i] + " -> " + result);
				
				if (!result.equals(testValues[i])) {
					fail(testATerm[i] + " did not parse correctly: " + result + " != " + testValues[i]);
				}
			}
		} catch (FactTypeUseException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}

	private final IEvaluator<Result<IValue>> setupWatchEvaluator() {
		return setupWatchEvaluator(false);
	}
	private final IEvaluator<Result<IValue>> setupWatchEvaluator(boolean debug) {
		var heap = new GlobalEnvironment();
		var root = heap.addModule(new ModuleEnvironment("___test___", heap));
		var evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), System.in, System.err, System.out,  root, heap);
		
		evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		
		evaluator.addRascalSearchPath(URIUtil.rootLocation("test-modules"));
		evaluator.addRascalSearchPath(URIUtil.rootLocation("benchmarks"));
		executeCommand(evaluator, "import IO;");
		executeCommand(evaluator, "int trig = 0;");
		executeCommand(evaluator, "void triggerWatch(LocationChangeEvent tp) { trig = trig + 1; " + (debug? "println(tp);": "") + " }");
		return evaluator;
	}

	private static IValue executeCommand(IEvaluator<Result<IValue>> eval, String command) {
		var result = eval.eval(null, command, URIUtil.rootLocation("stdin"));
		if (result.getStaticType().isBottom()) {
			return null;
		}
		return result.getValue();
	}

	private static boolean executeBooleanExpression(IEvaluator<Result<IValue>> eval, String expr) {
		var result = executeCommand(eval, expr);
		if (result instanceof IBool) {
			return ((IBool)result).getValue();
		}
		return false;
	}
	

	public void testWatch() throws InterruptedException {
		var evalTest = setupWatchEvaluator();
		executeCommand(evalTest, "writeFile(|tmp:///a/make-dir.txt|, \"hi\");");
		executeCommand(evalTest, "watch(|tmp:///a/|, true, triggerWatch);");
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"hi\");");
		Thread.sleep(100); // give it some time to trigger the watch callback
		
		assertTrue("Watch should have been triggered", executeBooleanExpression(evalTest, "trig > 0"));
	}

	public void testWatchNonRecursive() throws InterruptedException {
		var evalTest = setupWatchEvaluator(true);
		executeCommand(evalTest, "watch(|tmp:///a/test-watch.txt|, false, triggerWatch);");
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"hi\");");
		Thread.sleep(100); // give it some time to trigger the watch callback
		assertTrue("Watch should have been triggered", executeBooleanExpression(evalTest, "trig > 0"));
	}

	public void testWatchDelete() throws InterruptedException {
		var evalTest = setupWatchEvaluator();
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"hi\");");
		executeCommand(evalTest, "watch(|tmp:///a/|, true, triggerWatch);");
		executeCommand(evalTest, "remove(|tmp:///a/test-watch.txt|);");
		Thread.sleep(100); // give it some time to trigger the watch callback
		assertTrue("Watch should have been triggered for delete", executeBooleanExpression(evalTest, "trig > 0"));
	}
	

	public void testWatchSingleFile() throws InterruptedException {
		var evalTest = setupWatchEvaluator();
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch-a.txt|, \"making it exist\");"); 
		executeCommand(evalTest, "watch(|tmp:///a/test-watch-a.txt|, false, triggerWatch);");
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"bye\");");
		executeCommand(evalTest, "remove(|tmp:///a/test-watch.txt|);");
		Thread.sleep(100); // give it some time to trigger the watch callback
		assertTrue("Watch should not have triggered anything", executeBooleanExpression(evalTest, "trig == 0"));
	}

	public void testUnwatchStopsEvents() throws InterruptedException {
		var evalTest = setupWatchEvaluator();
		executeCommand(evalTest, "watch(|tmp:///a/|, true, triggerWatch);");
		Thread.sleep(10); 
		executeCommand(evalTest, "unwatch(|tmp:///a/|, true, triggerWatch);");
		Thread.sleep(100); // give it some time to trigger the watch callback
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"hi\");");
		executeCommand(evalTest, "remove(|tmp:///a/test-watch.txt|);");
		Thread.sleep(100); // give it some time to trigger the watch callback
		assertTrue("Watch should not have triggered anything", executeBooleanExpression(evalTest, "trig == 0"));
	}

	public void testUnwatchStopsEventsUnrecursive() throws InterruptedException {
		var evalTest = setupWatchEvaluator();
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"hi\");");
		executeCommand(evalTest, "watch(|tmp:///a/test-watch.txt|, false, triggerWatch);");
		Thread.sleep(10); 
		executeCommand(evalTest, "unwatch(|tmp:///a/test-watch.txt|, false, triggerWatch);");
		Thread.sleep(100); // give it some time to trigger the watch callback
		executeCommand(evalTest, "writeFile(|tmp:///a/test-watch.txt|, \"hi\");");
		executeCommand(evalTest, "remove(|tmp:///a/test-watch.txt|);");
		Thread.sleep(100); // give it some time to trigger the watch callback
		assertTrue("Watch should not have triggered anything", executeBooleanExpression(evalTest, "trig == 0"));
	}
}
