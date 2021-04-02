/** 
 * Copyright (c) 2019, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.uri.libraries;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;
import org.rascalmpl.uri.classloaders.SourceLocationClassLoader;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

/**
 * The goal of this resolver is to provide |lib://&ltlibName&gt/| for every Rascal library available in the current run-time environment.
 * To do this, it searches for META-INF/RASCAL.MF files in 2 places, and checks if the Project-Name inside of that file is equal to &ltlibName&gt:
 * <ul>
 *   <li>|plugin://&ltlibName&gt| is probed first, in order to give precedence to plugins loaded by application containers such as OSGI;</li>
 *   <li>Finally ClassLoader.getResources is probed to resolve to |jar+file://path-to-jar-on-classpath!/| if a RASCAL.MF can be found there with the proper Project-Name in it. So this only searches in the JVM start-up classpath via its URLClassLoaders, ignoring plugin mechanisms like OSGI and the like.</li>
 * </ul>  
 * <p>CAVEAT 1: this resolver caches the first resolution (plugin or jarfile) and does not rescind it afterwards even if the locations
 * cease to exist. This might happen due to plugin unloading. To re-initialize an
 * already resolved |lib://&ltlibName&gt| path, either the JVM must be reloaded (restart the IDE) or the current class must be reloaded 
 * (restart the plugin which loaded the Rascal run-time). TODO FIXME by allowing re-initialization of this entire resolver by the URIResolverRegistry.</p>
 * <p>CAVEAT 2: this resolver drops the query and offset/length components of the incoming source locations. TODO FIXME</p>
 * <p>CAVEAT 3: it is up to the respective run-time environments (Eclipse, OSGI, MVN, Spring, etc.) to provide the respective implementations
 * of ISourceLocation input for the plugin:// scheme. If it is not provided, this resolver only resolves to resources
 * which can be found via the System classloader.</p>
 */
public class RascalLibraryURIResolver implements ISourceLocationInput, IClassloaderLocationResolver {
    private final ConcurrentHashMap<String, ISourceLocation> classpathLibraries = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ISourceLocation> resolvedLibraries = new ConcurrentHashMap<>();
    private final URIResolverRegistry reg;
    
    public RascalLibraryURIResolver(URIResolverRegistry reg) {
        this.reg = reg;
        
        try {
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            RascalManifest mf = new RascalManifest();
            Enumeration<URL> mfs = getClass().getClassLoader().getResources(RascalManifest.META_INF_RASCAL_MF);

            Collections.list(mfs).forEach(url -> {
                try {
                    String libName = mf.getProjectName(url.openStream());
                    
                    if (libName != null && !libName.isEmpty()) {
                        ISourceLocation loc;

                        if (url.getProtocol().equals("jar") && url.getPath().startsWith("file:/")) {
                            loc = vf.sourceLocation("jar+file", null, url.getPath().substring("file:".length()));
                        }
                        else {
                            loc = vf.sourceLocation(URIUtil.fromURL(url));
                        }

                        loc = URIUtil.changePath(loc, loc.getPath().replace(RascalManifest.META_INF_RASCAL_MF, ""));

                        registerLibrary("detected", classpathLibraries, libName, loc);
                    }
                }
                catch (IOException | URISyntaxException e) {
                    System.err.println("WARNING: could not load Rascal manifest for library resolution of: " + url);
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e) {
            System.err.println("WARNING: could not resolve any Rascal library locations");
            e.printStackTrace();
        }
    }

    private void registerLibrary(String event, ConcurrentHashMap<String, ISourceLocation> libs, String libName, ISourceLocation loc) {
        System.err.println("INFO: " + event + " |lib://" + libName + "| at " + loc);
        libs.merge(libName, loc, (o, n) -> n);
    }
    
    /**
     * Resolve a lib location to either a plugin or a local classpath location, in that order of precedence.
     */
    private @Nullable ISourceLocation resolve(ISourceLocation uri) {
        String libName = uri.getAuthority();
        
        if (libName == null || libName.isEmpty()) {
            return null;
        }

        // if we resolved this library before, we stick with that initial resolution for efficiency's sake
        ISourceLocation resolved = resolvedLibraries.get(libName);
        if (resolved != null) {
            return URIUtil.getChildLocation(resolved, uri.getPath());
        }
        
        // then we try plugin libraries, taking precedence over classpath libraries
        ISourceLocation plugin = deferToScheme(uri, "plugin");
        if (plugin != null) {
            return plugin;
        }

        // finally we try the classpath libraries
        ISourceLocation classpath = classpathLibraries.get(libName);
        if (classpath != null) {
            return resolvedLocation(uri, libName, classpath);
        }
        
        return null;
    }
    
    /**
     * Tries to find a RASCAL.MF file in the deferred scheme's root and if it's present, the
     * prefix is cached and the child location is returned.
     */
    private ISourceLocation deferToScheme(ISourceLocation uri, String scheme) {
        String libName = uri.getAuthority();
        ISourceLocation libRoot = URIUtil.correctLocation(scheme, libName, "");

        if (isValidLibraryRoot(libRoot)) {
            return resolvedLocation(uri, libName, libRoot);
        }
        else {
            return null;
        }
    }

    /**
     * Check if this root contains a valid RASCAL.MF file
     */
    private boolean isValidLibraryRoot(ISourceLocation libRoot) {
        if (reg.exists(URIUtil.getChildLocation(libRoot, RascalManifest.META_INF_RASCAL_MF))) {
            assert new RascalManifest().getProjectName(libRoot).equals(libRoot.getAuthority()) 
                 : "Project-Name in RASCAL.MF does not align with authority of the " + libRoot.getScheme() + " scheme";
            return true;
        }
        
        return false;
    }

    /**
     * compute the resolved child location and cache the prefix as a side-effect for a future fast path
     */
    private ISourceLocation resolvedLocation(ISourceLocation uri, String libName, ISourceLocation deferredLoc) {
        registerLibrary("resolved", resolvedLibraries, libName, deferredLoc);
        return URIUtil.getChildLocation(deferredLoc, uri.getPath());
    }

    /**
     * Resolve a location and if not possible throw an exception
     */
    private ISourceLocation safeResolve(ISourceLocation uri) throws IOException {
        ISourceLocation resolved = resolve(uri);
        if (resolved == null) {
            throw new IOException("lib:// resolver could not resolve " + uri);
        }
        return resolved;
    }
    
    @Override
    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        return reg.getInputStream(safeResolve(uri));
    }

    @Override
    public Charset getCharset(ISourceLocation uri) throws IOException {
        return reg.getCharset(safeResolve(uri));
    }

    @Override
    public boolean exists(ISourceLocation uri) {
        ISourceLocation resolved = resolve(uri);
        if (resolved == null) {
            return false;
        }
        return reg.exists(resolved);
    }

    @Override
    public long lastModified(ISourceLocation uri) throws IOException {
        return reg.lastModified(safeResolve(uri));
    }

    @Override
    public boolean isDirectory(ISourceLocation uri) {
        try {
            return URIResolverRegistry.getInstance().isDirectory(safeResolve(uri));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean isFile(ISourceLocation uri) {
        try {
            return URIResolverRegistry.getInstance().isFile(safeResolve(uri));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        return reg.listEntries(safeResolve(uri));
    }

    @Override
    public String scheme() {
        return "lib";
    }

    @Override
    public boolean supportsHost() {
        return false;
    }

    @Override
    public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
        ISourceLocation resolved = resolve(loc);
        if (resolved != null) {
            return reg.getClassLoader(resolved, parent);
        }
        
        throw new IOException("Can not resolve classloader for " + loc);
    }
}
