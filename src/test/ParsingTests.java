package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Module.Default;
import org.meta_environment.rascal.parser.ASTBuilder;
import org.meta_environment.rascal.parser.ModuleParser;
import org.meta_environment.uptr.Factory;

public class ParsingTests extends TestCase {

	public void doParse(String dir) {
		ModuleParser parser = new ModuleParser();
		
		File directory = new File("demo/" + dir);

		File[] tests = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".rsc");
			}
		});

		boolean failed = false;
		
		for (File file : tests) {
			try {
				IConstructor tree = parser.parseModule(Collections.<String>emptyList(), Collections.<String>emptySet(), file.getAbsolutePath(), new FileInputStream(file));
				
				if (tree.getConstructorType() == Factory.ParseTree_Top) {
					Module.Default module = (Default) new ASTBuilder(new ASTFactory()).buildModule(tree);
					System.err.println("SUCCEEDED: " + module.getHeader());
				} else {
					System.err.println("FAILED: " + file + "\n\t" + tree);
					failed = true;

				}
			} catch (FactTypeUseException e) {
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
