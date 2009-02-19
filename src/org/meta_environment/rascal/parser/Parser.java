package org.meta_environment.rascal.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.meta_environment.rascal.ValueFactoryFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ParsetreeAdapter;

import sglr.SGLRInvoker;

/**
 * Parses a Rascal program and a UPTR node.
 */
public class Parser{
	public final static String PARSETABLE_PROPERTY = "rascal.parsetable.file";
	public final static String PARSETABLE_FILENAME = "resources/rascal.trm.tbl";
	
	private final static IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
	
	private final String parseTableFileName;
	
	private Parser(){
		super();
		
		parseTableFileName = getParseTable();
	}
	
	private String getParseTable(){
		String parseTableFile = System.getProperty(PARSETABLE_PROPERTY, PARSETABLE_FILENAME);
		File table = new File(parseTableFile);
		
		if(!table.exists()) {
			String parseTableLocation;
			try{
				parseTableLocation = extractParsetable();
			}catch(IOException ioex){
				throw new RuntimeException(ioex);
			}
			table = new File(parseTableLocation);
			
			if (!table.exists()) {
				throw new RuntimeException("Could not locate parse table ("+PARSETABLE_FILENAME+").");
			}
		}
		
		return table.getPath();
	}
	
	private static class InstanceKeeper{
		public final static Parser sInstance = new Parser();
	}
	
	public static Parser getInstance(){
		return InstanceKeeper.sInstance;
	}

	public IConstructor parseFromString(String inputString) throws IOException, FactTypeUseException{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromString(inputString, getTableFile());
		
		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(valueFactory, Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation("-");
	}

	public IConstructor parseFromStream(InputStream inputStringStream) throws IOException, FactTypeUseException{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromStream(inputStringStream, getTableFile());
		
		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(valueFactory, Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation("-");
	}
	
	public IConstructor parseFromFile(File inputFile) throws IOException, FactTypeUseException{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromFile(inputFile, getTableFile());

		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		IConstructor tree = (IConstructor) reader.read(valueFactory, Factory.ParseTree, bais);
		return new ParsetreeAdapter(tree).addPositionInformation(inputFile.getAbsolutePath());
	}
	
	private String getTableFile(){
		return parseTableFileName;
	}
	
	private String extractParsetable() throws IOException{
		URL url = Parser.class.getResource("/" + PARSETABLE_FILENAME);
		InputStream contents = url.openStream();
		
		String tmpdir = System.getProperty("java.io.tmpdir");
		File tmp = new File(tmpdir +  "/rascal.trm.tbl");
		
		if(!tmp.exists()){
			tmp.createNewFile();

			FileOutputStream s = null;
			try{
				s = new FileOutputStream(tmp);
				byte[] buf = new byte[8192];
				
				int count;
				while((count = contents.read(buf)) > 0){
					s.write(buf, 0, count);
				}
			}catch(IOException ioex){
				throw ioex;
			}finally{
				if(s != null){
					try{
						s.close();
					}catch(IOException ioex){
						throw ioex;
					}
				}
				if(contents != null){
					try{
						contents.close();
					}catch(IOException ioex){
						throw ioex;
					}
				}
			}
		}
		
		return tmp.getPath();
	}

}
