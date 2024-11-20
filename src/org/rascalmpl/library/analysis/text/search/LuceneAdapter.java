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
package org.rascalmpl.library.analysis.text.search;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.TokenGroup;
import org.apache.lucene.store.BaseDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.OutputStreamIndexOutput;
import org.apache.lucene.store.SingleInstanceLockFactory;
import org.apache.lucene.util.BytesRef;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
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
    private static final FieldType SOURCELOCATION_TYPE = makeSourceLocationType();
    private final IValueFactory vf;
    private final TypeFactory tf = TypeFactory.getInstance();
    private final Map<ISourceLocation, SingleInstanceLockFactory> lockFactories;
    private final TypeStore store = new TypeStore();
    
    private final Type Document = tf.abstractDataType(store, "Document");
    private final Type docCons = tf.constructor(store, Document, "document", tf.sourceLocationType(), "src");
    
    private final Type Term = tf.abstractDataType(store, "Term"); // str chars, int offset, str kind
    private final Type termCons = tf.constructor(store, Term, "term", tf.stringType(), "chars", tf.sourceLocationType(), "src", tf.stringType(), "kind");
    
    private final StandardTextReader valueParser = new StandardTextReader();
    
    public LuceneAdapter(IValueFactory vf) {
        this.vf = vf;
        lockFactories = new HashMap<>();
    }
    
    public void createIndex(ISourceLocation indexFolder, ISet documents, IConstructor analyzer, IString charset, IBool inferCharset) {
        try {
            IndexWriterConfig config = new IndexWriterConfig(makeAnalyzer(analyzer));
            SingleInstanceLockFactory lockFactory = makeLockFactory(indexFolder);
            Directory dir = makeDirectory(indexFolder, lockFactory);

            try (IndexWriter index = new IndexWriter(dir, config)) {
                for (IValue elem : documents) {
                    index.addDocument(makeDocument((IConstructor) elem, charset, inferCharset));
                }
                
                index.commit();
            }
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }

    public ISet listTerms(ISourceLocation indexFolder, IString fieldName, IInteger max) {
        try {
            DirectoryReader reader = makeReader(indexFolder);
            ISetWriter result = vf.setWriter();
            Fields fields = MultiFields.getFields(reader);

            for (String label : fields) {
                if (label.equals(fieldName.getValue())) {
                    Terms terms = fields.terms(label);

                    if (terms == null) {
                        continue;
                    }
                    TermsEnum list = terms.iterator();
                    BytesRef bytes;
                    int countDown = max.intValue();

                    while ((bytes = list.next()) != null && countDown-- > 0) {
                        IString val = vf.string(bytes.utf8ToString());
                        IInteger freq = vf.integer(reader.totalTermFreq(new Term(label, bytes)));
                        result.insert(vf.tuple(val, freq));
                    }
                }
            }

            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }
    
    public ISet listFields(ISourceLocation indexFolder) {
        try {
            DirectoryReader reader = makeReader(indexFolder);
            ISetWriter result = vf.setWriter();

            // str field, int docCount, int percentage
            for (LeafReaderContext subReader : reader.leaves()) {
                LeafReader sub = subReader.reader();
                for (FieldInfo field : sub.getFieldInfos()) {
                    IString name = vf.string(field.name);
                    IInteger docCount = vf.integer(sub.getDocCount(field.name));
                    IInteger termCount = vf.integer(sub.getSumTotalTermFreq(field.name));
                    result.insert(vf.tuple(name, docCount, termCount));
                }
            }

            return result.done();
        } catch (IOException e){
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }
    
    private Directory makeDirectory(ISourceLocation indexFolder, SingleInstanceLockFactory lockFactory) throws IOException {
        return new SourceLocationDirectory(vf, lockFactory, indexFolder);
    }

    public IList searchDocument(ISourceLocation doc, IString query, IConstructor analyzer, IInteger max, IString charset, IBool inferCharset) {
        try {
            String entireDocument = Prelude.readFile(vf, false, doc, charset.getValue(), inferCharset.getValue()).getValue();

            try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(doc, charset.getValue())) {
                TokenStream tokenStream = makeAnalyzer(analyzer).tokenStream(SRC_FIELD_NAME, reader);
                QueryParser parser = makeQueryParser(analyzer);
                Query queryExpression = parser.parse(query.getValue());
                QueryScorer scorer = new QueryScorer(queryExpression);
                final IListWriter result = vf.listWriter();

                Formatter formatter = new Formatter() {
                    @Override
                    public String highlightTerm(String originalText, TokenGroup tokenGroup) {
                        if (tokenGroup.getScore(tokenGroup.getNumTokens() - 1) > 0) {
                            int startOffset = tokenGroup.getStartOffset();
                            int endOffset = tokenGroup.getEndOffset();
                            result.append(vf.sourceLocation(doc, startOffset, endOffset - startOffset));
                        }
                        return tokenGroup.toString();
                    }
                };

                new Highlighter(formatter, scorer).getBestFragments(tokenStream, entireDocument, max.intValue());

                return result.done();
            }
            
        } catch (IOException e){
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
        catch (ParseException e) {
            int beginLine = e.currentToken.next.beginLine;
            int beginColumn = e.currentToken.next.beginColumn;
            // TODO: fix the coordinates below
            throw RuntimeExceptionFactory.parseError(vf.sourceLocation(doc, beginLine, beginColumn));

        }
        catch (ArithmeticException e) {
            throw RuntimeExceptionFactory.arithmeticException(e.getMessage(), null, null);
        }
        catch (InvalidTokenOffsetsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return vf.list();
        }
    }
    
    public IList analyzeDocument(IString doc, IConstructor analyzer) {
        return analyzeDocument(URIUtil.rootLocation("string"), new StringReader(doc.getValue()), analyzer);
    }
    
    public IList analyzeDocument(ISourceLocation doc, IConstructor analyzer) {
        try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(doc)) {
            return analyzeDocument(doc, reader, analyzer);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }
    
    private IList analyzeDocument(ISourceLocation src, Reader doc, IConstructor analyzer) {
        try {
            Analyzer theAnalyzer = makeAnalyzer(analyzer);

            TokenStream tokenStream = theAnalyzer.tokenStream(SRC_FIELD_NAME, doc);
            OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
            CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
            TypeAttribute typeAtt = tokenStream.addAttribute(TypeAttribute.class);
            IListWriter result = vf.listWriter();

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                int startOffset = offsetAttribute.startOffset();
                result.append(vf.constructor(termCons, vf.string(termAtt.toString()), vf.sourceLocation(src, startOffset, termAtt.length()), vf.string(typeAtt.type())));
            }

            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }
    
    public ISet searchIndex(ISourceLocation indexFolder, IString query, IConstructor analyzer, IInteger max) {
        try {
            // TODO the searcher should be cached on the indexFolder key
            IndexSearcher searcher = makeSearcher(indexFolder);
            QueryParser parser = makeQueryParser(analyzer);
            Query queryExpression = parser.parse(query.getValue());
            TopDocs docs = searcher.search(queryExpression, max.intValue());

            ISetWriter result = vf.setWriter();

            for (ScoreDoc doc : docs.scoreDocs) {
                org.apache.lucene.document.Document found = searcher.doc(doc.doc);
                String loc = found.get(ID_FIELD_NAME);
                ISourceLocation sloc = parseLocation(loc);

                if (loc != null) {
                    IConstructor node = vf.constructor(docCons, sloc);
                    Map<String, IValue> params = new HashMap<>();

                    params.put("score", vf.real(doc.score));

                    found.forEach((f) -> {
                        String value = f.stringValue();
                        String name = f.name();

                        if (value != null && !name.equals(ID_FIELD_NAME)) {
                            params.put(name, vf.string(value));
                        }
                    });


                    result.insert(node.asWithKeywordParameters().setParameters(params));
                }
            }

            return result.done();
        } catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
        catch (ParseException e) {
            int beginLine = e.currentToken.next.beginLine;
            int beginColumn = e.currentToken.next.beginColumn;
            // TODO: fix the coordinates below
            throw RuntimeExceptionFactory.parseError(vf.sourceLocation(indexFolder, beginLine, beginColumn));
        }
    }

    private ISourceLocation parseLocation(String loc) throws IOException {
        return (ISourceLocation) valueParser.read(vf, new StringReader(loc));
    }

    private IndexSearcher makeSearcher(ISourceLocation indexFolder) throws IOException {
        DirectoryReader reader = makeReader(indexFolder);
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    private DirectoryReader makeReader(ISourceLocation indexFolder) throws IOException {
        SingleInstanceLockFactory lockFactory = makeLockFactory(indexFolder);
        Directory dir = makeDirectory(indexFolder, lockFactory);
        DirectoryReader reader = DirectoryReader.open(dir);
        return reader;
    }

    private MultiFieldQueryParser makeQueryParser(IConstructor analyzer) throws IOException {
        Set<String> labels = analyzerFields(analyzer);
        Analyzer a = makeAnalyzer(analyzer);
        return new MultiFieldQueryParser(labels.toArray(new String[labels.size()]), a);
    }
    
    private SingleInstanceLockFactory makeLockFactory(ISourceLocation indexFolder) {
        SingleInstanceLockFactory lockFactory = lockFactories.get(indexFolder);
        if (lockFactory == null) {
            lockFactory = new SingleInstanceLockFactory();
            lockFactories.put(indexFolder, lockFactory);
        }
        return lockFactory;
    }

    private Set<String> analyzerFields(IConstructor node) {
        Set<String> result = new HashSet<>();
        result.add(ID_FIELD_NAME);
        result.add(SRC_FIELD_NAME);
        
        if (node.getName().equals("fieldsAnalyzer")) {
            result.addAll(node.asWithKeywordParameters().getParameterNames());
        }
        
        return result;
    }
    
    private Analyzer makeAnalyzer(IConstructor node) throws IOException {
        switch (node.getName()) {
            case "analyzerClass":
                return analyzerFromClass(((IString) node.get("analyzerClassName")).getValue());
            case "analyzer":
                return makeFunctionAnalyzer((IConstructor) node.get("tokenizer"), (IList) node.get("pipe"));
            case "fieldsAnalyzer":
                return makeFieldAnalyzer((IConstructor) node.get("src"), node.asWithKeywordParameters().getParameters());
            default:
                return new StandardAnalyzer();
        }
    }

    private Analyzer makeFieldAnalyzer(IConstructor src, Map<String, IValue> analyzers) throws IOException {
        return new PerFieldAnalyzerWrapper(new StandardAnalyzer(), makeFieldAnalyzers(src, analyzers));
    }
    
    private Map<String, Analyzer> makeFieldAnalyzers(IConstructor src, Map<String, IValue> analyzers) throws IOException {
        Map<String, Analyzer> analyzerMap = new HashMap<>();
        
        analyzerMap.put(ID_FIELD_NAME, new KeywordAnalyzer());
        analyzerMap.put(SRC_FIELD_NAME, makeAnalyzer(src));

        for (String label : analyzers.keySet()) {
            analyzerMap.put(label, makeAnalyzer((IConstructor) analyzers.get(label)));
        }
        
        return analyzerMap;
    }

    private Analyzer makeFunctionAnalyzer(IConstructor tokenizer, IList filters) throws IOException {
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
                return filterFromClass(stream, ((IString) node.get("filterClassName")).getValue());
            case "editFilter":
                return makeEditFilter(stream, ((IFunction) node.get("editor")));
            case "removeFilter":
                return makeRemoveFilter(stream, ((IFunction) node.get("accept")));
            case "splitFilter":
                return makeSplitFilter(stream, ((IFunction) node.get("splitter")));
            case "synonymFilter":
                return makeSynonymFilter(stream, ((IFunction) node.get("generator")));
            case "tagFilter":
                return makeTagFilter(stream, ((IFunction) node.get("generator")));    
            
            default:
                throw new IllegalArgumentException();
        }
    }

    private TokenStream makeEditFilter(TokenStream stream, IFunction function) {
        return new TokenFilter(stream) {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            
            @Override
            public boolean incrementToken() throws IOException {
                if (input.incrementToken()) {
                    final IString token = vf.string(new String(termAtt.buffer(), 0, termAtt.length()));

                    try {
                        IString result = (IString) function.call(token);

                        if (result.length() == 0) {
                            termAtt.setEmpty();
                        }
                        else {
                            char[] chars = result.getValue().toCharArray();
                            termAtt.copyBuffer(chars, 0, chars.length);
                        }
                    }
                    catch (MatchFailed e) {
                        // that's ok. case missed
                    }

                    return true;
                } 
                else {
                    return false;
                }
            }
        };
    }
    
    private TokenStream makeTagFilter(TokenStream stream, IFunction function) {
        return new TokenFilter(stream) {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
            
            @Override
            public boolean incrementToken() throws IOException {
                if (input.incrementToken()) {
                    final IString token = vf.string(new String(termAtt.buffer(), 0, termAtt.length()));
                    final IString tag = vf.string(typeAtt.type());
                    
                    try {
                        IString result = function.call(token, tag);

                        if (result.length() != 0) {
                            typeAtt.setType(result.getValue());
                        }
                    }
                    catch (Throw e) {
                        // that's ok. case missed (CallFailed)
                    }

                    return true;
                } 
                else {
                    return false;
                }
            }
        };
    }
    
    private TokenStream makeRemoveFilter(TokenStream stream, IFunction function) {
        return new FilteringTokenFilter(stream) {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            
            @Override
            protected boolean accept() throws IOException {
                final IString token = vf.string(new String(termAtt.buffer(), 0, termAtt.length()));

                try {
                    IBool result = function.call(token);
                    return result.getValue();
                }
                catch (MatchFailed e) {
                    // that's ok, case missed
                    // simply accept token in case of issues.
                    return true;
                }
            }
        };
    }
    
    private TokenStream makeSplitFilter(TokenStream stream, IFunction function) {
        return new TokenFilter(stream) {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            private PositionIncrementAttribute posAttr = addAttribute(PositionIncrementAttribute.class);
            private OffsetAttribute offsetAttr = getAttribute(OffsetAttribute.class);
            private int offset = 0;
            private IList backLog;
            
            @Override
            public boolean incrementToken() throws IOException {
                if (backLog != null && !backLog.isEmpty()) {
                    popOneTerm(1);
                    return true;
                }
                
                if (input.incrementToken()) {
                    final IString token = vf.string(new String(termAtt.buffer(), 0, termAtt.length()));

                    try {
                        backLog = function.call(token);
                        
                        if (backLog.length() > 0) {
                            offset = offsetAttr.startOffset();
                            popOneTerm(1);
                        }
                        else {
                            // do nothing, something went wrong
                            return true;
                        }
                    }
                    catch (Throw e) {
                        // that's ok, case missed
                        return true; // simply copy token
                    }
                    
                    return true;
                } 
                else {
                    return false;
                }
            }

            private void popOneTerm(int distance) {
                IString newTerm = (IString) backLog.get(0);
                backLog = backLog.delete(0);
                char[] charArray = newTerm.getValue().toCharArray();
                int len = charArray.length;
                termAtt.resizeBuffer(len);
                termAtt.copyBuffer(charArray, 0, len);
                offsetAttr.setOffset(offset, len + offset);
                offset += len;
                posAttr.setPositionIncrement(distance);
            }
        };
    }
    
    private TokenStream makeSynonymFilter(TokenStream stream, IFunction function) {
        return new TokenFilter(stream) {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            private PositionIncrementAttribute posAttr = addAttribute(PositionIncrementAttribute.class);
            private IList backLog;
            
            @Override
            public boolean incrementToken() throws IOException {
                if (backLog != null && !backLog.isEmpty()) {
                    popOneTerm(0);
                    return true;
                }
                
                if (input.incrementToken()) {
                    final IString token = vf.string(new String(termAtt.buffer(), 0, termAtt.length()));

                    try {
                        backLog = function.call(token);
                        
                        if (backLog.length() > 0) {
                            popOneTerm(1);
                        }
                        else {
                            // do nothing, something went wrong
                            return true;
                        }
                    }
                    catch (Throw e) {
                        // that's ok, case missed
                        return true; // simply copy token
                    }
                    
                    return true;
                } 
                else {
                    return false;
                }
            }

            private void popOneTerm(int distance) {
                IString newTerm = (IString) backLog.get(0);
                backLog = backLog.delete(0);
                char[] charArray = newTerm.getValue().toCharArray();
                termAtt.resizeBuffer(charArray.length);
                termAtt.copyBuffer(charArray, 0, charArray.length);
                posAttr.setPositionIncrement(distance);
            }
        };
    }

    private Tokenizer makeTokenizer(IConstructor node) {
        switch (node.getName()) {
            case "tokenizerClass":
                return tokenizerFromClass(((IString) node.get("tokenizerClassName")).getValue());
            case "tokenizer":
                return makeTokenizer(((IFunction) node.get("tokenizerFunction")));
            default:
                throw new IllegalArgumentException();
        }
    }

    private Tokenizer makeTokenizer(final IFunction function) {
        return new Tokenizer() {
            private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
            private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
            private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
            private Iterator<IValue> result;
            
            @Override
            public void reset() throws IOException {
                super.reset();
                result = null;
                clearAttributes();
            }
            
            @Override
            public boolean incrementToken() throws IOException {
                if (result == null) {
                    IString parameter = vf.string(Prelude.consumeInputStream(input));
                    try {
                        IList terms = function.call(parameter);
                        result = terms.iterator();
                    }
                    catch (Throwable e) {
                        // the parsing call back failed. TODO: report this but the code should be robust.
                        result = vf.list(vf.constructor(termCons, parameter, vf.sourceLocation(URIUtil.rootLocation("error"), 0, parameter.length()), vf.string("parse-error"))).iterator();
                    }
                }
                
                if (result.hasNext()) {
                    IConstructor termCons = ((IConstructor) result.next());
                    char[] token = ((IString) termCons.get(0)).getValue().toCharArray();
                    termAtt.copyBuffer(token, 0, token.length);
                    termAtt.setLength(token.length);
                    typeAtt.setType(((IString) termCons.get(2)).getValue());
                    int start = ((ISourceLocation) termCons.get(1)).getOffset();
                    offsetAtt.setOffset(start, start + token.length);
                    return true;
                }
                else {
                    return false;
                }
            }
        };
    }

    private TokenStream filterFromClass(TokenStream stream, String filterClass) {
        try {
            @SuppressWarnings("unchecked")
            Class<TokenStream> cls = (Class<TokenStream>) getClass().getClassLoader().loadClass(filterClass);
            
           return cls.getConstructor(TokenStream.class).newInstance(stream);
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(filterClass, e);
        }
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
            Class<T> cls = (Class<T>) getClass().getClassLoader().loadClass(name);
            
           return cls.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException e) {
            throw new IllegalArgumentException(name, e);
        }
    }

    private Document makeDocument(IConstructor elem, IString charset, IBool inferCharset) {
        Document luceneDoc = new Document();
        ISourceLocation loc = (ISourceLocation) elem.get("src");
        
        Field idField = new StringField(ID_FIELD_NAME, loc.toString(), Store.YES);
        luceneDoc.add(idField);
        
        if (URIResolverRegistry.getInstance().exists(loc)) {
            Field srcField = new Field(SRC_FIELD_NAME, Prelude.readFile(vf, false, loc, charset.getValue(), inferCharset.getValue()).getValue(), SOURCELOCATION_TYPE);
            luceneDoc.add(srcField);
        }
        
        IWithKeywordParameters<? extends IConstructor> kws = elem.asWithKeywordParameters();
        
        for (String label : kws.getParameterNames()) {
            IValue val = kws.getParameter(label);
            
            if (val.getType().isString()) {
                luceneDoc.add(new Field(label, ((IString) val).getValue() ,TextField.TYPE_STORED));
            }
            else if (val.getType().isSourceLocation()) {
                luceneDoc.add(new Field(label, Prelude.readFile(vf, false, (ISourceLocation) val, charset.getValue(), inferCharset.getValue()).getValue(), SOURCELOCATION_TYPE));
            }
            else {
                luceneDoc.add(new Field(label, val.toString() ,TextField.TYPE_STORED));
            }
        }

        return luceneDoc;
    }

    private static FieldType makeSourceLocationType() {
        FieldType sourceLocationType = new FieldType();
        sourceLocationType.setStored(false);
        sourceLocationType.setStoreTermVectors(true);
        sourceLocationType.setStoreTermVectorPositions(true);
        sourceLocationType.setStoreTermVectorOffsets(true);
        sourceLocationType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        sourceLocationType.freeze();
        return sourceLocationType;
    }
    
    /**
     * Implements Lucene's IndexOutput as a facade to ISourceLocation outputstreams
     */
    private static class SourceLocationIndexOutput extends OutputStreamIndexOutput {
        public SourceLocationIndexOutput(ISourceLocation src) throws IOException {
            super(src.toString(), src.toString(), URIResolverRegistry.getInstance().getOutputStream(src, false), 8192);
        }
    }
    
    /**
     * Implements Lucene's IndexInput as a facade to ISourceLocation inputstreams which are sucked into a byte[] right away.
     */
    private static class SourceLocationIndexInput extends IndexInput {
        // TODO: the length of the input is now maxed out at (MAX_INT - 8) due to the max size of arrays on the JVM.
        // we should probably wrap the byte[] input to enable larger files.
        private final byte[] input;
        private final int start;
        private final int end;
        
        private int cursor;
        
        public SourceLocationIndexInput(ISourceLocation src) throws IOException {
            super(src.toString());
            
            SourceLocationByteReader bytes = new SourceLocationByteReader(src);
            this.input = bytes.getByteArray();
            
            this.start = 0;
            this.cursor = start;
            this.end = bytes.size();
        }
        
        /**
         * shares the backing array with the caller. So this constructor is for private use in `clone` and `slice` only.
         */
        private SourceLocationIndexInput(String name, byte[] input, int sliceStart, int cursor, int sliceEnd) {
            super(name);
            
            this.input = input;

            assert sliceStart <= cursor && cursor < sliceEnd;

            this.start = sliceStart;
            this.cursor = cursor;
            this.end = sliceEnd;
        }
        
        @Override
        public void close() throws IOException { }

        @Override
        public long getFilePointer() {
            return cursor - start;
        }

        @Override
        public void seek(long pos) throws IOException {
            if (pos + start > end) {
                throw new EOFException();
            }
            
            if (pos > Integer.MAX_VALUE) {
                throw new IOException("SourceLocationIndexInput supports files up to MAX_INT bytes");
            }
            
            cursor = (int) (pos + start);
        }

        @Override
        public long length() {
            return end - start;
        }

        @Override
        public IndexInput slice(String sliceDescription, long offset, long length) throws IOException {
            if (offset + length > SourceLocationByteReader.MAX_ARRAY) {
                throw new IOException("SourceLocationIndexInput supports files up to MAX_INT bytes");
            }
            
            int newSliceStart = (int) (start + offset);
            int newSliceEnd = (int) (start + offset + length);
            
            return new SourceLocationIndexInput(sliceDescription, input, newSliceStart, newSliceStart, newSliceEnd);
        }

        @Override
        public byte readByte() throws IOException {
            if (cursor > end) {
                throw new EOFException();
            }
            
            return input[cursor++];
        }

        @Override
        public void readBytes(byte[] b, int offset, int len) throws IOException {
            if (cursor + len > end) {
                throw new EOFException();
            }
            
            System.arraycopy(input, cursor, b, offset, len);
            cursor += len;
        }
        
        @Override
        public IndexInput clone() {
            // cloned IndexInputs are never closed by Lucene, but since this InputStream does not keep any resources
            // open, it's not an issue. 
            return new SourceLocationIndexInput(this.toString() + "-clone", input, start, cursor, end);
        }
        
        /**
         * This reads in the entire contents of an InputStream pointed to by an ISourceLocation. 
         * It offers a direct reference to the allocated byte[]. Don't share without care.
         */
        private static final class SourceLocationByteReader {
            private static final int MAX_ARRAY = Integer.MAX_VALUE - 8;
            private static final int CHUNK_SIZE = 8192;

            private byte buf[];
            private int count;
            
            private SourceLocationByteReader(ISourceLocation src) throws IOException {
                // initial buffer is twice the chunk size to avoid growing and copying the array after the first read
                buf = new byte[CHUNK_SIZE << 1]; 
                
                try (InputStream in = URIResolverRegistry.getInstance().getInputStream(src)) {
                    int read;
                    while ((read = in.read(buf, count, CHUNK_SIZE)) != -1) {
                        count += read;
                        grow(count + CHUNK_SIZE);
                    }
                }
            }
            
            private void grow(int required) {
                if (required < 0) {
                    // due to overflow
                    throw new OutOfMemoryError();
                }
                
                if (required > buf.length) {
                    buf = Arrays.copyOf(buf, Math.min(Math.max(required, buf.length << 1), MAX_ARRAY));
                }
            }
            
            private byte[] getByteArray() {
                return buf;
            }
            
            private int size() {
                return count;
            }
        }
    }
    
    /**
     * Implements Lucene's file system abstraction as a facade to ISourceLocation directories
     */
    private static class SourceLocationDirectory extends BaseDirectory {
        private final ISourceLocation src;
        private final URIResolverRegistry reg;
        private final AtomicLong nextTempFileCounter = new AtomicLong();
        private final IValueFactory vf;

        public SourceLocationDirectory(IValueFactory vf, LockFactory lockFactory, ISourceLocation src) throws IOException {
            super(lockFactory);
            this.vf = vf;
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
            reg.remove(location(name), true); 
        }

        @Override
        public long fileLength(String name) throws IOException {
            try {
                return Prelude.__getFileSize(vf, location(name)).longValue();
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
            reg.rename(location(source), location(dest), true);
        }

        @Override
        public IndexInput openInput(String name, IOContext context) throws IOException {
            return new SourceLocationIndexInput(location(name));
        }

        @Override
        public void close() throws IOException {
            isOpen = false;
        }
    }
}