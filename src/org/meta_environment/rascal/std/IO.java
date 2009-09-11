package org.meta_environment.rascal.std;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.locations.URIResolverRegistry;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.TreeAdapter;

public class IO {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();	
	
	private volatile static PrintStream out = System.out;
	
	public static void setOutputStream(PrintStream out){
		IO.out = out;
	}
	
	public static void println(IValue V){
		PrintStream currentOutStream = out;
		
		synchronized(currentOutStream){
			Iterator<IValue> valueIterator = ((IList) V).iterator();
			while(valueIterator.hasNext()){
				IValue arg = valueIterator.next();
				
				if (arg.getType().isStringType()){
					currentOutStream.print(((IString) arg).getValue().toString());
				}
				else if (arg.getType().isSubtypeOf(Factory.Tree)) {
					currentOutStream.print(TreeAdapter.yield((IConstructor) arg));
				}
				else{
					currentOutStream.print(arg.toString());
				}
			}
			currentOutStream.println();
		}
		return;
	}
	
	public static void rawPrintln(IValue V) {
		PrintStream currentOutStream = out;
		
		synchronized(currentOutStream){
			Iterator<IValue> valueIterator = ((IList) V).iterator();
			while(valueIterator.hasNext()){
				currentOutStream.print(valueIterator.next().toString());
			}
			currentOutStream.println();
		}
		return;
	}

	@Deprecated
	public static IValue readFile(IString filename)
	{
		IList res = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename.getValue()));
			java.lang.String line;

			IListWriter w = types.listType(types.stringType()).writer(values);
			do {
				line = in.readLine();
				if(line != null){
					w.append(values.string(line));
				}
			} while (line != null);
			in.close();
			res =  w.done();
		}
		catch (FileNotFoundException e){
			throw RuntimeExceptionFactory.fileNotFound(filename, null, null);
		}
		catch (java.io.IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}

		return res;
	}
	
	public static IValue readFile(ISourceLocation file) {
		StringBuilder result = new StringBuilder();
		try {
			InputStream in = URIResolverRegistry.getInstance().getInputStream(file.getURI());
			byte[] buf = new byte[4096];
			int count;

			while ((count = in.read(buf)) != -1) {
				result.append(new java.lang.String(buf, 0, count));
			}
			
			in.close();
			
			java.lang.String str = result.toString();
			
			if (file.getOffset() != -1) {
				str = str.substring(file.getOffset(), file.getOffset() + file.getLength());
			}
			
			return values.string(str);

		}
		catch (FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(file, null, null);
		}
		catch (java.io.IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}

	}
	
	public static void writeFile(ISourceLocation file, IValue V) {
		try {
			OutputStream out = URIResolverRegistry.getInstance().getOutputStream(file.getURI());
			
			for (IValue elem : (IList) V) {
				if (elem.getType().isStringType()){
					out.write(((IString) elem).getValue().toString().getBytes());
				}
				else if (elem.getType().isSubtypeOf(Factory.Tree)) {
					out.write(TreeAdapter.yield((IConstructor) elem).getBytes());
				}
				else{
					out.write(elem.toString().getBytes());
				}
				out.write('\n');
			}
			out.flush();
			out.close();
		}
		catch (FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(file, null, null);
		}
		catch (java.io.IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}

		return;
	}
	
	public static IList readFileLines(ISourceLocation file)
	{
		IListWriter w = types.listType(types.stringType()).writer(values);
		
		try {
			InputStream stream = URIResolverRegistry.getInstance().getInputStream(file.getURI());
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			java.lang.String line;
			
			int i = 0;
			int offset = file.getOffset();
			int beginLine = file.getBeginLine();
			int beginColumn = file.getBeginColumn();
			int endLine = file.getEndLine();
			int endColumn = file.getEndColumn();

			do {
				line = in.readLine();
				i++;
				if(line != null){
					if (offset == -1) {
						w.append(values.string(line));
					}
					else {
						if (endColumn == -1) {
							endColumn = line.length();
						}
						if (i == beginLine) {
							if (i == endLine) {
								w.append(values.string(line.substring(beginColumn, endColumn)));
							}
							else {
								w.append(values.string(line.substring(beginColumn)));
							}
						}
						else if (i > beginLine) {
							if (i == endLine) {
								w.append(values.string(line.substring(0, endColumn)));
							}
							else if (i < endLine) {
								w.append(values.string(line));
							}
						}
					}
				}
			} while (line != null);
			in.close();
		}
		catch (FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(file, null, null);
		}
		catch (java.io.IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}

		return w.done();
	}
}
