@bootstrapParser
module lang::rascalcore::check::SyntaxGetters

/*
    Various getters to extract information from syntax constructs
*/

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