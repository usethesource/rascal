package org.meta_environment.rascal.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.uptr.Factory;

public class StringParser extends ModuleParser {

	private static final String OBJECT_LANGUAGE_KEY = "obj";

	public IConstructor parseString(List<String> sdfSearchPath, Set<String> sdfImports, String source) throws IOException {
		String table = getOrConstructParseTable(sdfImports, sdfSearchPath);
		IConstructor result = parseFromString(table, "-", source);
		if (result.getConstructorType() == Factory.ParseTree_Summary) {
			System.err.println("RESULT = " + result);
			throw new SyntaxError("-", new SummaryAdapter(result).getInitialSubject().getLocation());
		}
		return result;
	}
	
	private String getOrConstructParseTable(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		if (sdfImports.isEmpty()) {
			return Configuration.getDefaultParsetableProperty();
		}

		String table = getTable(OBJECT_LANGUAGE_KEY, sdfImports, sdfSearchPath);

		if (table == null) {
			return constructUserDefinedSyntaxTable(sdfImports, sdfSearchPath);
		}

		return table;
	}
	
	private String constructUserDefinedSyntaxTable(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		String tablefileName = getTableLocation(OBJECT_LANGUAGE_KEY, sdfImports, sdfSearchPath);

		Runtime.getRuntime().exec(new String[] {
				Configuration.getRascal2TableCommandProperty(),
				"-u",
				"-s", getImportParameter(sdfImports),
				"-p", getSdfSearchPath(sdfSearchPath),
				"-o", tablefileName
		}, new String[0], new File(Configuration.getRascal2TableBinDirProperty())
		);

		return tablefileName;
	}

	
}
