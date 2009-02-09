package org.meta_environment.rascal.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ParsetreeAdapter;
import org.meta_environment.uptr.TreeAdapter;

import sglr.SGLRInvoker;
import sun.security.action.GetPropertyAction;

/**
 * Parses a Rascal program and a UPTR node.
 */
public class Parser{
	public final static String PARSETABLE_PROPERTY = "rascal.parsetable.file";
	public final static String PARSETABLE_FILENAME = "resources/rascal.trm.tbl";
	
	private Parser(){
		super();
	}
	
	private static class InstanceKeeper{
		public final static Parser sInstance = new Parser();
	}
	
	public static Parser getInstance(){
		return InstanceKeeper.sInstance;
	}

	public IConstructor parseFromString(String inputString) throws IOException, FactTypeError{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromString(inputString, getTableFile());
		
		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(ValueFactory.getInstance(), Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation("-");
	}

	public IConstructor parseFromStream(InputStream inputStringStream) throws IOException, FactTypeError{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromStream(inputStringStream, getTableFile());
		
		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(ValueFactory.getInstance(), Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation("-");
	}
	
	public IConstructor parseFromFile(File inputFile) throws IOException, FactTypeError{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromFile(inputFile, getTableFile());

		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(ValueFactory.getInstance(), Factory.ParseTree, bais);
		return new TreeAdapter(tree).addPositionInformation(inputFile.getAbsolutePath());
	}
	
	private String getTableFile() throws IOException{
		String parseTableFile = System.getProperty(PARSETABLE_PROPERTY, PARSETABLE_FILENAME);
		File table = new File(parseTableFile);
		
		if(!table.exists()) {
			String loc = extractParsetable();
			table = new File(loc);
			
			if (!table.exists()) {
				throw new IOException("Could not locate parse table ("+PARSETABLE_FILENAME+").");
			}
		}
		
		return table.getPath();
	}
	
	private String extractParsetable() throws IOException {
		URL url = Parser.class.getResource("/" + PARSETABLE_FILENAME);
		InputStream contents = url.openStream();
		
		GetPropertyAction a = new GetPropertyAction("java.io.tmpdir");
		String tmpdir = ((String) AccessController.doPrivileged(a));
		File tmp = new File(tmpdir +  "/rascal.trm.tbl");
		
		if (!tmp.exists()) {
			tmp.createNewFile();

			FileOutputStream s = new FileOutputStream(tmp);
			byte[] buf = new byte[1024];
			int count = 0;

			while ((count = contents.read(buf)) >= 0) {
				s.write(buf, 0, count);
			}
			
			s.flush();
			s.close();
			contents.close();
		}
		
		return tmp.getPath();
	}

}
