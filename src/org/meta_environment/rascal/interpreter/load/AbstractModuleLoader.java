package org.meta_environment.rascal.interpreter.load;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.errors.SubjectAdapter;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.errors.ModuleLoadException;
import org.meta_environment.rascal.interpreter.errors.SyntaxError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public abstract class AbstractModuleLoader implements IModuleLoader {
	protected static final String RASCAL_FILE_EXT = ".rsc";
	protected static final Parser PARSER = Parser.getInstance();
	protected static final ASTBuilder BUILDER = new ASTBuilder(new ASTFactory());

	public Module loadModule(String name) throws ModuleLoadException {
		InputStream stream = null;
		
		try {
			stream = getStream(getFileName(name));
			IConstructor tree = PARSER
					.parseFromStream(stream);

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw new SyntaxError(parseError(tree, name));
			}

			return BUILDER.buildModule(tree);
		} catch (FactTypeUseException e) {
			throw new ModuleLoadException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ModuleLoadException(e.getMessage(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					throw new ModuleLoadException(e.getMessage(), e);
				}
			}
		}
	}

	abstract protected InputStream getStream(String fileName) throws IOException;

	protected String getFileName(String moduleName) {
		String fileName = moduleName.replaceAll("::", "/") + RASCAL_FILE_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}

	protected String parseError(IConstructor tree, String file) {
		SubjectAdapter subject = new SummaryAdapter(tree).getInitialSubject();

		return file + " at line " + subject.getEndLine() + ", column "
				+ subject.getEndColumn();
	}
}
