package org.rascalmpl.parser;

import java.io.PrintWriter;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ParsetreeAdapter;

public class ParserGenerator {
	private final Evaluator evaluator;
	private final JavaBridge bridge;
	private final IValueFactory vf;
	private static final String packageName = "org.rascalmpl.java.parser.object";

	public ParserGenerator(PrintWriter out, List<ClassLoader> loaders, IValueFactory factory) {
		this.bridge = new JavaBridge(out, loaders, factory);
		this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), out, out, new ModuleEnvironment("***parsergenerator***"), new GlobalEnvironment());
		this.vf = factory;
		evaluator.doImport("rascal::parser::Generator");
		evaluator.doImport("rascal::parser::Definition");
	}
	
	@SuppressWarnings("unchecked")
	public IGLL getParser(ISourceLocation loc, String name, IConstructor moduleTree) {
		try {
			// TODO: add caching
			IConstructor grammar = (IConstructor) evaluator.call("module2grammar", ParsetreeAdapter.getTop(moduleTree));
			System.err.println("Imported and normalized grammar: " + grammar);
			String normName = name.replaceAll("\\.", "_");
			IString classString = (IString) evaluator.call("generate", vf.string(packageName), vf.string(normName), grammar);
			Class<IGLL> parser = (Class<IGLL>) bridge.compileJava(loc, packageName + "." + normName, classString.getValue());
			return parser.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ImplementationError("parser generator: " + e.getMessage(), e);
		} catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		}
	}
}
