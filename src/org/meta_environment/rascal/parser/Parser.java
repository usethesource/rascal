package org.meta_environment.rascal.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.rascal.io.ATermReader;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.TreeAdapter;

/**
 * Parses a Rascal program and returns an AST hierarchy.
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
	
	private ITree parse(InputStream input) throws IOException, FactTypeError {
		ATermReader reader = new ATermReader();
		Process sglr = Runtime.getRuntime().exec("sglr -p /ufs/jurgenv/glt/src/rascal/resources/rascal.trm.tbl -t");
		
		pipe("sglr", input, sglr.getOutputStream());
		IValue tmp = reader.read(ValueFactory.getInstance(), Factory.ParseTree, sglr.getInputStream());
		waitForSglr(sglr);
		
		return (ITree) tmp;
	}

	private void waitForSglr(Process sglr) throws IOException {
		while (true) {
		  try {
			  int exitCode = sglr.waitFor();
			  if (exitCode != 0) {
				  throw new IOException("SGLR failed with exit code " + exitCode);
			  }
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
	
	public static void main(String[] args) {
		Parser parser = Parser.getInstance();
		String test = "module Aap";
		
		try {
			ITree tree = parser.parse(new ByteArrayInputStream(test.getBytes()));
			System.err.println("tree is " + tree);
			System.err.println("unparsed: " + new TreeAdapter(tree).yield());
		} catch (FactTypeError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
