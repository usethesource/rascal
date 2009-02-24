package org.meta_environment.rascal.interpreter.load;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;
import org.meta_environment.rascal.interpreter.errors.ModuleLoadException;
import org.meta_environment.rascal.interpreter.errors.SyntaxError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class LegacyModuleLoader implements IModuleLoader {
	private static final String[] SEARCH_PATH = {
		"src/StandardLibrary/", 
		"src/test/", 
		"src/",
		"demo/",
		"demo/Booleans/",
		"demo/Fun/",
		"demo/Graph/",
		"demo/Integers/",
		"demo/JavaFun/",					
		"demo/Lexicals/",
		"demo/Misc/",
		"demo/Pico/",
		"demo/PicoAbstract/",
		"demo/Rascal/"
	};
	
	protected static final String RASCAL_FILE_EXT = ".rsc";
	protected static final Parser PARSER = Parser.getInstance();
	protected static final ASTBuilder BUILDER = new ASTBuilder(new ASTFactory());

	public Module loadModule(String name) throws ModuleLoadException {
		try {
			String fileName = name.replaceAll("::","/") + RASCAL_FILE_EXT;
			fileName = Names.unescape(fileName);
			File file = new File(fileName);

			// TODO: support proper search path for modules
			// TODO: support properly packaged/qualified module names
			// TODO: mind the / at the end of each directory!
			String searchPath[] = SEARCH_PATH;

			for(int i = 0; i < searchPath.length; i++){
				file = new File(searchPath[i] + fileName);
				if(file.exists()){
					break;
				}
			}
			if (!file.exists()) {
				throw new ModuleLoadException("Can not find file for module " + name);
			}

			IConstructor tree = PARSER.parseFromFile(file);

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw new SyntaxError(parseError(tree, name));
			}

			return BUILDER.buildModule(tree);			
		} catch (FactTypeUseException e) {
			throw new ImplementationError("Something went wrong during parsing of " + name, e);
		} catch (FileNotFoundException e) {
			throw new ModuleLoadException("Could not import module " + name, e);
		} catch (IOException e) {
			throw new ModuleLoadException("Could not import module " + name, e);
		}
	}
	
	protected String parseError(IConstructor tree, String file) {
		ISourceRange range = new SummaryAdapter(tree).getInitialErrorRange();
		
	    return file + " at line " + range.getEndLine() + ", column " + range.getEndColumn();
	}

}
