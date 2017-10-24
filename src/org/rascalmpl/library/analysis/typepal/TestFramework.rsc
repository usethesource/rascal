@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module analysis::typepal::TestFramework

import ParseTree;
import IO;
import String;
import Set;
import Map;
import List;

extend analysis::typepal::TypePal;
import analysis::grammars::Ambiguity;

//import util::IDE;
import util::Reflective;

lexical TTL_id = ([A-Z][a-zA-Z0-9]* !>> [a-zA-Z0-9]) \ TTL_Reserved;
lexical TTL_String = "\"" ![\"]*  "\"";

keyword TTL_Reserved = "test" | "expect" ;

layout TTL_Layout = TTL_WhitespaceAndComment* !>> [\ \t\n\r];

lexical TTL_WhitespaceAndComment 
   = [\ \t\n\r]
   | @category="Comment" ws2: "@@" ![\n]+
   | @category="Comment" ws3: "\<@@" ![]*  "@@\>"
   ;
   
start syntax TTL = ttl: TTL_TestItem* items ;

lexical TTL_Token = ![\[\]] | "[" ![\[]* "]";

syntax TTL_TestItem
    = "test" TTL_id name "[[" TTL_Token* tokens "]]" TTL_Expect expect
    ;

syntax TTL_Expect
    = none: ()
    | "expect" "{" {TTL_String ","}* messages "}"
    ;
    
bool matches(str subject, str pat) =
    contains(toLowerCase(subject), toLowerCase(pat));


str deescape(str s)  {  // copied from RascalExpression, belongs in library
    res = visit(s) { 
        case /^\\<c: [\" \' \< \> \\]>/ => c
        case /^\\t/ => "\t"
        case /^\\n/ => "\n"
        case /^\\u<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
        case /^\\U<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
        case /^\\a<hex:[0-7][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
        }; 
    return res;
}

bool runTests(list[loc] suites, TModel(str txt) getModel, bool verbose = false){
    TTL ttlProgram;
    
    failedTests = ();
    ntests = 0;
    ok = true;
    for(suite <- suites){
        tr = parse(#start[TTL], suite, allowAmbiguity=true);
    
        // TODO: layout of the subject language may interfere with laout of TTL
       //       but this is a too harsh measure!
       
        if(amb(set[Tree] alternatives) := tr){
            ttlProgram = visit(tr){ case amb(set[Tree] alternatives1) => getOneFrom(alternatives1) }.top;
        } else {
            ttlProgram = visit(tr.top){ case amb(set[Tree] alternatives2) => getOneFrom(alternatives2) };
        }
    
       

        for(ti <- ttlProgram.items){
            ntests += 1;
            try {
              model = getModel("<ti.tokens>");
              messages = model.messages;
              if(verbose) println("runTests: <messages>");
              ok = ok && isEmpty(messages);
              expected = ti.expect is none ? {} : {deescape("<s>"[1..-1]) | TTL_String s <- ti.expect.messages};
              result = (isEmpty(messages) && isEmpty(expected)) || all(emsg <- expected, any(eitem <- messages, matches(eitem.msg, emsg)));
              println("Test <ti.name>: <result>");
              if(!result) failedTests[<"<ti.name>", suite>] = relocate(messages, ti.tokens@\loc); 
              //if(!result) iprintln(relocate(model, ti.tokens@\loc));  
           } catch ParseError(loc l): {
                failedTests[<"<ti.name>", suite>]  = {error("Parse error", relocate(l, ti.tokens@\loc))};
           } 
        }
    }
    nfailed = size(failedTests);
    println("Test summary: <ntests> tests executed, <ntests - nfailed> succeeded, <nfailed> failed");
    if(!isEmpty(failedTests)){
        println("Failed tests:");
        for(failed <- failedTests){
            msgs = failedTests[failed];
            <name, suite> = failed;
           
            if(isEmpty(msgs)){
                println("<suite>, <name>:\tExpected message not found");
            } else {
                println("<suite>, <name>:");
                for(msg <- msgs){
                    println("\t<msg>");
                }
            }
        }
    }
    return ok;
}

loc relocate(loc osrc, loc base){
    //println("relocate: <osrc>, <base>");
    nsrc = base;
    
    offset = base.offset + osrc.offset;
    length = osrc.length;
    
    endline = base.begin.line + osrc.end.line - 1;
    beginline = base.begin.line + osrc.begin.line - 1;
    
    begincolumn = osrc.begin.line == 1 ? base.begin.column + osrc.begin.column
                                       : osrc.begin.column;
   
    endcolumn = osrc.end.line == 1 ? base.begin.column + osrc.end.column
                                   : osrc.end.column;
    
    return |<base.scheme>://<base.authority>/<base.path>|(offset, length, <beginline, begincolumn>, <endline, endcolumn>);
    
    //println("relocate with base <base>: from <osrc> to <nsrc>");
    return nsrc;
}

Message relocate(Message msg, loc base){
    msg.at = relocate(msg.at, base);
    return msg;
}

set[Message] relocate(set[Message] messages, loc base)
    = { relocate(msg, base) | msg <- messages };

TModel relocate(TModel tm, loc base){
    return visit(tm) {
           case loc l => relocate(l, base) when l.scheme == "unknown"
    }

}
//void register() {
//    registerLanguage("TTL", "ttl", Tree (str x, loc l) { return parse(#start[TTL], x, l, allowAmbiguity=true); });
//    registerContributions("TTL", {
//      syntaxProperties(
//         fences = {<"{","}">,<"[[","]]">} ,
//         lineComment = "@@",
//         blockComment = <"\<@@"," *","@@\>">
//         )
//    });
//}    
