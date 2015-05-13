package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarInputStreamTreeHierachy extends JarTreeHierachy {

  private static class IndexFSEntry extends FSEntry {
    public int position;

    public IndexFSEntry(long lastModified, int position) {
      super(lastModified);
      this.position = position;
    }
    
  }
  public JarInputStreamTreeHierachy(InputStream in) {
    super();
    totalSize = 0;

		try (JarInputStream stream = new JarInputStream(in)) {
			JarEntry next = null;
			int pos = 0;
			
			while ((next = stream.getNextJarEntry()) != null) {
			  if (!next.isDirectory()) {
			    String name = next.getName();
			    totalSize += 16 + (name.length() * 2);
			    fs.put(name, new IndexFSEntry(next.getTime(), pos++));
			  }
			  else {
			    pos++; // we don't store directories
			  }
			}
		} catch (IOException e) {
		  throwMe = e;
		  fs.clear();
		}		
  }
  
  public int getPosition(String path) {
    IndexFSEntry ent = (IndexFSEntry) fs.get(path);
    if (ent == null) {
      return -1;
    }
    return ent.position;
  }

}
