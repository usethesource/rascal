package org.meta_environment.rascal.interpreter.load;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.PBFReader;
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
	protected static final String RASCAL_BIN_FILE_EXT = ".rsc.bin";
	protected static final Parser PARSER = Parser.getInstance();
	protected static final ASTBuilder BUILDER = new ASTBuilder(new ASTFactory());
	
	private IConstructor tryLoadBinary(String name){
		IConstructor tree = null;
		
		InputStream inputStream = null;
		try{
			inputStream = getInputStream(getBinaryFileName(name));
			
			PBFReader pbfReader = new PBFReader();
			tree = (IConstructor) pbfReader.read(ValueFactoryFactory.getValueFactory(), inputStream);
		}catch(IOException ioex){
			// Ignore; this is allowed.
		}catch(ClassCastException ccex){
			// Don't care.
		}finally{
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(IOException ioex){
					// Ignore.
				}
			}
		}
		
		return tree;
	}

	public Module loadModule(String name) throws IOException {
		InputStream stream = null;
		
		try {
			IConstructor tree = tryLoadBinary(name); // <-- Don't do this if you want to generate new binaries.
			
			if(tree == null){
				stream = getInputStream(getFileName(name));
				tree = PARSER.parseFromStream(stream, getFileName(name));
			}

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw parseError(tree, getFileName(name), name);
			}
			
			//try{dumpTree(name, tree);}catch(IOException ioex){ioex.printStackTrace();} // <-- Do this if you want to generate new binaries.
			
			return BUILDER.buildModule(tree);
		} catch (FactTypeUseException e) {
			throw new ImplementationError("Unexpected PDB typecheck exception", e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
	
	/*private void dumpTree(String moduleName, IConstructor tree) throws IOException{ // <-- Do this if you want to generate new binaries.
		PBFWriter pbfWriter = new PBFWriter();
		pbfWriter.write(tree, getOutputStream(getBinaryFileName(moduleName)));
	}*/

	abstract protected InputStream getInputStream(String fileName) throws IOException;

	abstract protected OutputStream getOutputStream(String fileName) throws IOException;
	
	protected static String getFileName(String moduleName) {
		String fileName = moduleName.replaceAll("::", "/") + RASCAL_FILE_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}
	
	protected static String getBinaryFileName(String moduleName) {
		String fileName = moduleName.replaceAll("::", "/") + RASCAL_BIN_FILE_EXT;
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
