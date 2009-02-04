package org.meta_environment.rascal.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.io.ATermReader;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.meta_environment.uptr.Factory;

import sglr.SGLRInvoker;

/**
 * Parses a Rascal program and a UPTR node.
 */
public class Parser{
	private final static String PARSETABLE_PROPERTY = "rascal.parsetable.file";
	private final static String PARSETABLE_FILENAME = "resources/rascal.trm.tbl";
	
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
		return (IConstructor) reader.read(ValueFactory.getInstance(), Factory.ParseTree, bais);
	}

	public IConstructor parseFromStream(InputStream inputStringStream) throws IOException, FactTypeError{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromStream(inputStringStream, getTableFile());
		
		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		return (IConstructor) reader.read(ValueFactory.getInstance(), Factory.ParseTree, bais);
	}
	
	public IConstructor parseFromFile(File inputFile) throws IOException, FactTypeError{
		SGLRInvoker sglrInvoker = SGLRInvoker.getInstance();
		byte[] result = sglrInvoker.parseFromFile(inputFile, getTableFile());

		ATermReader reader = new ATermReader();
		ByteArrayInputStream bais = new ByteArrayInputStream(result);
		return (IConstructor) reader.read(ValueFactory.getInstance(), Factory.ParseTree, bais);
	}
	
	private String getTableFile() throws IOException{
		String parseTableFile = System.getProperty(PARSETABLE_PROPERTY, PARSETABLE_FILENAME);
		File table = new File(parseTableFile);
		
		if(!table.exists()) throw new IOException("Could not locate parse table ("+PARSETABLE_FILENAME+").");
		
		return table.getPath();
	}
}
