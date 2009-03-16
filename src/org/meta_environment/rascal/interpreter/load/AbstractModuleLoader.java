package org.meta_environment.rascal.interpreter.load;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.errors.SubjectAdapter;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;


public abstract class AbstractModuleLoader implements IModuleLoader {
	protected static final String RASCAL_FILE_EXT = ".rsc";
	protected static final Parser PARSER = Parser.getInstance();
	protected static final ASTBuilder BUILDER = new ASTBuilder(new ASTFactory());

	public Module loadModule(String name) throws IOException {
		InputStream stream = null;
		
		try {
			stream = getStream(getFileName(name));
			IConstructor tree = PARSER
					.parseFromStream(stream, getFileName(name));

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw parseError(tree, getFileName(name), name);
			}

			return BUILDER.buildModule(tree);
		} catch (FactTypeUseException e) {
			throw new ImplementationError("Unexpected PDB typecheck exception", e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	abstract protected InputStream getStream(String fileName) throws IOException;

	protected String getFileName(String moduleName) {
		String fileName = moduleName.replaceAll("::", "/") + RASCAL_FILE_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}

	protected SyntaxError parseError(IConstructor tree, String file, String mod) throws MalformedURLException {
		SubjectAdapter subject = new SummaryAdapter(tree).getInitialSubject();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		URL url = new URL("file://" + file);
		ISourceLocation loc = vf.sourceLocation(url, subject.getOffset(), subject.getLength(), subject.getBeginLine(), subject.getEndLine(), subject.getBeginColumn(), subject.getEndColumn());

		return new SyntaxError("module " + mod, loc);
	}
}
