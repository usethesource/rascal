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
import java.nio.file.FileSystems;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.rascalmpl.uri.BadURIException;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

import io.usethesource.vallang.ISourceLocation;

public class FileURIResolver implements ISourceLocationInputOutput, IClassloaderLocationResolver, ISourceLocationWatcher {
	private final Map<Path, Consumer<ISourceLocationChanged>> watchers = new ConcurrentHashMap<>();
	private final WatchService watcher;
	
	public FileURIResolver() throws IOException {
		super();
		this.watcher = FileSystems.getDefault().newWatchService();
		createFileWatchingThread();
	}
	
	private void createFileWatchingThread() {
		Thread thread = new Thread("file:/// watcher") {
			public void run() {
				while (true) {
					
					// wait for key to be signaled
					WatchKey key;
					try {
						key = watcher.take();
						
						for (WatchEvent<?> event: key.pollEvents()) {
							WatchEvent.Kind<?> kind = event.kind();
							
							if (kind == StandardWatchEventKinds.OVERFLOW) {
								continue;
							}
							
							@SuppressWarnings("unchecked")
							WatchEvent<Path> ev = (WatchEvent<Path>) event;
							Path filename = ev.context();
							
							Consumer<ISourceLocationChanged> callback = watchers.get(filename);

							if (callback != null) {
								ISourceLocation loc = constructFileURI(filename.toAbsolutePath().toString());
								boolean isDirectory = filename.toFile().isDirectory();

								switch (kind.name()) {
									case "ENTRY_CREATE":
										callback.accept(ISourceLocationWatcher.makeChange(
											loc, 
											ISourceLocationChangeType.CREATED, 
											isDirectory ? ISourceLocationType.DIRECTORY : ISourceLocationType.FILE)
										);
										break;
									case "ENTRY_DELETE":
										callback.accept(ISourceLocationWatcher.makeChange(
											loc, 
											ISourceLocationChangeType.DELETED, 
											isDirectory ? ISourceLocationType.DIRECTORY : ISourceLocationType.FILE)
										);
										break;
									case "ENTRY_MODIFY":
										callback.accept(ISourceLocationWatcher.makeChange(
											loc, 
											ISourceLocationChangeType.MODIFIED, 
											isDirectory ? ISourceLocationType.DIRECTORY : ISourceLocationType.FILE)
										);
										break;
									case "OVERFLOW":
									// ignored?
								}
							}
						}
						
						if (key.reset()) {
							break; // we can go to the next key now
						}
					} catch (InterruptedException x) {
						return;
					}
				}
			};
		};
		
		thread.setDaemon(true);
		thread.setName("Watching files for the file:/// scheme");
		thread.start();
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
		new File(getPath(uri)).setLastModified(timestamp);
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
		new File(getPath(uri)).delete();
	} 
	
	public String scheme() {
		return "file";
	}
	
	public boolean exists(ISourceLocation uri) {
		return new File(getPath(uri)).exists();
	}
	
	/**
	* To override to build resolvers to specific locations using a prefix for example.
	*/
	protected String getPath(ISourceLocation uri) {
		return uri.getPath();
	}
	
	public boolean isDirectory(ISourceLocation uri) {
		return new File(getPath(uri)).isDirectory();
	}
	
	public boolean isFile(ISourceLocation uri) {
		return new File(getPath(uri)).isFile();
	}
	
	public long lastModified(ISourceLocation uri) {
		return new File(getPath(uri)).lastModified();
	}
	
	@Override
	public String[] list(ISourceLocation uri) {
		return new File(getPath(uri)).list();
	}
	
	public void mkDirectory(ISourceLocation uri) {
		new File(getPath(uri)).mkdirs();
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
			return FileChannel.open(new File(path).toPath(), StandardOpenOption.READ);
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
				options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ };
			}
			return FileChannel.open(new File(path).toPath(), options);
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	public void watch(ISourceLocation root, Consumer<ISourceLocationChanged> callback) throws IOException {
		watchers.putIfAbsent(Paths.get(root.getURI()), callback);

		Paths.get(root.getURI()).register(
			watcher, 
			StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY
		);
	}
}
