package org.meta_environment.rascal.parser;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Import;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Import.Default;
import org.meta_environment.rascal.interpreter.Configuration;

public class SdfImportExtractor {
	private final ASTBuilder builder = new ASTBuilder(new ASTFactory());
	
	Set<String> extractImports(IConstructor parseTree, List<String> searchPath) {
		Module module = builder.buildModule(parseTree);
		return extractImports(module, searchPath);
	}
	
	Set<String> extractImports(AbstractAST ast, List<String> searchPath) {
		Set<String> allImports = new HashSet<String>();
		ast.accept(new Extractor(allImports));
		return filterSDFImports(allImports, searchPath);
	}
	
	
	public static Set<String> filterSDFImports(Set<String> allImports, List<String> searchPath) {
		Set<String> result = new HashSet<String>();
		
		for (String i : allImports) {
			for (String path : searchPath) {
				File sdf = new File(new File(path), i.replaceAll("::",""+ File.separatorChar) + Configuration.getSDFExtensionProperty());
				if (sdf.exists()) {
					result.add(i);
				}
			}
		}
		
		return result;
	}

	private class Extractor extends NullASTVisitor<AbstractAST> {
		private Set<String> imports;

		public Extractor(Set<String> imports) {
			this.imports = imports;
		}
		
		@Override
		public AbstractAST visitModuleDefault(
				org.meta_environment.rascal.ast.Module.Default x) {
			x.getHeader().accept(this);
			return x;
		}
		
		@Override
		public AbstractAST visitHeaderDefault(
				org.meta_environment.rascal.ast.Header.Default x) {
			for (Import i : x.getImports()) {
				i.accept(this);
			}
			return x;
		}

		@Override
		public AbstractAST visitImportDefault(Default x) {
			imports.add(x.getModule().toString());
			return x;
		}
	}
}
