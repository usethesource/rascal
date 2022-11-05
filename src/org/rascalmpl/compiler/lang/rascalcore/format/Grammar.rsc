@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc {
  Convert the Rascal internal grammar representation format (Grammar) to 
  a syntax definition in Rascal source code.
}
module lang::rascalcore::format::Grammar

import lang::rascalcore::check::AType;
import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Literals;
import lang::rascalcore::format::Escape;
import IO;
import Set;
import List;
import String;
import ValueIO;
import analysis::graphs::Graph;
import Relation;
import Node;

public str prod2rascal(AProduction p) {
  switch (p) {
    case choice(s, alts) : {
        	<fst, rest> = takeOneFrom(alts);
			return "<prod2rascal(fst)><for (pr:prod(_,_) <- rest) {>
			       '| <prod2rascal(pr)><}><for (pr <- rest, prod(_,_) !:= pr) {>
			       '| <prod2rascal(pr)><}>";
		}
    case priority(s, alts) :
        return "<prod2rascal(head(alts))><for (pr <- tail(alts)) {>
               '\> <prod2rascal(pr)><}>"; 
    case associativity(s, a, alts) : {  
    		<fst, rest> = takeOneFrom(alts);
    		return "<associativity(a)> 
    		       '  ( <prod2rascal(fst)><for (pr <- rest) {>
    		       '  | <prod2rascal(pr)><}>
    		       '  )";
 		}

    case p:prod(AType rhs, list[AType] lhs) : {
        attrs = "";
        if(p.attributes?) attrs = isEmpty(p.attributes) ? "" :"<for (a <- p.attributes) {> <attr2mod(a)><}>";
        elms = "<for(s <- lhs){><symbol2rascal(s)> <}>";
        if(rhs.label?){
            return "<attrs><reserved(rhs.label)>: <elms>";
        } else {
            return "<attrs> <elms>";
        }
    }
 
    case regular(_) :
    	    return "";
    
    default: throw "missed a case <p>";
  }
}

str associativity(\left()) = "left";
str associativity(\right()) = "right";
str associativity(\assoc()) = "assoc";
str associativity(\non-assoc()) = "non-assoc";

private set[str] rascalKeywords = { "value" , "loc" , "node" , "num" , "type" , "bag" , "int" , "rat" , "rel" , "real" , "tuple" , "str" , "bool" , "void" , "datetime" , "set" , "map" , "list" , "int" ,"break" ,"continue" ,"rat" ,"true" ,"bag" ,"num" ,"node" ,"finally" ,"private" ,"real" ,"list" ,"fail" ,"filter" ,"if" ,"tag" ,"extend" ,"append" ,"rel" ,"void" ,"non-assoc" ,"assoc" ,"test" ,"anno" ,"layout" ,"data" ,"join" ,"it" ,"bracket" ,"in" ,"import" ,"false" ,"all" ,"dynamic" ,"solve" ,"type" ,"try" ,"catch" ,"notin" ,"else" ,"insert" ,"switch" ,"return" ,"case" ,"while" ,"str" ,"throws" ,"visit" ,"tuple" ,"for" ,"assert" ,"loc" ,"default" ,"map" ,"alias" ,"any" ,"module" ,"mod" ,"bool" ,"public" ,"one" ,"throw" ,"set" ,"start" ,"datetime" ,"value" };

public str reserved(str name) {
  return name in rascalKeywords ? "\\<name>" : name;   
}
//
//test bool noAttrs() = prod2rascal(prod(sort("ID-TYPE"), [sort("PICO-ID"),lit(":"),sort("TYPE")],{}))
//     == "PICO-ID \":\" TYPE ";
//
//test bool AttrsAndCons() = prod2rascal(
//     prod(label("decl",sort("ID-TYPE")), [sort("PICO-ID"), lit(":"), sort("TYPE")],
//              {})) ==
//               "decl: PICO-ID \":\" TYPE ";
//               
//test bool CC() = prod2rascal(
//	 prod(label("whitespace",sort("LAYOUT")),[\char-class([range(9,9), range(10,10),range(13,13),range(32,32)])],{})) ==
//	 "whitespace: [\\t \\n \\a0D \\ ] ";
//
//test bool Prio() = prod2rascal(
//	priority(sort("EXP"),[prod(sort("EXP"),[sort("EXP"),lit("||"),sort("EXP")],{}),
//	                   prod(sort("EXP"),[sort("EXP"),lit("-"),sort("EXP")],{}),
//	                   prod(sort("EXP"),[sort("EXP"),lit("+"),sort("EXP")],{})])) ==
//	"EXP \"||\" EXP \n\> EXP \"-\" EXP \n\> EXP \"+\" EXP ";	
//
public str attr2mod(Attr a) {
  switch(a) {
    case \bracket(): return "bracket";
    case \tag(str x(str y)) : return "@<x>=\"<escape(y)>\"";
    case \tag(str x()) : return "@<x>";
    case \assoc(Associativity as) : return associativity(as);
    default : return "@Unsupported(\"<escape("<a>")>\")";
  }
}

public str symbol2rascal(AType sym) {
    if(!isEmpty(sym.label)){
       return "<symbol2rascal(unset(sym, "alabel"))> <sym.label>"; 
    } 
  switch (sym) {
    
    case aadt(x, list[AType] parameters, SyntaxRole sr) : {
        if(sr == layoutSyntax()) return "";
        return isEmpty(parameters) ? x : "<x>[<params2rascal(parameters)>]";
    }
    // Type incorrect, PK
    //case \parameter(x) :
    //    return "&" + replaceAll(x, "-", "_");
    case alit(x) :
    	return "\"<escape(x)>\"";
    case acilit(x) :
    	return "\'<escape(x)>\'";
   
    case \char-class(x) : 
       if (\char-class(y) := complement(sym)) {
         str norm = cc2rascal(x);
         str comp = cc2rascal(y);
         return size(norm) > size(comp) ? "!<comp>" : norm;
       } 
       else throw "weird result of character class complement";
    case \seq(syms):
        return "( <for(s <- syms){> <symbol2rascal(s)> <}> )";
    case opt(x) : 
    	return "<symbol2rascal(x)>?";
    case iter(x) : 
    	return "<symbol2rascal(x)>+";
    case \iter-star(x) : 
    	return "<symbol2rascal(x)>*";
    case \iter-seps(x,seps) :
        return iterseps2rascal(x, seps, "+");
    case \iter-star-seps(x,seps) : 
    	return iterseps2rascal(x, seps, "*");
    case alt(set[AType] alts): {
        <f,as> = takeOneFrom(alts);
        return "(" + (symbol2rascal(f) | "<it> | <symbol2rascal(a)>" | a <- as) + ")";
    }
     case seq(list[AType] ss): {
        <f,as> = takeOneFrom(ss);
        return "(" + (symbol2rascal(f) | "<it> <symbol2rascal(a)>" | a <- as) + ")";
    }
   
    case \start(x):
    	return symbol2rascal(x);
    // Following are type-incorrect, PK.
    //case intersection(lhs, rhs):
    //    return "<symbol2rascal(lhs)> && <symbol2rascal(rhs)>";
    //case union(lhs, rhs):
    // 	return "<symbol2rascal(lhs)> || <symbol2rascal(rhs)>";
    //case difference(Class lhs, Class rhs):
    // 	return "<symbol2rascal(lhs)> -  <symbol2rascal(rhs)>";
    //case complement(Class lhs):
    // 	return "!<symbol2rascal(lhs)>";
    case conditional(AType s, {ACondition c, ACondition d, *ACondition r}):
        return symbol2rascal(conditional(conditional(s, {c}), {d, *r})); 
    case conditional(s, {delete(t)}) :
        return "<symbol2rascal(s)> \\ <symbol2rascal(t)>"; 
    case conditional(s, {follow(t)}) :
        return "<symbol2rascal(s)> \>\> <symbol2rascal(t)>";
    case conditional(s, {\not-follow(t)}) :
        return "<symbol2rascal(s)> !\>\> <symbol2rascal(t)>";
    case conditional(s, {precede(t)}) :
        return "<symbol2rascal(s)> \<\< <symbol2rascal(s)> ";
    case conditional(s, {\not-precede(t)}) :
        return "<symbol2rascal(s)> !\<\< <symbol2rascal(s)> ";    
    case conditional(s, {"a-at-column"(int i)}) :
        return "<symbol2rascal(s)>@<i>";
    case conditional(s, {"a-begin-of-line"()}) :
        return "^<symbol2rascal(s)>";
    case conditional(s, {"a-end-of-line"()}) :
        return "<symbol2rascal(s)>$";
    case conditional(s, {"a-except"(str x)}) :
        return "<symbol2rascal(s)>!<x>";
    case conditional(s, {}): {
        println("WARNING: empty conditional <sym>");
        return symbol2rascal(s);
    }
    case empty(): 
        return "()"; 
  }

  throw "symbol2rascal: missing case <sym>";
}

public str iterseps2rascal(AType sym, list[AType] seps, str iter){
  separators = "<for(sp <- seps){><symbol2rascal(sp)><}>";
  if (separators != "")
     return "{<symbol2rascal(sym)> <separators>}<iter>";
  else
    return "<symbol2rascal(sym)><separators><iter>";
}

public str params2rascal(list[AType] params){
  len = size(params);
  if(len == 0)
  	return "";
  if(len == 1)
  	return symbol2rascal(params[0]);
  sep = "";
  res = "";
  for(AType p <- params){
      res += sep + symbol2rascal(p);
      sep = ", ";
  }
  return res;	
}

public str cc2rascal(list[ACharRange] ranges) {
  if (ranges == []) return "[]"; 
  return "[<range2rascal(head(ranges))><for (r <- tail(ranges)){> <range2rascal(r)><}>]";
}

public str range2rascal(ACharRange r) {
  switch (r) {
    case arange(c,c) : return makeCharClassChar(c);
    case arange(c,d) : return "<makeCharClassChar(c)>-<makeCharClassChar(d)>";
    //TODO:
    //case \empty-range():
    //                  return "";
    default: throw "range2rascal: missing case <r>";
  }
}
