package org.meta_environment.rascal.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactParseError;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.errors.SummaryAdapter;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ParsetreeAdapter;

import sglr.SGLRInvoker;

public class ModuleParser {
	private final IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
	private final SdfImportExtractor importExtractor = new SdfImportExtractor();

	public Set<String> getSdfImports(List<String> sdfSearchPath, String fileName, InputStream source) throws IOException {
		try {
			IConstructor tree= parseFromStream(Configuration.getHeaderParsetableProperty(), fileName, source);

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
		String table = getOrConstructParseTable(sdfImports, sdfSearchPath);
		return parseFromString(table, fileName, command);
	}


	public IConstructor parseModule(List<String> sdfSearchPath, Set<String> sdfImports, String fileName, InputStream source) throws IOException {
		String table = getOrConstructParseTable(sdfImports, sdfSearchPath);
		try {
			return parseFromStream(table, fileName, source);
		} catch (FactParseError e) {
			throw new ImplementationError("parse tree format error", e);
		} 
	}

	protected String getOrConstructParseTable(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		if (sdfImports.isEmpty()) {
			return Configuration.getDefaultParsetableProperty();
		}

		String table = getTable(sdfImports, sdfSearchPath);

		if (table == null) {
			return constructUserDefinedSyntaxTable(sdfImports, sdfSearchPath);
		}

		return table;
	}

	private IConstructor parseFromStream(String table, String fileName, InputStream source) throws FactParseError, IOException {
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromStream(source, table);

		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(valueFactory,  Factory.getStore(),Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation(fileName);
	}

	protected IConstructor parseFromString(String table, String fileName, String source) throws FactParseError, IOException {
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromString(source, table);

		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(valueFactory,  Factory.getStore(),Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation(fileName);
	}

	private String constructUserDefinedSyntaxTable(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		String tablefileName = getTableLocation(sdfImports, sdfSearchPath);

		Runtime.getRuntime().exec(new String[] {
				Configuration.getRascal2TableCommandProperty(),
				"-s", getImportParameter(sdfImports),
				"-p", getSdfSearchPath(sdfSearchPath),
				"-o", tablefileName
		}, new String[0], new File(Configuration.getRascal2TableBinDirProperty())
		);

		return tablefileName;
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

	private String getSdfSearchPath(List<String> sdfSearchPath) {
		return joinAsPath(sdfSearchPath);
	}

	private String getImportParameter(Set<String> sdfImports) {
		return joinAsPath(sdfImports);
	}

	private String getTable(Set<String> imports, List<String> sdfSearchPath) throws IOException {
		String filename = getTableLocation(imports, sdfSearchPath);

		if (!new File(filename).canRead()) {
			return null;
		}

		return filename;
	}

	private String getTableLocation(Set<String> sdfImports, List<String> sdfSearchPath) throws IOException {
		List<String> sorted = new ArrayList<String>(sdfImports);
		Collections.sort(sorted);
		InputStream in = null;

		try {
			Process p = Runtime.getRuntime().exec(new String[] {  
					Configuration.getRascal2TableCommandProperty(),
					"-c",
					"-s", joinAsPath(sorted),
					"-p", joinAsPath(sdfSearchPath)
			}, new String[0], new File(Configuration.getRascal2TableBinDirProperty()));
			p.waitFor();

			if (p.exitValue() != 0) {
				throw new ImplementationError("Could not collect syntax for some reason");
			}

			in = p.getInputStream();

			byte[] result = new byte[32];
			in.read(result);

			return new File(Configuration.getTableCacheDirectoryProperty(), new String(result) + ".tbl").getAbsolutePath();
		} catch (InterruptedException e) {
			throw new IOException("could not compute table location: " + e.getMessage());
		} 
		finally {
			if (in != null) {
				in.close();
			}
		}
	}
}
