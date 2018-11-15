/** 
 * Copyright (c) 2018, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
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
package org.rascalmpl.shell;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class OnePageServer extends NanoHTTPD {
      private String html = null;
      
      public OnePageServer(int port) {
          super(port);
      }
      
      public void setHTML(String html) {
          this.html = html;
      }
      
      public void unsetHTML() {
          this.html = null;
      }

      public Response serve(String uri, Method method, java.util.Map<String,String> headers, java.util.Map<String,String> parms, java.util.Map<String,String> files) {
          if (html != null) {
              return newFixedLengthResponse(Status.OK, "text/html", html);                     
          }
          else {
              return newFixedLengthResponse(Status.NOT_FOUND, "text/plain", "no viewable content");
          }
      }
      
      public static OnePageServer getInstance() throws IOException {
          OnePageServer server = null;

          for(int port = 9050; port < 9050+25; port++){
              try {
                  server = new OnePageServer(port);
                  server.start();
                  // success
                  break;
              } catch (IOException e) {
                  // failure is expected if the port is taken
                  continue;
              }
          }

          if (server == null) {
              throw new IOException("Could not find port to run single page server on");
          }

          return server;
      }
  }