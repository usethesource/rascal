package org.meta_environment.rascal.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.io.ATermReader;
import org.meta_environment.uptr.Factory;

/**
 * Parses a Rascal program and a UPTR tree.
 *
 */
public class Parser {
	
	private static class InstanceKeeper {
		public static Parser sInstance = new Parser();
	}
	
	private Parser() { }
	
	public static Parser getInstance() {
		return InstanceKeeper.sInstance;
	}
	
	public ITree parse(InputStream input) throws IOException, FactTypeError {
		ATermReader reader = new ATermReader();
		Process sglr = Runtime.getRuntime().exec("sglr -p resources/rascal.trm.tbl -t");
		
		pipe("sglr", input, sglr.getOutputStream());
		IValue tmp = reader.read(ValueFactory.getInstance(), Factory.ParseTree, sglr.getInputStream());
		waitForSglr(sglr);
		
		return (ITree) tmp;
	}

	private void waitForSglr(Process sglr) throws IOException {
		while (true) {
		  try {
			  sglr.waitFor();
			  break;
		  } catch (InterruptedException e) {
			  // it happens
		  }
		}
	}
	
	static private void pipe(String label, final InputStream in,
			final OutputStream out) throws IOException {

		try {
			byte[] buffer = new byte[8192];
			int count;
			while ((count = in.read(buffer)) >= 0) {
				out.write(buffer, 0, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}
	
	
}
