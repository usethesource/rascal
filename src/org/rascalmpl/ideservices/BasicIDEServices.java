/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.ideservices;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rascalmpl.interpreter.ConsoleRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

/**
 * IDEServices for a Desktop environment that rely on the
 * default System browser and editor.
 *
 */
public class BasicIDEServices implements IDEServices {
  
  private static ConsoleRascalMonitor monitor = new ConsoleRascalMonitor();
  private PrintWriter stderr;

  public BasicIDEServices(PrintWriter stderr){
    this.stderr = stderr;
    monitor = new ConsoleRascalMonitor();
  }
  
  @Override
  public PrintWriter stderr() {
    return stderr;
  }
  
  public void browse(ISourceLocation loc){
      browse(loc.getURI());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#browse(java.net.URI)
   */
  public void browse(URI uri){
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(uri);
      } catch (IOException e) {
        stderr.println(e.getMessage());
      }
    } else {
      stderr.println("Desktop not supported, cannot open browser");
    }
  }
  
  @Override
  public void edit(ISourceLocation loc){
      if(loc.getScheme() != "file"){
         stderr.println("Can only edit files using the \"file\" scheme");
         return;
      }
      edit(Paths.get(loc.getURI()));
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#edit(java.nio.file.Path)
   */
  public void edit(Path path) {
    File file = new File(path.toString());
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.EDIT)) {
      try {
        desktop.edit(file);
      } catch (IOException e) {
        stderr.println(e.getMessage());
      }
    } else {
      stderr.println("Desktop not supported, cannout open editor");
    }
  }

  @Override
  public void jobStart(String name, int workShare, int totalWork) {
    monitor.jobStart(name, workShare, totalWork);
  }
  
  @Override
  public void jobStep(String name, String message, int inc) {
    monitor.jobStep(name, message, inc);
  }

  @Override
  public int jobEnd(String name, boolean succeeded) {
    return monitor.jobEnd(name, succeeded);
  }
  
  @Override
  public boolean jobIsCanceled(String name) {
      return monitor.jobIsCanceled(name);
  }
  
  @Override
  public void jobTodo(String name, int work) {
    monitor.jobTodo(name, work);
  }

  @Override
  public void warning(String message, ISourceLocation src) {
    monitor.warning(message,  src);
  }
}
