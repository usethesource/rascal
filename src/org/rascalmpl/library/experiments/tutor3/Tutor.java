package org.rascalmpl.library.experiments.tutor3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help.HelpManager;

public class Tutor {
	
	public static void main(String[] args) throws IOException, NoSuchRascalFunction, URISyntaxException, InterruptedException {
	  HelpManager hm = new HelpManager(new PrintWriter(System.out), new PrintWriter(System.err));
	  Thread.sleep(500);
	  hm.openInBrowser(new URI("http://localhost:" + hm.getPort() + "/TutorHome/index.html"));
	  Thread.sleep(864000000);  // a hack a day keeps the doctor away (and the debugger close)
	}
}
