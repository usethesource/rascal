@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Reusable test functions to run (generated) parsers on a test corpus}
module analysis::grammars::ParserTesting

import Exception;
import IO;
import List;
import ParseTree;
import analysis::grammars::Ambiguity;
import util::Monitor;

alias FileParser[&T] = &T (loc org);

@synopsis{Collect files of a specific extension from a folder recursively, while filtering ignored files and folders}
list[loc] constructParsingTestCorpus(loc root, set[str] extensions, set[loc] ignores={}) {
    if (root in ignores) {
        return [];
    }
    else if (isFile(root) && root.extension in extensions) {
        return [root];
    }
    else if (isDirectory(root)) {
        return [*constructParsingTestCorpus(e, extensions, ignores=ignores) | e <- root.ls];
    }
    else {
        return [];
    }
}

@synopsis{Run a generated parser on a corpus and print errors, diagnostics and summary statistics}
@description{
This functions returns true if and only if none of the files in the corpus exhibit a parse error or an ambiguity for the given grammar
}
@pitfalls{
* the grammar is best if it's a start symbol (since we are parsing files we need spaces before and after the top sort).
}
bool testCorpusForParseErrorsAndAmbiguity(type[&T <: Tree] grammar, list[loc] corpus, str jobLabel="Testing <grammar>") {
    &T (value,loc) generated = parser(grammar, allowAmbiguity=false, allowRecovery=false);
    FileParser[&T] wrapped = &T (loc src) { 
        return generated(src, src); 
    };

    return testCorpusForParseErrorsAndAmbiguity(wrapped, corpus, jobLabel=jobLabel);
}

@synopsis{Run a parser on a corpus and print errors, diagnostics and summary statistics}
@description{
This functions returns true if and only if none of the files in the corpus exhibit a parse error or an ambiguity
}
@benefits{
* this is a typical simple task for every project that has a grammar
* ambiguity reported here can be analyzed using analysis::grammars::Ambiguity or the drambiguity project
}
@pitfalls{
* the parser must not `allowAmbiguity=true` 
* the parser must not `allowRecovery=true` either
}
bool testCorpusForParseErrorsAndAmbiguity(FileParser[&T] parser, list[loc] corpus, str jobLabel="Testing parser ") { 
    int success = 0;
    int errors = 0;
    int ambs = 0;
    
    loopVoidJob(corpus, (loc src) {
        try {
            pt = parser(src);
            success += 1;
        }
        catch Ambiguity(loc l, str sym, str txt): {
            println("[ERROR] ambiguity: <l>");
            println("[INFO]    symbol : <sym>");
            println("[INFO]    source : `<txt>`");
            
            ambs += 1;
        }
        catch ParseError(loc l): {
            println("[ERROR] parse error: <l>");
            errors += 1;
        }
    }, label=jobLabel);
    
    println("[INFO] #total  : <size(corpus)>");
    println("[INFO] #ok     : <success>");
    println("[INFO] #errors : <errors>");
    println("[INFO] #ambs   : <ambs>");

    return errors == 0 && ambs == 0;
}