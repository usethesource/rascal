package org.rascalmpl.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.io.PBFReader;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.errors.SubjectAdapter;
import org.rascalmpl.values.errors.SummaryAdapter;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ParsetreeAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

import sglr.IInvoker;
import sglr.LegacySGLRInvoker;
import sglr.SGLRInvoker;

public class LegacyRascalParser implements IRascalParser {
	private static final String TBL_EXTENSION = ".tbl";
	private static final String SYMBOLS_EXTENSION = ".symbols";
	
	protected static final String META_LANGUAGE_KEY = "meta";
	protected static final String OBJECT_LANGUAGE_KEY = "obj";
	private final static IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
	private final SdfImportExtractor importExtractor = new SdfImportExtractor();
	
	protected class TableInfo {
		private final String tableName;
		private final String symbolsName;
		
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
	private final static IBytesToTree bytesToTree;
	protected static final int SGLR_OPTIONS_NO_INDIRECT_PREFERENCE = IInvoker.FILTERS_INDIRECT_PREFERENCE;
	protected static final int DEFAULT_SGLR_OPTIONS = IInvoker.FILTERS_DEFAULT;
	
	static{
		String osName = System.getProperty("os.name");
		
		if(osName.indexOf("Linux") != -1){ // Linux.
			sglrInvoker = SGLRInvoker.getInstance();
			bytesToTree = new PBFBytesToTree();
		}else if(osName.indexOf("Mac") != -1 || osName.indexOf("Darwin") != -1){ // Mac.
			sglrInvoker = SGLRInvoker.getInstance();
			bytesToTree = new PBFBytesToTree();
		}else{
			sglrInvoker = new LegacySGLRInvoker();
			bytesToTree = new ATermBytesToTree();
		}
	}
	
	public Set<String> getSdfImports(List<String> sdfSearchPath, URI location, byte[] data) throws IOException {
		try{
			IConstructor tree= parseFromData(Configuration.getHeaderParsetableProperty(), location, data, false);

			if (tree.getConstructorType() == Factory.ParseTree_Summary) {
				throw new SyntaxError(location.toString(), new SummaryAdapter(tree).getInitialSubject().getLocation());
			}
			return importExtractor.extractImports(tree, sdfSearchPath);
		}catch(FactParseError p){
			throw new ImplementationError("unexpected error: " + p.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.parser.IRascalParser#parseCommand(java.util.Set, java.util.List, java.net.URI, java.lang.String)
	 */
	public IConstructor parseCommand(Set<String> sdfImports, List<String> sdfSearchPath, URI location, String command) throws IOException {
		TableInfo table = lookupTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		
		return parseFromString(table.getTableName(), location, command, false);
	}
	
	public void generateModuleParser(List<String> sdfSearchPath, Set<String> sdfImports, Environment env) throws IOException {
		TableInfo info = getOrConstructParseTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		declareConcreteSyntaxTypes(info.getSymbolsName(), env);
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.parser.IRascalParser#parseModule(java.util.List, java.util.Set, java.net.URI, java.io.InputStream, org.rascalmpl.interpreter.env.ModuleEnvironment)
	 */
	public IConstructor parseModule(List<String> sdfSearchPath, Set<String> sdfImports, URI location, InputStream source, ModuleEnvironment env) throws IOException {
		TableInfo table = getOrConstructParseTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		declareConcreteSyntaxTypes(table.getSymbolsName(), env);
		
		try {
			return parseFromStream(table.getTableName(), location, source, false);
		} catch (FactParseError e) {
			throw new ImplementationError("parse tree format error", e);
		} 
	}
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.parser.IRascalParser#parseModule(java.util.List, java.util.Set, java.net.URI, byte[], org.rascalmpl.interpreter.env.ModuleEnvironment)
	 */
	public IConstructor parseModule(List<String> sdfSearchPath, Set<String> sdfImports, URI location, byte[] data, ModuleEnvironment env) throws IOException {
		TableInfo table = getOrConstructParseTable(META_LANGUAGE_KEY, sdfImports, sdfSearchPath);
		declareConcreteSyntaxTypes(table.getSymbolsName(), env);
		
		try {
			return parseFromData(table.getTableName(), location, data, false);
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
		
		// TODO: should actually also declare all complex symbols, since ones that are used but not present
		// in any SDF module won't work anyway. 
		
		for (IValue symbol : symbols) {
			IConstructor sym = (IConstructor) symbol;
			
			if (SymbolAdapter.isCf(sym) || SymbolAdapter.isLex(sym)) {
				sym = SymbolAdapter.getSymbol(sym);
			}
			
			if (SymbolAdapter.isSort(sym)) {
				String name = SymbolAdapter.getName(sym);
				
				if (!name.startsWith("_")) {
					env.concreteSyntaxType(SymbolAdapter.getName(sym), (IConstructor) symbol);
				}
			}
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

	private IConstructor bytesToParseTree(byte[] result, URI location) throws IOException{
		IConstructor tree = bytesToTree.bytesToTree(result);
		return ParsetreeAdapter.addPositionInformation(tree, location);
	}

	protected IConstructor parseFromStream(String table, URI location, InputStream source, boolean filter) throws FactParseError, IOException {
		byte[] result = sglrInvoker.parseFromStream(source, table, filter ? DEFAULT_SGLR_OPTIONS : SGLR_OPTIONS_NO_INDIRECT_PREFERENCE);

		return bytesToParseTree(result, location);
	}

	protected IConstructor parseFromString(String table, URI location, String source, boolean filter) throws FactParseError, IOException {
		byte[] result = sglrInvoker.parseFromString(source, table, filter ? DEFAULT_SGLR_OPTIONS : SGLR_OPTIONS_NO_INDIRECT_PREFERENCE);

		return bytesToParseTree(result, location);
	}

	protected IConstructor parseFromData(String table, URI location, byte[] data, boolean filter) throws FactParseError, IOException {
		byte[] result = sglrInvoker.parseFromData(data, table, filter ? DEFAULT_SGLR_OPTIONS : SGLR_OPTIONS_NO_INDIRECT_PREFERENCE);

		return bytesToParseTree(result, location);
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
		}
		catch (InterruptedException e){
			throw new IOException("could not compute table location: " + e.getMessage());
		}
		finally {
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
	
	/* (non-Javadoc)
	 * @see org.rascalmpl.parser.IRascalParser#parseString(java.util.List, java.util.Set, java.lang.String)
	 */
	public IConstructor parseString(List<String> sdfSearchPath, Set<String> sdfImports, String source)
			throws IOException {
				TableInfo table = getOrConstructParseTable(sdfImports, sdfSearchPath);
				IConstructor result = parseFromString(table.getTableName(), URI.create("stdin:///"), source, true);
				if (result.getConstructorType() == Factory.ParseTree_Summary) {
					//System.err.println("RESULT = " + result);
					SubjectAdapter x = new SummaryAdapter(result).getInitialSubject();
					ISourceLocation loc = x.getLocation();
					if (loc != null) {
						throw new SyntaxError("-", new SummaryAdapter(result).getInitialSubject().getLocation());
					}
					throw new SyntaxError("-", null);
				}
				return result;
			}

	/* (non-Javadoc)
	 * @see org.rascalmpl.parser.IRascalParser#parseStream(java.util.List, java.util.Set, java.io.InputStream)
	 */
	public IConstructor parseStream(List<String> sdfSearchPath, Set<String> sdfImports, InputStream source)
			throws IOException {
				TableInfo table = getOrConstructParseTable(sdfImports, sdfSearchPath);
				IConstructor result = parseFromStream(table.getTableName(), URI.create("stdin:///"), source, true);
				if (result.getConstructorType() == Factory.ParseTree_Summary) {
					//System.err.println("RESULT = " + result);
					SubjectAdapter x = new SummaryAdapter(result).getInitialSubject();
					ISourceLocation loc = x.getLocation();
					if (loc != null) {
						throw new SyntaxError("-", new SummaryAdapter(result).getInitialSubject().getLocation());
					}
					throw new SyntaxError("-", null);
				}
				return result;
			}

	private TableInfo getOrConstructParseTable(Set<String> sdfImports, List<String> sdfSearchPath)
			throws IOException {
				if (sdfImports.isEmpty()) {
					return new TableInfo(Configuration.getDefaultParsetableProperty());
				}
			
				TableInfo table = getTable(OBJECT_LANGUAGE_KEY, sdfImports, sdfSearchPath);
			
				if (table == null) {
					return constructUserDefinedSyntaxTable(sdfImports, sdfSearchPath);
				}
			
				return table; 
			}

	private TableInfo constructUserDefinedSyntaxTable(Set<String> sdfImports, List<String> sdfSearchPath)
			throws IOException {
				TableInfo tablefileName = getTableLocation(OBJECT_LANGUAGE_KEY, sdfImports, sdfSearchPath);
				
				Process p = Runtime.getRuntime().exec(new String[] {
						Configuration.getRascal2TableCommandProperty(),
						"-u",
						"-s", getImportParameter(sdfImports),
						"-p", getSdfSearchPath(sdfSearchPath),
						"-o", tablefileName.getTableName()
					}, new String[0], new File(Configuration.getRascal2TableBinDirProperty())
				);
				
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
				return tablefileName;
			}

	private interface IBytesToTree{
		public IConstructor bytesToTree(byte[] data) throws IOException;
	}
	
	private static class PBFBytesToTree implements IBytesToTree{
		public IConstructor bytesToTree(byte[] result) throws IOException{
			PBFReader reader = new PBFReader();
			ByteArrayInputStream bais = new ByteArrayInputStream(result);
			return (IConstructor) reader.read(valueFactory, Factory.getStore(), Factory.ParseTree, bais);
		}
	}
	
	private static class ATermBytesToTree implements IBytesToTree{
		public IConstructor bytesToTree(byte[] result) throws IOException{
			ATermReader reader = new ATermReader();
			ByteArrayInputStream bais = new ByteArrayInputStream(result);
			return (IConstructor) reader.read(valueFactory, Factory.getStore(), Factory.ParseTree, bais);
		}
	}


}