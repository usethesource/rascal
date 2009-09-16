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
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Import.Default;
import org.meta_environment.rascal.interpreter.Configuration;
import org.meta_environment.rascal.interpreter.utils.Names;

public class SdfImportExtractor {
	
	Set<String> extractImports(IConstructor parseTree, List<String> searchPath) {
		Module module = new ASTBuilder(new ASTFactory()).buildModule(parseTree);
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
			QualifiedName name = x.getModule().getName();
			StringBuilder builder = new StringBuilder();
			
			int i = 0;
			for (Name part : name.getNames()) {
				if (i++ != 0) {
					builder.append("::");
				}
				builder.append(Names.name(part));
			}
			imports.add(builder.toString());
			return x;
		}
	}
}
