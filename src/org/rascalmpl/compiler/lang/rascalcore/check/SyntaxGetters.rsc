@bootstrapParser
module lang::rascalcore::check::SyntaxGetters

import lang::rascal::\syntax::Rascal;

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
