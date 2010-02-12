package org.rascalmpl.interpreter.load;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Module;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.ModuleLoadError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.ModuleParser;
import org.rascalmpl.uri.FileURIResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.errors.SubjectAdapter;
import org.rascalmpl.values.errors.SummaryAdapter;
import org.rascalmpl.values.uptr.Factory;

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

	private SyntaxError parseError(IConstructor tree, String file, String mod){
		SummaryAdapter summary = new SummaryAdapter(tree);
		SubjectAdapter subject = summary.getInitialSubject();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		
		if (subject != null) {
			ISourceLocation loc = vf.sourceLocation(file, subject.getOffset(), subject.getLength(), subject.getBeginLine(), subject.getEndLine(), subject.getBeginColumn(), subject.getEndColumn());
			return new SyntaxError("module " + mod, loc);
		}
		
		return new SyntaxError("unknown location, maybe you used a keyword as an identifier)", vf.sourceLocation(file, 0,1,1,1,0,1));
	}
	
	private byte[] readModule(InputStream inputStream) throws IOException{
		byte[] buffer = new byte[8192];
		
		ByteArrayOutputStream inputStringData = new ByteArrayOutputStream();
		
		int bytesRead;
		while((bytesRead = inputStream.read(buffer)) != -1){
			inputStringData.write(buffer, 0, bytesRead);
		}
		
		return inputStringData.toByteArray();
	}

	public IConstructor parseModule(IModuleFileLoader loader, String fileName, String name, AbstractAST ast, ModuleEnvironment env) throws IOException{
		byte[] data;
		
		InputStream inputStream = null;
		Set<String> sdfImports;
		URI location = FileURIResolver.constructFileURI(fileName);
		try{
			inputStream = loader.getInputStream(fileName);
			if (inputStream == null) {
				throw new ModuleLoadError(name, "not in path", ast);
			}
			data = readModule(inputStream);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		
		sdfImports = parser.getSdfImports(getSdfSearchPath(), location, data);
		
		List<String> sdfSearchPath = getSdfSearchPath();
		IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, location, data, env);

		if (tree.getConstructorType() == Factory.ParseTree_Summary) {
			throw parseError(tree, fileName, name);
		}

		return tree;
	}

	public IConstructor parseModule(URI location, String moduleString, ModuleEnvironment env) throws IOException{
		byte[] data = moduleString.getBytes();
		
		List<String> sdfSearchPath = getSdfSearchPath();
		Set<String> sdfImports;
		sdfImports = parser.getSdfImports(sdfSearchPath, location, data);

		IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, location, data, env);

		if(tree.getConstructorType() == Factory.ParseTree_Summary){
			throw parseError(tree, location.toString(), "-");
		}

		return tree;
	}

	public IConstructor parseModule(URI location, ModuleEnvironment env) throws IOException{
		byte[] data;
		
		InputStream inputStream = null;
		List<String> sdfSearchPath = getSdfSearchPath();
		Set<String> sdfImports;
		try{
			inputStream = URIResolverRegistry.getInstance().getInputStream(location);
			data = readModule(inputStream);
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		
		sdfImports = parser.getSdfImports(sdfSearchPath, location, data);

		IConstructor tree = parser.parseModule(sdfSearchPath, sdfImports, location, data, env);
		
		if(tree.getConstructorType() == Factory.ParseTree_Summary){
			throw parseError(tree, location.toString(), "-");
		}
		
		return tree;
	}

	public void setParser(ModuleParser parser) {
		this.parser = parser;
	}
}
