/** 
 * Copyright (c) 2018, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.analysis.text.stemming;

import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.KpStemmer;
import org.tartarus.snowball.ext.LovinsStemmer;
import org.tartarus.snowball.ext.PorterStemmer;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class Snowball {
    private final IValueFactory vf;
    private final Map<String, SnowballProgram> stemmers = new HashMap<>();

    public Snowball(IValueFactory vf) {
        this.vf = vf;
        stemmers.put("kp", new KpStemmer());
        stemmers.put("porter", new PorterStemmer());
        stemmers.put("lovins", new LovinsStemmer());
    }
    
    public IString stem(IString word, IConstructor language) {
        SnowballProgram stemmer = getStemmerFor(language);
        return stem(word, stemmer);
    }

    private IString stem(IString word, SnowballProgram stemmer) {
        stemmer.setCurrent(word.getValue());
        stemmer.stem();
        return vf.string(stemmer.getCurrent());
    }
    
    private SnowballProgram getStemmerFor(IConstructor language) {
        try {
            String name = language.getName();
            SnowballProgram program = stemmers.get(name);

            if (program == null) {
                program = (SnowballProgram) Class.forName("org.tartarus.snowball.ext." + capitalize(name) + "Stemmer").newInstance();
                stemmers.put(name, program);
            }
            
            return program;
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw RuntimeExceptionFactory.illegalArgument(language, null, null);
        }
    }

    private String capitalize(String input) {
        if (input.length() == 0) {
            return input;
        }
        
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public IString kraaijPohlmannStemmer(IString word) {
        return stem(word, stemmers.get("kp"));
        
    }
    
    public IString porterStemmer(IString word) {
       return stem(word, stemmers.get("porter")); 
    }
    
    public IString lovinsStemmer(IString word) {
        return stem(word, stemmers.get("lovins"));
        
    }
}
