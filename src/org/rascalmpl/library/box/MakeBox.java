package org.rascalmpl.library.box;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.ASTFactoryFactory;
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
	private final String moduleName = "moduleName";

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
			.addModule(new ModuleEnvironment("***MakeBox***", heap));

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
		commandEvaluator.eval(s, URI.create("box:///"));
	}

	private IValue launchRascalProgram(String resultName) {
		execute("import box::Box2Text;");
		try {
			IValue v = new PBFReader().read(
					ValueFactoryFactory.getValueFactory(), ts, adt, data.get());
			store(v, varName);
			if (resultName == null) {
				execute("main(" + varName + ");");
				return null;
			}
			execute(resultName + "=box2data(" + varName + ");");
			IValue r = fetch(resultName);
			data.close();
			return r;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IValue launchRascalProgram(String cmd, URI src, URI dest) {
		execute("import box::Box2Text;");
		try {
			IValue d = new PBFReader().read(values, ts, adt, data.get());
			store(d, "d");
			ISourceLocation v = values.sourceLocation(src), w = values
					.sourceLocation(dest);
			store(v, "v");
			store(w, "w");
			execute("c=" + cmd + "(d, v, w);");
			IValue r = fetch("c");
			data.close();
			return r;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IValue launchConcreteProgram(String cmd, URI src, URI dest,
			String ext) {
		execute("import box::" + ext + "::Default;");
		ISourceLocation v = values.sourceLocation(src), w = values
				.sourceLocation(dest);
		store(v, "v");
		store(w, "w");
		execute("c=" + cmd + "(v, w);");
		IValue r = fetch("c");
		return r;
	}

	private IValue launchConcreteProgram(String cmd, URI uri, String ext) {
		// System.err.println("Start launch concrete"+uri);
		execute("import box::" + ext + "::Default;");
		ISourceLocation v = values.sourceLocation(uri);
		store(v, "v");
		execute("c=" + cmd + "(v);");
		IValue r = fetch("c");
		return r;
	}

	private IValue launchTemplateProgram(URI uri, String s) {
		final String resultName = "c";
		execute("import box::" + s + ";");
		ISourceLocation v = values.sourceLocation(uri);
		store(v, varName);
		String name = new File(uri.getPath()).getName();
		name = name.substring(0, name.lastIndexOf('.'));
		IString w = values.string(name);
		store(w, moduleName);
		execute(resultName + "=toStr(" + varName + "," + moduleName + ");");
		IValue r = fetch(resultName);
		return r;
	}

	public IValue makeBox(ISourceLocation loc,
			org.rascalmpl.interpreter.IEvaluatorContext c) {
		return computeBox(loc.getURI());
	}


	public IConstructor computeBox(URI uri) {
		try {
			// System.err.println("computeBox: start parsing");
			IConstructor moduleTree = commandEvaluator.parseModule(uri, null);
			IList z = TreeAdapter.getArgs(moduleTree);
			// System.err.println("computeBox: parsed");
			ASTBuilder astBuilder = new ASTBuilder(
					ASTFactoryFactory.getASTFactory());
			Module moduleAst = astBuilder.buildModule(moduleTree);
			// System.err.println("computeBox: build");
			commandEvaluator.getHeap().clear();
			if (moduleAst != null)
				return (IConstructor) eval.evalRascalModule(moduleAst, z);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IValue toSrc(URI uri) {
		start();
		int tail = uri.getPath().lastIndexOf('.');
		String s = uri.getPath().substring(tail + 1);
		s = s.substring(0, 1).toUpperCase() + s.substring(1);
		return launchTemplateProgram(uri, s);
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
		start();
		int tail = uri.getPath().lastIndexOf('.');
		String ext = uri.getPath().substring(tail + 1);
		return text2String(launchConcreteProgram(cmd, uri, ext));
	}

	public String toPrint(String cmd, URI uri1, URI uri2) {
		start();
		int tail = uri1.getPath().lastIndexOf('.');
		String ext = uri1.getPath().substring(tail + 1);
		return text2String(launchConcreteProgram(cmd, uri1, uri2, ext));
	}

	public IValue rascalToPrint(String cmd, URI src, URI dest) {
		start();
		try {
			IValue box = computeBox(src);
			data = new Data();
			new PBFWriter().write(box, data, ts);
			return launchRascalProgram(cmd, src, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toRichText(URI uri, String ext) {
		start();
		return text2String(launchConcreteProgram("toRichText", uri, ext));
	}

	public String toRichText(URI uri) {
		start();
		try {
			IValue box = computeBox(uri);
			data = new Data();
			new PBFWriter().write(box, data, ts);
			return text2String(launchRascalProgram("c"));
		} catch (IOException e) {
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
