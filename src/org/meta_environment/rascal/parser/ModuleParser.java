package org.meta_environment.rascal.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.ModuleEnvironment;
import org.meta_environment.rascal.interpreter.load.ModuleLoader;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ParsetreeAdapter;
import org.meta_environment.uptr.SymbolAdapter;

import sglr.IInvoker;
import sglr.LegacySGLRInvoker;
import sglr.SGLRInvoker;

public class ModuleParser {
	private static final String TBL_EXTENSION = ".tbl";
	private static final String SYMBOLS_EXTENSION = ".symbols";
	
	protected static final String META_LANGUAGE_KEY = "meta";
	private final IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
	private final SdfImportExtractor importExtractor = new SdfImportExtractor();
	
	public class TableInfo {
		private String tableName;
		private String symbolsName;
		
		public TableInfo(String defaultParsetableProperty) {
			this.tableName = defaultParsetableProperty;
			this.symbolsName = null;
		}
		
		public TableInfo(String tablefileName, String symbolsFileName) {
			this.tableName = tablefileName;
			this.symbolsName = symbolsFileName;
		}

		public String getSymbolsName() {
			return symbolsName;
		}
		
		public String getTableName() {
			return tableName;
		}
	}
	
	private final static IInvoker sglrInvoker;
	protected static final int SGLR_OPTIONS_NO_INDIRECT_PREFERENCE = IInvoker.FILTERS_DEFAULT & ~IInvoker.FILTERS_INDIRECT_PREFERENCE;
	protected static final int DEFAULT_SGLR_OPTIONS = IInvoker.FILTERS_DEFAULT;
	
	static{
		String osName = System.getProperty("os.name");
		
		if(osName.indexOf("Linux") != -1){ // Linux.
			sglrInvoker = SGLRInvoker.getInstance();
		}else if(osName.indexOf("Mac") != -1 || osName.indexOf("Darwin") != -1){ // Mac.
			sglrInvoker = SGLRInvoker.getInstance();
		}else{
			sglrInvoker = new LegacySGLRInvoker();
		}
	}

	public void setLoader(ModuleLoader loader) {
		// nop
	}
	
	public Set<String> getSdfImports(List<String> sdfSearchPath, String fileName, InputStream source) throws IOException {
		try {
			IConstructor tree= parseFromStream(Configuration.getHeaderParsetableProperty(), fileName, source, false);

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw new SyntaxError(fileName, new SummaryAdapter(tree).getInitialSubject().getLocation());
			}
			return importExtractor.extractImports(tree, sdfSearchPath);
		}
		catch (FactParseError p) {
			throw new ImplementationError("unexpected error: " + p.getMessage());
		}
	}

	public IConstructor parseCommand(Set<String> sdfImports, List<String> sdfSearchPath, String fileName, String command) throws IOException {
		TableInfo table = lookupTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		
		return parseFromString(table.getTableName(), fileName, command, false);
	}

	public void generateModuleParser(List<String> sdfSearchPath, Set<String> sdfImports, Environment env) throws IOException {
		TableInfo info = getOrConstructParseTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		declareConcreteSyntaxTypes(info.getSymbolsName(), env);
	}
	
	public IConstructor parseModule(List<String> sdfSearchPath, Set<String> sdfImports, String fileName, InputStream source, ModuleEnvironment env) throws IOException {
//		TableInfo table = lookupTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		TableInfo table = getOrConstructParseTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		declareConcreteSyntaxTypes(table.getSymbolsName(), env);
		
		try {
			return parseFromStream(table.getTableName(), fileName, source, false);
		} catch (FactParseError e) {
			throw new ImplementationError("parse tree format error", e);
		} 
	}

	protected void declareConcreteSyntaxTypes(String symbolsName, Environment env) throws IOException {
		if (symbolsName == null) {
			return;
		}
		ATermReader reader = new ATermReader();
		
		IList symbols = (IList) reader.read(valueFactory, Factory.uptr, Factory.Symbols, new FileInputStream(symbolsName));
		
		SymbolAdapter sym;
		
		// TODO: should actually also declare all complex symbols, since ones that are used but not present
		// in any SDF module won't work anyway. 
		
		for (IValue symbol : symbols) {
			sym = new SymbolAdapter((IConstructor) symbol);
			
			if (sym.isCf() || sym.isLex()) {
				sym = sym.getSymbol();
			}
			
			if (sym.isSort()) {
				String name = sym.getName();
				
				if (!name.startsWith("_")) {
					env.concreteSyntaxType(sym.getName(), (IConstructor) symbol);
				}
			}
		}
	}

	public IConstructor parseObjectLanguageFile(List<String> sdfSearchPath, Set<String> sdfImports, String fileName) throws IOException {
		TableInfo table = getOrConstructParseTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		try {
			return parseFromFile(table.getTableName(), fileName, true);
		} catch (FactParseError e) {
			throw new ImplementationError("parse tree format error", e);
		} 
	}
	
	protected TableInfo lookupTable(String key, Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		if (sdfImports.isEmpty()) {
			return new TableInfo(Configuration.getDefaultParsetableProperty());
		}

		TableInfo table = getTable(key, sdfImports, sdfSearchPath);

		if (table == null) {
			throw new ImplementationError("Should first generate a table");
		}
		
		return table;
	}
	
	protected TableInfo getOrConstructParseTable(String key, Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		if (sdfImports.isEmpty()) {
			return new TableInfo(Configuration.getDefaultParsetableProperty());
		}

		TableInfo table = getTable(key, sdfImports, sdfSearchPath);

		if (table == null) {
			return constructUserDefinedSyntaxTable(key, sdfImports, sdfSearchPath);
		}

		return table;
	}

	private IConstructor parseFromStream(String table, String fileName, InputStream source, boolean filter) throws FactParseError, IOException {
		byte[] result = sglrInvoker.parseFromStream(source, table, filter ? DEFAULT_SGLR_OPTIONS : SGLR_OPTIONS_NO_INDIRECT_PREFERENCE);

		return bytesToParseTree(fileName, result);
	}

	private IConstructor bytesToParseTree(String fileName, byte[] result)
			throws IOException {
		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(valueFactory,  Factory.getStore(),Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation(fileName);
	}
	
	protected IConstructor parseFromFile(String table, String fileName, boolean filter) throws FactParseError, IOException {
		byte[] result = sglrInvoker.parseFromFile(new File(fileName), table, filter ? DEFAULT_SGLR_OPTIONS : SGLR_OPTIONS_NO_INDIRECT_PREFERENCE);
		return bytesToParseTree(fileName, result);
	}

	protected IConstructor parseFromString(String table, String fileName, String source, boolean filter) throws FactParseError, IOException {
		byte[] result = sglrInvoker.parseFromString(source, table, filter ? DEFAULT_SGLR_OPTIONS : SGLR_OPTIONS_NO_INDIRECT_PREFERENCE);

		return bytesToParseTree(fileName, result);
	}

	protected TableInfo constructUserDefinedSyntaxTable(String key, Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		TableInfo info = getTableLocation(key, sdfImports, sdfSearchPath);

		//System.err.println(Configuration.getRascal2TableCommandProperty() + " -s " + getImportParameter(sdfImports) + " -p " 
		//		+ getSdfSearchPath(sdfSearchPath) + " -o " + tablefileName);
		Process p = Runtime.getRuntime().exec(new String[] {
				Configuration.getRascal2TableCommandProperty(),
				"-s", getImportParameter(sdfImports),
				"-p", getSdfSearchPath(sdfSearchPath),
				"-o", info.getTableName(),
				"-S", info.getSymbolsName(),
		}, new String[0], new File(Configuration.getRascal2TableBinDirProperty()));
		
		try{
			p.waitFor();
			if (p.exitValue() != 0) {
				throw new ImplementationError("Non-zero exit-status of rascal2table command.");
			}
		}catch(InterruptedException irex){
			throw new ImplementationError("Interrupted while waiting for the generation of the parse table.");
		}finally{
			p.destroy();
		}

		return info;
	}

	private String joinAsPath(Collection<?> list) {
		StringBuilder tmp = new StringBuilder();
		boolean first = true;
		for (Object object: list) {
			if (!first) {
				tmp.append(':');
			}
			tmp.append(object);
			first = false;
		}
		return tmp.toString();
	}

	protected String getSdfSearchPath(List<String> sdfSearchPath) {
		return joinAsPath(sdfSearchPath);
	}

	protected String getImportParameter(Set<String> sdfImports) {
		return joinAsPath(sdfImports);
	}

	protected TableInfo getTable(String key, Set<String> imports, List<String> sdfSearchPath) throws IOException {
		TableInfo info = getTableLocation(key, imports, sdfSearchPath);

		if (!new File(info.getTableName()).canRead()) {
			return null;
		}

		return info;
	}

	protected TableInfo getTableLocation(String key, Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		List<String> sorted = new ArrayList<String>(sdfImports);
		Collections.sort(sorted);
		InputStream in = null;

		Process p = Runtime.getRuntime().exec(new String[] {  
				Configuration.getRascal2TableCommandProperty(),
				"-c",
				"-s", joinAsPath(sorted),
				"-p", joinAsPath(sdfSearchPath)
		}, new String[0], new File(Configuration.getRascal2TableBinDirProperty()));
		
		try{
			p.waitFor();
			
			if (p.exitValue() != 0) {
				throw new ImplementationError("Could not collect syntax for some reason");
			}

			in = p.getInputStream();

			byte[] result = new byte[32];
			in.read(result);

			String tableFile = new File(Configuration.getTableCacheDirectoryProperty(), parseTableFileName(key, result)).getAbsolutePath();
			String symbolsFile = new File(Configuration.getTableCacheDirectoryProperty(), symbolsFileName(result)).getAbsolutePath();
			return new TableInfo(tableFile, symbolsFile);
		}catch(InterruptedException e){
			throw new IOException("could not compute table location: " + e.getMessage());
		}finally{
			if(p != null){
				p.destroy();
			}
		}
	}

	private String symbolsFileName(byte[] result) {
		return new String(result) + SYMBOLS_EXTENSION;
	}

	private String parseTableFileName(String key, byte[] result){
		return new String(result) + "-" + key + TBL_EXTENSION;
	}

}