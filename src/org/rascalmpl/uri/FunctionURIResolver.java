package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.result.ICallableValue;

// TODO: there is a chance of deadlock here since we have to lock on the evaluator
public class FunctionURIResolver implements ISourceLocationInputOutput {
	private final ICallableValue function;
	private final Type[] types = new Type[] { TypeFactory.getInstance().sourceLocationType() };
	private final IValue[] args = new IValue[] { null };
	private final URIResolverRegistry reg;
	private final String scheme;
	
	public FunctionURIResolver(String scheme, ICallableValue function) {
		this.scheme = scheme;
		this.function = function;
		this.reg = URIResolverRegistry.getInstance();
	}
	
	public ISourceLocation resolve(ISourceLocation uri) {
		args[0] = uri;
		return (ISourceLocation) function.call(types, args, null);
	}
	
	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		return reg.getInputStream(resolve(uri));
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return reg.getCharset(resolve(uri));
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		return reg.exists(resolve(uri));
	}

	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		return reg.lastModified(resolve(uri));
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
		return reg.isDirectory(resolve(uri));
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return reg.isFile(resolve(uri));
	}

	@Override
	public ISourceLocation[] list(ISourceLocation uri) throws IOException {
		return reg.list(resolve(uri));
	}

	@Override
	public String scheme() {
		return scheme;
	}

	@Override
	public boolean supportsHost() {
		return false;
	}

	@Override
	public OutputStream getOutputStream(ISourceLocation uri, boolean append)
			throws IOException {
		return reg.getOutputStream(resolve(uri), append);
	}

	@Override
	public void mkDirectory(ISourceLocation uri) throws IOException {
		reg.mkDirectory(resolve(uri));
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
	  reg.remove(resolve(uri));
	}
}
