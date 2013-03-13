package org.rascalmpl.interpreter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class RascalManifest {
  protected static final String DEFAULT_MAIN_MODULE = "Plugin";
  protected static final String DEFAULT_MAIN_FUNCTION = "main";
  protected static final String DEFAULT_SRC = "src";
  protected static final String SOURCE = "Source";
  protected static final String META_INF = "META-INF";
  protected static final String META_INF_RASCAL_MF = META_INF + "/RASCAL.MF";
  protected static final String MAIN_MODULE = "Main-Module";
  protected static final String MAIN_FUNCTION = "Main-Function";

  public Manifest getDefaultManifest() {
    Manifest manifest = new Manifest();
    Attributes mainAttributes = manifest.getMainAttributes();
    mainAttributes.put(SOURCE, DEFAULT_SRC);
    mainAttributes.put(MAIN_MODULE, DEFAULT_MAIN_MODULE);
    mainAttributes.put(MAIN_FUNCTION, DEFAULT_MAIN_FUNCTION);
    return manifest;
  }
  
  public boolean hasManifest(Class<?> clazz) {
    return manifest(clazz) != null;
  }
  
  public List<String> getSourceRoots(Class<?> clazz) {
    return getSourceRoots(manifest(clazz));
  }
  
  public String getMainFunction(Class<?> clazz) {
    return getMainFunction(manifest(clazz));
  }
  
  public String getMainModule(Class<?> clazz) {
    return getMainModule(manifest(clazz));
  }
  
  protected List<String> getSourceRoots(InputStream project) {
    return getAttributeList(project, SOURCE, DEFAULT_SRC);
  }
  
  protected String getMainModule(InputStream project) {
    return getAttribute(project, MAIN_MODULE, DEFAULT_MAIN_FUNCTION);
  }
  
  protected String getMainFunction(InputStream project) {
    return getAttribute(project, MAIN_FUNCTION, DEFAULT_MAIN_MODULE);
  }
  
  protected InputStream manifest(Class<?> clazz) {
    return clazz.getResourceAsStream(META_INF_RASCAL_MF);
  }
  
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

    return Arrays.<String>asList(new String[] { def });
  }
  
  protected String getAttribute(InputStream is, String label, String def) {
    if (is != null) {
      try {
        Manifest manifest = new Manifest(is);
        String result = manifest.getMainAttributes().getValue(label).trim();

        if (result != null) {
          return result;
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

