package org.meta_environment.rascal.interpreter.staticErrors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.uptr.visitors.AsfixWriter;

public class AmbiguousConcretePattern extends StaticError {
	private static final long serialVersionUID = -1319922379120654138L;
//	private static int suffix = 0;

	public AmbiguousConcretePattern(AbstractAST ast) {
		super("Concrete pattern is ambiguous, please rephrase (add more typed variables for example).", ast);
//		try {
//			String filename = "/tmp/amb" + suffix++ + ".pt";
//			Writer writer = new FileWriter(new File(filename));
//			ast.getTree().accept(new AsfixWriter(writer));
//			writer.close();
//			System.err.println("Ambiguous tree dumped to: " + filename);
//		} catch (VisitorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
