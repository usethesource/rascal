module experiments::Compiler::Examples::QL::util::Explode

import ParseTree;
import Node;
import lang::rascal::grammar::definition::Priorities;
import Grammar;
import String;
import List;
import IO;

default str useAsLayout() = " ";

&T<:Tree explode(type[&T <: Tree] sort, node ast) {
  if (&T t := value2tree(sort.symbol, ast, sort.definitions)) {
    return t;
  }
  assert false: "Wrong type returned by value2tree.";
}

Tree value2tree(Symbol x, value v, map[Symbol, Production] defs) {
  if (x is label) {
    x = x.symbol;
  }
  //println("X = <x>");
  if (node ast := v) {
    alts = defs[x].alternatives;
    for (/p:prod(label(n, _), _, _) <- alts) {
      //println("p = <p>");
      if (n == getName(ast)) {
        //println("FOUND ****************** <n>");
        return value2tree(p, ast, defs); 
      }
    }
    throw "Could not find production for <x> <ast>";
  }
  if (str s := v) {
    return appl(prod(x, [], {}), charsOf(s));
  }
  if (int i := v) {
    return appl(prod(x, [], {}), charsOf("<i>"));
  }
  if (list[value] l := v) {
    return list2regular(x, l, defs); 
  }
  assert false: "Missed a case in value2tree";
}


Tree list2regular(iters:\iter-star-seps(s, seps), list[value] vs, map[Symbol, Production] defs) {
  //println("values = <vs>");
  int last = size(vs) - 1;
  i = 0;
  as = [];
  as = args: for (v <- vs) {
     //as += [value2tree(s, v, defs)];
     append value2tree(s, v, defs);
     if (i < last) {
       for (Symbol sep <- seps) {
         //println("ADDDING SEP ------------ <sep>");
         //as += [sym2default(sep)];
         append args: sym2default(sep);
       }
     }
     i += 1;
  }
  return appl(regular(iters), as);
}

Tree value2tree(Production p, node ast, map[Symbol, Production] defs) 
  = appl(p, zipIt(p.symbols, getChildren(ast), defs));


list[Tree] zipIt(list[Symbol] syms, list[value] args, map[Symbol, Production] defs) {
  ////println("ARGS = <args>");
  //println("SYMS = <syms>");
  i = 0;
  ts = []; 
  ts = for (s <- syms) {
    //println("sym = <s>");
    if (isASTsymbol(s)) {
      //println("<s> = it\'s ast");
      //ts += [value2tree(s, args[i], defs)];
      append value2tree(s, args[i], defs);
      i += 1;
    }
    else {
      ////println("it\'s not");
      //ts += [sym2default(s)];
      append sym2default(s);
    }
  }
  return ts;
}

Tree sym2default(x:\layouts(l)) = appl(prod(x, [], {}), charsOf(useAsLayout()));
Tree sym2default(x:\lit(s)) = appl(prod(x, [], {}), charsOf(s));
Tree sym2default(x:\cilit(s)) = appl(prod(x, [], {}), charsOf(s));
Tree sym2default(conditional(Symbol x, _)) = sym2default(x);
Tree sym2default(x:empty()) = appl(prod(x, [], {}), []);


list[Tree] charsOf(str x) = [ char(c) | c <- chars(x) ];

bool isASTsymbol(\layouts(_)) = false; 
bool isASTsymbol(\keywords(str name)) = false;
bool isASTsymbol(\lit(str string)) = false;
bool isASTsymbol(\cilit(str string)) = false;
bool isASTsymbol(\conditional(Symbol s, _)) = isASTsymbol(s);
bool isASTsymbol(\empty()) = false;
default bool isASTsymbol(Symbol _) = true;

