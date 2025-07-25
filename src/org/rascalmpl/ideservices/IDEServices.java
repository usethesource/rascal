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

import java.io.PrintWriter;
import java.net.URI;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.Messages;
import org.rascalmpl.uri.LogicalMapResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;

/**
 * IDEServices provides external services that can be called by the
 * Rascal compiler and compiled REPL, but also by implementors of IDE (LSP) features.
 */
public interface IDEServices extends IRascalMonitor {

  PrintWriter stderr();
  
  /**
   * Open a browser for the give uri.
   * @param uri
   */
  void browse(URI uri, String title, int viewColumn);

  /**
   * Open an editor for file at given path.
   * @param path
   */
  void edit(ISourceLocation path);

  /**
   * Implements the project scheme by deferring to the IDEservices 
   * @param input
   * @return either the exact same loc in case nothing could be resolved, or a 
   *         new location which with a new scheme which can be resolved without
   *         communication with the IDE services
   */
  default ISourceLocation resolveProjectLocation(ISourceLocation input) {
    return input;
  }

  /**
   * Registers a new language definition with the surrounding IDE. Multiple registries for the same language are supported, registration order determines priority.
   * @param language
   */
  default void registerLanguage(IConstructor language) {
    throw new UnsupportedOperationException("registerLanguage is not implemented in this environment.");
  }

  /**
   * Unregisters a language definition with the surrounding IDE. Can be partial if module & function are not empty strings.
   * @param language
   */
  default void unregisterLanguage(IConstructor language) {
    throw new UnsupportedOperationException("registerLanguage is not implemented in this environment.");
  }

  /**
   * Get access to the current terminal.  <br>
   * used for features such as clearing the terminal, and starting a nested REPL. <br>
   * Can return null if there is no active terminal.
   * @return a terminal if there is one, null otherwise.
   */
  default Terminal activeTerminal() {
    return null;
  }

  /**
   * @deprecated replaced by {@link #applyFileSystemEdits(IList)}
   */
  @Deprecated(forRemoval = true)
  default void applyDocumentsEdits(IList edits) {
    applyFileSystemEdits(edits);
  }

  /**
   * Asks the IDE to apply document edits as defined in the standard library module
   * analysis::diff::edits::TextEdits, according to the semantics defined in
   * analysis::diff::edits::ExecuteTextEdits. However, the IDE can take care of these
   * changes in order to provide important UI experience features such as "preview"
   * and "undo". 
   * 
   * Typically a call to this IDE service method is included in the implementation
   * of refactoring and quick-fix features of the language service protocol. 
   * @param edits list of DocumentEdits
   */
  default void applyFileSystemEdits(IList edits) {
     throw new UnsupportedOperationException("applyFileSystemEdits is not implemented in this environment.");
  }

  /**
   * Read the standard library module `Message`
   * for how errors, warnings and info messages look.
   * 
   * This method would typically pop-up the message somewhere in the IDE
   */
  default void showMessage(IConstructor message) {
    logMessage(message);
  }

  /**
   * Read the standard library module `Message`
   * for how errors, warnings and info messages look.
   * 
   * This method would stream the message to a log view in the IDE
   */
  default void logMessage(IConstructor msg) {
      Messages.write(IRascalValueFactory.getInstance().list(msg), stderr());
  }

  /**
   * Read the standard library module `Message`
   * for how errors, warnings and info messages look.
   * @param messages
   * 
   * This method would register the messages with a "problems view" in the IDE
   */
  default void registerDiagnostics(IList messages) {
      Messages.write(messages, stderr());
  }

  default void registerDiagnostics(IList messages, ISourceLocation projectRoot) {
      registerDiagnostics(messages);
  }

  /**
   * Clears all registered diagnostics for the given resources/documents/files
   */
  default void unregisterDiagnostics(IList resources) { 
    
  }

  /**
   * This registers a map of logical URI to their physical counter-part in the IDE.
   * It makes logical URIs "editable" and "clickable" when clients can register new
   * mappings with the IDE.
   * 
   * @param scheme
   * @param auth
   * @param map
   */
  default void registerLocations(IString scheme, IString auth, IMap map) {
	  URIResolverRegistry.getInstance().registerLogical(new LogicalMapResolver(scheme.getValue(), auth.getValue(), map));
	}

  default void unregisterLocations(IString scheme, IString auth) {
    URIResolverRegistry.getInstance().unregisterLogical(scheme.getValue(), auth.getValue());
	}
}