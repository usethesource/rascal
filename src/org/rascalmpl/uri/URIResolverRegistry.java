/*******************************************************************************
 * Copyright (c) 2009-2017 CWI All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 * * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI * Paul Klint - Paul.Klint@cwi.nl - CWI * Mark Hills
 * - Mark.Hills@cwi.nl (CWI) * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.unicode.UnicodeDetector;
import org.rascalmpl.unicode.UnicodeInputStreamReader;
import org.rascalmpl.unicode.UnicodeOffsetLengthReader;
import org.rascalmpl.unicode.UnicodeOutputStreamWriter;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;
import org.rascalmpl.uri.watch.WatchRegistry;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class URIResolverRegistry {
	private static final int FILE_BUFFER_SIZE = 8 * 1024;
	private static final String RESOLVERS_CONFIG = "org/rascalmpl/uri/resolvers.config";
	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private final Map<String, ISourceLocationInput> inputResolvers = new ConcurrentHashMap<>();
	private final Map<String, ISourceLocationOutput> outputResolvers = new ConcurrentHashMap<>();
	private final Map<String, Map<String, ILogicalSourceLocationResolver>> logicalResolvers = new ConcurrentHashMap<>();
	private final Map<String, IClassloaderLocationResolver> classloaderResolvers = new ConcurrentHashMap<>();

	// we allow the user to define (using -Drascal.fallbackResolver=fully.qualified.classname) a single class that will handle
	// scheme's not statically registered. That class should implement at least one of these interfaces
	private volatile @Nullable ISourceLocationInput fallbackInputResolver;
	private volatile @Nullable ISourceLocationOutput fallbackOutputResolver;
	private volatile @Nullable ILogicalSourceLocationResolver fallbackLogicalResolver;
	private volatile @Nullable IClassloaderLocationResolver fallbackClassloaderResolver;

	private static class InstanceHolder {
		static URIResolverRegistry sInstance = new URIResolverRegistry();
	}

	private final WatchRegistry watchers;
	private URIResolverRegistry() {
		watchers = new WatchRegistry(this, this::safeResolve);
		loadServices();
	}


	/**
	 * Use with care! This (expensive) reinitialization method clears all caches of all resolvers by
	 * reloading them from scratch.
	 * 
	 * <p>
	 * This can be beneficial if the state of a system changes outside of the scope of the resolvers
	 * themselves, for example when projects open or close inside a workspace or when plugins are loaded
	 * or unloaded dynamically. In other words, when the URIs are possibly not uniquely identifying the
	 * same resource anymore, it's high time to re-initialize this registry and all of its resolvers
	 * from scratch. If such a URI re-defining event is detected, host environments (IDEs, app
	 * containers, language servers) should call this method.
	 * </p>
	 * 
	 * <p>
	 * CAVEAT: after this reinitialization all location caches have been removed and so the first
	 * locations to be used may require expensive indexing and probing operations, for example
	 * extracting and indexing jar files and testing whether plugins are loaded or projects have target
	 * folders, etc.
	 * </p>
	 * <p>
	 * CAVEAT: it is not possible and will not be possible to re-init specific URI schemes leaving
	 * others untouched. This in the interest of the black-box and immutable design of the URI
	 * resolution mechanism. The only reason to call reinitialize() is when this entire abstraction has
	 * failed, so when URIs are accidentally not URIs anymore.
	 */
	public void reinitialize() {
		loadServices();
	}

	private void loadServices() {
		try {
			Enumeration<URL> resources = getClass().getClassLoader().getResources(RESOLVERS_CONFIG);
			Collections.list(resources).forEach(f -> loadServices(f));
			var fallbackResolverClassName = System.getProperty("rascal.fallbackResolver");
			if (fallbackResolverClassName != null) {
				loadFallback(fallbackResolverClassName);
			}
		}
		catch (IOException e) {
			throw new Error("WARNING: Could not load URIResolverRegistry extensions from " + RESOLVERS_CONFIG, e);
		}
	}

	public Set<String> getRegisteredInputSchemes() {
		return Collections.unmodifiableSet(inputResolvers.keySet());
	}

	public Set<String> getRegisteredOutputSchemes() {
		return Collections.unmodifiableSet(outputResolvers.keySet());
	}

	public Set<String> getRegisteredLogicalSchemes() {
		return Collections.unmodifiableSet(logicalResolvers.keySet());
	}

	public Set<String> getRegisteredClassloaderSchemes() {
		return Collections.unmodifiableSet(classloaderResolvers.keySet());
	}

	private Object constructService(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(name);

		try {
			return clazz.getDeclaredConstructor(URIResolverRegistry.class).newInstance(this);
		}
		catch (NoSuchMethodException e) {
			return clazz.newInstance();
		}
	}

	private void loadFallback(String fallbackClass) {
		try {
			Object instance = constructService(fallbackClass);
			boolean ok = false;
			if (instance instanceof ILogicalSourceLocationResolver) {
				fallbackLogicalResolver = (ILogicalSourceLocationResolver) instance;
				ok = true;
			}

			if (instance instanceof ISourceLocationInput) {
				fallbackInputResolver = (ISourceLocationInput) instance;
				ok = true;
			}

			if (instance instanceof ISourceLocationOutput) {
				fallbackOutputResolver = (ISourceLocationOutput) instance;
				ok = true;
			}

			if (instance instanceof IClassloaderLocationResolver) {
				fallbackClassloaderResolver = (IClassloaderLocationResolver) instance;
				ok = true;
			}

			if (instance instanceof ISourceLocationWatcher) {
				watchers.setFallback((ISourceLocationWatcher) instance);
			}
			if (!ok) {
				System.err.println("WARNING: could not load fallback resolver " + fallbackClass
					+ " because it does not implement ISourceLocationInput or ISourceLocationOutput or ILogicalSourceLocationResolver");
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException
			| IllegalArgumentException | InvocationTargetException | SecurityException  e) {
			System.err.println("WARNING: could not load resolver due to " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void loadServices(URL nextElement) {
		try {
			for (String name : readConfigFile(nextElement)) {
				name = name.trim();

				if (name.startsWith("#") || name.isEmpty()) {
					// source code comment or empty line
					continue;
				}

				Object instance = constructService(name);

				boolean ok = false;

				if (instance instanceof ILogicalSourceLocationResolver) {
					registerLogical((ILogicalSourceLocationResolver) instance);
					ok = true;
				}

				if (instance instanceof ISourceLocationInput) {
					registerInput((ISourceLocationInput) instance);
					ok = true;
				}

				if (instance instanceof ISourceLocationOutput) {
					registerOutput((ISourceLocationOutput) instance);
					ok = true;
				}

				if (instance instanceof IClassloaderLocationResolver) {
					registerClassloader((IClassloaderLocationResolver) instance);
					ok = true;
				}

				if (instance instanceof ISourceLocationWatcher) {
					registerWatcher((ISourceLocationWatcher) instance);
				}

				if (!ok) {
					System.err.println("WARNING: could not load resolver " + name
						+ " because it does not implement ISourceLocationInput or ISourceLocationOutput or ILogicalSourceLocationResolver");
				}

			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException
			| IllegalArgumentException | InvocationTargetException | SecurityException | IOException e) {
			System.err.println("WARNING: could not load resolver due to " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String[] readConfigFile(URL nextElement) throws IOException {
		try (Reader in = new InputStreamReader(nextElement.openStream())) {
			StringBuilder res = new StringBuilder();
			char[] chunk = new char[1024];
			int read;
			while ((read = in.read(chunk, 0, chunk.length)) != -1) {
				res.append(chunk, 0, read);
			}
			return res.toString().split("\n");
		}
	}

	public static URIResolverRegistry getInstance() {
		return InstanceHolder.sInstance;
	}

	private static InputStream makeBuffered(InputStream original) {
		if (original instanceof BufferedInputStream || original instanceof ByteArrayInputStream) {
			return original;
		}
		return new BufferedInputStream(original);
	}

	private OutputStream makeBuffered(ISourceLocation loc, boolean existed, OutputStream original) {
		if (original instanceof NotifyingOutputStream) {
			return original;
		}

		if (original instanceof BufferedOutputStream || original instanceof ByteArrayOutputStream) {
			return new NotifyingOutputStream(
				original, 
				loc, 
				existed ? ISourceLocationWatcher.modified(loc) : ISourceLocationWatcher.created(loc)
			);
		}

		return new NotifyingOutputStream(new BufferedOutputStream(original), 
			loc, 
			existed ? ISourceLocationWatcher.modified(loc) : ISourceLocationWatcher.created(loc)
		);
	}
	private class NotifyingOutputStream extends FilterOutputStream {
		private final ISourceLocationChanged event;
		private ISourceLocation loc;

		public NotifyingOutputStream(OutputStream wrapped, ISourceLocation loc, ISourceLocationChanged event) {
			super(wrapped);
			assert loc != null && event != null;
			this.loc = loc;
			this.event = event;
		}

		public void close() throws IOException {
			super.close();
			notifyWatcher(loc, event);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			this.out.write(b, off, len);
		}
	}

	/**
	 * Translates a logical location (i.e. for a specific language scheme) to a physical location. For
	 * this mapping the registered {@link ILogicalSourceLocationResolver} collection is used. These are
	 * indexed first by scheme and then by authority. If the scheme is registered but the authority is
	 * not, then the same lookup is tried again without authority.
	 * 
	 * @param loc logical source location
	 * @return physical source location
	 * @throws IOException when there is no registered resolver for the logical scheme provided
	 */
	public ISourceLocation logicalToPhysical(ISourceLocation loc) throws IOException {
		ISourceLocation result = physicalLocation(loc);
		if (result == null) {
			throw new FileNotFoundException(loc == null ? "null loc passed!" : loc.toString());
		}
		return result;
	}

	private static ISourceLocation resolveAndFixOffsets(ISourceLocation loc, ILogicalSourceLocationResolver resolver, Iterable<ILogicalSourceLocationResolver> backups) throws IOException {
		ISourceLocation prev = loc;
		boolean removedOffset = false;

		if (resolver != null) {
			loc = resolver.resolve(loc);
		}

		if (loc == null && prev.hasOffsetLength()) {
			loc = resolver.resolve(URIUtil.removeOffset(prev));
			removedOffset = true;
		}

		if (loc == null || prev.equals(loc)) {
			for (ILogicalSourceLocationResolver backup : backups) {
				removedOffset = false;
				loc = backup.resolve(prev);

				if (loc == null && prev.hasOffsetLength()) {
					loc = backup.resolve(URIUtil.removeOffset(prev));
					removedOffset = true;
				}

				if (loc != null && !prev.equals(loc)) {
					break; // continue to offset/length handling below with found location
				}
			}
		}

		if (loc == null || prev.equals(loc)) {
			return null;
		}

		if (removedOffset || !loc.hasOffsetLength()) { // then copy the offset from the logical one
			if (prev.hasLineColumn()) {
				return vf.sourceLocation(loc, prev.getOffset(), prev.getLength(), prev.getBeginLine(),
					prev.getEndLine(), prev.getBeginColumn(), prev.getEndColumn());
			}
			else if (prev.hasOffsetLength()) {
				if (loc.hasOffsetLength()) {
					return vf.sourceLocation(loc, prev.getOffset() + loc.getOffset(), prev.getLength());
				}
				else {
					return vf.sourceLocation(loc, prev.getOffset(), prev.getLength());
				}
			}
		}
		else if (loc.hasLineColumn()) { // the logical location offsets relative to the physical offset, possibly
										// including line numbers
			if (prev.hasLineColumn()) {
				return vf.sourceLocation(loc, loc.getOffset() + prev.getOffset(), loc.getLength(),
					loc.getBeginLine() + prev.getBeginLine() - 1, loc.getEndLine() + prev.getEndLine() - 1,
					loc.getBeginColumn(), loc.getEndColumn());
			}
			else if (prev.hasOffsetLength()) {
				return vf.sourceLocation(loc, loc.getOffset() + prev.getOffset(), loc.getLength());
			}
		}
		else if (loc.hasOffsetLength()) { // the logical location offsets relative to the physical one
			if (prev.hasOffsetLength()) {
				return vf.sourceLocation(loc, loc.getOffset() + prev.getOffset(), loc.getLength());
			}
		}
		// otherwise we return the loc without any offsets
		return loc;
	}

	private ISourceLocation physicalLocation(ISourceLocation loc) throws IOException {
		ISourceLocation original = loc;
		while (loc != null && logicalResolvers.containsKey(loc.getScheme())) {
			Map<String, ILogicalSourceLocationResolver> map = logicalResolvers.get(loc.getScheme());
			String auth = loc.hasAuthority() ? loc.getAuthority() : "";
			ILogicalSourceLocationResolver resolver = map.get(auth);
			loc = resolveAndFixOffsets(loc, resolver, map.values());
		}
		
		if (fallbackLogicalResolver != null) {
			var fallbackResult = resolveAndFixOffsets(loc == null ? original : loc, fallbackLogicalResolver, Collections.emptyList());
			return fallbackResult == null ? loc : fallbackResult;
		}
		return loc;
	}

	private @NonNull ISourceLocation safeResolve(@NonNull ISourceLocation loc) {
		ISourceLocation resolved = null;

		try {
			resolved = physicalLocation(loc);
		}
		catch (Throwable e) {
			// robustness
		}

		return resolved != null ? resolved : loc;
	}

	private void registerInput(ISourceLocationInput resolver) {
		inputResolvers.put(resolver.scheme(), resolver);
	}

	private void registerOutput(ISourceLocationOutput resolver) {
		outputResolvers.put(resolver.scheme(), resolver);
	}

	public void registerLogical(ILogicalSourceLocationResolver resolver) {
		Map<String, ILogicalSourceLocationResolver> map =
			logicalResolvers.computeIfAbsent(resolver.scheme(), k -> new ConcurrentHashMap<>());
		map.put(resolver.authority(), resolver);
	}

	private void registerClassloader(IClassloaderLocationResolver resolver) {
		classloaderResolvers.put(resolver.scheme(), resolver);
	}

	private void registerWatcher(ISourceLocationWatcher resolver) {
		watchers.registerNative(resolver.scheme(), resolver);
	}

	public void unregisterLogical(String scheme, String auth) {
		Map<String, ILogicalSourceLocationResolver> map = logicalResolvers.get(scheme);
		if (map != null) {
			map.remove(auth);
		}
	}

	private static final Pattern splitScheme = Pattern.compile("^([^\\+]*)\\+");

	private ISourceLocationInput getInputResolver(String scheme) {
		ISourceLocationInput result = inputResolvers.get(scheme);
		if (result == null) {
			Matcher m = splitScheme.matcher(scheme);
			if (m.find()) {
				String subScheme = m.group(1);
				result = inputResolvers.get(subScheme);
				if (result != null) {
					return result;
				}
			}
			return fallbackInputResolver;
		}
		return result;
	}

	private IClassloaderLocationResolver getClassloaderResolver(String scheme) {
		IClassloaderLocationResolver result = classloaderResolvers.get(scheme);
		if (result == null) {
			Matcher m = splitScheme.matcher(scheme);
			if (m.find()) {
				String subScheme = m.group(1);
				result = classloaderResolvers.get(subScheme);
				if (result != null) {
					return result;
				}
			}
			return fallbackClassloaderResolver;
		}
		return result;
	}

	private ISourceLocationOutput getOutputResolver(String scheme) {
		ISourceLocationOutput result = outputResolvers.get(scheme);
		if (result == null) {
			Matcher m = splitScheme.matcher(scheme);
			if (m.find()) {
				String subScheme = m.group(1);
				result = outputResolvers.get(subScheme);
				if (result != null) {
					return result;
				}
			}
			return fallbackOutputResolver;
		}
		return result;
	}

	public boolean supportsHost(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());
		if (resolver == null) {
			ISourceLocationOutput resolverOther = getOutputResolver(uri.getScheme());
			if (resolverOther == null) {
				return false;
			}
			return resolverOther.supportsHost();
		}
		return resolver.supportsHost();
	}

	public boolean supportsReadableFileChannel(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());
		if (resolver == null) {
			return false;
		}
		return resolver.supportsReadableFileChannel();
	}

	public boolean supportsWritableFileChannel(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());
		if (resolver == null) {
			return false;
		}
		return resolver.supportsWritableFileChannel();
	}

	public boolean exists(ISourceLocation uri) {
		uri = safeResolve(uri);

		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			return false;
		}

		return resolver.exists(uri);
	}

	/**
	 * set the last modification date of a file
	 * 
	 * @param timestamp in millis since the epoch
	 * @throws IOException
	 */
	public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
		uri = safeResolve(uri);

		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());

		if (resolver == null) {
			throw new FileNotFoundException(uri.toString());
		}

		resolver.setLastModified(uri, timestamp);
	}

	public boolean isDirectory(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			return false;
		}
		return resolver.isDirectory(uri);
	}

	public void mkDirectory(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		mkParentDir(uri);

		resolver.mkDirectory(uri);
		notifyWatcher(URIUtil.getParentLocation(uri), ISourceLocationWatcher.created(uri));
	}

	private void notifyWatcher(ISourceLocation key, ISourceLocationChanged event) {
		watchers.notifySimulatedWatchers(key, event);
	}

	public void remove(ISourceLocation uri, boolean recursive) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationOutput out = getOutputResolver(uri.getScheme());

		if (out == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		// we need to keep it for the notifyWatcher call after removing
		var isDir = isDirectory(uri);
		if (isDir) {
			if (recursive) {
				for (ISourceLocation element : list(uri)) {
					remove(element, recursive);
				}
			}
			else if (listEntries(uri).length != 0) {
				throw new IOException("directory is not empty " + uri);
			}
		}

		out.remove(uri);
		notifyWatcher(uri, ISourceLocationWatcher.deleted(uri));
	}

	/**
	 * Moves a file from source name to target name. If the source is a folder, then it is moved recursively.
	 * 
	 * @param from       existing name of file or folder
	 * @param to         new name of file or folder
	 * @param overwrite  if `false` and the target folder or file already exists, throw an exception
	 * @throws IOException when the source can not be read or the target can not be written, or when the target
	 * exists and overwrite was `false`.
	 */
	public void rename(ISourceLocation from, ISourceLocation to, boolean overwrite) throws IOException {
		from = safeResolve(from);
		to = safeResolve(to);

		if (from.getScheme().equals(to.getScheme())) {
			ISourceLocationOutput out = getOutputResolver(from.getScheme());

			if (out == null) {
				throw new UnsupportedSchemeException(from.getScheme());
			}

			out.rename(from, to, overwrite);
		}
		else {
			copy(from, to, true, overwrite);
			remove(from, true);
		}
	}

	public boolean isFile(ISourceLocation uri) {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			return false;
		}
		return resolver.isFile(uri);
	}

	public long lastModified(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		long result = resolver.lastModified(uri);

		// implementations are allowed to return 0L or throw FileNotFound, but
		// here we iron it out:
		if (result == 0L) {
			throw new FileNotFoundException(uri.toString());
		}

		return result;
	}

	public long created(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		long result = resolver.created(uri);

		// implementations are allowed to return 0L or throw FileNotFound, but
		// here we iron it out:
		if (result == 0L) {
			throw new FileNotFoundException(uri.toString());
		}

		return result;
	}

	public boolean isWritable(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		var resolver = getOutputResolver(uri.getScheme());
		if (resolver != null) {
			return resolver.isWritable(uri);
		}
		// for writeable schemes we return false unless the file does not exist
		if (!exists(uri)) {
			throw new FileNotFoundException(uri.toString());
		}
		return false;
	}
	public boolean isReadable(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		var resolver = getInputResolver(uri.getScheme());
		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}
		return resolver.isReadable(uri);
	}

	/**
	 * This is byte size, and should not be exposed to the rascal users. 
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public long size(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		return resolver.size(uri);
	}

	private boolean isRootLogical(ISourceLocation uri) {
		return uri.getAuthority().isEmpty() && uri.getPath().equals("/")
			&& logicalResolvers.containsKey(uri.getScheme());
	}

	public String[] listEntries(ISourceLocation uri) throws IOException {		uri = safeResolve(uri);
		if (isRootLogical(uri)) {
			// if it's a location without any path and authority
			// we want to list possible authorities if it's a logical one
			// (logical resolvers cannot handle this call themselves)
			Map<String, ILogicalSourceLocationResolver> candidates = logicalResolvers.get(uri.getScheme());
			if (candidates != null) {
				return candidates.keySet().toArray(new String[0]);
			}
		}
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		String[] results = resolver.list(uri);
		if (results == null) {
			throw new FileNotFoundException(uri.toString());
		}

		return results;
	}

	/**
	 * Copies a file or directory to another location
	 * @param source     the source to read
	 * @param target     the target to write
	 * @param recursive  if `true` directories will be copied recursively
	 * @param overwrite  if `false` an IOException will be thrown when a target file or folder already exists
	 * @throws IOException when overwrite is false and the target already exists or when a file or folder can not be created or
	 * when a source folder or file can not be read
	 */
	public void copy(ISourceLocation source, ISourceLocation target, boolean recursive, boolean overwrite) throws IOException {
		var sourceResolved = safeResolve(source);
		var targetResolved = safeResolve(target);
		if (sourceResolved.getScheme().equals(targetResolved.getScheme())) {
			var commonResolver = getOutputResolver(sourceResolved.getScheme());
			if (commonResolver != null && commonResolver.supportsCopy()) {
				commonResolver.copy(sourceResolved, targetResolved, recursive, overwrite);
				return;
			}
		}
		if (isFile(sourceResolved)) {
			copyFile(source, target, overwrite);
		}
		else {
			if (exists(targetResolved) && !isDirectory(targetResolved)) {
				if (overwrite) {
					remove(targetResolved, false);
				}
				else {
					throw new IOException("can not make directory because file exists: " + target);
				}
			}
			
			mkDirectory(targetResolved);

			for (String elem : URIResolverRegistry.getInstance().listEntries(sourceResolved)) {
				ISourceLocation srcChild = URIUtil.getChildLocation(sourceResolved, elem);
				ISourceLocation targetChild = URIUtil.getChildLocation(targetResolved, elem);

				if (isFile(srcChild) || recursive) {
					copy(srcChild, targetChild, recursive, overwrite);
				}
				else {
					// make the directory but the recursion stops
					mkDirectory(targetChild);
				}
			}
		}
	}

	private void copyFile(ISourceLocation source, ISourceLocation target, boolean overwrite) throws IOException {
		if (exists(target) && !overwrite) {
			throw new IOException("file exists " + source);
		}

		if (exists(target) && overwrite) {
			remove(target, false);
		}
		
		if (supportsReadableFileChannel(source) && supportsWritableFileChannel(target) && size(source) > 8*1024) {
			try (FileChannel from = getReadableFileChannel(source)) {
				try (FileChannel to = getWriteableFileChannel(target, false)) {
					long transferred = 0;
					while (transferred < from.size()) {
						transferred += from.transferTo(transferred, from.size() - transferred, to);
					}
				}
			}
			return;
		}

		try (InputStream from = getInputStream(source)) {
			try (OutputStream to = getOutputStream(target, false)) {
				from.transferTo(to);
			}
		}
	}

	public ISourceLocation[] list(ISourceLocation uri) throws IOException {
		String[] entries = listEntries(uri);

		if (entries == null) {
			return new ISourceLocation[0];
		}

		ISourceLocation[] list = new ISourceLocation[entries.length];
		int i = 0;
		for (String entry : entries) {
			list[i++] = URIUtil.getChildLocation(uri, entry);
		}
		return list;
	}


	public Reader getCharacterReader(ISourceLocation uri) throws IOException {
		return getCharacterReader(uri, getCharset(uri));
	}

	public Reader getCharacterReader(ISourceLocation uri, String encoding) throws IOException {
		return getCharacterReader(uri, Charset.forName(encoding));
	}

	public Reader getCharacterReader(ISourceLocation uri, Charset encoding) throws IOException {
		uri = safeResolve(uri);
		Reader res = new UnicodeInputStreamReader(getInputStream(uri), encoding);

		if (uri.hasOffsetLength()) {
			return new UnicodeOffsetLengthReader(res, uri.getOffset(), uri.getLength());
		}
		else {
			return res;
		}
	}

	/**
	 * Return a character Writer for the given uri, using the given character encoding.
	 * 
	 * @param uri       file to write to or append to
	 * @param encoding  how to encode individual characters @see Charset
	 * @param append    whether to append or start at the beginning.
	 * @return
	 * @throws IOException 
	 */
	public Writer getCharacterWriter(ISourceLocation uri, String encoding, boolean append) throws IOException {
		uri = safeResolve(uri);
		return new UnicodeOutputStreamWriter(getOutputStream(uri, append), encoding);
	}

	public ClassLoader getClassLoader(ISourceLocation uri, ClassLoader parent) throws IOException {
		IClassloaderLocationResolver resolver = getClassloaderResolver(safeResolve(uri).getScheme());

		if (resolver != null) {
			// we always try the most specific implementation for efficiency's sake
			return resolver.getClassLoader(uri, parent);
		}
		else {
			// the generic class loader can always produces the byte[] of any class file
			return new GenericSourceLocationClassLoader(uri, parent);
		}
	}


	/**
	 * Generic implementation of a ClassLoader that uses the registry's ability
	 * to open an InputStream for any existing location. It is much fast if a {@see IClassloaderLocationResolver}
	 * exists for any scheme, since that could produce an index a URLClassLoader instance.
	 */
	private class GenericSourceLocationClassLoader extends ClassLoader {
		private final ISourceLocation root;

		public GenericSourceLocationClassLoader(ISourceLocation root, ClassLoader parent) {
			super(parent);
			this.root = root;
		}

		@Override
		protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
			var file = URIUtil.getChildLocation(root, qualifiedClassName.replaceAll("\\.", "/") + ".class");

			if (exists(file)) {
				try {
					byte[] bytes = Prelude.consumeInputStream(getInputStream(file));
					return defineClass(qualifiedClassName, bytes, 0, bytes.length);
				}
				catch (IOException e) {
					// fall through
				}
			}
			// Workaround for "feature" in Java 6
			// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
			try {
				Class<?> c = Class.forName(qualifiedClassName);
				return c;
			} catch (ClassNotFoundException nf) {
				// Ignore and fall through
			}

			return super.findClass(qualifiedClassName);
		}
	}


	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		return makeBuffered(resolver.getInputStream(uri));
	}

	public FileChannel getReadableFileChannel(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null || !resolver.supportsReadableFileChannel()) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		return resolver.getReadableFileChannel(uri);
	}

	public Charset detectCharset(ISourceLocation sloc) {
		URIResolverRegistry reg = URIResolverRegistry.getInstance();
		
		// in case the file already has a encoding, we have to correctly append that.
		Charset detected = null;
		try (InputStream in = reg.getInputStream(sloc);) {
			detected = reg.getCharset(sloc);

			if (detected == null) {
				detected = UnicodeDetector.estimateCharset(in);
			}
		}
		catch (IOException e) {
			// we stick with the default if something happened above.
			// if the writing hereafter fails as well, the exception will
			// be just as descriptive
			detected = null; 
		} 

		return detected != null ? Charset.forName(detected.name()) : Charset.defaultCharset();
	}

	public Charset getCharset(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationInput resolver = getInputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		return resolver.getCharset(uri);
	}

	public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
		uri = safeResolve(uri);
		boolean existedBefore = exists(uri);
		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());

		if (resolver == null) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		if (uri.getPath() != null && uri.getPath().startsWith("/..")) {
			throw new IllegalArgumentException("Can not navigate beyond the root of a URI: " + uri);
		}

		mkParentDir(uri);

		return makeBuffered(uri, existedBefore, resolver.getOutputStream(uri, append));
	}

	public FileChannel getWriteableFileChannel(ISourceLocation uri, boolean append) throws IOException {
		uri = safeResolve(uri);
		ISourceLocationOutput resolver = getOutputResolver(uri.getScheme());

		if (resolver == null || !resolver.supportsWritableFileChannel()) {
			throw new UnsupportedSchemeException(uri.getScheme());
		}

		if (uri.getPath() != null && uri.getPath().startsWith("/..")) {
			throw new IllegalArgumentException("Can not navigate beyond the root of a URI: " + uri);
		}

		mkParentDir(uri);

		// It is assumed that if writeable file channels are supported for a given scheme,
		// that also a watcher is registered for the given stream, so we do not have to
		// notify any watchers ourselves
		return resolver.getWritableOutputStream(uri, append);
	}

	private void mkParentDir(ISourceLocation uri) throws IOException {
		uri = safeResolve(uri);
		ISourceLocation parentURI = URIUtil.getParentLocation(uri);

		if (parentURI != null && !parentURI.equals(uri) && !exists(parentURI)) {
			mkDirectory(parentURI);
		}
	}

	public void watch(ISourceLocation loc, boolean recursive, final Consumer<ISourceLocationChanged> callback) throws IOException {
		watchers.watch(loc, recursive, this::anyIOResolverRegistered, callback);
	}

	/**
	 * Assumes already resolve location
	 */
	private boolean anyIOResolverRegistered(ISourceLocation loc) {
		return inputResolvers.containsKey(loc.getScheme()) || outputResolvers.containsKey(loc.getScheme());
	}


	public void unwatch(ISourceLocation loc, boolean recursive, Consumer<ISourceLocationChanged> callback)
		throws IOException {
		watchers.unwatch(loc, recursive, this::anyIOResolverRegistered, callback);
	}

	// these types must align with their correspondig types in IO.rsc
	private final TypeFactory tf = TypeFactory.getInstance();
	private final TypeStore capabilitiesStore = new TypeStore();
	private final Type IOcapability = tf.abstractDataType(capabilitiesStore, "IOCapability");
	private final Type readCap = tf.constructor(capabilitiesStore, IOcapability, "reading");
	private final Type writeCap = tf.constructor(capabilitiesStore, IOcapability, "writing");
	private final Type loadCap = tf.constructor(capabilitiesStore, IOcapability, "classloading");
	private final Type logicalCap = tf.constructor(capabilitiesStore, IOcapability, "resolving");
	private final Type watchCap = tf.constructor(capabilitiesStore, IOcapability, "watching");

	public ISet capabilities(ISourceLocation loc) {
		var vf = IRascalValueFactory.getInstance();
		var scheme = loc.getScheme();
		ISetWriter result = vf.setWriter();

		if (logicalResolvers.containsKey(scheme)) {
			result.insert(vf.constructor(logicalCap));
			var resolved = safeResolve(loc);

			if (resolved != loc) {
				result.insertAll(capabilities(resolved));
			}
		}

		if (inputResolvers.containsKey(scheme)) {
			result.insert(vf.constructor(readCap));
		}

		if (outputResolvers.containsKey(scheme)) {
			result.insert(vf.constructor(writeCap));
		}

		if (classloaderResolvers.containsKey(scheme)) {
			result.insert(vf.constructor(loadCap));
		}

		if (watchers.hasNativeSupport(scheme)) {
			result.insert(vf.constructor(watchCap));
		}
	
		return result.done();
	}

	public boolean hasReadableResolver(ISourceLocation loc) {
		return inputResolvers.containsKey(loc.getScheme()) || inputResolvers.containsKey(safeResolve(loc).getScheme());
	}

	public boolean hasWritableResolver(ISourceLocation loc) {
		return outputResolvers.containsKey(loc.getScheme()) || outputResolvers.containsKey(safeResolve(loc).getScheme());
	}

	public boolean hasEfficientlyClassloadableResolver(ISourceLocation loc) {
		return classloaderResolvers.containsKey(loc.getScheme()) || classloaderResolvers.containsKey(safeResolve(loc).getScheme());
	}

	public boolean hasLogicalResolver(ISourceLocation loc) {
		return logicalResolvers.containsKey(loc.getScheme());
	}

	public boolean hasNativelyWatchableResolver(ISourceLocation loc) {
		return watchers.hasNativeSupport(loc.getScheme()) || watchers.hasNativeSupport(safeResolve(loc).getScheme()) || watchers.hasFallback();
	}

	public FileAttributes stat(ISourceLocation loc) throws IOException {
		loc = safeResolve(loc);
		var resolver = getInputResolver(loc.getScheme());
		if (resolver == null) {
			throw new IOException("Unsupported scheme: " + loc.getScheme());
		}
		try {
			return resolver.stat(loc);
		} catch (FileNotFoundException fe) {
			return new FileAttributes(false, false, -1,-1, false, false, 0);
		}
	}

}
