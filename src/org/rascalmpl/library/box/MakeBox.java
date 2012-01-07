/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.box;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.BinaryValueReader;
import org.eclipse.imp.pdb.facts.io.BinaryValueWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.Module;
import org.rascalmpl.interpreter.BoxEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class MakeBox {

	private final String varName = "boxData";

	private final IValueFactory values;

	public MakeBox(IValueFactory values) {
		super();
		final PrintWriter stderr = new PrintWriter(System.err);
		final PrintWriter stdout = new PrintWriter(System.out);
		commandEvaluator = new Evaluator(values, stderr, stdout, root, heap);
		this.values = values;
	}

	public MakeBox() {
		final PrintWriter stderr = new PrintWriter(System.err);
		final PrintWriter stdout = new PrintWriter(System.out);
		this.values = ValueFactoryFactory.getValueFactory();
		commandEvaluator = new Evaluator(values, stderr, stdout, root, heap);
	}

	public MakeBox(PrintWriter stdout, PrintWriter stderr) {
		this.values = ValueFactoryFactory.getValueFactory();
		commandEvaluator = new Evaluator(values, stderr, stdout, root, heap);
	}

	class Data extends ByteArrayOutputStream {
		ByteArrayInputStream get() {
			return new ByteArrayInputStream(this.buf);
		}
	}

	BoxEvaluator eval = new BoxEvaluator();

	final private GlobalEnvironment heap = new GlobalEnvironment();
	final private ModuleEnvironment root = heap
			.addModule(new ModuleEnvironment("___MakeBox___", heap));

	final private Evaluator commandEvaluator;

	public Evaluator getCommandEvaluator() {
		return commandEvaluator;
	}

	private Data data;
	private TypeStore ts = BoxEvaluator.getTypeStore();
	private Type adt = BoxEvaluator.getType();

	void store(IValue v, String varName) {
		Result<IValue> r = makeResult(v.getType(), v, commandEvaluator);
		root.storeVariable(varName, r);
		r.setPublic(true);
	}

	IValue fetch(String varName) {
		Result<IValue> r = root.getVariable(varName);
		return r.getValue();
	}

	private void start() {
		commandEvaluator.addClassLoader(getClass().getClassLoader());
	}

	private void execute(String s) {
		commandEvaluator.eval(commandEvaluator.getMonitor(), s, URI.create("box:///"));
	}

	private IValue launchRascalProgram(String resultName, boolean richText) {
		execute("import lang::box::util::Box2Text;");
		try {
			IValue v = new BinaryValueReader().read(
					ValueFactoryFactory.getValueFactory(), ts, adt, data.get());
			store(v, varName);
			if (resultName == null) {
				execute("main(" + varName + ");");
				return null;
			}
			if (richText)
				execute(resultName + "=box2data(" + varName + ");");
			else
				execute(resultName + "=box2text(" + varName + ");");
			IValue r = fetch(resultName);
			data.close();
			return r;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IValue launchRascalProgramExport(String cmd, URI src, URI dest,
			String ext2) {
		execute("import lang::box::util::Box2Text;");
		try {
			IValue d = new BinaryValueReader().read(values, ts, adt, data.get());
			store(d, "d");
			ISourceLocation v = values.sourceLocation(src), w = values
					.sourceLocation(dest);
			IString x = values.string(ext2);
			store(v, "v");
			store(w, "w");
			store(x, "x");
			execute("c=" + cmd + "(d);");
			execute("toExport(v, w, c, x)");
			IValue r = fetch("c");
			data.close();
			return r;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IValue launchConcreteProgramExport(URI src, URI dest, String ext,
			String cmd, String ext2) {
		execute("import lang::box::util::Box2Text;");
		if (ext.equals("oberon0"))
			execute("import lang::" + ext + "::utils::BoxFormat;");
		else
			execute("import lang::" + ext + "::util::BoxFormat;");
		ISourceLocation v = values.sourceLocation(src), w = values
				.sourceLocation(dest);
		IString x = values.string(ext2);
		store(v, "v");
		store(w, "w");
		store(x, "x");
		execute("c=" + cmd + "(toBox(v));");
		execute("toExport(v, w, c, x)");
		IValue r = fetch("c");
		return r;
	}

	private IValue launchConcreteProgram(URI uri, String ext) {
		// System.err.println("Start launch concrete"+uri);
		execute("import lang::box::util::Box2Text;");
		if (ext.equals("oberon0"))
			execute("import lang::" + ext + "::utils::BoxFormat;");
		else
			execute("import lang::" + ext + "::util::BoxFormat;");
		ISourceLocation v = values.sourceLocation(uri);
		store(v, "v");
		execute("c=box2data(toBox(v));");
		IValue r = fetch("c");
		return r;
	}

	public IValue makeBox(ISourceLocation loc,
			org.rascalmpl.interpreter.IEvaluatorContext c) {
		return computeBox(loc.getURI());
	}

	public IConstructor computeBox(URI uri) {
		try {
			// System.err.println("computeBox: start parsing");
			IConstructor moduleTree = commandEvaluator.parseModule(commandEvaluator, uri, null);
			IList z = TreeAdapter.getArgs(moduleTree);
			// System.err.println("computeBox: parsed");
			Module moduleAst = new ASTBuilder().buildModule(moduleTree);
			// System.err.println("computeBox: build");
			commandEvaluator.getHeap().clear();
			if (moduleAst != null)
				return (IConstructor) eval.evalRascalModule(moduleAst, z);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String text2String(IValue v) {
		IList rules = (IList) v;
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < rules.length(); i++) {
			b.append(((IString) rules.get(i)).getValue());
			b.append("\n");
		}
		return b.toString();
	}

	public String toPrint(String cmd, URI uri) {
		return "";
		/*
		 * start(); int tail = uri.getPath().lastIndexOf('.'); String ext =
		 * uri.getPath().substring(tail + 1); return
		 * text2String(launchConcreteProgram(cmd, uri, ext));
		 */
	}

	public String toExport(String cmd, String ext2, URI uri1, URI uri2) {
		start();
		int tail = uri1.getPath().lastIndexOf('.');
		String ext1 = uri1.getPath().substring(tail + 1);
		return text2String(launchConcreteProgramExport(uri1, uri2, ext1, cmd,
				ext2));
	}

	public IValue rascalToExport(String cmd, String ext2, URI src, URI dest) {
		start();
		try {
			IValue box = computeBox(src);
			data = new Data();
			new BinaryValueWriter().write(box, data, ts);
			return launchRascalProgramExport(cmd, src, dest, ext2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toRichText(URI uri, String ext) {
		start();
		return text2String(launchConcreteProgram(uri, ext));
	}

	public String toRichTextRascal(URI uri) {
		start();
		try {
			IValue box = computeBox(uri);
			data = new Data();
			new BinaryValueWriter().write(box, data, ts);
			return text2String(launchRascalProgram("c", true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String box2String(IValue box) {
		data = new Data();
		try {
			new BinaryValueWriter().write(box, data, ts);
			return text2String(launchRascalProgram("c", false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public IValue getFigure(URI uri, String layout) {
		start();
		execute("import experiments::Concept::GetFigure;");
		ISourceLocation v = values.sourceLocation(uri);
		store(v, "v");
		IString w = values.string(layout);
		store(w, "w");
		execute("c=getFigure(v, w);");
		IValue r = fetch("c");
		return r;
	}

}
