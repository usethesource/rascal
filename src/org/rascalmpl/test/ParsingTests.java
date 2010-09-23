package org.rascalmpl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Module.Default;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.IRascalParser;
import org.rascalmpl.parser.LegacyRascalParser;
import org.rascalmpl.values.uptr.Factory;

public class ParsingTests extends TestCase {

	public void doParse(String dir) {
		IRascalParser parser = new LegacyRascalParser();
		
		File directory = new File("demo/" + dir);

		File[] tests = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".rsc");
			}
		});

		boolean failed = false;
		
		for (File file : tests) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				
				IConstructor tree = parser.parseModule(Collections.<String>emptyList(), Collections.<String>emptySet(),file.getAbsoluteFile().toURI(), fis, new ModuleEnvironment("***dummy***"));
				
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
			}finally{
				if(fis != null){
					try{
						fis.close();
					}catch(IOException ioex){
						// Don't care.
					}
				}
			}
		}
		if (failed) fail();
	}
	
}
