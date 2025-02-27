@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::SyntaxGetters

/*
    Various getters to extract information from syntax constructs
*/
import ParseTree;
import lang::rascalcore::check::BasicRascalConfig;
import lang::rascal::\syntax::Rascal;

import Map;
import Set;

list[TypeArg] getFormals(Variant variant)
    = [ta | TypeArg ta <- variant.arguments];
    
list[Pattern] getFormals(Parameters parameters)
    = [pat | Pattern pat <- parameters.formals.formals];

list[KeywordFormal] getKwFormals(Variant variant)
    =  variant.keywordArguments is \default ? [kwf | kwf <- variant.keywordArguments.keywordFormalList] : [];
    

list[KeywordFormal] getKwFormals(Parameters parameters){
    if(parameters.keywordFormals is \default) {
        if({KeywordFormal ","}+ keywordFormalList := parameters.keywordFormals.keywordFormalList){
            return [kwf | kwf <- keywordFormalList];
        }
    }
    return [];
}

Vis getVis((Visibility) `private`, Vis dv)  = privateVis();
Vis getVis((Visibility) `public`, Vis dv)   = publicVis();
Vis getVis((Visibility) ``, Vis dv)         = dv;

list[TypeVar] getTypeParameters(UserType userType)
    = userType is parametric ? [p.typeVar | p <- userType.parameters] : [];

list[Sym] getTypeParameters(Sym sym)
    =  [p |/Sym p := sym, p is parameter];

list[KeywordFormal] getCommonKwFormals(Declaration decl)
   = decl.commonKeywordParameters is present ?  [kwf | kwf <- decl.commonKeywordParameters.keywordFormalList] : [];


str md5Contrib4Tags(Tags tags){
    for(tg <- tags.tags){
        if("<tg.name>" == "javaClass"){  
            if(tg has contents){
                return"<tg.contents.contents>";
            } else {
                return "";
            }
        }
    }
    return "";
}

map[str,str] getTags(Tags tags){
    res = ();
    for(tg <- tags.tags){
        if(tg has contents){
            res["<tg.name>"] = "<tg.contents.contents>";
        } else {
            res["<tg.name>"] = "";
        }
    }
    return res;
}    

// TODO: replaced by the above code, due to compiler issue, see lang::rascal::tests::basic::CompilerIssues::TemplateInConditional
//map[str,str] getTags(Tags tags)
//    =  ("<tg.name>" : tg has contents ? "<tg.contents.contents>" : "" | tg <- tags.tags);

bool ignoreCompiler(map[str,str] tagsMap)
    = !isEmpty(domain(tagsMap) &  {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

tuple[bool, str] getDeprecated(map[str,str] tagsMap){
    for(depr <- {"deprecated", "Deprecated"}){
        if(tagsMap[depr]?)
            return <true, tagsMap[depr]>;
   }
   return <false, "">;
}

tuple[bool, TagString] getExpected(Tags tags){
    for(tg <- tags.tags){
        if("<tg.name>" in {"expected", "Expected"}){
            return <true, tg.contents>;
        }
   }
   return <false, [TagString]"{None}">;
}


list[TypeVar] getTypeParams(Tree t){
    res = [];
    top-down-break visit(t){
        case TypeVar tp : {
                res += tp;
                if(tp is bounded) res += getTypeParams(tp.bound);
            }
        //case FunctionType tp: ;
        //    // only type parameters in return type of a function type will be considered
        //    res += getTypeParams(tp.\type);
    }
        
    return res;
}

set[Name] getTypeParamNames(Tree t){
    return { tp.name | tp <- getTypeParams(t) };
}

bool containsReturn(Tree t)
    = Statement s := t && /(Statement) `return <Statement _>` := s;