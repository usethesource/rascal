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
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

/**
 * IDEServices for a Desktop environment that rely on the
 * default System browser and editor.
 *
 */
public class BasicIDEServices implements IDEServices {
  
  private static ConsoleRascalMonitor monitor = new ConsoleRascalMonitor();
  private IValueFactory vf;
  private PrintWriter stderr;

  public BasicIDEServices(PrintWriter stderr){
    this.stderr = stderr;
    monitor = new ConsoleRascalMonitor();
    vf = IRascalValueFactory.getInstance();
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

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#startJob(java.lang.String)
   */
  @Override
  public void startJob(String name) {
    monitor.startJob(name);
  }
  
  public void startJob(IString name) {
      startJob(name.getValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#startJob(java.lang.String, int)
   */
  @Override
  public void startJob(String name, int totalWork) {
    monitor.startJob(name, totalWork);
  }
  
  public void startJob(IString name, IInteger totalWork){
      startJob(name.getValue(), totalWork.intValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#startJob(java.lang.String, int, int)
   */
  @Override
  public void startJob(String name, int workShare, int totalWork) {
    monitor.startJob(name, workShare, totalWork);
  }
  
  public void startJob(IString name, IInteger workShare, IInteger totalWork) {
      startJob(name.getValue(), workShare.intValue(), totalWork.intValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#event(java.lang.String)
   */
  @Override
  public void event(String name) {
    monitor.event(name);
  }
  
  public void event(IString name){
      event(name.getValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#event(java.lang.String, int)
   */
  @Override
  public void event(String name, int inc) {
    monitor.event(name,inc);
  }
  
  public void event(IString name, IInteger inc){
      event(name.getValue(), inc.intValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#event(int)
   */
  @Override
  public void event(int inc) {
    monitor.event(inc);
  }
  
  public void event(IInteger inc){
      event(inc.intValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#endJob(boolean)
   */
  @Override
  public int endJob(boolean succeeded) {
    return monitor.endJob(succeeded);
  }
  
  public IInteger endJob(IBool succeeded){
      return vf.integer(endJob(succeeded.getValue()));
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#isCanceled()
   */
  @Override
  public boolean isCanceled() {
      return monitor.isCanceled();
  }
  
  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#todo(int)
   */
  @Override
  public void todo(int work) {
    monitor.todo(work);
  }
  
  public void todo(IInteger work){
      todo(work.intValue());
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.debug.IRascalMonitor#warning(java.lang.String, io.usethesource.vallang.ISourceLocation)
   */
  @Override
  public void warning(String message, ISourceLocation src) {
    monitor.warning(message,  src);
  }
  
  public void warning(IString message, ISourceLocation src){
      warning(message.getValue(), src);
  }
}
