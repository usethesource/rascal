package org.rascalmpl.shell;

import java.io.IOException;

public interface ShellRunner {
  void run(String[] args) throws IOException;
}
