/** 
 * Copyright (c) 2016, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.lang.json.internal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.visitors.IValueVisitor;

/**
 * This class streams am IValue stream directly to an JSon stream. Useful to communicate IValues to browsers.
 */
public class JsonValueWriter {
  private ThreadLocal<SimpleDateFormat> format;
  private boolean datesAsInts = true;
  private boolean unpackedLocations = false;
  private boolean dropOrigins = true;
  private boolean explicitConstructorNames = false;
  private boolean explicitDataTypes;
  
  public JsonValueWriter() {
    setCalendarFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  }
  
  /**
   * Builder method to set the format to use for all date-time values encoded as strings
   */
  public JsonValueWriter setCalendarFormat(String format) {
    // SimpleDateFormat is not thread safe, so here we make sure
    // we can use objects of this reader in different threads at the same time
    this.format = new ThreadLocal<SimpleDateFormat>() {
      protected SimpleDateFormat initialValue() {
        return new SimpleDateFormat(format);
      }
    };
    return this;
  }
   
  public JsonValueWriter setDatesAsInt(boolean setting) {
    this.datesAsInts = setting;
    return this;
  }
  
  public JsonValueWriter setUnpackedLocations(boolean setting) {
      this.unpackedLocations = setting;
      return this;
  }

  public JsonValueWriter setDropOrigins(boolean setting) {
    this.dropOrigins = setting;
    return this;
  }

  public JsonValueWriter setExplicitConstructorNames(boolean setting) {
    this.explicitConstructorNames = setting;
    return this;
  }

  public JsonValueWriter setExplicitDataTypes(boolean setting) {
    this.explicitDataTypes = setting;
    return this;
  }

  public void write(JsonWriter out, IValue value) throws IOException {
    value.accept(new IValueVisitor<Void, IOException>() {

      @Override
      public Void visitString(IString o) throws IOException {
        out.value(o.getValue());
        return null;
      }

      @Override
      public Void visitReal(IReal o) throws IOException {
        // TODO: warning might loose precision here?!
        out.value(o.doubleValue());
        return null;
      }

      @Override
      public Void visitRational(IRational o) throws IOException {
          out.value(o.getStringRepresentation());
          return null;
      }

      @Override
      public Void visitList(IList o) throws IOException {
        out.beginArray();
        for (IValue v : o) {
          v.accept(this);
        }
        out.endArray();
        return null;
      }

      @Override
      public Void visitSet(ISet o) throws IOException {
        out.beginArray();
        for (IValue v : o) {
          v.accept(this);
        }
        out.endArray();
        return null;
      }

      @Override
      public Void visitSourceLocation(ISourceLocation o) throws IOException {
          if (unpackedLocations) {
              out.beginObject();
              
              out.name("scheme");
              out.value(o.getScheme());
              
              out.name("authority");
              out.value(o.getAuthority());
              
              out.name("path");
              out.value(o.getPath());
              
              if (!o.getFragment().isEmpty()) {
                  out.name("fragment");
                  out.value(o.getFragment());
              }
              
              if (!o.getQuery().isEmpty()) {
                  out.name("query");
                  out.value(o.getQuery());
              }
              
              if (o.hasOffsetLength()) {
                  out.name("offset");
                  out.value(o.getOffset());
                  out.name("length");
                  out.value(o.getLength());
              }
              
              if (o.hasLineColumn()) {
                  out.name("begin");
                  out.beginArray();
                  out.value(o.getBeginLine());
                  out.value(o.getBeginColumn());
                  out.endArray();
                  
                  out.name("end");
                  out.beginArray();
                  out.value(o.getEndLine());
                  out.value(o.getEndColumn());
                  out.endArray();
              }
              
              out.endObject();
          }
          else {
              if (!o.hasOffsetLength()) {
                  if ("file".equals(o.getScheme())) {
                      out.value(o.getPath());
                  }
                  else {
                      out.value(o.getURI().toASCIIString());
                  }
              }
              else {
                  out.value(o.toString());
              }
          }
        
        return null;
      }

      @Override
      public Void visitTuple(ITuple o) throws IOException {
        out.beginArray();
        for (IValue v : o) {
          v.accept(this);
        }
        out.endArray();
        return null;
      }

      @Override
      public Void visitNode(INode o) throws IOException {
        out.beginObject();
        int i = 0;
        for (IValue arg : o) {
          out.name("arg" + i++); 
          arg.accept(this);
        }

        Map<String, IValue> parameters = o.asWithKeywordParameters().getParameters();

        String originKey = parameters.containsKey("rascal-src") ? "rascal-src" : "src";
        
        for (Entry<String,IValue> e : parameters.entrySet()) {
          if (dropOrigins && e.getKey().equals(originKey)) {
            continue;
          }

          out.name(e.getKey()); 
          e.getValue().accept(this); 
        }
        out.endObject();
        
        return null;
      }

      @Override
      public Void visitConstructor(IConstructor o) throws IOException {
        if (!explicitConstructorNames && !explicitDataTypes && o.getConstructorType().getArity() == 0 && !o.asWithKeywordParameters().hasParameters()) {
          // enums!
          out.value(o.getName());
          return null;
        }

        out.beginObject();

        if (explicitConstructorNames || explicitDataTypes) {
          out.name("_constructor");
          out.value(o.getName());
        }

        if (explicitDataTypes) {
          out.name("_type");
          out.value(o.getType().getName());
        }

        int i = 0;
        for (IValue arg : o) {
          out.name(o.getConstructorType().getFieldName(i));
          arg.accept(this);
          i++;
        }
        for (Entry<String,IValue> e : o.asWithKeywordParameters().getParameters().entrySet()) {
          out.name(e.getKey()); 
          e.getValue().accept(this); 
        }
        out.endObject();
        
        return null;
      }

      @Override
      public Void visitInteger(IInteger o) throws IOException {
        // TODO: may loose precision
        out.value(o.longValue());
        return null;
      }

      @Override
      public Void visitMap(IMap o) throws IOException {
        if (o.getKeyType().isString()) {
          out.beginObject();
          for (IValue key : o) {
            out.name(((IString) key).getValue()); 
            o.get(key).accept(this); 
          }
          out.endObject();
        }
        else if (o.getKeyType().isSourceLocation() && !unpackedLocations) {
          out.beginObject();
          for (IValue key : o) {
            ISourceLocation l = (ISourceLocation) key;
            
            if (!l.hasOffsetLength()) {
              if ("file".equals(l.getScheme())) { 
                out.name(l.getPath()); 
              }
              else {
                out.name(l.getURI().toASCIIString());
              }
            }
            else {
              out.name(l.toString());
            }

            o.get(key).accept(this); 
          }
          out.endObject();
        }
        else {
          out.beginArray();
          for (IValue key : o) {
            out.beginArray();
            key.accept(this);
            o.get(key).accept(this);
            out.endArray();
          }
          out.endArray();
        }
        
        return null;
      }

      @Override
      public Void visitBoolean(IBool boolValue) throws IOException {
        out.value(boolValue.getValue());
        return null;
      }

      @Override
      public Void visitExternal(IExternalValue externalValue) throws IOException {
        // TODO
        throw new IOException("External values are not supported by JSon serialisation yet");
      }

      @Override
      public Void visitDateTime(IDateTime o) throws IOException {
        if (datesAsInts) {
          out.value(o.getInstant());
          return null;
        }
        else {
          throw new IOException("Dates as strings not yet implemented: " + format.get().toPattern());
        }
      }
    });
    }
}