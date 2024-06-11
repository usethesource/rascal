package org.rascalmpl.interpreter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.jar.JarURIResolver;

import io.usethesource.vallang.ISourceLocation;

/**
 * The META-INF/RASCAL.MF file contains information about 
 *   - project configuration options, such as location of source code relative to the root
 *   - deployment configuration options, such as which is the main module and the main function to cal
 *   
 * This class is intended to be sub-classed for different build and execution environments, yet
 * all available options are intended to be retrieved from this class. Sub-classes will add mainly
 * information on where to find the META-INF/RASCAL.MF file.
 */
public class RascalManifest {
    public static final String DEFAULT_MAIN_MODULE = "Plugin";
    public static final String DEFAULT_MAIN_FUNCTION = "main";
    public static final String DEFAULT_SRC = "src";
    protected static final String SOURCE = "Source";
    protected static final String META_INF = "META-INF";
    public static final String META_INF_RASCAL_MF = META_INF + "/RASCAL.MF";
    protected static final String MAIN_MODULE = "Main-Module";
    protected static final String MAIN_FUNCTION = "Main-Function";
    protected static final String PROJECT_NAME = "Project-Name";
    protected static final String REQUIRE_BUNDLES = "Require-Bundles";
    protected static final String REQUIRE_LIBRARIES = "Require-Libraries";

    public static String getRascalVersionNumber() {
        try {
            Enumeration<URL> resources = RascalManifest.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                String bundleName = manifest.getMainAttributes().getValue("Name");
                if (bundleName != null && bundleName.equals("rascal")) {
                    String result = manifest.getMainAttributes().getValue("Specification-Version");
                    if (result != null) {
                        return result;
                    }
                }
            }

            return "Rascal version not specified in META-INF/MANIFEST.MF???";
        } catch (IOException e) {
            return "unknown (due to " + e.getMessage();
        }
    }

    public String getManifestVersionNumber(ISourceLocation project) throws IOException {
        Manifest mf = new Manifest(manifest(project));

        String bundleName = mf.getMainAttributes().getValue("Name");
        if (bundleName != null && bundleName.equals("rascal")) {
            String result = mf.getMainAttributes().getValue("Specification-Version");
            if (result != null) {
                return result;
            }
        }

        return "Unknown version for " + project + " (missing MANIFEST.MF or Specification-Version in MANIFEST.MF)";
    }
    
    public Manifest getDefaultManifest(String projectName) {
        Manifest manifest = new Manifest();
        Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "0.0.1");
        mainAttributes.put(new Attributes.Name(SOURCE), DEFAULT_SRC);
        mainAttributes.put(new Attributes.Name(MAIN_MODULE), DEFAULT_MAIN_MODULE);
        mainAttributes.put(new Attributes.Name(MAIN_FUNCTION), DEFAULT_MAIN_FUNCTION);
        mainAttributes.put(new Attributes.Name(PROJECT_NAME), projectName);
        mainAttributes.put(new Attributes.Name(REQUIRE_LIBRARIES), "");
        return manifest;
    }
    
    public boolean hasManifest(ISourceLocation root) {
        return hasManifest(manifest(root));
    }

    public boolean hasManifest(Class<?> clazz) {
        return hasManifest(manifest(clazz));
    }

    public boolean hasManifest(InputStream is) {
        try {
            return is != null;
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // too bad
            }
        }
    }

    /**
     * @return a list of paths relative to the root of the jar, if no such option is configured
     *         it will return ["src"].
     */
    public List<String> getSourceRoots(Class<?> clazz) {
        return getManifestSourceRoots(manifest(clazz));
    }

    /**
     * @return a list of paths relative to the root of the jar, if no such option is configured
     *         it will return ["src"].
     */
    public List<String> getSourceRoots(JarInputStream jarStream) {
        return getManifestSourceRoots(manifest(jarStream));
    }

    /**
     * @return the name of the main function of a deployment unit, or 'null' if none is configured.
     */
    public String getMainFunction(Class<?> clazz) {
        return getManifestMainFunction(manifest(clazz));
    }


    /**
     * @return the name of the main function of a deployment unit, or 'null' if none is configured.
     */
    public String getMainFunction(JarInputStream jarStream) {
        return getManifestMainFunction(manifest(jarStream));
    }

    public String getProjectName(Class<?> clazz) {
        return getManifestProjectName(manifest(clazz));
    }

    public String getManifestProjectName(InputStream manifest) {
        return getManifestAttribute(manifest, PROJECT_NAME, "");
    }

    /**
     * @return the name of the main function of a deployment unit, or 'null' if none is configured.
     */
    public String getMainFunction(File jarFile) {
        return getManifestMainFunction(manifest(jarFile));
    }
    
    public String getProjectName(File jarFile) {
        return getManifestProjectName(manifest(jarFile));
    }
    
    /**
     * @return a list of bundle names this jar depends on, or 'null' if none is configured.
     */
    public List<String> getRequiredLibraries(JarInputStream jarStream) {
        return getManifestRequiredLibraries(manifest(jarStream));
    }

    /**
     * @return the name of the main module of a deployment unit, or 'null' if none is configured.
     */
    public String getMainModule(JarInputStream jarStream) {
        return getManifestMainModule(manifest(jarStream));
    }
    
    public String getProjectName(JarInputStream jarStream) {
        return getManifestProjectName(manifest(jarStream));
    }
    
    public String getProjectName(InputStream in) {
        return getManifestProjectName(in);
    }

    /**
     * @return the name of the main module of a deployment unit, or 'null' if none is configured.
     */
    public String getMainModule(File jarFile) {
        return getManifestMainModule(manifest(jarFile));
    }

    /**
     * @return the name of the main module of a deployment unit, or 'null' if none is configured.
     */
    public String getMainModule(Class<?> clazz) {
        return getManifestMainModule(manifest(clazz));
    }

    /**
     * @return 'true' if the main module of a deployment unit exists, or 'false' if none is configured.
     */
    public boolean hasMainModule(Class<?> clazz) {
        return getManifestMainModule(manifest(clazz)) != null;
    }  

    /**
     * @return a list of bundle names this jar depends on, or 'null' if none is configured.
     */
    public List<String> getRequiredLibraries(File jarFile) {
        return getManifestRequiredLibraries(manifest(jarFile));
    }

    /**
     * @return a list of bundle names this jar depends on, or 'null' if none is configured.
     */
    public List<String> getRequiredLibraries(Class<?> clazz) {
        return getManifestRequiredLibraries(manifest(clazz));
    }

    /**
     * @return a list of paths relative to the root of the jar, if no such option is configured
     *         it will return ["src"].
     */
    public List<String> getManifestSourceRoots(InputStream manifestFile) {
        return getManifestAttributeList(manifestFile, SOURCE, DEFAULT_SRC);
    }

    /**
     * @return a list of paths relative to the root of the jar, if no such option is configured
     *         it will return ["src"].
     */
    public List<String> getSourceRoots(File file) {
        return getManifestSourceRoots(manifest(file));
    }

    /**
     * @return the name of the main module of a deployment unit, or 'null' if none is configured.
     */
    public String getManifestMainModule(InputStream project) {
        return getManifestAttribute(project, MAIN_MODULE, null);
    }

    /**
     * @return the name of the main function of a deployment unit, or 'null' if none is configured.
     */
    public String getManifestMainFunction(InputStream project) {
        return getManifestAttribute(project, MAIN_FUNCTION, null);
    }
    
    /**
     * @return a list of bundle names this jar depends on, or 'null' if none is configured.
     */
    public List<String> getManifestRequiredLibraries(InputStream project) {
        return getManifestAttributeList(project, REQUIRE_LIBRARIES, null);
    }
    
    public List<String> getManifestRequiredLibraries(ISourceLocation root) {
        return getManifestAttributeList(manifest(root), REQUIRE_LIBRARIES, null);
    }

    public InputStream manifest(Class<?> clazz) {
        return clazz.getResourceAsStream("/" + META_INF_RASCAL_MF);
    }

    public InputStream manifest(ISourceLocation root) {
        try {
            return URIResolverRegistry.getInstance().getInputStream(URIUtil.getChildLocation(JarURIResolver.jarify(root), META_INF_RASCAL_MF));
        } catch (IOException e) {
            return null;
        }
    }

    public String getProjectName(ISourceLocation root) {
        return getManifestProjectName(manifest(root));
    }
    
    public List<String> getSourceRoots(ISourceLocation root) {
        return getManifestSourceRoots(manifest(root));
    }

    public List<String> getRequiredLibraries(ISourceLocation root) {
        return getManifestRequiredLibraries(manifest(root));
    }

    public InputStream manifest(JarInputStream stream) {
        JarEntry next = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            while ((next = stream.getNextJarEntry()) != null) {
                if (next.getName().equals(META_INF_RASCAL_MF)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = stream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    return new ByteArrayInputStream(out.toByteArray());
                }
            }
        } catch (IOException e) {
            return null;
        }

        return null;
    }

    public InputStream manifest(File jarFile) {
        try (JarFile file = new JarFile(jarFile)) {
            return file.getInputStream(new ZipEntry(META_INF_RASCAL_MF));
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * This is to read a comma separated value for a certain label in the manifest.
     * 
     * @param mf    stream to the manifest file, will be closed by this function.
     * @param label the configuration option from the manifest to find
     * @param def   may be null, returned if the configuration option with label is not defined
     * @return the list of strings labeled by the given option.
     */
    public List<String> getManifestAttributeList(InputStream mf, String label, String def) {
        if (mf != null) {
            try {
                Manifest manifest = new Manifest(mf);
                String source = manifest.getMainAttributes().getValue(label);

                if (source != null && !source.trim().isEmpty()) {
                    return Arrays.<String>asList(trim(source.split(",")));
                }
            }
            catch (IOException e) {
                // ignore
            }
            finally {
                try {
                    mf.close();
                } catch (IOException e) {
                    // too bad
                }
            }
        }

        if (def == null) {
            return Collections.emptyList();
        } else {
            return Arrays.<String>asList(new String[] { def });
        }
    }

    /**
     * This is to read a value for a certain label in the manifest.
     * 
     * @param mf    stream to the manifest file, will be closed by this function.
     * @param label the configuration option from the manifest to find
     * @param def   may be null, returned if the configuration option with label is not defined
     * @return either the configured option, or the given default value
     */
    public String getManifestAttribute(InputStream is, String label, String def) {
        if (is != null) {
            try {
                Manifest manifest = new Manifest(is);
                String result = manifest.getMainAttributes().getValue(label);

                if (result != null) {
                    return result.trim();
                }
            }
            catch (IOException e) {
                // ignore;
            }
            finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // too bad
                }
            }
        }

        return def;
    }

    private static String[] trim(String[] elems) {
        for (int i = 0; i < elems.length; i++) {
            elems[i] = elems[i].trim();
        }
        return elems;
    }
}

