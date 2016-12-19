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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.uptr.IRascalValueFactory;

/**
 * IDEServices for a Desktop environment that rely on the
 * default System browser and editor. File changes are implemented
 * Java WatchService.
 *
 */
public class BasicIDEServices implements IDEServices {
  
  private WatchService watcher;
  private static ConsoleRascalMonitor monitor = new ConsoleRascalMonitor();
  private HashSet<Path> roots = new HashSet<>();
  private Map<WatchKey,Path> keys;
  private IValueFactory vf;

  public BasicIDEServices(){
    monitor = new ConsoleRascalMonitor();
    roots = new HashSet<>();
    vf = IRascalValueFactory.getInstance();
  }
  
  public BasicIDEServices(IValueFactory vf){
      this.vf = vf;
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
        System.err.println(e.getMessage());
      }
    } else {
      System.err.println("Desktop not supported, cannot open browser");
    }
  }
  
  public void edit(ISourceLocation loc){
      if(loc.getScheme() != "file"){
         System.err.println("Can only edit files using the \"file\" scheme"); 
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
        System.err.println(e.getMessage());
      }
    } else {
      System.err.println("Desktop not supported, cannout open editor");
    }
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#watch(java.nio.file.Path)
   */
  public void watch(Path root) throws IOException {
    if(watcher == null){
      watcher = FileSystems.getDefault().newWatchService();
      keys = new HashMap<>();
    }
    roots.add(root);
    registerRecursive(root);
  }
  
  private void registerRecursive(final Path root) throws IOException {
    Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        keys.put(key,  dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }
  
  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#unwatch(java.nio.file.Path)
   */
  public void unwatch(Path root) throws IOException{
    if(watcher != null && roots.contains(root)){
      roots.remove(root);
      watcher.close();
    }
    if(!roots.isEmpty()){
      watcher = FileSystems.getDefault().newWatchService();
      for(Path path : roots){
        registerRecursive(path);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#unwatchAll()
   */
  public void unwatchAll() throws IOException {
    if(watcher != null){
      watcher.close();
      watcher = null;
      roots = null;
    }
  }

  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#fileChanges()
   */
  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#fileChanges()
   */
  public List<Path> fileChanges() {
    List<Path> results = new ArrayList<>();
    WatchKey key;
    while((key = watcher.poll()) != null) {

      for (WatchEvent<?> event: key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();

        // An OVERFLOW event can occur regardless if events are lost or discarded.
        if (kind == StandardWatchEventKinds.OVERFLOW) {
          continue;
        }

        // The filename is the context of the event.
        @SuppressWarnings("unchecked")
        WatchEvent<Path> ev = (WatchEvent<Path>)event;
        results.add(keys.get(key).resolve(ev.context()));
      }

      // Reset the key -- this step is critical if you want to
      // receive further watch events.  If the key is no longer valid,
      // the directory is inaccessible so exit the loop.
      boolean valid = key.reset();
      if (!valid) {
        break;
      }
    }
    return results;
  }
  
  /* (non-Javadoc)
   * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices#anyFileChanges()
   */
  public boolean anyFileChanges() {
      return fileChanges().size() > 0;
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
      startJob(name.getValue(), totalWork.intValue(), totalWork.intValue());
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
   * @see org.rascalmpl.debug.IRascalMonitor#warning(java.lang.String, org.rascalmpl.value.ISourceLocation)
   */
  @Override
  public void warning(String message, ISourceLocation src) {
    monitor.warning(message,  src);
  }
  
  public void warning(IString message, ISourceLocation src){
      warning(message.getValue(), src);
  }
}
