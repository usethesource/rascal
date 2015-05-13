package org.rascalmpl.uri;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileTreeHierachy extends JarTreeHierachy {

  public JarFileTreeHierachy(File jar) {
    super();
    totalSize = 0;
    try(JarFile jarFile = new JarFile(jar)) {
      for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
        JarEntry je = e.nextElement();
        if (je.isDirectory()) {
          continue;
        }
        String name = je.getName();
        totalSize += 8 + (name.length() * 2);
        fs.put(name, new FSEntry(je.getTime()));
      }
    } catch (IOException e1) {
      throwMe = e1;
    }
  }

}
