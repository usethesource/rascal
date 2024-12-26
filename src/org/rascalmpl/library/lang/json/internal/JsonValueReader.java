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

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.math.RoundingMode;
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
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.types.ReifiedType;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.maybe.UtilMaybe;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.ITypeVisitor;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

import com.google.common.math.DoubleMath;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

/**
 * This class streams a JSON stream directly to an IValue representation and validates the content
 * to a given type as declared in a given type store. See the Rascal file lang::json::IO::readJson for documentation.
 */
public class JsonValueReader {
  private final class ExpectedTypeDispatcher implements ITypeVisitor<IValue, IOException> {
    private final JsonReader in;
    private int lastPos;

    private ExpectedTypeDispatcher(JsonReader in) {
      this.in = in;
    }

    /** 
     * Wrapping in.nextString for better error reporting
     */
    private String nextString() throws IOException {
      // need to cache the last position before parsing the string, because
      // when the string does not have a balancing quote the read accidentally
      // rewinds the position to 0.
      lastPos=getPos();
      return in.nextString();
    }

    /** 
     * Wrapping in.nextName for better error reporting
     */
    private String nextName() throws IOException {
      // need to cache the last position before parsing the string, because
      // when the string does not have a balancing quote the read accidentally
      // rewinds the position to 0.
      lastPos=getPos();
      return in.nextName();
    }

    @Override
    public IValue visitInteger(Type type) throws IOException {
      try {
        switch (in.peek()) {
          case NUMBER:
            return vf.integer(in.nextLong());
          case STRING:
            return vf.integer(nextString());
          case NULL:
            in.nextNull();
            return inferNullValue(nulls, type);
          default:
            throw parseErrorHere("Expected integer but got " + in.peek());
        }
      }
      catch (NumberFormatException e) {
        throw parseErrorHere("Expected integer but got " + e.getMessage());
      }
    }

    @Override
    public IValue visitReal(Type type) throws IOException {
      try {
        switch (in.peek()) {
          case NUMBER:
            return vf.real(in.nextDouble());
          case STRING:
            return vf.real(nextString());
          case NULL:
            in.nextNull();
            return inferNullValue(nulls, type);
          default:
            throw parseErrorHere("Expected integer but got " + in.peek());
        }
      }
      catch (NumberFormatException e) {
        throw parseErrorHere("Expected integer but got " + e.getMessage());
      }
    }
  
    private IValue inferNullValue(Map<Type, IValue> nulls, Type expected) {
        return nulls.keySet().stream()
          .sorted((x,y) -> x.compareTo(y))                         // smaller types are matched first 
          .filter(superType -> expected.isSubtypeOf(superType))    // remove any type that does not fit
          .findFirst()                                             // give the most specific match
          .map(t -> nulls.get(t))                                  // lookup the corresponding null value
          .filter(r -> r.getType().isSubtypeOf(expected))          // the value in the table still has to fit the currently expected type
          .orElse(null);                                           // or muddle on and throw NPE elsewhere

        // The NPE triggering "elsewhere" should help with fault localization. 
    }

    @Override
    public IValue visitExternal(Type type) throws IOException {
      throw parseErrorHere("External type " + type + "is not implemented yet by the json reader:" + in.getPath());
    }

    @Override
    public IValue visitString(Type type) throws IOException {
      if (isNull()) {
        return inferNullValue(nulls, type);
      }
  
      return vf.string(nextString());
    }

      @Override
      public IValue visitTuple(Type type) throws IOException {
        if (isNull()) {
          return null;
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
      throw parseErrorHere("Can not read json values of type void: " + in.getPath());
    }

    @Override
    public IValue visitFunction(Type type) throws IOException {
      throw parseErrorHere("Can not read json values of function types: " + in.getPath());
    }

    @Override
    public IValue visitSourceLocation(Type type) throws IOException {
      switch (in.peek()) {
        case STRING:
          return sourceLocationString();
        case BEGIN_OBJECT:
          return sourceLocationObject();
          default:
            throw parseErrorHere("Could not find string or source location object here: " + in.getPath());
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
          String name = nextName();
          
          switch (name) {
              case "scheme":
                  scheme = nextString();
                  break;
              case "authority":
                  authority = nextString();
                  break;
              case "path":
                  path = nextString();
                  break;
              case "fragment":
                  fragment = nextString();
                  break;
              case "query":
                  query = nextString();
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
                  throw parseErrorHere("unexpected property name " + name + " :" + in.getPath());
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
          throw parseErrorHere("Could not parse complete source location: " + in.getPath());
        }
        if (offset != -1 && length != -1 && beginLine != -1 && endLine != -1 && beginColumn != -1 && endColumn != -1) {
            return vf.sourceLocation(root, offset, length, beginLine, endLine, beginColumn, endColumn);
        }
        if (offset != -1 && length != -1) {
            return vf.sourceLocation(root, offset, length);
        }
        return root;
      } catch (URISyntaxException e) {
          throw parseErrorHere(e.getMessage());
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
          return vf.string(nextName());
        case NULL:
          in.nextNull();
          return inferNullValue(nulls, type);
        default:
          throw parseErrorHere("Did not expect end of Json value here, while looking for " + type + " + at " + in.getPath());
      }
    }

    private IValue sourceLocationString() throws IOException {  
        try {
          String val = nextString().trim();
          
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
        } 
        catch (URISyntaxException e) {
          throw parseErrorHere(e.getMessage());
        }
    }

    @Override
    public IValue visitRational(Type type) throws IOException {
      if (isNull()) {
        return inferNullValue(nulls, type);
      }
      
      switch (in.peek()) {
        case BEGIN_ARRAY:
          in.beginArray();
          IInteger numA = (IInteger) read(in, TF.integerType());
          IInteger denomA = (IInteger) read(in, TF.integerType());
          in.endArray();
          return vf.rational(numA, denomA);
        case STRING:
          return vf.rational(nextString());
        default:
          throw parseErrorHere("Expected integer but got " + in.peek());
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
          if (!type.getKeyType().isString() && in.peek() != JsonToken.END_OBJECT) {
            throw parseErrorHere("Can not read JSon object as a map if the key type of the map (" + type + ") is not a string at " + in.getPath());
          }
          
          while (in.hasNext()) {
            w.put(vf.string(nextName()), read(in, type.getValueType()));
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
          throw parseErrorHere("Expected a map encoded as an object or an nested array to match " + type);  
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
        return Math.max(0, (int) posHandler.get(in) - 1);
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

    protected Throw parseErrorHere(String cause) {
      var location = src == null ?  URIUtil.rootLocation("unknown") : src;
      int offset = Math.max(getPos(), lastPos);
      int line = getLine();
      int col = getCol();
     
      return RuntimeExceptionFactory.jsonParseError(
          vf.sourceLocation(location,offset, 1, line, line, col, col + 1),
          cause,
          in.getPath());
    }

    /** 
     * Expecting an ADT we found NULL on the lookahead.
     * This is either a Maybe or we can use the map 
     * of null values.
     */
    private IValue visitNullAsAbstractData(Type type) {
      return inferNullValue(nulls, type);
    }

    /**
     * Expecting an ADT we found a string value instead.
     * Now we can (try to) apply the parsers that were passed in.
     * If that does not fly, we can interpret strings as nullary ADT
     * constructors.
     */
    private IValue visitStringAsAbstractData(Type type) throws IOException {
      var stringInput = nextString();

      // might be a parsable string. let's see.
      if (parsers != null) {
        var reified = new org.rascalmpl.types.TypeReifier(vf).typeToValue(type, new TypeStore(), vf.map()); 
        
        try {
           return parsers.call(Collections.emptyMap(), reified, vf.string(stringInput));
        }
        catch (Throw t) { 
          Type excType = t.getException().getType();

          if (excType.isAbstractData() && ((IConstructor) t.getException()).getConstructorType().getName().equals("ParseError")) {
              throw t; // that's a real parse error to report
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
        throw parseErrorHere("parser failed to recognize \"" + stringInput + "\" and no nullary constructor found for " + type + "either");
      }
      else {
        throw parseErrorHere("no nullary constructor found for " + type + ", that matches " + stringInput);
      }
    }

    /**
     * This is the main workhorse. Every object is mapped one-to-one to an ADT constructor
     * instance. The field names (keyword parameters and positional) are mapped to field
     * names of the object. The name of the constructor is _not_ consequential.
     * @param type
     * @return
     * @throws IOException
     */
    private IValue visitObjectAsAbstractData(Type type) throws IOException {
      Set<Type> alternatives = null;

      in.beginObject();
      int startPos = getPos();
      int startLine = getLine();
      int startCol = getCol();
      
      // use explicit information in the JSON to select and filter constructors from the TypeStore
      // we expect always to have the field _constructor before _type.
      if (explicitConstructorNames || explicitDataTypes) {
        String consName = null;
        String typeName = null; // this one is optional, and the order with cons is not defined.
        String consLabel = in.nextName();

        // first we read either a cons name or a type name
        if (explicitConstructorNames && "_constructor".equals(consLabel)) {
          consName = in.nextString();
        }
        else if (explicitDataTypes && "_type".equals(consLabel)) {
          typeName = in.nextString();
        }

        // optionally read the second field
        if (explicitDataTypes && typeName == null) {
          // we've read a constructor name, but we still need a type name
          consLabel = in.nextName();
          if (explicitDataTypes && "_type".equals(consLabel)) {
            typeName = in.nextString();
          }
        }
        else if (explicitDataTypes && consName == null) {
          // we've read type name, but we still need a constructor name
          consLabel = in.nextName();
          if (explicitDataTypes && "_constructor".equals(consLabel)) {
            consName = in.nextString();
          }
        }

        if (explicitDataTypes && typeName == null) {
            throw parseErrorHere("Missing a _type field: " + in.getPath());
        }
        else if (explicitConstructorNames && consName == null) {
            throw parseErrorHere("Missing a _constructor field: " + in.getPath());
        }

        if (typeName != null && consName != null) {
          // first focus on the given type name
          var dataType = TF.abstractDataType(store, typeName);
          alternatives = store.lookupConstructor(dataType, consName);
        }
        else {
          // we only have a constructor name
          // lookup over all data types by constructor name
          alternatives = store.lookupConstructors(consName);
        }
      }
      else {
        alternatives = store.lookupAlternatives(type);
      }

      if (alternatives.size() > 1) {
        monitor.warning("selecting arbitrary constructor for " + type, vf.sourceLocation(in.getPath()));
      }
      else if (alternatives.size() == 0) {
        throw parseErrorHere("No fitting constructor found for " + in.getPath());
      }
      
      Type cons = alternatives.iterator().next();
        
      IValue[] args = new IValue[cons.getArity()];
      Map<String,IValue> kwParams = new HashMap<>();
        
      if (!cons.hasFieldNames() && cons.getArity() != 0) {
        throw parseErrorHere("For the object encoding constructors must have field names " + in.getPath());
      }
    
      while (in.hasNext()) {
        String label = in.nextName();
        if (cons.hasField(label)) {
          IValue val = read(in, cons.getFieldType(label));
          if (val != null) {
            args[cons.getFieldIndex(label)] = val;
          }
          else {
            throw parseErrorHere("Could not parse argument " + label + ":" + in.getPath());
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
          else {
            var nullValue = inferNullValue(nulls, cons.getAbstractDataType());
            if (nullValue != null) {
              kwParams.put(label, nullValue);
            }
          }
        } 
        else { // its a normal arg, pass its label to the child
          if (!explicitConstructorNames && "_constructor".equals(label)) {
            // ignore additional _constructor fields.
            in.nextString(); // skip the constructor value
            continue;
          }
          else if (!explicitDataTypes && "_type".equals(label)) {
            // ignore additional _type fields.
            in.nextString(); // skip the type value
            continue;
          }
          else {
            // field label does not match data type definition
            throw parseErrorHere("Unknown field " + label + ":" + in.getPath());
          }
        }
      } 
        
      in.endObject();
      int endPos = getPos();
      int endLine = getLine();
      int endCol = getCol();
      
      for (int i = 0; i < args.length; i++) {
        if (args[i] == null) {
          throw parseErrorHere("Missing argument " + cons.getFieldName(i) + " to " + cons + ":" + in.getPath());
        }
      }
      
      if (src != null) {
        kwParams.put(kwParams.containsKey("src") ? "rascal-src" : "src", vf.sourceLocation(src, startPos, endPos - startPos + 1, startLine, endLine, startCol, endCol + 1));
      }

      return vf.constructor(cons, args, kwParams);
    }

    @Override
    public IValue visitAbstractData(Type type) throws IOException {
      if (UtilMaybe.isMaybe(type)) {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return UtilMaybe.nothing();
        }
        else {
          // dive into the wrapped type, and wrap the result. Could be a str, int, or anything.
          return UtilMaybe.just(type.getTypeParameters().getFieldType(0).accept(this));
        }
      }

      switch (in.peek()) {
        case NULL:
          return visitNullAsAbstractData(type);
        case STRING:
          return visitStringAsAbstractData(type);
        case BEGIN_OBJECT:
          return visitObjectAsAbstractData(type);
        default:
          throw parseErrorHere("Expected ADT:" + type + ", but found " + in.peek().toString());
      }
    }

    @Override
    public IValue visitConstructor(Type type) throws IOException {
      return read(in, type.getAbstractDataType());
    }

    @Override
    public IValue visitNode(Type type) throws IOException {
      if (isNull()) {
        return inferNullValue(nulls, type);
      }

      in.beginObject();
      int startPos = getPos();
      int startLine = getLine();
      int startCol = getCol();
     
      Map<String,IValue> kws = new HashMap<>();
      Map<String,IValue> args = new HashMap<>();
      
      String name = "object";

      while (in.hasNext()) {
        String kwName = nextName();

        if (kwName.equals("_name")) {
          name = ((IString) read(in, TF.stringType())).getValue();
          continue;
        }

        boolean positioned = kwName.startsWith("arg");

        if (!isNull()) { // lookahead for null to give default parameters the preference.
          IValue val = read(in, TF.valueType());
          
          if (val != null) {
              // if the value is null we'd use the default value of the defined field in the constructor
              (positioned ? args : kws).put(kwName, val);
          }
        }
        else {
          var nullValue = inferNullValue(nulls, TF.valueType());
          if (nullValue != null) {
            (positioned ? args : kws).put(kwName, nullValue);
          }
        }
      }
      
      in.endObject();
      int endPos = getPos();
      int endLine = getLine();
      int endCol = getCol();

      if (originTracking) {
        kws.put(kws.containsKey("src") ? "rascal-src" : "src", vf.sourceLocation(src, startPos, endPos - startPos + 1, startLine, endLine, startCol, endCol + 1));
      }
      
      IValue[] argArray = args.entrySet().stream()
        .sorted((e, f) -> e.getKey().compareTo(f.getKey()))
        .map(e -> e.getValue())
        .toArray(IValue[]::new);
      
      return vf.node(name, argArray, kws);  
    }

    @Override
    public IValue visitNumber(Type type) throws IOException {
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
          return visitRational(type);
        }
        
        String numberString = in.nextString();

        if (numberString.contains("r")) {
          return vf.rational(numberString);
        }
        if (numberString.matches("[\\.eE]")) {
          return vf.real(numberString);
        }
        else {
          return vf.integer(numberString);
        }
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
            lastPos = getPos();
            Date parsedDate = format.get().parse(nextString());
            return vf.datetime(parsedDate.toInstant().toEpochMilli());
          case NUMBER:
            return vf.datetime(in.nextLong());
          default:
            throw parseErrorHere("Expected a datetime instant " + in.getPath());
        }
      } catch (ParseException e) {
        throw parseErrorHere("Could not parse date: " + in.getPath());
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
  }

  private static final TypeFactory TF = TypeFactory.getInstance();
  private final TypeStore store;
  private final IValueFactory vf;
  private ThreadLocal<SimpleDateFormat> format;
  private final IRascalMonitor monitor;
  private ISourceLocation src;
  private boolean originTracking;
  private VarHandle posHandler;
  private VarHandle lineHandler;
  private VarHandle lineStartHandler;
  private boolean explicitConstructorNames;
  private boolean explicitDataTypes;
  private IFunction parsers;
  private Map<Type, IValue> nulls = Collections.emptyMap();
  
  /**
   * @param vf     factory which will be used to construct values
   * @param store  type store to lookup constructors of abstract data-types in and the types of keyword fields
   * @param monitor provides progress reports and warnings
   * @param src    loc to use to identify the entire file.
   */
  public JsonValueReader(IValueFactory vf, TypeStore store, IRascalMonitor monitor, ISourceLocation src) {
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
        this.originTracking = (src != null);
      }
      catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
        // we disable the origin tracking if we can not get to the fields
        src = null;
        originTracking = false;
        monitor.warning("Unable to retrieve origin information due to: " + e.getMessage(), src);
      }
    }
  }
  
  public JsonValueReader(IValueFactory vf, IRascalMonitor monitor, ISourceLocation src) {
    this(vf, new TypeStore(), monitor, src);
  }

  public JsonValueReader setExplicitConstructorNames(boolean value) {
    this.explicitConstructorNames = value;
    return this;
  }

  public JsonValueReader setExplicitDataTypes(boolean value) {
    this.explicitDataTypes = value;
    if (value) {
      this.explicitConstructorNames = true;
    }
    return this;
  }

  /**
   * @param vf     factory which will be used to construct values
   * @param store  type store to lookup constructors of abstract data-types in and the types of keyword fields
   */
  public JsonValueReader(IRascalValueFactory vf, TypeStore store, IRascalMonitor monitor, ISourceLocation src) {
    this.vf = vf;
    this.store = store;
    this.monitor = monitor;
    this.src = (src == null) ? URIUtil.rootLocation("unknown") : src;
   
    setCalendarFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    // this is for origin tracking as well as accurate parse errors
    try {
      var lookup = MethodHandles.lookup();
      var privateLookup = MethodHandles.privateLookupIn(JsonReader.class, lookup);
      this.posHandler = privateLookup.findVarHandle(JsonReader.class, "pos", int.class);
      this.lineHandler = privateLookup.findVarHandle(JsonReader.class, "lineNumber", int.class);
      this.lineStartHandler = privateLookup.findVarHandle(JsonReader.class, "lineStart", int.class);
      this.originTracking = (src != null);
    }
    catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
      // we disable the origin tracking if we can not get to the fields
      originTracking = false;
      src = null;
      monitor.warning("Unable to retrieve origin information due to: " + e.getMessage(), src);
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
    var dispatch = new ExpectedTypeDispatcher(in);

    try {
      return expected.accept(dispatch);
    }
    catch (EOFException | JsonParseException | NumberFormatException | MalformedJsonException | IllegalStateException | NullPointerException e) {
      throw dispatch.parseErrorHere(e.getMessage());
    }
  }
}
