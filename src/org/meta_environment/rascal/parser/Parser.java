package org.meta_environment.rascal.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.imp.pdb.facts.INode;
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
	private static String parseTable;
	
	private static class InstanceKeeper {
		public static Parser sInstance = new Parser();
	}
	
	private Parser() { }
	
	public static Parser getInstance() {
		return InstanceKeeper.sInstance;
	}
	
	public INode parse(InputStream input) throws IOException, FactTypeError {
		ATermReader reader = new ATermReader();
		String tableFilename = getTableFile();
		Process sglr = Runtime.getRuntime().exec("sglr -p " + tableFilename + " -t");
		
		
		pipe("sglr", input, sglr.getOutputStream());
		IValue tmp = reader.read(ValueFactory.getInstance(), Factory.ParseTree, sglr.getInputStream());
		waitForSglr(sglr);
		
		return (INode) tmp;
	}

	private String getTableFile() throws IOException {
		File table = new File("resources/rascal.trm.tbl");
		if (table.exists()) {
			return table.getPath();
		}
		else {
		   if (parseTable == null) {
			   parseTable = extractParsetable();
		   }
		   return parseTable;
		}
	}

	private String extractParsetable() throws IOException {
		URL url = getClass().getResource("/resources/rascal.trm.tbl");
		InputStream contents = url.openStream();
		File tmp = File.createTempFile("rascal.trm.tbl", "");
		FileOutputStream s = new FileOutputStream(tmp);
		int ch;

		while ((ch = contents.read()) != -1) {
			s.write(ch);
		}
		s.close();
		contents.close();
		return tmp.getPath();
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
