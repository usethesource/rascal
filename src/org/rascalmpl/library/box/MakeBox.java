package org.rascalmpl.library.box;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.Module;
import org.rascalmpl.interpreter.BoxEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.IO;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.ValueFactoryFactory;

public class MakeBox {

	private final String varName = "boxData";

	class Data extends ByteArrayOutputStream {
		ByteArrayInputStream get() {
			return new ByteArrayInputStream(this.buf);
		}
	}

	BoxEvaluator eval = new BoxEvaluator();

	private Evaluator commandEvaluator;
	private GlobalEnvironment heap;
	private ModuleEnvironment root;
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
		heap = new GlobalEnvironment();
		root = heap.addModule(new ModuleEnvironment("***makeBox***"));
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		commandEvaluator = new Evaluator(ValueFactoryFactory.getValueFactory(),
				stderr, stdout, root, heap);
		commandEvaluator.addClassLoader(getClass().getClassLoader());
		Object ioInstance = commandEvaluator.getJavaBridge()
				.getJavaClassInstance(IO.class);
		// Set output collector.
		((IO) ioInstance).setOutputStream(new PrintStream(System.out));
	}

	private void execute(String s) {
		commandEvaluator.eval(s, URI.create("box:///"));
	}

	private IValue launchRascalProgram(String resultName) {
		execute("import box::Box2Text;");
		try {
			IValue v = new PBFReader().read(ValueFactoryFactory
					.getValueFactory(), ts, adt, data.get());
			store(v, varName);
			if (resultName == null) {
				execute("main(" + varName + ");");
				return null;
			}
			execute(resultName + "=toList(" + varName + ");");
			IValue r = fetch(resultName);
			data.close();
			return r;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private IValue launchConcreteProgram(URI uri) {
		final String resultName = "c";
		execute("import box::Concrete;");
		// IString v = ValueFactoryFactory.getValueFactory().string(fileName);
		ISourceLocation v = ValueFactoryFactory.getValueFactory()
				.sourceLocation(uri);
		store(v, varName);
		execute(resultName + "=toList(" + varName + ");");
		IValue r = fetch(resultName);
		return r;
	}

	private IValue computeBox(URI uri) {
		ModuleEnvironment currentModule = (ModuleEnvironment) commandEvaluator
				.getCurrentEnvt().getRoot();
		try {
			IConstructor moduleTree = commandEvaluator.parseModule(uri,
					currentModule);
			ASTBuilder astBuilder = new ASTBuilder(new ASTFactory());
			Module moduleAst = astBuilder.buildModule(moduleTree);
			if (moduleAst != null)
				return eval.evalRascalModule(moduleAst);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IValue toRichTxt(URI uri) {
		start();
		try {
			IValue box = computeBox(uri);
			data = new Data();
			new PBFWriter().write(box, data, ts);
			return launchRascalProgram("c");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IValue toTxt(URI uri) {
		start();
		try {
			FileInputStream inp = new FileInputStream(uri.getRawPath());
			inp.close();
			return launchConcreteProgram(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
