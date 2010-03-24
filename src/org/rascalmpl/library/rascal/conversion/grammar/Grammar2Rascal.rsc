module rascal::conversion::grammar::Grammar2Rascal

import rascal::parser::Grammar;
import Set;
import ParseTree;

public str grammar2rascal(Grammar g, str name) {
  return "module <name> <grammar2rascal(g)>";
}

public str grammar2rascal(Grammar g) {
return "<for (Production p <- g.productions, /prod(_,_,_) := p) {><prod2rascal(p)><}>";
}

public str topProd2rascal(Production p) {
  if (/prod(_,rhs,_) := p) {
    return "<start(_) := rhs ? "start":""> <symbol2rascal(rhs)> = <prod2rascal(p)>";
  }
  throw "could not find out defined symbol for <p>";
}

public str prod2rascal(Production p) {
  switch (p) {
    case choice(s) : {
      <f,s> = takeOneFrom(s);
      return "<prod2rascal(f)>
<for (pr <- s){>| <prod2rascal(pr)><}>";
    }
    case first(s) : {
      <f,s> = takeOneFrom(s);
      return "<prod2rascal(f)>
<for (pr <- s){> <prod2rascal(pr)><}>";
    }
    case \assoc(a,s) : {
      <f,s> = takeOneFrom(s);
      return "(<assoc2rascal(a)>:
<prod2rascal(f)>
<for (pr <- s){>| <prod2rascal(pr)><}>
)";
    }
    case diff(p,s) : {
     <f,s> = takeOneFom(s);
     return "<prod2rascal(p)>
           - <prod2rascal(f)>
 <for (pr <- s){>- <prod2rascal(pr)><}>";
    }
    case prod(_,lit(_),_) : return "";
    case prod(lhs,rhs,attrs) : {
      return "<attrs2mods(attrs)> <for(s <- lhs){><symbol2rascal(s)><}>;";
    }
    case regular(_,_) : return "";
    default: throw "missed a case <p>";
  }
}

public str attrs2mods(Attributes attrs) {
  switch (attrs) {
    case \no-attrs(): return "";
    case \attrs([a*,term(cons(c)),b*]) : return attrs2mods(\attrs([a,b])) + " <c>:";
    case \attrs([a,b*]) : return "<attr2mod(a)> <attrs2mods(b)>";
    default:   throw "missed a case <attrs>";
  }
}

public str attr2mod(Attr a) {
  switch(a) {
    case \assoc(\left()): return "left";
    case \assoc(\right()): return "right";
    case \assoc(\non-assoc()): return "non-assoc";
    case \assoc(\assoc()): return "assoc";
    case term("lex"()): return "lex";
    case term(t): return "<t>";
  }
}

public str symbol2rascal(Symbol sym) {
  switch (sym) {
    case sort(x) : return x;
    case lit(x) : return "\"<x>\"";
    case \char-class(x) : return cc2rascal(x);
    case opt(x) : return "<symbol2rascal(x)>?";
    case iter(x) : return "<symbol2rascal(x)>+";
    case \iter-star(x) : return "<symbol2rascal(x)>*";
    case \iter-sep(x,seps) : return "{<symbol2rascal(x)> <seps2rascal(seps[1])>}+";
    case \iter-star-sep(x,seps) : return "{<symbol2rascal(x)> <seps2rascal(seps[1])>}*";
  }
  throw "missed a case <sym>";
}

public str seps2rascal(list[Symbol] syms) {
  switch (syms) {
    case [_,s,_] : return symbol2rascal(s);
    case [s] : return symbol2rascal(s);
    case [] : return "";
    default: throw "missed a case <syms>";
  }
}

public str cc2rascal(list[CharRange] ranges) {
  return "[<for (r <- ranges){><range2rascal(r)><}>]";
}

public str range2rascal(CharRange range) {
  switch (range) {
    case range(c,c) : return char2rascal(c);
    case range(c,d) : return "<char2rascal(c)-char2rascal(d)>";
    default: throw "missed a case <range>";
  }
}

public str char2rascal(int ch) {
  if (ch < 256) {
    return "\\<ch % 256><ch % 64><ch % 8>";
  }
  else {
    return "\\u<ch & 65536><ch % 4096><ch % 256><ch % 16>";
  }
}