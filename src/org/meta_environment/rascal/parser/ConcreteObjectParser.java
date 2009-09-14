package org.meta_environment.rascal.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.meta_environment.errors.SubjectAdapter;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.uptr.Factory;

public class ConcreteObjectParser extends ModuleParser {
	
	public IConstructor parseString(List<String> sdfSearchPath, Set<String> sdfImports, String source) throws IOException {
		TableInfo table = getOrConstructParseTable(sdfImports, sdfSearchPath);
		IConstructor result = parseFromString(table.getTableName(), "-", source, true);
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

	public IConstructor parseStream(List<String> sdfSearchPath, Set<String> sdfImports, InputStream source) throws IOException {
		TableInfo table = getOrConstructParseTable(sdfImports, sdfSearchPath);
		IConstructor result = parseFromStream(table.getTableName(), "-", source, true);
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
	
	private TableInfo getOrConstructParseTable(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		if (sdfImports.isEmpty()) {
			return new TableInfo(Configuration.getDefaultParsetableProperty());
		}

		TableInfo table = getTable(OBJECT_LANGUAGE_KEY, sdfImports, sdfSearchPath);

		if (table == null) {
			return constructUserDefinedSyntaxTable(sdfImports, sdfSearchPath);
		}

		return table; 
	}
	
	private TableInfo constructUserDefinedSyntaxTable(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
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
}
