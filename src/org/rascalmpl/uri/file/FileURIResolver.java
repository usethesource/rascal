/*******************************************************************************
* Copyright (c) 2009-2017 CWI
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:

*   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*   * Paul Klint - Paul.Klint@cwi.nl - CWI
*   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.uri.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.rascalmpl.uri.BadURIException;
import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

import engineering.swat.watch.ActiveWatch;
import engineering.swat.watch.Approximation;
import engineering.swat.watch.Watch;
import engineering.swat.watch.WatchEvent;
import engineering.swat.watch.WatchScope;
import io.usethesource.vallang.ISourceLocation;

public class FileURIResolver implements ISourceLocationInputOutput, IClassloaderLocationResolver, ISourceLocationWatcher {
	private final Map<WatchId, ActiveWatch> watchers = new ConcurrentHashMap<>();
	
	public FileURIResolver() throws IOException {
		super();
	}
	
	
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			return new FileInputStream(path);
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	/**
	* Returns a URL classloader for the jar file or directory pointed to by the loc parameter.
	*/
	public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
		assert loc.getScheme().equals(scheme());
		String path = loc.getPath();
		
		if (isDirectory(loc) && !path.endsWith("/")) {
			path += "/"; // the URL class loader assumes directories end with a /
		}
		
		if (!isDirectory(loc) && !path.endsWith(".jar")) {
			// dictated by URLClassLoader semantics
			throw new IOException("Can only provide classloaders for directories or jar files, not for " + loc);
		} 
		
		return new URLClassLoader(new URL[] { new File(path).toURI().toURL() }, parent);
	}
	
	@Override
	public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
		resolveToFile(uri).setLastModified(timestamp);
	}
	
	public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			return new BufferedOutputStream(new FileOutputStream(getPath(uri), append));
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
		resolveToFile(uri).delete();
	} 

	@Override
	public void rename(ISourceLocation from, ISourceLocation to, boolean overwrite) throws IOException {
		if (isFile(from) || (isDirectory(from) && list(from).length == 0)) {
			if (exists(to) && !overwrite) {
				throw new IOException("file exists " + to);
			}

			// fast path
			if (!resolveToFile(from).renameTo(resolveToFile(to))) {
				throw new IOException("rename failed: " + from + " to " + to);
			}
		}
		else {
			ISourceLocationInputOutput.super.rename(from, to, overwrite);
		}
	}
	
	public String scheme() {
		return "file";
	}
	
	public boolean exists(ISourceLocation uri) {
		return resolveToFile(uri).exists();
	}
	
	/**
	* To override to build resolvers to specific locations using a prefix for example.
	*/
	protected String getPath(ISourceLocation uri) {
		assert !uri.hasAuthority();
		return uri.getPath();
	}
	
	public boolean isDirectory(ISourceLocation uri) {
		return resolveToFile(uri).isDirectory();
	}
	
	public boolean isFile(ISourceLocation uri) {
		return resolveToFile(uri).isFile();
	}
	
	public long lastModified(ISourceLocation uri) {
		return resolveToFile(uri).lastModified();
	}

	@Override
	public long created(ISourceLocation uri) throws IOException {
		BasicFileAttributeView basicfile = Files.getFileAttributeView(resolveToFile(uri).toPath(), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = basicfile.readAttributes() ;
        return attr.creationTime().toMillis();
	}


	@Override
	public boolean isWritable(ISourceLocation uri) throws IOException {
		return Files.isWritable(resolveToFile(uri).toPath());
	}

	@Override
	public long size(ISourceLocation uri) throws IOException {
		return resolveToFile(uri).length();
	}

	@Override
	public FileAttributes stat(ISourceLocation loc) throws IOException {
		var file = resolveToFile(loc).toPath();
		var attrs = Files.readAttributes(file, BasicFileAttributes.class);
		return new FileAttributes(true, 
			attrs.isRegularFile(),
			attrs.creationTime().toMillis(),
			attrs.lastModifiedTime().toMillis(),
			Files.isWritable(file),
			attrs.size()
		);
	}
	
	@Override
	public String[] list(ISourceLocation uri) {
		return resolveToFile(uri).list();
	}


	private File resolveToFile(ISourceLocation uri) {
		return new File(getPath(uri));
	}
	
	public void mkDirectory(ISourceLocation uri) {
		resolveToFile(uri).mkdirs();
	}
	
	/**
	* Utility function to create a URI from an absolute path.
	* 
	* @param path a platform-dependent string representation of this path
	* @return a file schema URI
	*/
	public static ISourceLocation constructFileURI(String path) {
		try{
			return URIUtil.createFileLocation(path);
		}catch(URISyntaxException usex){
			throw new BadURIException(usex);
		}
	}
	
	public boolean supportsHost() {
		return false;
	}

    
	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		// TODO: this requires an explanation. The docs
		// of the super implementation do not say anything about returning null.
		return null;
	}
	
	@Override
	public boolean supportsReadableFileChannel() {
		return true;
	}
	
	@Override
	public FileChannel getReadableFileChannel(ISourceLocation uri) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			return FileChannel.open(resolveToFile(uri).toPath(), StandardOpenOption.READ);
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	public boolean supportsWritableFileChannel() {
		return true;
	}
	
	@Override
	public FileChannel getWritableOutputStream(ISourceLocation uri, boolean append) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			OpenOption[] options;
			if (append) {
				options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.APPEND };
			}
			else {
				options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING };
			}
			return FileChannel.open(new File(path).toPath(), options);
		}
		throw new IOException("uri has no path: " + uri);
	}

	private final ExecutorService watcherPool = Executors.newCachedThreadPool((Runnable r) -> {
		SecurityManager s = System.getSecurityManager();
		ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		Thread t = new Thread(group, r, "file:/// watcher thread-pool");
		t.setDaemon(true);
		return t;
	});
	
	@Override
	public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> callback, boolean recursive) throws IOException {
		var rootFile = resolveToFile(root);

		WatchScope scope;
		if (rootFile.isFile()) {
			scope = WatchScope.PATH_ONLY;
		}
		else if (recursive) {
			scope = WatchScope.PATH_AND_ALL_DESCENDANTS;
		}
		else {
			scope = WatchScope.PATH_AND_CHILDREN;
		}

		var watch = Watch.build(rootFile.toPath(), scope)
			.onOverflow(Approximation.ALL) // on an overflow, generate events for all files
			.withExecutor(watcherPool)
			.on(e -> {
				var change = calculateChange(e, root);
				if (change != null) {
					watcherPool.submit(() -> callback.accept(change));
				}
			})
			.start();

		watchers.put(new WatchId(root, callback, recursive), watch);
	}

	@Override
	public boolean supportsRecursiveWatch() {
		return true;
	}

	@Override
	public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> callback, boolean recursive) throws IOException {
		var activeWatch = watchers.remove(new WatchId(root, callback, recursive));
		if (activeWatch != null) {
			activeWatch.close();
		}
	}

	private ISourceLocationChanged calculateChange(WatchEvent event, ISourceLocation root) {
		var subject = URIUtil.getChildLocation(root, event.getRelativePath().toString());
		switch (event.getKind()) {
			case CREATED:
				return ISourceLocationWatcher.created(subject);
			case DELETED:
				return ISourceLocationWatcher.deleted(subject);
			case MODIFIED:
				return ISourceLocationWatcher.modified(subject);
			case OVERFLOW:
			default:
				return null;
		}
	}


	private static class WatchId {
		final ISourceLocation root;
		final Consumer<ISourceLocationChanged> callback;
		final boolean recursive;
		public WatchId(ISourceLocation root, Consumer<ISourceLocationChanged> callback, boolean recursive) {
			this.root = root;
			this.callback = callback;
			this.recursive = recursive;
		}
		@Override
		public int hashCode() {
			return Objects.hash(root, callback, recursive);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof WatchId)) {
				return false;
			}
			WatchId other = (WatchId) obj;
			return Objects.equals(root, other.root) 
				&& Objects.equals(callback, other.callback)
				&& recursive == other.recursive;
		}
		
	}
}
