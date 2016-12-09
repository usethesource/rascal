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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.desktop;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.value.ISourceLocation;

public class BasicIDEServices implements IDEServices {
  
  private WatchService watcher;
  private ConsoleRascalMonitor monitor;

  public BasicIDEServices(){
    monitor = new ConsoleRascalMonitor();
  }

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

  public void watch(Path dir) throws IOException{
    if(watcher == null){
      watcher = FileSystems.getDefault().newWatchService();
    }
    dir.register(watcher,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY);
  }

  public boolean anyFileChanges() {
    boolean any = watcher.poll() != null;
    while(watcher.poll() != null) {
      // consume all change events
    }
    return any;
  }

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
        WatchEvent<Path> ev = (WatchEvent<Path>)event;
        results.add(ev.context());
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

  public void unwatchAll() throws IOException {
    if(watcher != null){
      watcher.close();
      watcher = null;
    }
  }

  @Override
  public void startJob(String name) {
    monitor.startJob(name);
  }

  @Override
  public void startJob(String name, int totalWork) {
    monitor.startJob(name, totalWork);
  }

  @Override
  public void startJob(String name, int workShare, int totalWork) {
    monitor.startJob(name, workShare, totalWork);
  }

  @Override
  public void event(String name) {
    monitor.event(name);
  }

  @Override
  public void event(String name, int inc) {
    monitor.event(name,inc);
  }

  @Override
  public void event(int inc) {
    monitor.event(inc);
  }

  @Override
  public int endJob(boolean succeeded) {
    return monitor.endJob(succeeded);
  }

  @Override
  public boolean isCanceled() {
      return monitor.isCanceled();
  }

  @Override
  public void todo(int work) {
    monitor.todo(work);
  }

  @Override
  public void warning(String message, ISourceLocation src) {
    monitor.warning(message,  src);
  }
}
