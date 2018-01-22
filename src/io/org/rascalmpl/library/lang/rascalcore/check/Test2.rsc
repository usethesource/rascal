@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
@bootstrapParser
module lang::rascalcore::check::Test2
 
import analysis::graphs::Graph;
//import IO;
import Set;
import Map;
import Message;
//import Node;
import Relation;
//mport util::Reflective;
//import DateTime;
import String;
//import ValueIO;
import Type;

//import lang::rascal::checker::ListUtils;
//import lang::rascal::checker::TreeUtils;
import lang::rascal::types::AbstractKind;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
//import lang::rascal::types::ConvertType;
import lang::rascal::types::TypeSignature;
import lang::rascal::types::TypeInstantiation;

//import lang::rascal::grammar::definition::Symbols;
import lang::rascal::meta::ModuleInfo;
//import lang::rascal::types::Util;

extend lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckModule;
import lang::rascal::\syntax::Rascal;


public CheckResult convertAndExpandType(Type t, Configuration c) { }
@doc{Check the types of Rascal parameters: Default (DONE) }
public CheckResult checkParameters((Parameters)`( <Formals fs> <KeywordFormals kfs>)`, Configuration c) { }

@doc{Check the types of Rascal parameters: VarArgs (DONE) }
public CheckResult checkParameters((Parameters)`( <Formals fs> ... <KeywordFormals kfs>)`, Configuration c) { }

public KeywordFormals getKeywordFormals((Parameters)`( <Formals fs> <KeywordFormals kfs>)`) = kfs;
public KeywordFormals getKeywordFormals((Parameters)`( <Formals fs> ... <KeywordFormals kfs>)`) = kfs;

public tuple[Configuration,KeywordParamMap] checkKeywordFormals((KeywordFormals)`<OptionalComma oc> <{KeywordFormal ","}+ kfl>`, Configuration c, bool typesOnly=true) {
}

public CheckResult checkStatementSequence(list[Statement] ss, Configuration c) { }
private Configuration prepareFunctionBodyEnv(Configuration c) { }

@doc{Check the types of Rascal expressions: Closure (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Type t> <Parameters ps> { <Statement+ ss> }`, Configuration c) {
    
    //Configuration cFun; // TODO: type was added for new (experimental) type checker
    < cFun, rt > = convertAndExpandType(t,c);
    Symbol funType = Symbol::\func(rt,[],[]);
    cFun = addClosure(cFun, funType, ( ), exp@\loc);
    
    //Symbol ptTuple;// TODO: type was added for new (experimental) type checker
    < cFun, ptTuple > = checkParameters(ps, cFun);
    list[Symbol] parameterTypes = getTupleFields(ptTuple);

    < cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(ps), cFun, typesOnly=false);
    
    paramFailures = { pt | pt <- (parameterTypes+toList(keywordParams<1>)), isFailType(pt) };
    if (size(paramFailures) > 0) {
        funType = collapseFailTypes(paramFailures + makeFailType("Could not calculate function type because of errors calculating the parameter types", exp@\loc));     
    } else {
        funType = makeFunctionTypeFromTuple(rt, false, \tuple(parameterTypes));
    }
    
    cFun.store[head(cFun.stack)].rtype = funType;
    cFun.store[head(cFun.stack)].keywordParams = keywordParams;
        
    cFun = prepareFunctionBodyEnv(cFun);
    < cFun, st > = checkStatementSequence([ssi | ssi <- ss], cFun);
    
    c = recoverEnvironmentsAfterCall(cFun,c);
 
    if (isFailType(funType))
        return markLocationFailed(c, exp@\loc, funType); 
    else
        return markLocationType(c,exp@\loc, funType);
}