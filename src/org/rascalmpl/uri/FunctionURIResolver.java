package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.result.ICallableValue;

public class FunctionURIResolver implements IURIInputOutputResolver {
	private final ICallableValue function;
	private final Type[] types = new Type[] { TypeFactory.getInstance().sourceLocationType() };
	private final IValue[] args = new IValue[] { null };
	private final URIResolverRegistry reg;
	private final String scheme;
	
	public FunctionURIResolver(String scheme, ICallableValue function) {
		this.scheme = scheme;
		this.function = function;
		this.reg = function.getEval().getResolverRegistry();
	}
	
	public URI resolve(URI uri) {
		args[0] = function.getEval().getValueFactory().sourceLocation(uri);
		ISourceLocation result = (ISourceLocation) function.call(types, args, null);
		return result.getURI();
	}
	
	@Override
	public InputStream getInputStream(URI uri) throws IOException {
		return reg.getInputStream(resolve(uri));
	}

	@Override
	public Charset getCharset(URI uri) throws IOException {
		return reg.getCharset(resolve(uri));
	}

	@Override
	public boolean exists(URI uri) {
		return reg.exists(resolve(uri));
	}

	@Override
	public long lastModified(URI uri) throws IOException {
		return reg.lastModified(resolve(uri));
	}

	@Override
	public boolean isDirectory(URI uri) {
		return reg.isDirectory(resolve(uri));
	}

	@Override
	public boolean isFile(URI uri) {
		return reg.isFile(resolve(uri));
	}

	@Override
	public String[] listEntries(URI uri) throws IOException {
		return reg.listEntries(resolve(uri));
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
	public OutputStream getOutputStream(URI uri, boolean append)
			throws IOException {
		return reg.getOutputStream(resolve(uri), append);
	}

	@Override
	public void mkDirectory(URI uri) throws IOException {
		reg.mkDirectory(resolve(uri));
	}
	
	@Override
	public void remove(URI uri) throws IOException {
	  reg.remove(resolve(uri));
	}

	@Override
	public URI getResourceURI(URI uri) throws IOException {
		return reg.getResourceURI(resolve(uri));
	}
}
