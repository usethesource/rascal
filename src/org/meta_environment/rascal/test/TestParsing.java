package org.meta_environment.rascal.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Module.Default;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.Parser;
import org.meta_environment.uptr.Factory;

public class TestParsing extends TestCase {
	public void testParser() {
		Parser parser = Parser.getInstance();
		File directory = new File("../rascal-grammar/spec/tests/terms");

		File[] tests = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".trm");
			}
		});

		ASTBuilder b = new ASTBuilder(new ASTFactory());

		boolean failed = false;
		
		for (File file : tests) {
			try {
				FileInputStream s = new FileInputStream(file);
				INode tree = parser.parse(s);

				if (tree.getTreeNodeType() == Factory.ParseTree_Top) {
					Module.Default module = (Default) b.buildModule(tree);
					System.err.println("SUCCEEDED: " + module.getHeader());
				} else {
					System.err.println("FAILED: " + file + "\n\t" + tree);
					failed = true;

				}
			} catch (FactTypeError e) {
				System.err.println("FAILED: " + file);
				e.printStackTrace();
				failed = true;
			} catch (IOException e) {
				System.err.println("FAILED: " + file);
				e.printStackTrace();
				failed = true;

			}
		}
		if (failed) fail();
	}
}
