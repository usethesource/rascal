/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.box;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class MakeAsfix {

	static private Display screen = Display.getCurrent() == null ? new Display()
			: Display.getCurrent();
	static private Shell shell = new Shell(screen);
	
	private static String module;
	
	private static String lang;
	
	private static String inputDir;
	
	static final private String subDir = "asfix";

	static private void getModuleName() {
		DirectoryDialog directoryDialog = new DirectoryDialog(shell);
		// String[] filterExtensions = new String[] { "*.rsc" };
		// dialog.setFilterExtensions(filterExtensions);
		final String defaultDir = System.getProperty("SDFDIR");
		System.err.println(defaultDir);
		if (defaultDir != null)
			directoryDialog.setFilterPath(defaultDir+File.separatorChar+"languages");
		String fileName = directoryDialog.open();
		if (fileName == null) {
			System.err.println("Canceled");
			System.exit(0);
		}
		lang = new File(fileName).getName();
		String moduleName = lang.substring(0, 1).toUpperCase()+lang.substring(1);
		module = "languages/"+lang+"/syntax/"+ moduleName;
		inputDir = System.getProperty("user.home")+File.separatorChar+subDir+File.separatorChar+lang;
		System.err.println(module);
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setFilterPath(inputDir);
		String[] filterExtensions = new String[] { "*.src" };
		fileDialog.setFilterExtensions(filterExtensions);
		fileName = fileDialog.open();
		if (fileName == null) {
			System.err.println("Canceled");
			System.exit(0);
		}
		System.err.println(fileName);
		
		
		Set<String> sdfImports = new HashSet<String>(1);
		sdfImports.add(module);
		ArrayList<String> sdfSearchPath = new ArrayList<String>(1);
		sdfSearchPath.add(defaultDir);
		FileInputStream f;
		try {
			f = new FileInputStream(new File(fileName));
			// TODO Use the right parse method.
			/*IConstructor t = new Parser().parseStream(f, new VoidActionExecutor());
			String outputName = fileName.substring(0, fileName.lastIndexOf('.'))+".asf";
			System.err.println("Result:"+outputName);
			File output = new File(outputName);
			PBFWriter.writeValueToFile(t, output, Factory.getStore());*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		getModuleName();
		
	}

}
