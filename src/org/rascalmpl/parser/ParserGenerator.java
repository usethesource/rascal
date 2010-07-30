package org.rascalmpl.parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
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
		evaluator.doImport("rascal::parser::Normalization");
		evaluator.doImport("rascal::parser::Definition");
	}

	/**
	 * Generate a parser from a Rascal syntax definition (a set of production rules).
	 * 
	 * @param loc     a location for error reporting
	 * @param name    the name of the parser for use in code generation and for later reference
	 * @param imports a set of syntax definitions (which are imports in the Rascal grammar)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IGLL getParser(ISourceLocation loc, String name, ISet imports) {
		try {
			// TODO: add caching
			System.err.println("Importing and normalizing grammar");
			IConstructor grammar = getGrammar(imports);
			String normName = name.replaceAll("\\.", "_");
			System.err.println("Generating java source code for parser");
			IString classString = (IString) evaluator.call("generate", vf.string(packageName), vf.string(normName), grammar);
			FileOutputStream s = null;
			try {
				s = new FileOutputStream("/tmp/parser.java");
				s.write(classString.getValue().getBytes());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			System.err.println("compiling generated java code");
			Class<IGLL> parser = (Class<IGLL>) bridge.compileJava(loc, packageName + "." + normName, classString.getValue());
			System.err.println("instantiating generated parser class");
			return parser.newInstance();
		}  catch (ClassCastException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError("parser generator:" + e.getMessage(), e);
		} 
	}

	public IConstructor getGrammar(ISet imports) {
		return (IConstructor) evaluator.call("imports2grammar", imports);
	}
}
