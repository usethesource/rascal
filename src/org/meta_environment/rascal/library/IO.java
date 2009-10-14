package org.meta_environment.rascal.library;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.TreeAdapter;
import org.meta_environment.uri.URIResolverRegistry;
import org.meta_environment.values.ValueFactoryFactory;

public class IO{
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();	
	
	private volatile static PrintStream out = System.out;
	
	public static void setOutputStream(PrintStream out){
		IO.out = out;
	}
	
	public static void println(IList V){
		PrintStream currentOutStream = out;
		
		synchronized(currentOutStream){
			try{
				Iterator<IValue> valueIterator = ((IList) V).iterator();
				while(valueIterator.hasNext()){
					IValue arg = valueIterator.next();
					
					if(arg.getType().isStringType()){
						currentOutStream.print(((IString) arg).getValue().toString());
					}else if(arg.getType().isSubtypeOf(Factory.Tree)){
						currentOutStream.print(TreeAdapter.yield((IConstructor) arg));
					}else{
						currentOutStream.print(arg.toString());
					}
				}
				currentOutStream.println();
			}finally{
				currentOutStream.flush();
			}
		}
	}
	
	public static void rawPrintln(IList V){
		PrintStream currentOutStream = out;
		
		synchronized(currentOutStream){
			try{
				Iterator<IValue> valueIterator = V.iterator();
				while(valueIterator.hasNext()){
					currentOutStream.print(valueIterator.next().toString());
				}
				currentOutStream.println();
			}finally{
				currentOutStream.flush();
			}
		}
	}

	@Deprecated
	public static IValue readFile(IString filename){
		IListWriter w = types.listType(types.stringType()).writer(values);
		
		BufferedReader in = null;
		try{
			in = new BufferedReader(new FileReader(filename.getValue()));
			java.lang.String line;

			do {
				line = in.readLine();
				if(line != null){
					w.append(values.string(line));
				}
			} while (line != null);
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(values.sourceLocation(filename.getValue()), null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
		
		return w.done();
	}
	
	public static IValue readFile(ISourceLocation file){
		StringBuilder result = new StringBuilder();
		
		InputStream in = null;
		try{
			in = URIResolverRegistry.getInstance().getInputStream(file.getURI());
			byte[] buf = new byte[4096];
			int count;

			while((count = in.read(buf)) != -1){
				result.append(new java.lang.String(buf, 0, count));
			}
			
			java.lang.String str = result.toString();
			
			if(file.getOffset() != -1){
				str = str.substring(file.getOffset(), file.getOffset() + file.getLength());
			}
			
			return values.string(str);
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(file, null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}
	
	public static void writeFile(ISourceLocation file, IList V){
		OutputStream out = null;
		try{
			out = URIResolverRegistry.getInstance().getOutputStream(file.getURI());
			
			for(IValue elem : V){
				if (elem.getType().isStringType()){
					out.write(((IString) elem).getValue().toString().getBytes());
				}else if (elem.getType().isSubtypeOf(Factory.Tree)) {
					out.write(TreeAdapter.yield((IConstructor) elem).getBytes());
				}else{
					out.write(elem.toString().getBytes());
				}
				out.write('\n');
			}
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(file, null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		}finally{
			if(out != null){
				try{
					out.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}

		return;
	}
	
	public static IList readFileLines(ISourceLocation file){
		IListWriter w = types.listType(types.stringType()).writer(values);
		
		BufferedReader in = null;
		try{
			InputStream stream = URIResolverRegistry.getInstance().getInputStream(file.getURI());
			in = new BufferedReader(new InputStreamReader(stream));
			java.lang.String line;
			
			int i = 0;
			int offset = file.getOffset();
			int beginLine = file.getBeginLine();
			int beginColumn = file.getBeginColumn();
			int endLine = file.getEndLine();
			int endColumn = file.getEndColumn();

			do{
				line = in.readLine();
				i++;
				if(line != null){
					if(offset == -1){
						w.append(values.string(line));
					}else{
						if(endColumn == -1){
							endColumn = line.length();
						}
						if(i == beginLine){
							if(i == endLine){
								w.append(values.string(line.substring(beginColumn, endColumn)));
							}else{
								w.append(values.string(line.substring(beginColumn)));
							}
						}else if(i > beginLine){
							if(i == endLine){
								w.append(values.string(line.substring(0, endColumn)));
							}
							else if(i < endLine){
								w.append(values.string(line));
							}
						}
					}
				}
			}while(line != null);
		}catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(file, null, null);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException ioex){
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}

		return w.done();
	}
}
