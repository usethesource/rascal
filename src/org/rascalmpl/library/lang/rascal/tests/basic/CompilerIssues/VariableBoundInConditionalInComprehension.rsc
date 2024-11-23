module lang::rascal::tests::basic::CompilerIssues::VariableBoundInConditionalInComprehension

data AType = avalue() | aparameter(str pname, AType bound);

// The original code from CollectDeclaration:
// The issue is that "typeVarBounds[tvname2]" is moved before the comprehension (because the variable "tvname2" has to be declared)
// and then gives IndexOutOfBounds error

map[str, AType] propagateParams(map[str,AType] typeVarBounds){
    return (tvname : (aparameter(tvname2, _) := typeVarBounds[tvname]) ? typeVarBounds[tvname2] : typeVarBounds[tvname] | tvname <- typeVarBounds);}
  
// The following equivalent code is compiled correcly:  
//map[str, AType] propagateParams(map[str,AType] typeVarBounds){
//    AType find(str tvname) = (aparameter(tvname2, _) := typeVarBounds[tvname]) ? typeVarBounds[tvname2] : typeVarBounds[tvname] ;
//    return (tvname : find(tvname) | tvname <- typeVarBounds);
//}

@ignoreCompiler{Compiled code crahes with IndexOutOfBounds}
test bool variableBoundInConditionalInComprehension() =  propagateParams(("T":avalue())) == ("T" : avalue());