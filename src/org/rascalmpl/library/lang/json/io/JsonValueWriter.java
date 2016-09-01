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
package org.rascalmpl.library.lang.json.io;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.visitors.IValueVisitor;

import com.google.gson.stream.JsonWriter;

/**
 * This class streams a JSON stream directly to an IValue representation and validates the content
 * to a given type as declared in a given type store. See the Rascal file lang::json::IO::readJson for documentation.
 
 */
public class JsonValueWriter {
  private ThreadLocal<SimpleDateFormat> format;
  private boolean implicitConstructors = true;
  private boolean implicitNodes = true;
  private boolean datesAsInts = true;
  
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
  
  public JsonValueWriter setImplicitConstructors(boolean setting) {
    this.implicitConstructors = setting;
    return this;
  }
  
  public JsonValueWriter setImplicitNodes(boolean setting) {
    this.implicitNodes = setting;
    return this;
  }
  
  public JsonValueWriter setDatesAsInt(boolean setting) {
    this.datesAsInts = setting;
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
      public Void visitRelation(ISet o) throws IOException {
        return visitSet(o);
      }

      @Override
      public Void visitListRelation(IList o) throws IOException {
        return visitList(o);
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
        if (implicitNodes) {
          out.beginObject();
          int i = 0;
          for (IValue arg : o) {
            out.name("arg" + i++); 
            arg.accept(this);
          }
          for (Entry<String,IValue> e : o.asWithKeywordParameters().getParameters().entrySet()) {
            out.name(e.getKey()); 
            e.getValue().accept(this); 
          }
          out.endObject();
        }
        else {
          out.beginArray();
          out.value(o.getName());
          out.beginArray();
          for (IValue arg : o) {
            arg.accept(this);
          }
          out.endArray();
          
          if (o.asWithKeywordParameters().hasParameters()) {
            out.beginObject();
            for (Entry<String,IValue> e : o.asWithKeywordParameters().getParameters().entrySet()) {
              out.name(e.getKey()); 
              e.getValue().accept(this); 
            }
            out.endObject();
          }
          
          out.endArray();
        }
        
        return null;
      }

      @Override
      public Void visitConstructor(IConstructor o) throws IOException {
        if (implicitConstructors) {
          out.beginObject();
          int i = 0;
          for (IValue arg : o) {
            if (arg instanceof INode) {
              out.name(((INode) arg).getName());
            }
            else {
              out.name(o.getConstructorType().getFieldName(i));
            }
            arg.accept(this);
          }
          for (Entry<String,IValue> e : o.asWithKeywordParameters().getParameters().entrySet()) {
            out.name(e.getKey()); 
            e.getValue().accept(this); 
          }
          out.endObject();
        }
        else {
          out.beginArray();
          out.value(o.getName());
          out.beginArray();
          for (IValue arg : o) {
            arg.accept(this);
          }
          out.endArray();
          
          if (o.asWithKeywordParameters().hasParameters()) {
            out.beginObject();
            for (Entry<String,IValue> e : o.asWithKeywordParameters().getParameters().entrySet()) {
              out.name(e.getKey()); 
              e.getValue().accept(this); 
            }
            out.endObject();
          }
          
          out.endArray();
        }
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