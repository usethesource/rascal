module lang::rascal::checker::TTL::Library

import Type;
import List;
import IO;
import String;
import Map;

data Symbol = LUB(Symbol l, Symbol r);

alias BIND = tuple[bool matches, map[str, Symbol] bindings];

BIND bind(\parameter(name, t1), Symbol s) = <true, (name : s)>;

// list and lrel
BIND bind(\list(Symbol s), \list(Symbol t)) = bind(s, t); 
BIND bind(\lrel(list[Symbol] l), \lrel(list[Symbol] r)) = bindList(l, r);

BIND bind(\list(Symbol s), \lrel(list[Symbol] r)) = (s == \void()) ? <true, ()> : bind(s, /*(size(r) == 1) ? r[0] : */\tuple(r));
BIND bind(\lrel(list[Symbol] l), \list(Symbol r)) = (r == \void()) ? <true, ()> : bind(/*(size(l) == 1) ? l[0] : */\tuple(l), r);

// set and rel

BIND bind(\set(Symbol s), \set(Symbol t)) = bind(s, t);
BIND bind(\rel(list[Symbol] l), \rel(list[Symbol] r)) = bindList(l, r);

BIND bind(\set(Symbol s), \rel(list[Symbol] r)) = (s == \void()) ? <true, ()> : bind(s, /*(size(r) == 1) ? r[0] : */\tuple(r));
BIND bind(\rel(list[Symbol] l), \set(Symbol r)) = (r == \void()) ? <true, ()> : bind(/*(size(l) == 1) ? l[0] :*/ \tuple(l), r);

BIND bind(\tuple(t1), \tuple(t2)) = bindList(t1, t2);

BIND bind(\tuple(t1), \void()) = bindList(t1, [\void() | i <- [0..size(t1)]]);

BIND bind(\map(from1, to1), \map(from2, to2)) {
  <m1, bindings1> = bind(from1, from2);
  <m2, bindings2> = bind(to1, to2);
  return <m1 && m2, bindings1 + bindings2>;
}

default BIND bind(Symbol t1, Symbol t2) = <subtype(t2, t1), ()>;

BIND bindList(list[Symbol] left, list[Symbol] right){
  if(size(left) != size(right))
  	return <false, ()>;
 
  bindings = ();
  for(int i <- index(left)){
     <matches, newBindings> = bind(left[i], right[i]);
     if(!matches)
     	return <false, bindings>;
     bindings = merge(bindings, newBindings);
  }
  return <true, bindings>;
}

test bool tst(&L arg){
 <m, b> = bind(typeOf(arg), typeOf(arg));
 return m;
}

map[str, Symbol] merge(map[str, Symbol] lbindings, map[str,Symbol] rbindings){
 common = domain(lbindings) & domain(rbindings);
 res = ();
 for(k <- lbindings){
    res[k] = (k in common) ? lub(lbindings[k], rbindings[k]) : lbindings[k];
 }
 for(k <- rbindings){
    if(k notin common)
    	res[k] = rbindings[k];
 }
 return res;
}

Symbol normalize(Symbol s1, map[str, Symbol] bindings){
  s2 = visit(s1){ case parameter(p1,t1) => bindings[p1] ? \void() };
  s3 = visit(s2){ case LUB(t1, t2) => lub(t1, t2) };
  s4 = visit(s3){
   	case \list(\tuple(t)) => \lrel(t)
   	case \list(\void())   => \lrel([\void()])
   	case \set(\tuple(t))  => \rel(t)
   	case \set(\void())    => \rel([\void()])
   };
   
   //println("normalize: <s1>, <bindings> ==\> <s4>");
   return s4;
}

value escape(value v){
   if(str s := v){
 
    s1 = visit(s){
      case /^\</ => "\\\<"
      case /^\>/ => "\\\>"
      case /^"/  => "\\\""
      case /^'/  => "\\\'"
      case /^\\/ => "\\\\"
    };
    return "\"" + s1 +  "\"";
   }
   return v;
}

bool validate(str tname, str input, Symbol actualType, Symbol expectedType, str descr){
  if(subtype(actualType, expectedType))
     return true;
  if(descr != "") descr = " --- " + descr;
  println("[<tname>] *** Failed test for: <input><descr>\nexpectedType: <expectedType>, actualType: <actualType>\n");
  return false;
}

bool invalid(str tname, str input, Symbol actualType, Symbol expectedType, str descr){
  if(descr != "") descr = " --- " + descr;
  if(subtype(actualType, expectedType)){
     println("[<tname>] *** Failed test for: <input><descr>\nexpectedType: <expectedType>, actualType: <actualType>\n");
     return false;
  }   
  return true;
}

bool validate(str tname, str input, Symbol actualType, Symbol expectedType, value arg1, value arg2, str descr){
  if(subtype(actualType, expectedType))
     return true;
  if(descr != "") descr = " --- " + descr;
  println("[<tname>] *** Failed test for: <input><descr>\narg1=<arg1>, arg2=<arg2>\nexpectedType: <expectedType>, actualType: <actualType>\n");
  return false;
}

bool validate(str tname, str input, Symbol actualType, Symbol expectedType, value arg1, str descr){
  if(subtype(actualType, expectedType))
     return true;
  if(descr != "") descr = " --- " + descr;
  println("[<tname>] *** Failed test for: <input><descr>\narg1=<arg1>\nexpectedType: <expectedType>, actualType: <actualType>\n");
  return false;
}

str buildStatements(str txt, map[str, tuple[type[&T] tp, value val]] env){
   //println("buildStatements: <txt>, <env>");
   for(id <- env){
      //println(" tp = <env[id].tp>");
      txt = replaceAll(txt, "_X<id>", "X<id>");
      txt = replaceAll(txt, "_T<id>", "<env[id].tp>");
      txt = replaceAll(txt, "_V<id>", "<escape(env[id].val)>");
   }
   // println("buildStatements =\> <txt>");
   return txt;              
}
