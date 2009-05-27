package org.meta_environment.rascal.interpreter.load;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.eclipse.imp.pdb.facts.io.PBFWriter;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.errors.SubjectAdapter;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.ModuleLoadError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.ModuleParser;
import org.meta_environment.uptr.Factory;

public class ModuleLoader{
	protected static final ASTBuilder BUILDER = new ASTBuilder(new ASTFactory());
	
	private List<IModuleFileLoader> loaders = new ArrayList<IModuleFileLoader>();
	private List<ISdfSearchPathContributor> contributors = new ArrayList<ISdfSearchPathContributor>();
	private final ModuleParser parser;
	
	public ModuleLoader(){
		this(new ModuleParser());
	}
	
	public ModuleLoader(ModuleParser parser){
		this.parser = parser;
	}

	public void addFileLoader(IModuleFileLoader loader){
		loaders.add(0, loader);
	}
	
	public void addSdfSearchPathContributor(ISdfSearchPathContributor contrib){
		contributors.add(0, contrib);
	}
	
	private InputStream getInputStream(String name){
		for(IModuleFileLoader loader : loaders){
			try{
				return loader.getInputStream(name);
			}catch(IOException ioex){
				// this happens regularly
			}
		}
		
		return null;
	}
	
	private IConstructor tryLoadBinary(String name){
		IConstructor tree = null;
		
		InputStream inputStream = getInputStream(getBinaryFileName(name));
		if(inputStream == null) return null;
		
		PBFReader pbfReader = new PBFReader();
		try{
			tree = (IConstructor) pbfReader.read(ValueFactoryFactory.getValueFactory(), inputStream);
		}catch(IOException ioex){
			// Ignore; this is allowed.
		}  
		finally{
			try{
				inputStream.close();
			}catch(IOException ioex){
				throw new ImplementationError(ioex.getMessage(), ioex);
			}
		}
		
		return tree;
	}
	
	private void writeBinary(String name, IConstructor tree){
		File binFile = new File(getBinaryFileName(name));
		
		System.out.println("Writing binary for: "+name+" > "+binFile.getAbsolutePath());
		OutputStream outputStream = null;
		
		PBFWriter pbfWriter = new PBFWriter();
		try{
			outputStream = new FileOutputStream(binFile);
			pbfWriter.write(tree, outputStream);
		}catch(IOException ioex){
			ioex.printStackTrace();
		}finally{
			if(outputStream != null){
				try{
					outputStream.close();
				}catch(IOException ioex){
					ioex.printStackTrace();
				}
			}
		}
	}

	public Module loadModule(String name, AbstractAST ast){
		if(isSdfModule(name)){
			return null;
		}
		
		try{
			IConstructor tree = tryLoadBinary(name); // <-- Don't do this if you want to generate new binaries.
			if(tree == null){
				String fileName = getFileName(name);
				
				tree = parseModule(fileName, name, ast);
				
				//writeBinary(name, tree); // Enable if you want to generate new binaries.
			}

			return BUILDER.buildModule(tree);
		}catch (FactTypeUseException e){
			throw new ImplementationError("Unexpected PDB typecheck exception", e);
		}catch (IOException e){
			throw new ModuleLoadError(name, e.getMessage(), ast);
		}
	}
	
	public boolean isSdfModule(String name){
		for(String path : getSdfSearchPath()){
			if(new File(new File(path), getSdfFileName(name)).exists()){
			   return true;
			}
		}
		
		return false;
	}

	public List<String> getSdfSearchPath(){
		List<String> result = new ArrayList<String>();
		for (ISdfSearchPathContributor c : contributors){
			result.addAll(c.contributePaths());
		}
		return result;
	}

	private static String getFileName(String moduleName){
		String fileName = moduleName.replaceAll("::", "/") + Configuration.RASCAL_FILE_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}
	
	private static String getSdfFileName(String moduleName){
		String fileName = moduleName.replaceAll("::", "/") + Configuration.SDF_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}
	
	private static String getBinaryFileName(String moduleName){
		String fileName = moduleName.replaceAll("::", "/") + Configuration.RASCAL_BIN_FILE_EXT;
		fileName = Names.unescape(fileName);
		return fileName;
	}

	private SyntaxError parseError(IConstructor tree, String file, String mod) throws MalformedURLException{
		SubjectAdapter subject = new SummaryAdapter(tree).getInitialSubject();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		URL url = new URL("file://" + file);
		ISourceLocation loc = vf.sourceLocation(url, subject.getOffset(), subject.getLength(), subject.getBeginLine(), subject.getEndLine(), subject.getBeginColumn(), subject.getEndColumn());

		return new SyntaxError("module " + mod, loc);
	}


//	@SuppressWarnings("unchecked")
//	public IConstructor parseCommand(String command, String fileName) throws IOException {
//		// TODO: add support for concrete syntax here (now it ignores the sdf imports)
//		return parser.parseCommand(command);
//	}
	
	public IConstructor parseModule(String fileName, String name, AbstractAST ast) throws IOException{
		InputStream inputStream = null;
		Set<String> sdfImports;
		try{
			inputStream = getInputStream(fileName);
			if (inputStream == null) {
				throw new ModuleLoadError(name, "not in path", ast);
			}
			sdfImports = parser.getSdfImports(getSdfSearchPath(), fileName, inputStream);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}

		InputStream secondInputStream = null;
		try{
			List<String> sdfSearchPath = getSdfSearchPath();
			secondInputStream = getInputStream(fileName);
			IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, fileName, secondInputStream);

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw parseError(tree, fileName, name);
			}

			return tree;
		}finally{
			if(secondInputStream != null){
				secondInputStream.close();
			}
		}
	}

	public IConstructor parseModule(String fileName, String name, String moduleString) throws IOException{
		List<String> sdfSearchPath = getSdfSearchPath();
		
		InputStream inputStream = null;
		Set<String> sdfImports;
		try{
			inputStream = new ByteArrayInputStream(moduleString.getBytes());
			
			sdfImports = parser.getSdfImports(sdfSearchPath, fileName, inputStream);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}

		InputStream secondInputStream = null;
		try{
			secondInputStream = new ByteArrayInputStream(moduleString.getBytes());
			IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, fileName, secondInputStream);

			if(tree.getConstructorType() == Factory.ParseTree_Summary){
				throw parseError(tree, fileName, name);
			}

			return tree;
		}finally{
			if (secondInputStream != null) {
				secondInputStream.close();
			}
		}
	}
	
}
