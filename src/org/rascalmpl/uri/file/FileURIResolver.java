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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import org.rascalmpl.uri.BadURIException;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

import io.usethesource.vallang.ISourceLocation;

public class FileURIResolver implements ISourceLocationInputOutput, IClassloaderLocationResolver, ISourceLocationWatcher {
	private final Map<ISourceLocation, Set<Consumer<ISourceLocationChanged>>> watchers = new ConcurrentHashMap<>();
	private final Map<WatchKey, ISourceLocation> watchKeys = new ConcurrentHashMap<>();
	private final WatchService watcher;
	
	public FileURIResolver() throws IOException {
		super();
		this.watcher = FileSystems.getDefault().newWatchService();
		createFileWatchingThread();
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

	@Override
	public void rename(ISourceLocation from, ISourceLocation to, boolean overwrite) throws IOException {
		if (isFile(from) || (isDirectory(from) && list(from).length == 0)) {
			if (exists(to) && !overwrite) {
				throw new IOException("file exists " + to);
			}

			// fast path
			if (!new File(getPath(from)).renameTo(new File(getPath(to)))) {
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
	public long created(ISourceLocation uri) throws IOException {
		BasicFileAttributeView basicfile = Files.getFileAttributeView(Paths.get(getPath(uri)), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = basicfile.readAttributes() ;
        return attr.creationTime().toMillis();
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

        // Commented out during PR draft; this makes no sense. Is a test dependent on this?
	// @Override
	// public Charset getCharset(ISourceLocation uri) throws IOException {
	// 	return null;
	// }
	
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
		watchers.computeIfAbsent(root, k -> ConcurrentHashMap.newKeySet()).add(callback);

		WatchKey key = Paths.get(root.getURI()).register(watcher,
			StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY
		);

		synchronized(watchKeys) {
			// to avoid races with an unwatch, we lock here
			watchKeys.put(key, root);
		}
	}

	@Override
	public void unwatch(ISourceLocation root, Consumer<ISourceLocationChanged> callback) throws IOException {
		Set<Consumer<ISourceLocationChanged>> registered = watchers.get(root);
		if (registered == null || !registered.remove(callback)) {
			return;
		}
		if (registered.isEmpty()) {
			// we have to try and clear the relavant watchkey
			// but sadly this is a full loop
			for (Entry<WatchKey, ISourceLocation> e: watchKeys.entrySet()) {
				if (e.getValue().equals(root)) {
					synchronized(watchKeys) {
						// avoiding races with watch, we lock on the map remove
						if (registered.isEmpty()) {// check again if we truly aren't replacing a key that should still be active
							WatchKey k = e.getKey();
							k.cancel();
							watchKeys.remove(k);
						}
					}
					break;
				}
			}
		}
	}

	private void createFileWatchingThread() {
		// make a threadpool of daemon threads, to avoid this pool keeping the process running
		ExecutorService exec = Executors.newCachedThreadPool(new ThreadFactory() {
			public Thread newThread(Runnable r) {
            	SecurityManager s = System.getSecurityManager();
            	ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
				Thread t = new Thread(group, r, "file:/// watcher thread-pool");
				t.setDaemon(true);
				return t;
			}
		});

		exec.execute(() -> {
			while (true) {
				// wait for key to be signaled
				try {
					WatchKey key = watcher.take();
					try {
						ISourceLocation root = watchKeys.get(key);
						if (root == null) {
							continue; // key not in the map anymore/yet?
						}
						for (WatchEvent<?> event: key.pollEvents()) {
							WatchEvent.Kind<?> kind = event.kind();
							
							if (kind == StandardWatchEventKinds.OVERFLOW) {
								continue;
							}
							
							Set<Consumer<ISourceLocationChanged>> callbacks = watchers.get(root);
							if (callbacks != null) {
								@SuppressWarnings("unchecked")
								Path filename = ((WatchEvent<Path>)event).context();
								ISourceLocation subject = URIUtil.getChildLocation(root, filename.toString());

								ISourceLocationChanged change = calculateChange(key, root, kind, subject);
								if (change != null) {
									for (Consumer<ISourceLocationChanged> c: callbacks) {
										exec.execute(() -> c.accept(change));
									}
								}
							}
						}

					} finally {
						if (!key.reset()) {
							watchKeys.remove(key);
						}
					}

				} catch (InterruptedException e ){
					return;
				} catch (Throwable x) {
					System.err.println("Error handling callback in FileURIResolver: " + x.getMessage());
					x.printStackTrace(System.err);
					continue;
				}
			}
		});
	}


	private ISourceLocationChanged calculateChange(WatchKey key, ISourceLocation root, WatchEvent.Kind<?> kind,
		ISourceLocation subject) {
		boolean isDirectory = URIResolverRegistry.getInstance().isDirectory(subject);
		switch (kind.name()) {
			case "ENTRY_CREATE":
				return ISourceLocationWatcher.makeChange(
					subject, 
					ISourceLocationChangeType.CREATED, 
					isDirectory ? ISourceLocationType.DIRECTORY : ISourceLocationType.FILE);
			case "ENTRY_DELETE":

				watchers.remove(subject);
				if (root.equals(subject)) {
					watchKeys.remove(key);
				}

				return ISourceLocationWatcher.makeChange(
					subject, 
					ISourceLocationChangeType.DELETED, 
					isDirectory ? ISourceLocationType.DIRECTORY : ISourceLocationType.FILE);

			case "ENTRY_MODIFY":
				return ISourceLocationWatcher.makeChange(
					subject, 
					ISourceLocationChangeType.MODIFIED, 
					isDirectory ? ISourceLocationType.DIRECTORY : ISourceLocationType.FILE)
				;
			case "OVERFLOW": //ignored
			default:
				return null;
		}
	}
}
