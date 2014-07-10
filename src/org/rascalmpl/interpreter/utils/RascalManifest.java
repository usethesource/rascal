package org.rascalmpl.interpreter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

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
  protected static final String META_INF_RASCAL_MF = META_INF + "/RASCAL.MF";
  protected static final String MAIN_MODULE = "Main-Module";
  protected static final String MAIN_FUNCTION = "Main-Function";
  protected static final String REQUIRE_BUNDLES = "Require-Bundles";

  public Manifest getDefaultManifest() {
    Manifest manifest = new Manifest();
    Attributes mainAttributes = manifest.getMainAttributes();
    mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "0.0.1");
    mainAttributes.put(new Attributes.Name(SOURCE), DEFAULT_SRC);
    mainAttributes.put(new Attributes.Name(MAIN_MODULE), DEFAULT_MAIN_MODULE);
    mainAttributes.put(new Attributes.Name(MAIN_FUNCTION), DEFAULT_MAIN_FUNCTION);
    return manifest;
  }
  
  public boolean hasManifest(Class<?> clazz) {
    return hasManifest(manifest(clazz));
  }
  
  protected boolean hasManifest(InputStream is) {
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
    return getSourceRoots(manifest(clazz));
  }
  
  /**
   * @return the name of the main function of a deployment unit, or 'null' if none is configured.
   */
  public String getMainFunction(Class<?> clazz) {
    return getMainFunction(manifest(clazz));
  }
  
  /**
   * @return the name of the main module of a deployment unit, or 'null' if none is configured.
   */
  public String getMainModule(Class<?> clazz) {
    return getMainModule(manifest(clazz));
  }

  /**
   * @return 'true' if the main module of a deployment unit exists, or 'false' if none is configured.
   */
  public boolean hasMainModule(Class<?> clazz) {
    return getMainModule(manifest(clazz)) != null;
  }  
  
  /**
   * @return a list of bundle names this jar depends on, or 'null' if none is configured.
   */
  public List<String> getRequiredBundles(Class<?> clazz) {
    return getRequiredBundles(manifest(clazz));
  }

  /**
   * @return a list of paths relative to the root of the jar, if no such option is configured
   *         it will return ["src"].
   */
  protected List<String> getSourceRoots(InputStream project) {
    return getAttributeList(project, SOURCE, DEFAULT_SRC);
  }
  
  /**
   * @return the name of the main module of a deployment unit, or 'null' if none is configured.
   */
  protected String getMainModule(InputStream project) {
    return getAttribute(project, MAIN_MODULE, null);
  }
  
  /**
   * @return the name of the main function of a deployment unit, or 'null' if none is configured.
   */
  protected String getMainFunction(InputStream project) {
    return getAttribute(project, MAIN_FUNCTION, null);
  }
  
  /**
   * @return a list of bundle names this jar depends on, or 'null' if none is configured.
   */
  protected List<String> getRequiredBundles(InputStream project) {
    return getAttributeList(project, REQUIRE_BUNDLES, null);
  }

  protected InputStream manifest(Class<?> clazz) {
    return clazz.getResourceAsStream("/" + META_INF_RASCAL_MF);
  }
  
  /**
   * This is to read a comma separated value for a certain label in the manifest.
   * 
   * @param mf    stream to the manifest file, will be closed by this function.
   * @param label the configuration option from the manifest to find
   * @param def   may be null, returned if the configuration option with label is not defined
   * @return the list of strings labeled by the given option.
   */
  protected List<String> getAttributeList(InputStream mf, String label, String def) {
    if (mf != null) {
      try {
        Manifest manifest = new Manifest(mf);
        String source = manifest.getMainAttributes().getValue(label);
        
        if (source != null) {
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
      return null;
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
  protected String getAttribute(InputStream is, String label, String def) {
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

