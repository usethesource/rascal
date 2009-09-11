package org.meta_environment.rascal.interpreter.load;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.errors.SubjectAdapter;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.staticErrors.ModuleLoadError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.utils.Names;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.ModuleParser;
import org.meta_environment.uptr.Factory;

public class ModuleLoader{
	private List<IModuleFileLoader> loaders = new ArrayList<IModuleFileLoader>();
	private List<ISdfSearchPathContributor> contributors = new ArrayList<ISdfSearchPathContributor>();
	private ModuleParser parser;
	private final boolean saveParsedModules = (System.getProperty("rascal.options.saveBinaries") != null ? System.getProperty("rascal.options.saveBinaries").equals("true") : false);
	
	public ModuleLoader(){
		this(new ModuleParser());
	}
	
	public ModuleLoader(ModuleParser parser){
		this.parser = parser;
		parser.setLoader(this);
	}

	public ModuleParser getParser() {
		return parser;
	}
	
	public void addFileLoader(IModuleFileLoader loader){
		loaders.add(0, loader);
	}
	
	public void addSdfSearchPathContributor(ISdfSearchPathContributor contrib){
		contributors.add(0, contrib);
	}
	
	private IModuleFileLoader getModuleLoader(String filename){
		for(IModuleFileLoader loader : loaders){
			if(loader.fileExists(filename)){
				return loader;
			}
		}
		
		return null;
	}
	
	private IConstructor tryLoadBinary(IModuleFileLoader loader, String name){
		IConstructor tree = null;
		
		InputStream inputStream = loader.getInputStream(name);
		if(inputStream == null) return null;
		
		PBFReader pbfReader = new PBFReader();
		try{
			tree = (IConstructor) pbfReader.read(ValueFactoryFactory.getValueFactory(), inputStream);
		}catch(IOException ioex){
			// Ignore; this is allowed.
		}finally{
			try{
				inputStream.close();
			}catch(IOException ioex){
				throw new ImplementationError(ioex.getMessage(), ioex);
			}
		}
		
		return tree;
	}

	public Module loadModule(String name, AbstractAST ast, ModuleEnvironment env) {
		if(isSdfModule(name)){
			return null;
		}
		
		String fileName = getFileName(name);
		String binaryName = getBinaryFileName(name);
		
		try{
			IModuleFileLoader loader = getModuleLoader(fileName);
			if(loader == null) throw new ModuleLoadError(name, null, ast);
			
			IConstructor tree = null;
			if(loader.supportsLoadingBinaries()){
				if (!saveParsedModules) {
					tree = tryLoadBinary(loader, binaryName);
				}
			}
			
			if(tree == null){
				tree = parseModule(loader, fileName, name, ast, env);
			}
			
			if (saveParsedModules) {
				loader.tryWriteBinary(fileName, binaryName, tree);
			}
			
			Module moduleAst = new ASTBuilder(new ASTFactory()).buildModule(tree);
			
			if (moduleAst == null) {
				throw new ImplementationError("Unexpected implementation error, all ambiguous ast's have been filtered for module " + name, ast.getLocation());
			}
			return moduleAst;
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
		ISourceLocation loc = vf.sourceLocation(file, subject.getOffset(), subject.getLength(), subject.getBeginLine(), subject.getEndLine(), subject.getBeginColumn(), subject.getEndColumn());

		return new SyntaxError("module " + mod, loc);
	}


	public IConstructor parseModule(IModuleFileLoader loader, String fileName, String name, AbstractAST ast, ModuleEnvironment env) throws IOException{
		InputStream inputStream = null;
		Set<String> sdfImports;
		try{
			inputStream = loader.getInputStream(fileName);
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
			secondInputStream = loader.getInputStream(fileName);
			IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, fileName, secondInputStream, env);

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

	public IConstructor parseModule(String fileName, String moduleString, ModuleEnvironment env) throws IOException{
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
			IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, fileName, secondInputStream, env);

			if(tree.getConstructorType() == Factory.ParseTree_Summary){
				throw parseError(tree, fileName, "-");
			}

			return tree;
		}finally{
			if (secondInputStream != null) {
				secondInputStream.close();
			}
		}
	}

	public void setParser(ModuleParser parser) {
		this.parser = parser;
	}
	
}
