module lang::rascalcore::check::Test3

data AType;
data Expression;
alias Keyword     = tuple[/*str fieldName,*/ AType fieldType, Expression defaultExp];
data ADTSummary = 
    adtSummary(str adtName, 
               list[AType] parameters, 
               set[AType] constructors = {}, 
               set[Keyword] commonKeywordFields = {}, 
               SyntaxRole syntaxRole = dataSyntax(), 
               set[AType] starts = {}, 
               set[AProduction] productions = {});

value f(){
    ADTSummary s;
    s.commonKeywordFields;
}