package org.rascalmpl.uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.libraries.ClassResourceInput;
import org.rascalmpl.value.ISourceLocation;

public class ManifestURIResolver implements ISourceLocationInput {
  private List<ISourceLocationInput> resolvers;

  private static class SourceFolderInput extends ClassResourceInput {
    public SourceFolderInput(String folder) {
      super("manifest", RascalManifest.class, folder);
    }
  }
  
  public ManifestURIResolver() {
    List<String> sourceRoots = new RascalManifest().getSourceRoots(RascalManifest.class);
    
    for (String source : sourceRoots) {
      resolvers.add(new SourceFolderInput(source));
    }
  }
  
  @Override
  public InputStream getInputStream(ISourceLocation uri) throws IOException {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return folder.getInputStream(uri);
      }
    }
    
    throw new FileNotFoundException(uri.toString());
  }

  @Override
  public Charset getCharset(ISourceLocation uri) throws IOException {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return folder.getCharset(uri);
      }
    }
    
    throw new FileNotFoundException(uri.toString());
  }

  @Override
  public boolean exists(ISourceLocation uri) {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return true;
      }
    }
    
    return false;
  }

  @Override
  public long lastModified(ISourceLocation uri) throws IOException {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return folder.lastModified(uri);
      }
    }
    
    throw new FileNotFoundException(uri.toString());
  }

  @Override
  public boolean isDirectory(ISourceLocation uri) {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return folder.isDirectory(uri);
      }
    }
    
    return false;
  }

  @Override
  public boolean isFile(ISourceLocation uri) {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return folder.isFile(uri);
      }
    }
    
    return false;
  }

  @Override
  public String[] list(ISourceLocation uri) throws IOException {
    for (ISourceLocationInput folder : resolvers) {
      if (folder.exists(uri)) {
        return folder.list(uri);
      }
    }
    
    throw new FileNotFoundException(uri.toString());
  }

  @Override
  public String scheme() {
    return "manifest";
  }

  @Override
  public boolean supportsHost() {
    return false;
  }
}
