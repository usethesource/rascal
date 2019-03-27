/** 
 * Copyright (c) 2019, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
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
package org.rascalmpl.library.analysis.text;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.BaseDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.OutputStreamIndexOutput;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * Provides full access to Lucene's indexing and search facilities, as well as text analyzers to Rascal programmers.
 * 
 *    * The file abstraction of ISourceLocations is maintained by the adapter, so all schemes are allowed for writing to and reading from indexes.
 *    * Text analyzers written in Java as well as in Rascal can be used.
 *    * Documents are modelled as constructors with keyword parameters as fields. See Lucene.rsc for details.
 */
public class LuceneAdapter {
    private static final String SRC_FIELD_NAME = "src";
    private static final String ID_FIELD_NAME = "$id$";
    private final IValueFactory vf;
    private final TypeFactory tf = TypeFactory.getInstance();
    private final Prelude prelude;
    private final Map<ISourceLocation, SingleInstanceLockFactory> lockFactories;
    private final TypeStore store = new TypeStore();
    private final Type Document = tf.abstractDataType(store, "Document");
    private final Type docCons = tf.constructor(store, Document, "document", tf.sourceLocationType(), "src");
    private final StandardTextReader valueParser = new StandardTextReader();
    
    public LuceneAdapter(IValueFactory vf) {
        this.vf = vf;
        this.prelude = new Prelude(vf);
        lockFactories = new HashMap<>();
    }
    
    public void createIndex(IConstructor i, ISet documents) {
        try {
            ISourceLocation indexFolder = (ISourceLocation) i.get("folder");
            ISet analyzers = getAnalyzers(i);
            Analyzer analyzer = makeFieldAnalyzer(analyzers);
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            SingleInstanceLockFactory lockFactory = makeLockFactory(indexFolder);
            Directory dir = makeDirectory(indexFolder, lockFactory);

            try (IndexWriter index = new IndexWriter(dir, config)) {
                for (IValue elem : documents) {
                    index.addDocument(makeDocument((IConstructor) elem));
                }
            }
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }

    private ISet getAnalyzers(IConstructor i) {
        ISet result = (ISet) i.asWithKeywordParameters().getParameter("analyzers");
        if (result == null) {
            return vf.set();
        }
        else {
            return result;
        }
    }

    private Directory makeDirectory(ISourceLocation indexFolder, SingleInstanceLockFactory lockFactory)
        throws IOException {
//      for debugging purposes we can replace the SourceLocationDirectory with this FSDirectory and it will all only work with the `file` scheme..
//        return FSDirectory.open(Paths.get(indexFolder.getPath()));
        return new SourceLocationDirectory(lockFactory, prelude, indexFolder);
    }

    public IList searchIndex(IConstructor index, IString query, IInteger max) throws IOException, ParseException {
        // TODO the searcher should be cached on the indexFolder key
        ISourceLocation indexFolder = (ISourceLocation) index.get("folder");
        ISet analyzers = getAnalyzers(index);
        IndexSearcher searcher = makeSearcher(indexFolder);
        
        QueryParser parser = makeQueryParser(analyzers);
        Query queryExpression = parser.parse(query.getValue());
        TopDocs docs = searcher.search(queryExpression, max.intValue());

        IListWriter result = vf.listWriter();
        
        for (ScoreDoc doc : docs.scoreDocs) {
            org.apache.lucene.document.Document found = searcher.doc(doc.doc);
            String loc = found.get(ID_FIELD_NAME);
            
            if (loc != null) {
                IConstructor node = vf.constructor(docCons, valueParser.read(vf, new StringReader(loc)));
                Map<String, IValue> params = new HashMap<>();
                
                params.put("score", vf.real(doc.score));
                
                // TODO: put the other stored fields into the document, if any
                
                result.append(node.asWithKeywordParameters().setParameters(params));
            }
        }
        
        return result.done();
    }

    private IndexSearcher makeSearcher(ISourceLocation indexFolder) throws IOException {
        SingleInstanceLockFactory lockFactory = makeLockFactory(indexFolder);
        Directory dir = makeDirectory(indexFolder, lockFactory);
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    private MultiFieldQueryParser makeQueryParser(ISet analyzers) throws IOException {
        Analyzer analyzer = makeFieldAnalyzer(analyzers);
        
        ArrayList<String> labels = new ArrayList<>(analyzers.size() + 1);
        labels.add(ID_FIELD_NAME);
        labels.add(SRC_FIELD_NAME);
        analyzers.asRelation().project(0).forEach((s) -> labels.add(((IString) s).getValue()));
        
        return new MultiFieldQueryParser(labels.toArray(new String[labels.size()]), analyzer);
    }
    
    private SingleInstanceLockFactory makeLockFactory(ISourceLocation indexFolder) {
        SingleInstanceLockFactory lockFactory = lockFactories.get(indexFolder);
        if (lockFactory == null) {
            lockFactory = new SingleInstanceLockFactory();
            lockFactories.put(indexFolder, lockFactory);
        }
        return lockFactory;
    }

    private Analyzer makeFieldAnalyzer(ISet analyzers) throws IOException {
        Map<String, Analyzer> analyzerMap = new HashMap<>();
        
        analyzerMap.put(ID_FIELD_NAME, new KeywordAnalyzer());

        for (IValue elem : analyzers) {
            ITuple tup = (ITuple) elem;
            String label = ((IString) tup.get(0)).getValue();
            IConstructor node = (IConstructor) tup.get(1);

            switch (node.getName()) {
                case "analyzerClass":
                    analyzerMap.put(label, analyzerFromClass(((IString) node.get("analyzerClassName")).getValue()));
                    break;
                case "analyzer":
                    analyzerMap.put(label, makeAnalyzer((IConstructor) node.get("tokenizer"), (IList) node.get("filters")));
                    break;
            }
        }
        
        return new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerMap);
    }

    private Analyzer makeAnalyzer(IConstructor tokenizer, IList filters) throws IOException {
         final Tokenizer tokens = makeTokenizer(tokenizer);
         TokenStream stream = tokens;
         
         for (IValue elem : filters) {
             stream = makeFilter(stream, (IConstructor) elem);
         }
         
         final TokenStream filtered = stream;
         
         return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
                    return new TokenStreamComponents(tokens, filtered);
            }
         };
    }

    
    private TokenStream makeFilter(TokenStream stream, IConstructor node) {
        switch (node.getName()) {
            case "filterClass":
                return filterFromClass(((IString) node.get("filterClassName")).getValue());
            case "filter":
                return makeFilter(stream, ((ICallableValue) node.get("filterFunction")));
            default:
                throw new IllegalArgumentException();
        }
    }

    private TokenStream makeFilter(TokenStream stream, ICallableValue function) {
        return new TokenFilter(stream) {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            
            @Override
            public boolean incrementToken() throws IOException {
                if (input.incrementToken()) {
                    final IString token = vf.string(new String(termAtt.buffer()));

                    IString result = (IString) function.call(new Type[] { TypeFactory.getInstance().stringType() }, new IValue[] { token }, null).getValue();

                    if (result.length() == 0) {
                        termAtt.setEmpty();
                    }
                    else {
                        termAtt.resizeBuffer(result.length());
                        char[] chars = result.getValue().toCharArray();
                        termAtt.copyBuffer(chars, 0, chars.length);
                    }

                    return true;
                } 
                else {
                    return false;
                }
            }
        };
    }

    private Tokenizer makeTokenizer(IConstructor node) {
        switch (node.getName()) {
            case "tokenizerClass":
                return tokenizerFromClass(((IString) node.get("tokenizerClassName")).getValue());
            case "tokenizer":
                return makeTokenizer(((ICallableValue) node.get("tokenizerFunction")));
            default:
                throw new IllegalArgumentException();
        }
    }

    private Tokenizer makeTokenizer(final ICallableValue function) {
        return new Tokenizer() {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            private Iterator<IValue> result;
            
            @Override
            public boolean incrementToken() throws IOException {
                if (result == null) {
                    IString parameter = vf.string(Prelude.consumeInputStream(input));
                    IList strings = (IList) function.call(new Type[] { tf.stringType() }, new IValue[] { parameter }, null).getValue();
                    result = strings.iterator();
                }
                
                if (result.hasNext()) {
                    char[] token = ((IString) result.next()).getValue().toCharArray();
                    termAtt.setLength(token.length);
                    termAtt.copyBuffer(token, 0, token.length);
                    return true;
                }
                else {
                    return false;
                }
            }
        };
    }

    private TokenStream filterFromClass(String filterClass) {
        return fromClass(TokenStream.class, filterClass);
    }
    
    private Analyzer analyzerFromClass(String analyzerClass) {
        return fromClass(Analyzer.class, analyzerClass);
    }
    
    private Tokenizer tokenizerFromClass(String analyzerClass) {
        return fromClass(Tokenizer.class, analyzerClass);
    }
    
    private <T> T fromClass(Class<T> clz, String name) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> cls = (Class<T>) Class.forName(name);
            
           return cls.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
            throw new IllegalArgumentException(name);
        }
    }

    private Document makeDocument(IConstructor elem) {
        Document luceneDoc = new Document();
        ISourceLocation loc = (ISourceLocation) elem.get("src");
        
        Field idField = new StringField(ID_FIELD_NAME, loc.toString(), Store.YES);
        luceneDoc.add(idField);
        
        if (URIResolverRegistry.getInstance().exists(loc)) {
            Field srcField = new Field(SRC_FIELD_NAME, prelude.readFile(loc).getValue(), TextField.TYPE_NOT_STORED);
            luceneDoc.add(srcField);
        }
        
        IWithKeywordParameters<? extends IConstructor> kws = elem.asWithKeywordParameters();
        
        for (String label : kws.getParameterNames()) {
            IValue val = kws.getParameter(label);
            
            if (val.getType().isString()) {
                luceneDoc.add(new Field(label, ((IString) val).getValue() ,TextField.TYPE_STORED));
            }
            else if (val.getType().isSourceLocation()) {
                luceneDoc.add(new Field(label, prelude.readFile((ISourceLocation) val).getValue(), TextField.TYPE_NOT_STORED));
            }
            else {
                luceneDoc.add(new Field(label, val.toString() ,TextField.TYPE_STORED));
            }
        }

        return luceneDoc;
    }
    
    /**
     * Implements Lucene's index outputstreams as a facade to ISourceLocation outputstreams
     */
    private static class SourceLocationIndexOutput extends OutputStreamIndexOutput {
        public SourceLocationIndexOutput(ISourceLocation src) throws IOException {
            super(src.toString(), src.toString(), URIResolverRegistry.getInstance().getOutputStream(src, false), 8192);
        }
    }
    
    private static class SourceLocationIndexInput extends IndexInput {
        private final ISourceLocation src;
        private InputStream input;
        private final long size;
        private long cursor;
        private final long sliceStart;
        private final long sliceEnd;
        private final Prelude prelude;
        
        public SourceLocationIndexInput(Prelude prelude, ISourceLocation src) throws IOException {
            super(src.toString());
            try {
                this.src = src;
                this.input = URIResolverRegistry.getInstance().getInputStream(src);
                this.size = prelude.__getFileSize(src).longValue();
                this.sliceStart = 0;
                this.sliceEnd = size;
                this.prelude = prelude;
                this.cursor = 0;
            }
            catch (URISyntaxException e) {
                throw new IOException(e);
            }
        }
        
        public SourceLocationIndexInput(Prelude prelude, String desc, ISourceLocation src, long sliceStart, long sliceLength) throws IOException {
            super(desc);
            
            try {
                this.src = src;
                this.input = URIResolverRegistry.getInstance().getInputStream(src);
                this.sliceStart = sliceStart;
                this.sliceEnd = sliceStart + sliceLength;
                this.size = prelude.__getFileSize(src).longValue();
                this.prelude = prelude;
                this.cursor = 0;
                internalSkip(sliceStart);
            }
            catch (URISyntaxException e) {
                throw new IOException(e);
            }
        }

        private void internalSkip(long count) throws IOException {
            // TODO: this might not terminate on all InputStreams due to the InputStream.skip contract which may always return 0.
            long target = cursor + count;
            while (cursor < target) {
                 cursor += this.input.skip(target - cursor);
            }
        }

        @Override
        public void close() throws IOException {
            input.close();
        }

        @Override
        public long getFilePointer() {
            return cursor - sliceStart;
        }

        @Override
        public void seek(long pos) throws IOException {
            if (pos + sliceStart > sliceEnd) {
                throw new EOFException();
            }
            
            if (pos + sliceStart == cursor) {
                return;
            }
            
            if (pos + sliceStart < cursor) {
                input.close();
                input = URIResolverRegistry.getInstance().getInputStream(src);
                internalSkip(pos + sliceStart);
            }
            else {
                internalSkip(pos + sliceStart - cursor);
            }
        }

        @Override
        public long length() {
            return sliceEnd - sliceStart;
        }

        @Override
        public IndexInput slice(String sliceDescription, long offset, long length) throws IOException {
            return new SourceLocationIndexInput(prelude, sliceDescription, src, offset + sliceStart, length);
        }

        @Override
        public byte readByte() throws IOException {
            if (cursor >= sliceEnd) {
                throw new EOFException();
            }
            
            try {
                return (byte) input.read();
            }
            finally {
                cursor +=1 ;
            }
        }

        @Override
        public void readBytes(byte[] b, int offset, int len) throws IOException {
            if (cursor >= sliceEnd) {
                throw new EOFException();
            }
            
            cursor += input.read(b, offset, len);
        }
    }
    
    /**
     * Implements Lucene's file system abstraction as a facade to ISourceLocation directories
     */
    private static class SourceLocationDirectory extends BaseDirectory {
        private final ISourceLocation src;
        private final URIResolverRegistry reg;
        private final Prelude prelude;
        private final AtomicLong nextTempFileCounter = new AtomicLong();

        public SourceLocationDirectory(LockFactory lockFactory, Prelude prelude, ISourceLocation src) throws IOException {
            super(lockFactory);
            this.prelude = prelude;
            this.src = src;
            this.reg = URIResolverRegistry.getInstance();
            
            if (!reg.exists(src)) {
                reg.mkDirectory(src);
            }
        }
        
        @Override
        public String[] listAll() throws IOException {
            return reg.listEntries(src);
        }

        @Override
        public void deleteFile(String name) throws IOException {
            reg.remove(location(name)); 
        }

        @Override
        public long fileLength(String name) throws IOException {
            try {
                return prelude.__getFileSize(location(name)).longValue();
            }
            catch (URISyntaxException e) {
                throw new IOException(e);
            }
        }

        @Override
        public IndexOutput createOutput(String name, IOContext context) throws IOException {
            return new SourceLocationIndexOutput(location(name));
        }

        private ISourceLocation location(String name) {
            return URIUtil.getChildLocation(src, name);
        }

        @Override
        public IndexOutput createTempOutput(String prefix, String suffix, IOContext context) throws IOException {
            ensureOpen();

            while (true) {
              try {
                String name = IndexFileNames.segmentFileName(prefix, suffix + "_" + Long.toString(nextTempFileCounter.getAndIncrement(), Character.MAX_RADIX), "tmp");
                return createOutput(name, context);
              } catch (FileAlreadyExistsException faee) {
                // Retry with next incremented name
              }
            }
        }

        @Override
        public void sync(Collection<String> names) throws IOException {
            // TODO; need sync support in URIResolverResistry
            // this is to make sure memory mapped files are written to disk
        }

        @Override
        public void syncMetaData() throws IOException {
            // TODO; need sync support in URIResolverResistry
            // this is to make sure memory mapped files are written to disk
        }

        @Override
        public void rename(String source, String dest) throws IOException {
            reg.copy(location(source), location(dest));
            reg.remove(location(source));
        }

        @Override
        public IndexInput openInput(String name, IOContext context) throws IOException {
            return new SourceLocationIndexInput(prelude, location(name));
        }

        @Override
        public void close() throws IOException {
            isOpen = false;
        }
    }
}