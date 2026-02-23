/**
 * Copyright (c) 2016, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.library.lang.json.internal;

import java.io.EOFException;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
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
import java.util.Map.Entry;
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

import com.google.gson.JsonParseException;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

/**
 * This class streams a JSON stream directly to an IValue representation and validates the content
 * to a given type as declared in a given type store. See the Rascal file lang::json::IO::readJson
 * for documentation.
 */
public class JsonValueReader {
    private static final TypeFactory TF = TypeFactory.getInstance();
    private final TypeStore store;
    private final IValueFactory vf; 
    private final IRascalMonitor monitor;
    private final ISourceLocation src;
    private VarHandle posHandler;
    private VarHandle lineHandler;
    private VarHandle lineStartHandler;
    
    /* options */
    private ThreadLocal<SimpleDateFormat> format;
    private boolean trackOrigins = false;
    private boolean stopTracking = false;
    private boolean explicitConstructorNames;
    private boolean explicitDataTypes;
    private boolean lenient;
    private IFunction parsers;
    private Map<Type, IValue> nulls = Collections.emptyMap();
    

    private final class ExpectedTypeDispatcher implements ITypeVisitor<IValue, IOException> {
        private final JsonReader in;
        private final OriginTrackingReader tracker;

        /**
         * In this mode we read directly from a given JsonReader, under which we can not
         * encapsulate its Reader for counting offsets. This is used by the JSON-RPC bridge.
         * @param in
         */
        private ExpectedTypeDispatcher(JsonReader in) {
            this(in, null);
        }

        /**
         * In this mode we have created an OriginTrackingReader which feeds the JsonReader from below.
         * Accurate offsets can be tracked like this, which enables accurate error locations. When
         * trackOrigins=true we get accurate origin src fields for objects.
         */
        public ExpectedTypeDispatcher(JsonReader in, OriginTrackingReader tracker) {
            this.in = in;
            this.tracker = tracker;
        }

        @Override
        public IValue visitInteger(Type type) throws IOException {
            
            try {
                switch (in.peek()) {
                    case NUMBER:
                        // fallthrough
                    case STRING:
                        return vf.integer(in.nextString());
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
                        // fallthrough
                    case STRING:
                        return vf.real(in.nextString());
                    case NULL:
                        in.nextNull();
                        return inferNullValue(nulls, type);
                    default:
                        throw parseErrorHere("Expected real but got " + in.peek());
                }
            }
            catch (NumberFormatException e) {
                throw parseErrorHere("Expected real but got " + e.getMessage());
            }
        }

        private IValue inferNullValue(Map<Type, IValue> nulls, Type expected) {
            return nulls.entrySet().stream().map(Entry::getKey).sorted(Type::compareTo)
                // give the most specific match:
                .filter(superType -> expected.isSubtypeOf(superType)).findFirst()
                // lookup the corresponding null value
                .map(t -> nulls.get(t)) 
                // the value in the table still has to fit the currently expected type
                .filter(r -> r.getType().isSubtypeOf(expected)) 
                // or we muddle on and throw NPE elsewhere. This NPE is important for fault localization. We don't want to hide it here.
                .orElse(null); 
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

            return vf.string(in.nextString());
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
                    l.add(type.getFieldType(i).accept(this));
                }
            }
            else {
                for (int i = 0; i < type.getArity(); i++) {
                    l.add(type.getFieldType(i).accept(this));
                }
            }

            in.endArray();

            // filter all the null values
            l.forEach(e -> {
                if (e == null) {
                    throw parseErrorHere("Tuples can not have null elements.");
                }
            });

            assert type.getArity() == l.size();

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
            if (isNull()) {
                return inferNullValue(nulls, type);
            }

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
                if (offset != -1 && length != -1 && beginLine != -1 && endLine != -1 && beginColumn != -1
                    && endColumn != -1) {
                    return vf.sourceLocation(root, offset, length, beginLine, endLine, beginColumn, endColumn);
                }
                if (offset != -1 && length != -1) {
                    return vf.sourceLocation(root, offset, length);
                }
                return root;
            }
            catch (URISyntaxException e) {
                throw parseErrorHere(e.getMessage());
            }
        }

        @Override
        public IValue visitValue(Type type) throws IOException {
            switch (in.peek()) {
                case NUMBER:
                    return visitNumber(TF.numberType());
                case STRING:
                    return visitString(TF.stringType());
                case BEGIN_ARRAY:
                    return visitList(TF.listType(TF.valueType()));
                case BEGIN_OBJECT:
                    return visitNode(TF.nodeType());
                case BOOLEAN:
                    return visitBool(TF.boolType());
                case NAME:
                    // this would be weird though. names are part of objects, not top-level values.
                    // this is probably unreachable given the rest of this parser.
                    return vf.string(in.nextName());
                case NULL:
                    in.nextNull();
                    return inferNullValue(nulls, type);
                default:
                    throw parseErrorHere(
                        "Did not expect end of Json value here, while looking for " + type + " + at " + in.getPath());
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
                    IInteger numA = (IInteger) TF.integerType().accept(this);
                    IInteger denomA = (IInteger) TF.integerType().accept(this);
                    in.endArray();
                    return vf.rational(numA, denomA);
                case STRING:
                    return vf.rational(in.nextString());
                default:
                    throw parseErrorHere("Expected rational but got " + in.peek());
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
                        throw parseErrorHere("Can not read JSon object as a map if the key type of the map (" + type
                            + ") is not a string at " + in.getPath());
                    }

                    while (in.hasNext()) {
                        IString label = vf.string(in.nextName());
                        IValue value = type.getValueType().accept(this);
                        if (value != null) {
                            w.put(label, value);
                        }
                    }
                    in.endObject();
                    return w.done();
                case BEGIN_ARRAY:
                    in.beginArray();
                    while (in.hasNext()) {
                        in.beginArray();
                        IValue key = type.getKeyType().accept(this);
                        IValue value = type.getValueType().accept(this);
                        if (key != null && value != null) {
                            w.put(key, value);
                        }
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

        /**
         * @return the offset where the parser cursor is _right now_. 
         * and `this.lastPos` is set to the last internal `pos` field of the GsonReader `in`
         * and `offset` is set to the current character offset in the input.
         * 
         * This method depends on arbitraire private details of JsonReader from the gson package.
         * In particular it tries to detect when its internal buffer has wrapped (probably at 1024 characters)
         * The internal private field `pos` in JsonReader holds the index into the buffer, and it is 
         * advanced by 1 with every character. We use it to update a locale file offset field, and we
         * have to watch out for the buffer reset (pos is set to 0 again) while doing this.
         * 
         * KNOWN BUG: These tricks break when a comment or whitespace section between normal tokens is larger
         * than or equal to 1024 Java chars. getPos() will not be able to detect the offset increase,
         * because it has not been called in between from JsonValueReader to JsonReader and the condition
         * `internalPos < lastPos` will not have had the opportunity to evaluate to `true`.
         */
        private int getOffset() {
            if (stopTracking) {
                return 0;
            }

            try {
                assert posHandler != null;
                var internalPos = (int) posHandler.get(in);
                
                return tracker.getOffsetAtBufferPos(internalPos);
            }
            catch (IllegalArgumentException | SecurityException e) {
                // we stop trying to track positions if it fails so hard,
                // this way we at least can get some form of DOM back.
                stopTracking = true;
                return 0;
            }
        }

        private int getLine() {
            if (stopTracking) {
                return 1;
            }

            try {
                var internalPos = (int) posHandler.get(in);
                return tracker.getLineAtBufferPos(internalPos);
            }
            catch (IllegalArgumentException | SecurityException e) {
                // stop trying to recover the positions
                stopTracking = true;
                return 1;
            }
        }

        /**
         * We try to recover the column position. This used internal private fields
         * of the GsonReader class. 
         * 
         *
         * @return the column position the parser is at currently.
         */
        private int getCol() {
            if (stopTracking) {
                return 0;
            }

            try {
                assert posHandler != null;
                var internalPos = (int) posHandler.get(in);

                return tracker.getColumnAtBufferPos(internalPos);
            }
            catch (IllegalArgumentException | SecurityException e) {
                // stop trying to recover the positions
                stopTracking = true;
                return 0;
            }
        }

        protected Throw parseErrorHere(String cause) {
            var location = getRootLoc();
            int offset = getOffset();
            int line = getLine();
            int col = getCol();

            if (!stopTracking) {
                return RuntimeExceptionFactory
                    .jsonParseError(vf.sourceLocation(location, offset, 1, line, line, col, col + 1), cause, in.getPath());
            }
            else {
                // if we didn't track the offset, we can at least produce line and column information, but not as a 
                // default Rascal ParseError with '0' or '-1' for offset, because that can trigger assertions and 
                // break other assumptions clients make about the source location values.
                return RuntimeExceptionFactory
                    .jsonParseError(location, line, col, cause, in.getPath());
            }
        }

        /**
         * Expecting an ADT we found NULL on the lookahead. This is either a Maybe or we can use the map of
         * null values.
         */
        private IValue visitNullAsAbstractData(Type type) {
            return inferNullValue(nulls, type);
        }

        /**
         * Expecting an ADT we found a string value instead. Now we can (try to) apply the parsers that were
         * passed in. If that does not fly, we can interpret strings as nullary ADT constructors.
         */
        private IValue visitStringAsAbstractData(Type type) throws IOException {
            var stringInput = in.nextString();

            // might be a parsable string. let's see.
            if (parsers != null) {
                var reified = new org.rascalmpl.types.TypeReifier(vf).typeToValue(type, new TypeStore(), vf.map());

                try {
                    return parsers.call(Collections.emptyMap(), reified, vf.string(stringInput));
                }
                catch (Throw t) {
                    Type excType = t.getException().getType();

                    if (excType.isAbstractData()
                        && ((IConstructor) t.getException()).getConstructorType().getName().equals("ParseError")) {
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
                throw parseErrorHere("parser failed to recognize \"" + stringInput
                    + "\" and no nullary constructor found for " + type + "either");
            }
            else {
                throw parseErrorHere("no nullary constructor found for " + type + ", that matches " + stringInput);
            }
        }

        /**
         * This is the main workhorse. Every object is mapped one-to-one to an ADT constructor instance. The
         * field names (keyword parameters and positional) are mapped to field names of the object. The name
         * of the constructor is _not_ consequential.
         * 
         * @param type
         * @return
         * @throws IOException
         */
        private IValue visitObjectAsAbstractData(Type type) throws IOException {
            Set<Type> alternatives = null;

            int startPos = Math.max(getOffset() - 1, 0);
            int startLine = getLine();
            int startCol = getCol() - 1;

            in.beginObject();
            
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
            Map<String, IValue> kwParams = new HashMap<>();

            if (!cons.hasFieldNames() && cons.getArity() != 0) {
                throw parseErrorHere("For the object encoding constructors must have field names " + in.getPath());
            }

            while (in.hasNext()) {
                String label = in.nextName();
                if (cons.hasField(label)) {
                    IValue val = cons.getFieldType(label).accept(this);
                    if (val != null) {
                        args[cons.getFieldIndex(label)] = val;
                    }
                    else {
                        throw parseErrorHere("Could not parse argument " + label + ":" + in.getPath());
                    }
                }
                else if (cons.hasKeywordField(label, store)) {
                    if (!isNull()) { // lookahead for null to give default parameters the preference.
                        IValue val = store.getKeywordParameterType(cons, label).accept(this);
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

            int endPos = Math.max(getOffset() - 1, 0);
            assert endPos > startPos : "offset tracking messed up while stopTracking is " + stopTracking + " and trackOrigins is " + trackOrigins;

            int endLine = getLine();
            int endCol = getCol() - 1;

            in.endObject();
            
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    throw parseErrorHere(
                        "Missing argument " + cons.getFieldName(i) + " to " + cons + ":" + in.getPath());
                }
            }

            if (trackOrigins && !stopTracking) {
                kwParams.put(kwParams.containsKey("src") ? "rascal-src" : "src",
                    vf.sourceLocation(getRootLoc(), startPos, endPos - startPos + 1, startLine, endLine, startCol, endCol + 1));
            }

            return vf.constructor(cons, args, kwParams);
        }

        private ISourceLocation getRootLoc() {
            if (src == null) {
                return URIUtil.rootLocation("unknown");
            }
            else {
                return src;
            }
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
            return type.getAbstractDataType().accept(this);
        }

        @Override
        public IValue visitNode(Type type) throws IOException {
            if (isNull()) {
                return inferNullValue(nulls, type);
            }

            in.beginObject();

            int startPos = Math.max(getOffset(), 0);
            int startLine = getLine();
            int startCol = getCol();

            
            Map<String, IValue> kws = new HashMap<>();
            Map<String, IValue> args = new HashMap<>();

            String name = "object";

            while (in.hasNext()) {
                String kwName = in.nextName();

                if (kwName.equals("_name")) {
                    name = ((IString) TF.stringType().accept(this)).getValue();
                    continue;
                }

                boolean positioned = kwName.startsWith("__arg");

                if (!isNull()) { // lookahead for null to give default parameters the preference.
                    IValue val = TF.valueType().accept(this);

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

            int endPos = Math.max(getOffset(), 0);
            int endLine = getLine();
            int endCol = getCol() - 1;

            
            if (trackOrigins && !stopTracking) {
                kws.put(kws.containsKey("src") ? "rascal-src" : "src",
                    vf.sourceLocation(getRootLoc(), startPos, endPos - startPos + 1, startLine, endLine, startCol, endCol + 1));
            }

            IValue[] argArray = args.entrySet().stream().sorted((e, f) -> e.getKey().compareTo(f.getKey()))
                .filter(e -> e.getValue() != null).map(e -> e.getValue()).toArray(IValue[]::new);

            return vf.node(name, argArray, kws);
        }

        @Override
        public IValue visitNumber(Type type) throws IOException {
            if (isNull()) {
                return inferNullValue(nulls, type);
            }

            if (in.peek() == JsonToken.BEGIN_ARRAY) {
                return visitRational(type);
            }

            String numberString = in.nextString();

            if (numberString.contains("r")) {
                return vf.rational(numberString);
            }
            if (numberString.matches(".*[\\.eE].*")) {
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
                        Date parsedDate = format.get().parse(in.nextString());
                        return vf.datetime(parsedDate.toInstant().toEpochMilli());
                    case NUMBER:
                        return vf.datetime(in.nextLong());
                    default:
                        throw parseErrorHere("Expected a datetime instant " + in.getPath());
                }
            }
            catch (ParseException e) {
                throw parseErrorHere("Could not parse date: " + in.getPath());
            }
        }

        @Override
        public IValue visitList(Type type) throws IOException {
            if (isNull()) {
                return inferNullValue(nulls, type);
            }

            IListWriter w = vf.listWriter();
            getOffset();
            in.beginArray();
            while (in.hasNext()) {
                // here we pass label from the higher context
                IValue elem =
                    isNull() ? inferNullValue(nulls, type.getElementType()) : type.getElementType().accept(this);

                if (elem != null) {
                    w.append(elem);
                }
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
                IValue elem =
                    isNull() ? inferNullValue(nulls, type.getElementType()) : type.getElementType().accept(this);

                if (elem != null) {
                    w.insert(elem);
                }
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


    /**
     * @param vf factory which will be used to construct values
     * @param store type store to lookup constructors of abstract data-types in and the types of keyword
     *        fields
     * @param monitor provides progress reports and warnings
     * @param src loc to use to identify the entire file.
     */
    public JsonValueReader(IValueFactory vf, TypeStore store, IRascalMonitor monitor, ISourceLocation src) {
        this.vf = vf;
        this.store = store;
        this.monitor = monitor;
        this.src = src;
        this.stopTracking = false;

        setCalendarFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        try {
            var lookup = MethodHandles.lookup();
            var privateLookup = MethodHandles.privateLookupIn(JsonReader.class, lookup);
            this.posHandler = privateLookup.findVarHandle(JsonReader.class, "pos", int.class);
            this.lineHandler = privateLookup.findVarHandle(JsonReader.class, "lineNumber", int.class);
            this.lineStartHandler = privateLookup.findVarHandle(JsonReader.class, "lineStart", int.class);

            if (posHandler == null) {
                stopTracking = true;
            }
        }
        catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            // we disable the origin tracking if we can not get to the fields
            stopTracking = true;
            monitor.warning("Unable to retrieve origin information due to: " + e.getMessage(), src);
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
     * @param vf factory which will be used to construct values
     * @param store type store to lookup constructors of abstract data-types in and the types of keyword
     *        fields
     */
    public JsonValueReader(IRascalValueFactory vf, TypeStore store, IRascalMonitor monitor, ISourceLocation src) {
        this.vf = vf;
        this.store = store;
        this.monitor = monitor;
        this.src = src;

        setCalendarFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        // this is for origin tracking as well as accurate parse errors
        try {
            var lookup = MethodHandles.lookup();
            var privateLookup = MethodHandles.privateLookupIn(JsonReader.class, lookup);
            this.posHandler = privateLookup.findVarHandle(JsonReader.class, "pos", int.class);
            this.lineHandler = privateLookup.findVarHandle(JsonReader.class, "lineNumber", int.class);
            this.lineStartHandler = privateLookup.findVarHandle(JsonReader.class, "lineStart", int.class);
        }
        catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            // we disable the origin tracking if we can not get to the fields
            stopTracking = true;
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

    public JsonValueReader setTrackOrigins(boolean trackOrigins) {
        this.trackOrigins = trackOrigins;
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

    public JsonValueReader setLenient(boolean value) {
        this.lenient = true;
        return this;
    }

    /**
     * Read and validate a Json stream as an IValue. This version does not support accurate error messages
     * or origin tracking.
     * 
     * @param in json stream
     * @param expected type to validate against (recursively)
     * @return an IValue of the expected type
     * @throws IOException when either a parse error or a validation error occurs
     */
    public IValue read(JsonReader in, Type expected) throws IOException {
        in.setStrictness(lenient ? Strictness.LENIENT : Strictness.LEGACY_STRICT);
        
        // we can't track accurately because we don't have a handle to the raw buffer under `in`
        this.stopTracking = true;
        var dispatch = new ExpectedTypeDispatcher(in);

        try {
            var result = expected.accept(dispatch);
            if (result == null) {
                throw new JsonParseException(
                    "null occurred outside an optionality context and without a registered representation.");
            }
            return result;
        }
        catch (EOFException | JsonParseException | NumberFormatException | MalformedJsonException
            | IllegalStateException | NullPointerException e) {
            throw dispatch.parseErrorHere(e.getMessage());
        }
    }

    /**
     * Read and validate a Json stream as an IValue. This version supports accurate error messages
     * and origin tracking.
     * 
     * @param in json stream
     * @param expected type to validate against (recursively)
     * @return an IValue of the expected type
     * @throws IOException when either a parse error or a validation error occurs
     */
    public IValue read(Reader in, Type expected) throws IOException {
        try (OriginTrackingReader wrappedIn = new OriginTrackingReader(in); JsonReader jsonIn = new JsonReader(wrappedIn)) {
            jsonIn.setStrictness(lenient ? Strictness.LENIENT : Strictness.LEGACY_STRICT);

            var dispatch = new ExpectedTypeDispatcher(jsonIn, wrappedIn);

            try {
                var result = expected.accept(dispatch);
                if (result == null) {
                    throw new JsonParseException("null occurred outside an optionality context and without a registered representation.");
                }
                return result;
            } 
            catch (NullPointerException e) {
                throw dispatch.parseErrorHere("Unexpected internal NullPointerException");
            }
            catch (EOFException | JsonParseException | NumberFormatException | MalformedJsonException | IllegalStateException e) {
                throw dispatch.parseErrorHere(e.getMessage());
            }
        }
    }

    /**
     * This wraps a normal reader to make it possible for a client to detect accurate
     * character offsets (> buffersize) in a large file, even if the underlying stream is buffered.
     * 
     * This implementation is tightly coupled (semantically) with the internals of JsonReader. It provides
     * just enough information, together with internal private fields of JsonReader, to compute Rascal-required
     * offsets. We get only the character offset in the file, at the start of each streamed buffer contents.
     * That should be just enough information to recompute the actual offset of every Json element, using the
     * current position in the buffer (the private field `pos` of JsonReader).
     */
    public static class OriginTrackingReader extends FilterReader {
        // TODO: some of these fields may be derived from one another for speed or space reasons.

        // offset is a codepoint counter which always represents the point in the file where JsonReader.pos == 0
        private int offset = 0;
        // shift is the amount of high surrogate pairs encountered so far
        private int shift = 0;
        // columnShift is the amount of high surrogate pairs encountered on the current line
        private int columnShift = 0;
        // limit is always pointing to the amount of no-junk chars in the underlying buffer below buffer.length
        private int limit = 0;
        // the codepoints array maps char offsets to the number of codepoints since the start of the buffer
        private int[] codepoints = null;
        // columns maps char offsets to codepoint column positions
        private int[] columns = null;
        // lines maps char offsets to codepoint line numbers
        // TODO: lines may be superfluous if GsonReader can compute line numbers accurately for Unicode content already
        private int[] lines = null;

        // the current column position
        private int column = 0;
        // the current line
        private int line = 1;
        // the current amount of high surrogates counted in this buffer
        
        protected OriginTrackingReader(Reader in) {
            super(in);
        }

        /* This private method from JsonReader must be mirrored by `read`
        private boolean fillBuffer(int minimum) throws IOException {
            char[] buffer = this.buffer;
            lineStart -= pos;
            if (limit != pos) {
                limit -= pos;
                System.arraycopy(buffer, pos, buffer, 0, limit);
            } else {
                limit = 0;
            }

            pos = 0;
            int total;
            while ((total = in.read(buffer, limit, buffer.length - limit)) != -1) {
                limit += total;

                // if this is the first read, consume an optional byte order mark (BOM) if it exists
                if (lineNumber == 0 && lineStart == 0 && limit > 0 && buffer[0] == '\ufeff') {
                    pos++;
                    lineStart++;
                    minimum++;
                }

                if (limit >= minimum) {
                    return true;
                }
            }
            return false;
        } */
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            initializeBuffers(cbuf);

            // `codepoints[limit - 1] - 1` is the offset of the last character read with the previous call to read.
            // So the new offset starts there. We look back `off` chars because of possible left-overs before the limit.
            offset += (limit == 0 ? 0 : codepoints[limit - off - 1] + 1) ;

            // make sure we are only a transparant facade for the real reader. 
            // parameters are mapped one-to-one without mutations.
            var charsRead = in.read(cbuf, off, len);

            // now we simulate exactly what JsonReader does to `cbuf` on our administration of surrogate pairs:
            shiftRemaindersLeft(off);
            
            // the next buffer[0] offset will be after this increment.
            // Note that `fillBuffer.limit == read.limit`
            limit = off + charsRead;

            // and then we can fill our administration of surrogate pairs quickly
            precomputeSurrogatePairCompensation(cbuf, off, limit);

            // and return only the number of characters read.
            return charsRead;
        }

        /**
         * The cbuf char buffer may contain "surrogate pairs", where two int16 chars represent
         * one unicode codepoint. We want to count in codepoints so we store here for every
         * character in cbuf what it's codepoint offset is, what its codepoint column is
         * and what its codepoint line is. 
         * 
         * Later when the JSONValueReader needs to know "current positions", this OriginTrackerReader
         * will have the answers stored in its buffers.
         * 
         * @param cbuf
         * @param off
         * @param charsRead
         */
        private void precomputeSurrogatePairCompensation(char[] cbuf, int off, int limit) {
            // NB we assume here that the remainder of the content pos..limit has already been shifted to cbuf[0];
            // So codepoints[0..off], columns[0..off] and lines[0..off] have been filled already.
            for (int i = off; i < limit; i++) {
                codepoints[i] = i - shift;
                columns[i] = column - columnShift;
                lines[i] = line;

                if (Character.isHighSurrogate(cbuf[i])) { 
                    // for every high surrogate we assume a low surrogate will follow,
                    // and we count only one of them for the character offset by increasing `shift`
                    shift++;
                    columnShift++;
                    // do not assume the low surrogate is in the current buffer yet (boundary condition)
                }
                else if (cbuf[i] == '\n') {
                    line++;
                    column = 0;
                    columnShift = 0;
                }
                else {
                    column++;
                }
            }
        }

        private void shiftRemaindersLeft(int off) {
            if (off > 0) {
                System.arraycopy(codepoints, codepoints.length - off, codepoints, 0, off);
                System.arraycopy(columns, columns.length - off, columns, 0, off);
                System.arraycopy(lines, lines.length - off, lines, 0, off);
            }
        }

        private void initializeBuffers(char[] cbuf) {
            if (codepoints == null) {
                assert columns == null;
                assert lines == null;

                codepoints = new int[cbuf.length];
                columns = new int[cbuf.length];
                lines = new int[cbuf.length];
            }

            // nothing else changed in the mean time, especially not the length of cbuf.
            assert codepoints.length == cbuf.length;
            assert columns.length == cbuf.length;
            assert lines.length == cbuf.length;
        }

        /**
         * @return the codepoint offset (from the start of the streaming content)
         * for the character at char position `pos` in the last buffered content.
         */
        public int getOffsetAtBufferPos(int pos) {
            return (pos >= limit) ? (offset + codepoints[pos - 1] + 1) : (offset + codepoints[pos]);
        }

        /**
         * @return the codepoint column (from the start of the current line)
         * for the character at char position `pos` in the last buffered content.
         */
        public int getColumnAtBufferPos(int pos) {
            return (pos >= limit) ? column : columns[pos];
        } 
        
        /**
         * @return the codepoint line (from the start of the entire content)
         * for the character at char position `pos` in the last buffered content.
         */
        public int getLineAtBufferPos(int pos) {
            return (pos >= limit) ? line : lines[pos];
        } 
    }
}
