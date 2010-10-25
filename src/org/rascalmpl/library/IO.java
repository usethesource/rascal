package org.rascalmpl.library;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class IO {
	private static final TypeFactory types = TypeFactory.getInstance();	

	private final IValueFactory values;
	private volatile PrintStream out;
	
	public IO(IValueFactory values){
		super();
		
		this.values = values;
		out = System.out;
	}
	
	public void setOutputStream(PrintStream out){
		this.out = out;
	}
	
	public void println(IList V){
		PrintStream currentOutStream = out;
		
		synchronized(currentOutStream){
			try{
				Iterator<IValue> valueIterator = V.iterator();
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
	
	public void rawPrintln(IList V){
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
	public IValue readFile(IString filename){
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
	
	public IValue exists(ISourceLocation sloc, IEvaluatorContext ctx) {
		return values.bool(ctx.getResolverRegistry().exists(sloc.getURI()));
	}
	
	public IValue lastModified(ISourceLocation sloc, IEvaluatorContext ctx) {
		try {
			return values.datetime(ctx.getResolverRegistry().lastModified(sloc.getURI()));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	public IValue isDirectory(ISourceLocation sloc, IEvaluatorContext ctx) {
		return values.bool(ctx.getResolverRegistry().isDirectory(sloc.getURI()));
	}
	
	public IValue isFile(ISourceLocation sloc, IEvaluatorContext ctx) {
		return values.bool(ctx.getResolverRegistry().isFile(sloc.getURI()));
	}
	
	public IValue absolutePath(ISourceLocation sloc, IEvaluatorContext ctx) {
		try {
			return values.string(ctx.getResolverRegistry().absolutePath(sloc.getURI()));
		} catch (IOException e) {
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}
	}
	
	public IValue mkDirectory(ISourceLocation sloc, IEvaluatorContext ctx) throws IOException {
		//File f = new File(sloc.getURI().getPath());
		//System.err.println("mkDirectory: " + sloc.getURI().getPath());
		//return values.bool(f.mkdir());
		return values.bool(ctx.getResolverRegistry().mkDirectory(sloc.getURI()));
	}
	
	public IValue listEntries(ISourceLocation sloc, IEvaluatorContext ctx) {
		try {
			java.lang.String [] entries = ctx.getResolverRegistry().listEntries(sloc.getURI());
			IListWriter w = values.listWriter(types.stringType());
			for(java.lang.String entry : entries){
				w.append(values.string(entry));
			}
			return w.done();
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), ctx.getCurrentAST(), ctx.getStackTrace());
		} 
	}
	
	
	public IValue readFile(ISourceLocation sloc, IEvaluatorContext ctx){
		StringBuilder result = new StringBuilder();
		
		InputStream in = null;
		try{
			in = ctx.getResolverRegistry().getInputStream(sloc.getURI());
			byte[] buf = new byte[4096];
			int count;

			while((count = in.read(buf)) != -1){
				result.append(new java.lang.String(buf, 0, count));
			}
			
			java.lang.String str = result.toString();
			
			if(sloc.getOffset() != -1){
				str = str.substring(sloc.getOffset(), sloc.getOffset() + sloc.getLength());
			}
			
			return values.string(str);
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
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
	
	public void writeFile(ISourceLocation sloc, IList V, IEvaluatorContext ctx) {
		writeFile(sloc, V, false, ctx);
	}
	
	private void writeFile(ISourceLocation sloc, IList V, boolean append, IEvaluatorContext ctx){
		OutputStream out = null;
		try{
			out = ctx.getResolverRegistry().getOutputStream(sloc.getURI(), append);
			
			for(IValue elem : V){
				if (elem.getType().isStringType()){
					out.write(((IString) elem).getValue().toString().getBytes());
				}else if (elem.getType().isSubtypeOf(Factory.Tree)) {
					out.write(TreeAdapter.yield((IConstructor) elem).getBytes());
				}else{
					out.write(elem.toString().getBytes());
				}
			}
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
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
	
	public void appendToFile(ISourceLocation sloc, IList V, IEvaluatorContext ctx){
		writeFile(sloc, V, true, ctx);
	}
	
	public IList readFileLines(ISourceLocation sloc, IEvaluatorContext ctx){
		IListWriter w = types.listType(types.stringType()).writer(values);
		
		BufferedReader in = null;
		try{
			InputStream stream = ctx.getResolverRegistry().getInputStream(sloc.getURI());
			in = new BufferedReader(new InputStreamReader(stream));
			java.lang.String line;
			
			int i = 0;
			int offset = sloc.getOffset();
			int beginLine = sloc.getBeginLine();
			int beginColumn = sloc.getBeginColumn();
			int endLine = sloc.getEndLine();
			int endColumn = sloc.getEndColumn();

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
		}catch(MalformedURLException e){
		    throw RuntimeExceptionFactory.malformedURI(sloc.toString(), null, null);
		}catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
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
	
	public IList readFileBytes(ISourceLocation sloc, IEvaluatorContext ctx){
		IListWriter w = types.listType(types.integerType()).writer(values);
		
		BufferedInputStream in = null;
		try{
			InputStream stream = ctx.getResolverRegistry().getInputStream(sloc.getURI());
			in = new BufferedInputStream(stream);
			int read;
			final int size = 256;
			byte bytes[] = new byte[size];
			
			do{
				read = in.read(bytes);
				for (int i = 0; i < read; i++) {
					w.append(values.integer(bytes[i] & 0xff));
				}
			}while(read != -1);
		}catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
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
