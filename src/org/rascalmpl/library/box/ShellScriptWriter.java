package org.rascalmpl.library.box;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class ShellScriptWriter {

	static final String binDir = System.getProperty("user.home") + "/bin";

	static final File rascalFile = new File(binDir, "rascal");
	
	static final File rascalSh = new File(".", "rascal.sh");
	
	static final File box2latexSh = new File(".", "box2latex.sh");
	
	static final File box2htmlSh = new File(".", "box2html.sh");
	
	static final File box2textSh = new File(".", "box2text.sh");
	
    static final File box2latexFile = new File(binDir, "box2latex");
    
    static final File box2htmlFile = new File(binDir, "box2html");
	
	static final File box2textFile = new File(binDir, "box2text");

	public static void copyFile(File in, PrintStream fos) throws IOException {
		FileInputStream fis = new FileInputStream(in);
		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
	}

	public static void main(String[] args) {
		try {
			PrintStream rascalStream = new PrintStream(rascalFile), 
			box2textStream = new PrintStream(box2textFile),
			box2htmlStream = new PrintStream(box2htmlFile),
			box2latexStream = new PrintStream(box2latexFile);
			String javaHome = System.getProperty("java.home");
			// System.err.println("javaHome:" + javaHome);
			String workspace_loc = System.getProperty("workspace_loc");
			// System.err.println("workspace_loc:" + workspace_loc);
			String rascal_project_loc = System
					.getProperty("rascal_project_loc");
			// System.err.println("rascal_project_loc:" + rascal_project_loc);
			File rascalProject = new File(rascal_project_loc);
			File installedHome = rascalProject.getParentFile();
			// System.err.println("installedHome:" + installedHome);
			rascalStream.println("INSTALLEDHOME="+installedHome);
			rascalStream.println("WORK="+workspace_loc);
			rascalStream.println("JAVAHOME="+javaHome);
		    copyFile(rascalSh, rascalStream);
		    System.out.println("copy to "+rascalFile.getCanonicalPath()+ " finished");
		    box2textStream.println("#!/bin/bash");
		    box2textStream.println("BINDIR="+binDir);
		    copyFile(box2textSh, box2textStream);
		    System.out.println("copy to "+box2textFile.getCanonicalPath()+ " finished");
		    box2latexStream.println("#!/bin/bash");
		    box2latexStream.println("BINDIR="+binDir);
		    copyFile(box2latexSh, box2latexStream);
		    System.out.println("copy to "+box2latexFile.getCanonicalPath()+ " finished");
		    box2htmlStream.println("#!/bin/bash");
		    box2htmlStream.println("BINDIR="+binDir);
		    copyFile(box2htmlSh, box2htmlStream);
		    System.out.println("copy to "+box2htmlFile.getCanonicalPath()+ " finished");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
