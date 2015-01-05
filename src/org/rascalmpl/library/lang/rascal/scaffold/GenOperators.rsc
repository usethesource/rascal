@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::semantics::GenOperators

import Relation;
import Set;
import String;
import List;

public int numOfCases() = 
  ( 0 | it + size(unOps[op]) | op <- unOps )
  + ( 0 | it + size(binOps[op]) | op <- binOps )
  + ( 0 | it + size(triOps[op]) | op <- triOps );

public map[str,set[str]] unOps = ( 
  "FieldAccess": {"tuple", "loc", "map", "cons", "datetime"},
  "FieldProject": {"rel"},
  "Is": {"cons", "node"},
  "Has": {"cons", "tuple", "rel", "map", "node"}, 
  "TransitiveClosure": {"rel", "list"},
  "TransitiveReflexiveClosure": {"rel", "list"},
  "IsDefined": {"map"},
  "Negation": {"bool"},
  "Negative": {"num", "int", "real", "rat"},
  "Splice": {"list", "set"}
);

public map[str, rel[str,str]] binOps = (
  "Subscript": {<"tuple", "int">, <"list", "int">, <"node", "int">, 
                <"map", "value">, <"rel", "value">, <"str", "int">}, 
  "FieldUpdate": {<"tuple", "value">, <"loc", "value">, <"map", "value">, <"cons", "value">,
                   <"datetime", "value">},
  "SetAnnotation": {<"cons", "value">, <"node", "value">},
  "GetAnnotation": {<"cons", "value">, <"node", "value">},
  "Composition": {"fun", "map", "rel", "list"} * {"fun", "map", "rel", "list"},  
  "Product": {"set", "map", "list"} * {"set", "map", "list"} + 
               {"int", "real", "rat", "num"} * {"int", "real", "rat", "num"} +
               {"int"} * {"list", "str", "set"},  
  "Join": {"set", "map", "list"} * {"set", "map", "list"},  
  "Remainder": {"int", "num", "rat"} * {"int", "num", "rat"},   
  "Division": {"int", "num", "real", "rat"} * {"int", "num", "real", "rat"},   
  "Intersection": {"set", "map", "list"} * {"set", "map", "list"},  
  "Addition": {"set", "list"} * {"set", "list"} + {<"map", "map">, <"str", "str">,<"fun", "fun">} +   
          {"int", "num", "real", "rat"} * {"int", "num", "real", "rat"},
  "Subtraction": {"set", "list"} * {"set", "list"} + {<"map", "map">, 
                    <"map", "list">, <"map", "set">, <"str", "str">} +   
          {"int", "num", "real", "rat"} * {"int", "num", "real", "rat"},
  "InsertAfter": {"set", "list"} * {"value"} + {<"str", "str">, <"int", "int">},
  "InsertBefore": {"value"} * {"set", "list"} + {<"str", "str">, <"int", "int">},
  "Modulo": {<"int", "int">, <"rat", "rat">},
  "NotIn": {"value"} * {"set", "list", "map"} + {<"str", "str">},
  "In": {"value"} * {"set", "list", "map"} + {<"str", "str">},
  "GreaterThanOrEq": {"int", "rat", "real", "num"} * {"int", "rat", "real", "num"} +
                ident({"set", "list", "map", "str", "tuple", "loc", "datetime", "bool"}),
  "LessThanOrEq": {"int", "rat", "real", "num"} * {"int", "rat", "real", "num"} +
                ident({"set", "list", "map", "str", "tuple", "loc", "datetime", "bool"}),
  "LessThan": {"int", "rat", "real", "num"} * {"int", "rat", "real", "num", "bool"} +
                ident({"set", "list", "map", "str", "tuple", "loc", "datetime", "bool"}),
  "GreaterThan": {"int", "rat", "real", "num"} * {"int", "rat", "real", "num", "bool"} +
                ident({"set", "list", "map", "str", "tuple", "loc", "datetime", "bool"}),
  "Equals": ident({"int", "real", "num", "set", "list", "map", "str", "loc", "tuple",
                "datetime", "value", "cons", "node"}),
  "NonEquals": ident({"int", "real", "num", "rat", "set", "list", "map", "str", "loc", "tuple",
                "datetime", "value", "cons", "node", "bool"}),
  "IfDefinedOtherwise": {<"value", "value">},
  "NoMatch": {<"value", "value">},
  "Match": {<"value", "value">},
  "Implication": {<"bool", "bool">},
  "Equivalence": {<"bool", "bool">},
  "And": {<"bool", "bool">},
  "Or": {<"bool", "bool">}
);
  
 public map[str, rel[str,str,str]] triOps = 
  ("IfThenElse": {<"bool", "value", "value">});
  
public set[str] allTypes 
  = ( {} | it + carrier(binOps[op]) | op <- binOps )
  + ( {} | it + carrier(triOps[op]) | op <- triOps )
  + ( {} | it + unOps[op] | op <- unOps );
 
 // println(( "" | it + "public str type2iface(\"<t>\") = \"\";\n" | t <- allTypes )) 
public str type2iface("fun") = "FunctionType";
public str type2iface("list") = "IList";
public str type2iface("real") = "IReal";
public str type2iface("map") = "IMap";
public str type2iface("datetime") = "IDateTime";
public str type2iface("rel") = "IRelation";
public str type2iface("cons") = "IConstructor";
public str type2iface("str") = "IString";
public str type2iface("value") = "IValue";
public str type2iface("int") = "IInteger";
public str type2iface("bool") = "IBool";
public str type2iface("tuple") = "ITuple";
public str type2iface("num") = "INumber";
public str type2iface("rat") = "IRational";
public str type2iface("set") = "ISet";
public str type2iface("node") = "INode";
public str type2iface("loc") = "ISourceLocation";

 

public str genClasses() {
  ms = for (op <- binOps) {
    append genBinClass(op, binOps[op]);
  }
  return intercalate("\n\n", ms);
}

public str genBinClass(str op, rel[str,str] args) {
  return "public abstract class <classNameForOp(op)> extends Expression {
         '  <for (<a1, a2> <- args) {>
         '  <genMethod(op, [a1, a2])>
         '  <}>
         '}";
}
 
public str classNameForOp(str op) = op;
 
public str genMethod(str op, list[str] types) {
   // todo: do we need the return type or will we just cast?
   types = [ type2iface(t) | t <- types ];
   params = [ "<types[i]> arg<i>" | i <- [0,1..size(types)] ];
   return "public static abstract class <op>On<intercalate("And", types)> extends <classNameForOp(op)> {
          '  public IValue interpret(<intercalate(", ", params)>) {
          '    throw new ImplementationError(\"Operator <op> is not implemented for argument types <intercalate(" and ", types)>\");
          '  }
          '}";
}
  
