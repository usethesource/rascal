package org.rascalmpl.library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.BinaryValueReader;
import org.eclipse.imp.pdb.facts.io.BinaryValueWriter;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.unicode.UnicodeDetector;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

/*
 * This class overrides methods from Prelude that need to be handled differenty in compiled code.
 * In most (all?) cases this will be library function with a @reflect{...} tag that makes them dependent on
 * IEvaluatorContext, the context of the Rascal interpreter.
 */
public class PreludeCompiled extends Prelude {

	public PreludeCompiled(IValueFactory values) {
		super(values);
	}
	
	public IValue exists(ISourceLocation sloc, RascalExecutionContext rex) {
		sloc = rex.resolveSourceLocation(sloc);
		return values.bool(rex.getResolverRegistry().exists(sloc.getURI()));
	}
	
	public IValue lastModified(ISourceLocation sloc, RascalExecutionContext rex) {
		sloc = rex.resolveSourceLocation(sloc);

		try {
			return values.datetime(rex.getResolverRegistry().lastModified(sloc.getURI()));
		} catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public IValue isDirectory(ISourceLocation sloc, RascalExecutionContext rex) {
		sloc = rex.resolveSourceLocation(sloc);
		return values.bool(rex.getResolverRegistry().isDirectory(sloc.getURI()));
	}
	
	public IValue isFile(ISourceLocation sloc, RascalExecutionContext rex) {
		sloc = rex.resolveSourceLocation(sloc);
		return values.bool(rex.getResolverRegistry().isFile(sloc.getURI()));
	}
	
	public void remove(ISourceLocation sloc, RascalExecutionContext rex) {
		try {
			sloc = rex.resolveSourceLocation(sloc);
			rex.getResolverRegistry().remove(sloc.getURI());
		}
		catch (IOException e) {
			RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public void mkDirectory(ISourceLocation sloc, RascalExecutionContext rex) {
		try {
			sloc = rex.resolveSourceLocation(sloc);
			rex.getResolverRegistry().mkDirectory(sloc.getURI());
		}
		catch (IOException e) {
			RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	public IValue listEntries(ISourceLocation sloc, RascalExecutionContext rex) {
		sloc = rex.resolveSourceLocation(sloc);

		try {
			java.lang.String [] entries = rex.getResolverRegistry().listEntries(sloc.getURI());
			IListWriter w = values.listWriter();
			for(java.lang.String entry : entries){
				w.append(values.string(entry));
			}
			return w.done();
		} catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} 
	}
	
	public IValue readFile(ISourceLocation sloc, RascalExecutionContext rex){
		sloc = rex.resolveSourceLocation(sloc);
		Reader reader = null;

		try {
			Charset c = rex.getResolverRegistry().getCharset(sloc.getURI());
			if (c != null) {
				return readFileEnc(sloc, values.string(c.name()), rex);
			}
			sloc = rex.resolveSourceLocation(sloc);
			reader = rex.getResolverRegistry().getCharacterReader(sloc.getURI());
			return consumeInputStream(sloc, reader, rex);
		} catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
	
	public IValue readFileEnc(ISourceLocation sloc, IString charset, RascalExecutionContext rex){
		sloc = rex.resolveSourceLocation(sloc);

		try (Reader reader = rex.getResolverRegistry().getCharacterReader(sloc.getURI(), charset.getValue())){
			return consumeInputStream(sloc, reader, rex);
		} catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}
	
	private IValue consumeInputStream(ISourceLocation sloc, Reader in, RascalExecutionContext rex) {
		try{
			java.lang.String str = null;
			if(!sloc.hasOffsetLength() || sloc.getOffset() == -1){
				StringBuilder result = new StringBuilder(1024 * 1024);
				char[] buf = new char[4096];
				int count;
	
				while((count = in.read(buf)) != -1) {
					result.append(new java.lang.String(buf, 0, count));
				}
				str = result.toString();
			}
			else {
				BufferedReader buffer = new BufferedReader(in, 4096);
				try {
					// first scan for offset
					int offset = sloc.getOffset();
					int seen = 0 ;
					while (seen < offset) {
						char c = (char)buffer.read();
						if (Character.isHighSurrogate(c)) {
							c = (char)buffer.read();
							if (!Character.isLowSurrogate(c))
								seen++;// strange string but it is possible

						}
						seen++;
					}
	
					// offset reached, start reading and possibly merging
					int targetLength = sloc.getLength();
					StringBuilder result = new StringBuilder(targetLength);
					int charsRead = 0;
					while (charsRead < targetLength) {
						int c = buffer.read();
						if (c == -1) {
							break; // EOF
						}
						charsRead++;
						result.append((char)c);
						if (Character.isHighSurrogate((char)c)) {
							c = buffer.read();
							if (c == -1) {
								break; // EOF
							}
							result.append((char)c);
							if (!Character.isLowSurrogate((char)c)) {
								// strange but in case of incorrect unicode stream
								// let's not eat the next character
								charsRead++;
							}
						}
					}
					str = result.toString();
				}
				finally {
					buffer.close();
				}
				
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
	
	public IValue md5HashFile(ISourceLocation sloc, RascalExecutionContext rex){
		StringBuilder result = new StringBuilder(1024 * 1024);

		InputStream in = null;
		try{
			in = rex.getResolverRegistry().getInputStream(sloc.getURI());
			MessageDigest md = MessageDigest.getInstance("MD5");
			in = new DigestInputStream(in, md);
			byte[] buf = new byte[4096];
			int count;

			while((count = in.read(buf)) != -1){
				result.append(new java.lang.String(buf, 0, count));
			}

			return values.string(new String(md.digest()));
		}catch(FileNotFoundException fnfex){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}catch(IOException ioex){
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
		} catch (NoSuchAlgorithmException e) {
			throw RuntimeExceptionFactory.io(values.string("Cannot load MD5 digest algorithm"), null, null);
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
	
	public void writeFile(ISourceLocation sloc, IList V, RascalExecutionContext rex) {
		writeFile(sloc, V, false, rex);
	}
	
	public void writeFileEnc(ISourceLocation sloc, IString charset, IList V, RascalExecutionContext rex) {
		writeFileEnc(sloc, charset, V, false, rex);
	}
	
	private void writeFile(ISourceLocation sloc, IList V, boolean append, RascalExecutionContext rex){
		sloc = rex.resolveSourceLocation(sloc);

		IString charset = values.string("UTF8");
		if (append) {
			// in case the file already has a encoding, we have to correctly append that.
			InputStream in = null;
			Charset detected = null;
			try {
				detected = rex.getResolverRegistry().getCharset(sloc.getURI());
				if (detected == null) {
					in = rex.getResolverRegistry().getInputStream(sloc.getURI());
					detected = UnicodeDetector.estimateCharset(in);
				}
			}catch(FileNotFoundException fnfex){
				throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
			} catch (IOException e) {
				throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
			}
			finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
					}
				}
			}
			if (detected != null)
				charset = values.string(detected.name());
			else {
				charset = values.string(Charset.defaultCharset().name());
			}
		}
		writeFileEnc(sloc, charset, V, append, rex);
	}
	
	private void writeFileEnc(ISourceLocation sloc, IString charset, IList V, boolean append, RascalExecutionContext rex){
		sloc = rex.resolveSourceLocation(sloc);

		OutputStreamWriter out = null;

		if (!Charset.forName(charset.getValue()).canEncode()) {
			throw RuntimeExceptionFactory.illegalArgument(charset, null, null);
		}

		try{
			out = new UnicodeOutputStreamWriter(rex.getResolverRegistry().getOutputStream(sloc.getURI(), append), charset.getValue(), append);

			for(IValue elem : V){
				if (elem.getType().isString()) {
					out.append(((IString) elem).getValue());
				}else if (elem.getType().isSubtypeOf(Factory.Tree)) {
					out.append(TreeAdapter.yield((IConstructor) elem));
				}else{
					out.append(elem.toString());
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
	
	public void writeFileBytes(ISourceLocation sloc, IList blist, RascalExecutionContext rex){
		sloc = rex.resolveSourceLocation(sloc);
		BufferedOutputStream out=null;
		try{
			OutputStream stream = rex.getResolverRegistry().getOutputStream(sloc.getURI(), false);
			out = new BufferedOutputStream(stream);
			Iterator<IValue> iter = blist.iterator();
			while (iter.hasNext()){
				IValue ival = iter.next();
				out.write((byte) (((IInteger) ival).intValue()));
			}
			out.flush();
			out.close();
		}catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
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
	
	public void appendToFile(ISourceLocation sloc, IList V, RascalExecutionContext rex){
		writeFile(sloc, V, true, rex);
	}
	
	public void appendToFileEnc(ISourceLocation sloc, IString charset, IList V, RascalExecutionContext rex){
		writeFileEnc(sloc, charset, V, true, rex);
	}
	
	public IList readFileLines(ISourceLocation sloc, RascalExecutionContext rex){
		  sloc = rex.resolveSourceLocation(sloc);
		  Reader reader = null;
		  
			try {
				Charset detected = rex.getResolverRegistry().getCharset(sloc.getURI());
				if (detected != null) {
					return readFileLinesEnc(sloc, values.string(detected.name()), rex);
				}
				reader = rex.getResolverRegistry().getCharacterReader(sloc.getURI());
	      return consumeInputStreamLines(sloc, reader, rex);
			}catch(MalformedURLException e){
			    throw RuntimeExceptionFactory.malformedURI(sloc.toString(), null, null);
			}catch(FileNotFoundException e){
				throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
			}catch(IOException e){
				throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
			} finally {
			  if (reader != null) {
			    try {
	          reader.close();
	        } catch (IOException e) {
	          // forgot about it
	        }
			  }
			}
		}
	
	public IList readFileLinesEnc(ISourceLocation sloc, IString charset, RascalExecutionContext rex){
		  sloc = rex.resolveSourceLocation(sloc);
		  
			try {
				return consumeInputStreamLines(sloc, rex.getResolverRegistry().getCharacterReader(sloc.getURI(),charset.getValue()), rex);
			}catch(MalformedURLException e){
			    throw RuntimeExceptionFactory.malformedURI(sloc.toString(), null, null);
			}catch(FileNotFoundException e){
				throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
			}catch(IOException e){
				throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
			}
		}
	
	private IList consumeInputStreamLines(ISourceLocation sloc,	Reader stream, RascalExecutionContext rex ) {
		IListWriter w = values.listWriter();

		BufferedReader in = null;
		try{
			in = new BufferedReader(stream);
			java.lang.String line;

			int i = 0;
			//			int offset = sloc.getOffset();
			int beginLine = sloc.hasLineColumn() ? sloc.getBeginLine() : -1;
			int beginColumn = sloc.hasLineColumn() ? sloc.getBeginColumn() : -1;
			int endLine = sloc.hasLineColumn() ? sloc.getEndLine() : -1;
			int endColumn = sloc.hasLineColumn() ? sloc.getEndColumn() : -1;

			do{
				line = in.readLine();
				i++;
				if(line != null){
					if(!sloc.hasOffsetLength()){
						w.append(values.string(line));
					}else{
						if(!sloc.hasLineColumn()){
							endColumn = line.length();
						}
						if(i == beginLine){
							if(i == endLine){
								w.append(values.string(line.substring(beginColumn, Math.min(endColumn, line.length()))));
							}else{
								w.append(values.string(line.substring(beginColumn)));
							}
						}else if(i > beginLine){
							if(i == endLine){
								w.append(values.string(line.substring(0, Math.min(endColumn, line.length()))));
							}
							else if(i < endLine){
								w.append(values.string(line));
							}
						}
					}
				}
			}while(line != null);
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
	
	public IList readFileBytes(ISourceLocation sloc, RascalExecutionContext rex){
		IListWriter w = values.listWriter();
		sloc = rex.resolveSourceLocation(sloc);
		
		BufferedInputStream in = null;
		try{
			InputStream stream = rex.getResolverRegistry().getInputStream(sloc.getURI());
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
	
	
	
	@Override
	// public java &T<:Tree parse(type[&T<:Tree] begin, str input);
	public IValue parse(IValue start, ISourceLocation input, IEvaluatorContext ctx) {
		return RascalPrimitive.getParsingTools().parse(super.values.string("XXX"), start, input);
	}
	
	@Override
	// public java &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin);
	public IValue parse(IValue start, IString input, IEvaluatorContext ctx) {
		return RascalPrimitive.getParsingTools().parse(super.values.string("XXX"), start, input, null);
	}
	
	private TypeStore typeStore = new TypeStore();
	
	@Override
	public IConstructor makeConstructor(Type returnType, String name, IEvaluatorContext ctx, IValue ...args) {
		// TODO: in general, the following should be the call to an overloaded function
		IValue value = values.constructor(typeStore.lookupConstructor(returnType, name, TypeFactory.getInstance().tupleType(args)), args, new HashMap<String, IValue>());
		Type type = value.getType();
		if (type.isAbstractData()) {
			return (IConstructor)value;
		}
		throw RuntimeExceptionFactory.implodeError("Calling of constructor " + name + " did not return a constructor", null, null);
	}
	
	@Override
	public IValue implode(IValue reifiedType, IConstructor tree, IEvaluatorContext ctx) {
		typeStore = new TypeStore();
		Type type = tr.valueToType((IConstructor) reifiedType, typeStore);
		try {
			IValue result = implode(typeStore, type, tree, false, ctx); 
			if (isUntypedNodeType(type) && !type.isTop() && (TreeAdapter.isList(tree) || TreeAdapter.isOpt(tree))) {
				result = values.node("", result);
			}
			return result;
		}
		catch (Backtrack b) {
			throw b.exception;
		}
	}
	
	public IInteger getFileLength(ISourceLocation g, RascalExecutionContext rex) throws IOException {
		File f = new File(rex.getResolverRegistry().getResourceURI(g.getURI()));
		if (!f.exists() || f.isDirectory()) throw new IOException();
		return values.integer(f.length());
	}

	public IValue readBinaryValueFile(IValue type, ISourceLocation loc, RascalExecutionContext rex){

		//		TypeStore store = ctx.getCurrentEnvt().getStore();
		TypeStore store = new TypeStore();

		// TODO: commented out the following lines and that seems to sove the duplicate declaration of ParseTree.
		//		 Why was this import here? Can someone check?

		//		ModuleEnvironment pt = ctx.getHeap().getModule("ParseTree");
		//		if(pt != null){
		//			store.importStore(pt.getStore());
		//		}
		Type start = tr.valueToType((IConstructor) type, store);
		loc = rex.resolveSourceLocation(loc);

		InputStream in = null;
		try{
			in = new BufferedInputStream(rex.getResolverRegistry().getInputStream(loc.getURI()));
			return new BinaryValueReader().read(values, store, start, in);
		}catch(IOException e){
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}catch(Exception e){
			e.printStackTrace();
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
	}


	public IValue readTextValueFile(IValue type, ISourceLocation loc, RascalExecutionContext rex){
		loc = rex.resolveSourceLocation(loc);

		TypeStore store = new TypeStore();
		Type start = tr.valueToType((IConstructor) type, store);

		InputStream in = null;
		try{
			in = new BufferedInputStream(rex.getResolverRegistry().getInputStream(loc.getURI()));
			return new StandardTextReader().read(new RascalValuesValueFactory(), store, start, new InputStreamReader(in, "UTF8"));
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
	}

	public IValue readTextValueString(IValue type, IString input, RascalExecutionContext rex) {
		//	TypeStore store = ctx.getCurrentEnvt().getStore();
		TypeStore store = new TypeStore();
		//		ModuleEnvironment pt = ctx.getHeap().getModule("ParseTree");
		//		if(pt != null){
		//			store.importStore(pt.getStore());
		//		}
		Type start = tr.valueToType((IConstructor) type, store);

		StringReader in = new StringReader(input.getValue());
		try {
			return new StandardTextReader().read(values, store, start, in);
		} catch (FactTypeUseException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		} catch (IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
	}

	public void writeBinaryValueFile(ISourceLocation loc, IValue value, IBool compression, RascalExecutionContext rex){
		loc = rex.resolveSourceLocation(loc);

		OutputStream out = null;
		try{
			out = rex.getResolverRegistry().getOutputStream(loc.getURI(), false); 
			new BinaryValueWriter().write(value, out, compression.getValue());
		}catch (IOException ioex){
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
	}

	public void writeTextValueFile(ISourceLocation loc, IValue value, RascalExecutionContext rex){
		loc = rex.resolveSourceLocation(loc);

		OutputStream out = null;
		try{
			out = rex.getResolverRegistry().getOutputStream(loc.getURI(), false);
			new StandardTextWriter().write(value, new OutputStreamWriter(out, "UTF8"));
		}
		catch(IOException e) {
			throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
		}
		finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				}
				catch(IOException ioex) {
					throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}

}
