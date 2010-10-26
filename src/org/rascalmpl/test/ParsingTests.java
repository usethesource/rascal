package org.rascalmpl.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.rascalmpl.ast.ASTFactoryFactory;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Module.Default;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.Parser;

public class ParsingTests extends TestCase {

	public void doParse(String dir) {
		Parser parser = new Parser();
		
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
				
				IConstructor tree = parser.parseModule(file.getAbsoluteFile().toURI(), fis,new ModuleEnvironment("***dummy***"));
				Module.Default module = (Default) new ASTBuilder(ASTFactoryFactory.getASTFactory()).buildModule(tree);
				System.err.println("SUCCEEDED: " + module.getHeader());
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
