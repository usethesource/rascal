/** 
 * Copyright (c) 2017, paulklint, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.lang.rascal.syntax;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;

import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class CompareParsers {
    
    private static final int FILE_BUFFER_SIZE = 8 * 1024;
    static IValueFactory vf = ValueFactoryFactory.getValueFactory();
    
    private static String consumeInputStream(Reader in) throws IOException {
        StringBuilder res = new StringBuilder();
        char[] chunk = new char[FILE_BUFFER_SIZE];
        int read;
        while ((read = in.read(chunk, 0, chunk.length)) != -1) {
            res.append(chunk, 0, read);
        }
        return res.toString();
    }
    
    public static String readFile(ISourceLocation sloc){
        try (Reader reader = URIResolverRegistry.getInstance().getCharacterReader(sloc);){
            return consumeInputStream(reader);
        } 
        catch(FileNotFoundException e){
            throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
        }
        catch (IOException e) {
            throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
        }
    }

    public static void main(String[] args) {
        IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
         
        ISourceLocation loc = null;
        try {
            loc = vf.sourceLocation("home", "", "git/rascal/src/org/rascalmpl/library/Message.rsc");
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String content = readFile(loc);
        System.err.println(content);
        ITree tree =  new RascalParserCOMP().parse(Parser.START_MODULE, loc.getURI(), content.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
        System.err.println(tree);
    }

}
