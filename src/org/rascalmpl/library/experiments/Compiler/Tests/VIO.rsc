module experiments::Compiler::Tests::VIO

import IO;
import ValueIO;
import Type;

void main(){
  l = |rascal:///experiments/Compiler/Tests/XXX|;
  v = #int;
  writeTextValueFile(l, v);
  w = readTextValueFileWithEmbeddedTypes(type(\value(), #Symbol.definitions), l);
  //w1 = visit(w) { case "type"(t, definitions) => type(nodeToSymbol(t), definitions) }
  println("<typeOf(v)> v = <v>, <typeOf(w)>  w = <w>");
}

//Symbol nodeToSymbol("int"()) = \int();
//Symbol nodeToSymbol("bool"()) = \bool();
//Symbol nodeToSymbol("real"()) = \real();
//Symbol nodeToSymbol("rat"()) = \rat();
//Symbol nodeToSymbol("str"()) = \str();
//Symbol nodeToSymbol("num"()) = \num();
//Symbol nodeToSymbol("node"()) = \node();
//Symbol nodeToSymbol("void"()) = \void();
//Symbol nodeToSymbol("value"()) = \value();
//Symbol nodeToSymbol("loc"()) = \loc();
//Symbol nodeToSymbol("datetime"()) = \datetime();  
//
//Symbol nodeToSymbol("label"(str name, node sym)) = \label(name, nodeToSymbol(sym));
// 
//Symbol nodeToSymbol("set"(node sym)) = \set(nodeToSymbol(sym));
//Symbol nodeToSymbol("rel"(list[node] syms)) = \rel([nodeToSymbol(sym) | sym <- syms]);
//
//Symbol nodeToSymbol("list"(node sym)) = \list(nodeToSymbol(sym));
//Symbol nodeToSymbol("lrel"(list[node] syms)) = \lrel([nodeToSymbol(sym) | sym <- syms]);
//
//Symbol nodeToSymbol("tuple"(list[node] syms)) = \tuple([nodeToSymbol(sym) | sym <- syms]);
//Symbol nodeToSymbol("map"(node from, node to)) = \map(nodeToSymbol(from),  nodeToSymbol(to));
//Symbol nodeToSymbol("bag"(node sym)) = \bag(nodeToSymbol(sym));
//Symbol nodeToSymbol("adt"(str name, list[node] parameters)) = \adt(name, [nodeToSymbol(param) | param <- parameters]);
//Symbol nodeToSymbol("cons"(node adt, str name(), list[node] parameters)) = \adt(nodeToSymbol(adt), name, [nodeToSymbol(param) | param <- parameters]);
//Symbol nodeToSymbol("alias"(str name, list[node] parameters, node aliased)) = \alias(name, [nodeToSymbol(param) | param <- parameters], nodeToSymbol(aliased));
//Symbol nodeToSymbol("func"(node ret, list[node] parameters)) = \func(nodeToSymbol(ret), [nodeToSymbol(param) | param <- parameters]);
//Symbol nodeToSymbol("var-func"(node ret, list[node] parameters, Symbol varArg)) = \func(nodeToSymbol(ret), [nodeToSymbol(param) | param <- parameters], nodeToSymbol(varArg));
//Symbol nodeToSymbol("reified"(node sym)) = \reified(nodeToSymbol(sym));
//
//Symbol nodeToSymbol("parameter"(str name, node bound)) = \parameter(name, nodeToSymbol(bound));