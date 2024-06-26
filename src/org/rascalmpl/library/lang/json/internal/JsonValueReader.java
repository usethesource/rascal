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
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.types.ReifiedType;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.ITypeVisitor;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

/**
 * This class streams a JSON stream directly to an IValue representation and validates the content
 * to a given type as declared in a given type store. See the Rascal file lang::json::IO::readJson for documentation.
 
 */
public class JsonValueReader {
  private static final TypeFactory TF = TypeFactory.getInstance();
  private final TypeStore store;
  private final IRascalValueFactory vf;
  private ThreadLocal<SimpleDateFormat> format;
  private final IRascalMonitor monitor;
  private ISourceLocation src;
  private VarHandle posHandler;
  private VarHandle lineHandler;
  private VarHandle lineStartHandler;
  private IFunction parsers;
  private Map<Type, IValue> nulls = Collections.emptyMap();
  
  /**
   * @param vf     factory which will be used to construct values
   * @param store  type store to lookup constructors of abstract data-types in and the types of keyword fields
   */
  public JsonValueReader(IRascalValueFactory vf, TypeStore store, IRascalMonitor monitor, ISourceLocation src) {
    this.vf = vf;
    this.store = store;
    this.monitor = monitor;
    this.src = src;
    setCalendarFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    if (src != null) {
      try {
        var lookup = MethodHandles.lookup();
        var privateLookup = MethodHandles.privateLookupIn(JsonReader.class, lookup);
        this.posHandler = privateLookup.findVarHandle(JsonReader.class, "pos", int.class);
        this.lineHandler = privateLookup.findVarHandle(JsonReader.class, "lineNumber", int.class);
        this.lineStartHandler = privateLookup.findVarHandle(JsonReader.class, "lineStart", int.class);
      }
      catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
        // we disable the origin tracking if we can not get to the fields
        src = null;
        monitor.warning("Unable to retrieve origin information due to: " + e.getMessage(), src);
      }
    }
  }
  
  public JsonValueReader(IRascalValueFactory vf, IRascalMonitor monitor, ISourceLocation src) {
    this(vf, new TypeStore(), monitor, src);
  }
  
  public JsonValueReader setNulls(Map<Type, IValue> nulls) {
      this.nulls = nulls;
      return this;
  }
  /**
   * Builder method to set the format to use for all date-time values encoded as strings
   */
  public JsonValueReader setCalendarFormat(String format) {
    // SimpleDateFormat is not thread safe, so here we make sure
    // we can use objects of this reader in different threads at the same time
    this.format = new ThreadLocal<SimpleDateFormat>() {
      protected SimpleDateFormat initialValue() {
        return new SimpleDateFormat(format);
      }
    };
    return this;
  }

  public JsonValueReader setParsers(IFunction parsers) {
    if (parsers.getType() instanceof ReifiedType && parsers.getType().getTypeParameters().getFieldType(0).isTop()) {
			// ignore the default parser
			parsers = null;
    }

    this.parsers = parsers;
    return this;
  }

  /**
   * Read and validate a Json stream as an IValue
   * @param  in       json stream
   * @param  expected type to validate against (recursively)
   * @return an IValue of the expected type
   * @throws IOException when either a parse error or a validation error occurs
   */
  public IValue read(JsonReader in, Type expected) throws IOException {
    IValue res = expected.accept(new ITypeVisitor<IValue, IOException>() {
      @Override
      public IValue visitInteger(Type type) throws IOException {
        try {
          switch (in.peek()) {
            case NUMBER:
              return vf.integer(in.nextLong());
            case STRING:
              return vf.integer(in.nextString());
            case NULL:
              in.nextNull();
              return inferNullValue(nulls, type);
            default:
              throw new IOException("Expected integer but got " + in.peek());
          }
        }
        catch (NumberFormatException e) {
          throw new IOException("Expected integer but got " + e.getMessage());
        }
      }
      
      public IValue visitReal(Type type) throws IOException {
        try {
          switch (in.peek()) {
            case NUMBER:
              return vf.real(in.nextDouble());
            case STRING:
              return vf.real(in.nextString());
            case NULL:
              in.nextNull();
              return inferNullValue(nulls, type);
            default:
              throw new IOException("Expected integer but got " + in.peek());
          }
        }
        catch (NumberFormatException e) {
          throw new IOException("Expected integer but got " + e.getMessage());
        }
      }
      
      private IValue inferNullValue(Map<Type, IValue> nulls, Type expected) {
          return nulls.keySet().stream()
            .sorted((x,y) -> x.compareTo(y))                         // smaller types are matched first 
            .filter(superType -> expected.isSubtypeOf(superType))    // remove any type that does not fit
            .findFirst()                                             // give the most specific match
            .map(t -> nulls.get(t))                                  // lookup the corresponding null value
            .orElse(null);                                     // or muddle on and throw NPE elsewhere

          // The NPE triggering "elsewhere" should help with fault localization. 
      }

      @Override
      public IValue visitExternal(Type type) throws IOException {
        throw new IOException("External type " + type + "is not implemented yet by the json reader:" + in.getPath());
      }
      
      @Override
      public IValue visitString(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }
        
        return vf.string(in.nextString());
      }
      
      @Override
      public IValue visitTuple(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }

        List<IValue> l = new ArrayList<>();
        in.beginArray();
        
        if (type.hasFieldNames()) {
          for (int i = 0; i < type.getArity(); i++) {
            l.add(read(in, type.getFieldType(i)));
          }
        }
        else {
          for (int i = 0; i < type.getArity(); i++) {
            l.add(read(in, type.getFieldType(i)));
          }
        }

        in.endArray();
        return vf.tuple(l.toArray(new IValue[l.size()]));
      }
      
      @Override
      public IValue visitVoid(Type type) throws IOException {
        throw new IOException("Can not read json values of type void: " + in.getPath());
      }

      @Override
      public IValue visitFunction(Type type) throws IOException {
        throw new IOException("Can not read json values of function types: " + in.getPath());
      }
      
      @Override
      public IValue visitSourceLocation(Type type) throws IOException {
        switch (in.peek()) {
          case STRING:
            return sourceLocationString();
          case BEGIN_OBJECT:
            return sourceLocationObject();
            default:
              throw new IOException("Could not find string or source location object here: " + in.getPath());
        }
      }        
        
      private IValue sourceLocationObject() throws IOException {
        String scheme = null;
        String authority = null;
        String path = null;
        String fragment = "";
        String query = "";
        int offset = -1;
        int length = -1;
        int beginLine = -1;
        int endLine = -1;
        int beginColumn = -1;
        int endColumn = -1;
        
        in.beginObject();
        
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "scheme":
                    scheme = in.nextString();
                    break;
                case "authority":
                    authority = in.nextString();
                    break;
                case "path":
                    path = in.nextString();
                    break;
                case "fragment":
                    fragment = in.nextString();
                    break;
                case "query":
                    query = in.nextString();
                    break;
                case "offset":
                    offset = in.nextInt();
                    break;
                case "length":
                    length = in.nextInt();
                    break;
                case "start":
                case "begin":
                    in.beginArray();
                    beginLine = in.nextInt();
                    beginColumn = in.nextInt();
                    in.endArray();
                    break;
                case "end":
                    in.beginArray();
                    endLine = in.nextInt();
                    endColumn = in.nextInt();
                    in.endArray();
                    break;
                default:
                    throw new IOException("unexpected property name " + name + " :" + in.getPath());
            }
        }
        
        in.endObject();
        try {
          ISourceLocation root;
          if (scheme != null && authority != null && query != null && fragment != null) {
              root = vf.sourceLocation(scheme, authority, path, query, fragment);
          }
          else if (scheme != null) {
              root = vf.sourceLocation(scheme, authority == null ? "" : authority, path);
          }
          else if (path != null) {
              root = URIUtil.createFileLocation(path);
          }
          else {
            throw new IOException("Could not parse complete source location: " + in.getPath());
          }
          if (offset != -1 && length != -1 && beginLine != -1 && endLine != -1 && beginColumn != -1 && endColumn != -1) {
              return vf.sourceLocation(root, offset, length, beginLine, endLine, beginColumn, endColumn);
          }
          if (offset != -1 && length != -1) {
              return vf.sourceLocation(root, offset, length);
          }
          return root;
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
      }
      
      @Override
      public IValue visitValue(Type type) throws IOException {
        switch (in.peek()) {
          case NUMBER:
            try {
              return vf.integer(in.nextLong());
            } catch (NumberFormatException e) {
                return vf.real(in.nextDouble());
            }
          case STRING:
            return visitString(TF.stringType());
          case BEGIN_ARRAY:
            return visitList(TF.listType(TF.valueType()));
          case BEGIN_OBJECT:
            return visitNode(TF.nodeType());
          case BOOLEAN:
            return visitBool(TF.nodeType());
          case NAME:
            // this would be weird though
            return vf.string(in.nextName());
          case NULL:
            in.nextNull();
            return inferNullValue(nulls, type);
          default:
            throw new IOException("Did not expect end of Json value here, while looking for " + type + " + at " + in.getPath());
        }
      }
      
      private IValue sourceLocationString() throws IOException {  
          try {
            String val = in.nextString().trim();
            
            if (val.startsWith("|") && (val.endsWith("|") || val.endsWith(")"))) {
              return new StandardTextReader().read(vf, new StringReader(val));
            }
            else if (val.contains("://")) {
              return vf.sourceLocation(URIUtil.createFromEncoded(val));
            }
            else {
              // will be simple interpreted as an absolute file name
              return URIUtil.createFileLocation(val);
            }
          } catch (URISyntaxException e) {
            throw new IOException("could not parse URI:" + in.getPath() + " due to " + e.getMessage(), e);
          }
      }
      
      public IValue visitRational(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }
        
        switch (in.peek()) {
          case BEGIN_OBJECT:
            in.beginObject();
            IInteger nomO = null, denomO = null;
            while (in.hasNext()) {
              switch (in.nextName()) {
                case "nominator":
                  nomO = (IInteger) read(in, TF.integerType());
                case "denominator":
                  denomO = (IInteger) read(in, TF.integerType());
              }
            }

            in.endObject();

            if (nomO == null || denomO == null) {
              throw new IOException("Did not find all fields of expected rational at " + in.getPath());
            }

            return vf.rational(nomO, denomO);
          case BEGIN_ARRAY:
            in.beginArray();
            IInteger nomA = (IInteger) read(in, TF.integerType());
            IInteger denomA = (IInteger) read(in, TF.integerType());
            in.endArray();
            return vf.rational(nomA, denomA);
          case STRING:
            return vf.rational(in.nextString());
          default:
            throw new IOException("Expected integer but got " + in.peek());
        }
      }
      
      @Override
      public IValue visitMap(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }
        IMapWriter w = vf.mapWriter();
        
        switch (in.peek()) {
          case BEGIN_OBJECT:
            in.beginObject();
            if (!type.getKeyType().isString()) {
              throw new IOException("Can not read JSon object as a map if the key type of the map (" + type + ") is not a string at " + in.getPath());
            }
            
            while (in.hasNext()) {
              w.put(vf.string(in.nextName()), read(in, type.getValueType()));
            }
            in.endObject();
            return w.done();
          case BEGIN_ARRAY:
            in.beginArray();
            while (in.hasNext()) {
              in.beginArray();
              IValue key = read(in, type.getKeyType());
              IValue value = read(in, type.getValueType());
              w.put(key,value);
              in.endArray();
            }
            in.endArray();
            return w.done();
          default:
            throw new IOException("Expected a map encoded as an object or an nested array to match " + type);  
        }
      }
      
      @Override
      public IValue visitAlias(Type type) throws IOException {
        while (type.isAliased()) {
          type = type.getAliased();
        }
        
        return type.accept(this);
      }
      
      @Override
      public IValue visitBool(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }
        return vf.bool(in.nextBoolean());
      }
      
      private int getPos() {
        if (src == null) {
          return 0;
        }

        try {
          return (int) posHandler.get(in) - 1;
        }
        catch (IllegalArgumentException | SecurityException e) {
          // stop trying to recover the positions
          src = null;
          return 0;
        }
      }

      private int getLine() {
        if (src == null) {
          return 0;
        }

        try {
           return (int) lineHandler.get(in) + 1;
        }
        catch (IllegalArgumentException | SecurityException e) {
           // stop trying to recover the positions
           src = null;
           return 0;
        }
      }

      private int getCol() {
        if (src == null) {
          return 0;
        }

        try {
          return getPos() - (int) lineStartHandler.get(in);
        }
        catch (IllegalArgumentException | SecurityException e) {
          // stop trying to recover the positions
          src = null;
          return 0;
        }
      }


      @Override
      public IValue visitAbstractData(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }

        if (in.peek() == JsonToken.STRING) {
          var stringInput = in.nextString();

          // might be a parsable string. let's see.
          if (parsers != null) {
            var reified = new org.rascalmpl.types.TypeReifier(vf).typeToValue(type, new TypeStore(), vf.map()); 
            
            try {
               return parsers.call(Collections.emptyMap(), reified, vf.string(stringInput));
            }
            catch (Throw t) { 
              Type excType = t.getException().getType();

              if (excType.isAbstractData() && ((IConstructor) t.getException()).getConstructorType().getName().equals("ParseError")) {
                  throw new IOException(t); // an actual parse error is meaningful to report
              }
              // otherwise we fall through to enum recognition
            }
          }

          // enum!
          Set<Type> enumCons = store.lookupConstructor(type, stringInput);

          for (Type candidate : enumCons) {
            if (candidate.getArity() == 0) {
                return vf.constructor(candidate);
            }
          }
          
          if (parsers != null) {
            throw new IOException("parser failed to recognize \"" + stringInput + "\" and no nullary constructor found for " + type + "either");
          }
          else {
            throw new IOException("no nullary constructor found for " + type);
          }
        }

        assert in.peek() == JsonToken.BEGIN_OBJECT || in.peek() == JsonToken.NULL;

        Set<Type> alternatives = store.lookupAlternatives(type);
        if (alternatives.size() > 1) {
          monitor.warning("selecting arbitrary constructor for " + type, vf.sourceLocation(in.getPath()));
        }
        Type cons = alternatives.iterator().next();
       
        in.beginObject();
        int startPos = getPos();
        int startLine = getLine();
        int startCol = getCol();
        
        IValue[] args = new IValue[cons.getArity()];
        Map<String,IValue> kwParams = new HashMap<>();
        
        if (!cons.hasFieldNames() && cons.getArity() != 0) {
          throw new IOException("For the object encoding constructors must have field names " + in.getPath());
        }
        
        while (in.hasNext()) {
          String label = in.nextName();
          if (cons.hasField(label)) {
            IValue val = read(in, cons.getFieldType(label));
            if (val != null) {
              args[cons.getFieldIndex(label)] = val;
            }
            else {
              throw new IOException("Could not parse argument " + label + ":" + in.getPath());
            }
          }
          else if (cons.hasKeywordField(label, store)) {
            if (!isNull()) { // lookahead for null to give default parameters the preference.
              IValue val = read(in, store.getKeywordParameterType(cons, label));
              // null can still happen if the nulls map doesn't have a default
              if (val != null) {
                // if the value is null we'd use the default value of the defined field in the constructor
                kwParams.put(label, val);
              }
            }
          }
          else { // its a normal arg, pass its label to the child
            throw new IOException("Unknown field " + label + ":" + in.getPath());
          }
        }
        
        in.endObject();
        int endPos = getPos();
        int endLine = getLine();
        int endCol = getCol();
        
        for (int i = 0; i < args.length; i++) {
          if (args[i] == null) {
            throw new IOException("Missing argument " + cons.getFieldName(i) + " to " + cons + ":" + in.getPath());
          }
        }
        
        if (src != null) {
          kwParams.put(kwParams.containsKey("src") ? "rascal-src" : "src", vf.sourceLocation(src, startPos, endPos - startPos + 1, startLine, endLine, startCol, endCol + 1));
        }


        return vf.constructor(cons, args, kwParams);
      }
      
      @Override
      public IValue visitConstructor(Type type) throws IOException {
        return read(in, type.getAbstractDataType());
      }
      
      @Override
      public IValue visitNode(Type type) throws IOException {
        in.beginObject();
        int startPos = getPos();
        int startLine = getLine();
        int startCol = getCol();
       
        Map<String,IValue> kws = new HashMap<>();
        
        while (in.hasNext()) {
          String kwName = in.nextName();
          IValue value = read(in, TF.valueType());
          
          if (value != null) {
            kws.put(kwName, value);
          }
        }
        
        in.endObject();
        int endPos = getPos();
        int endLine = getLine();
        int endCol = getCol();

        if (src != null) {
          kws.put(kws.containsKey("src") ? "rascal-src" : "src", vf.sourceLocation(src, startPos, endPos - startPos + 1, startLine, endLine, startCol, endCol + 1));
        }
        
        return vf.node("object", new IValue[] { }, kws);
      }
      
      @Override
      public IValue visitNumber(Type type) throws IOException {
        return visitInteger(type);
      }
      
      @Override
      public IValue visitParameter(Type type) throws IOException {
        return type.getBound().accept(this);
      }
      
      @Override
      public IValue visitDateTime(Type type) throws IOException {
        try {
          switch (in.peek()) {
            case STRING:
              Date parsedDate = format.get().parse(in.nextString());
              return vf.datetime(parsedDate.toInstant().toEpochMilli());
            case NUMBER:
              return vf.datetime(in.nextLong());
            default:
              throw new IOException("Expected a datetime instant " + in.getPath());
          }
        } catch (ParseException e) {
          throw new IOException("Could not parse date: " + in.getPath());
        }
      }
      
      @Override
      public IValue visitList(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }

        IListWriter w = vf.listWriter();
        in.beginArray();
        while (in.hasNext()) {
          // here we pass label from the higher context
          w.append(read(in, type.getElementType()));
        }

        in.endArray();
        return w.done();
      }
      
      public IValue visitSet(Type type) throws IOException {
        if (isNull()) {
          return inferNullValue(nulls, type);
        }

        ISetWriter w = vf.setWriter();
        in.beginArray();
        while (in.hasNext()) {
          // here we pass label from the higher context
          w.insert(read(in, type.getElementType()));
        }

        in.endArray();
        return w.done();
      }

      private boolean isNull() throws IOException {
        // we use null in JSon to encode optional values.
        // this will be mapped to keyword parameters in Rascal,
        // or an exception if we really need a value
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return true;
        }
        return false;
      }

    });
    
    return res;
  }


}