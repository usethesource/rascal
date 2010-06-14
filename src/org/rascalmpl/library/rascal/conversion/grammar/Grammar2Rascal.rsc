module rascal::conversion::grammar::Grammar2Rascal

import rascal::parser::Grammar;
import Set;
import ParseTree;

// TODO this is work in progress

public str grammar2rascal(Grammar g, str name) {
  return "module <name> <grammar2rascal(g)>";
}

public str grammar2rascal(Grammar g) {
  return ( "" | it + topProd2rascal(p) | Production p <- g.productions);
}

public str topProd2rascal(Production p) {
  if (/prod(_,lit(_),_) := p) return ""; // ignore generated productions

  if (/prod(_,rhs,_) := p) {
    return "<(start(_) := rhs) ? "start ":"">syntax <symbol2rascal(rhs)> = <prod2rascal(p)>;\n\n";
  }
  if (regular(_,_) := p) {
    return ""; // ignore generated stubs
  }
  throw "could not find out defined symbol for <p>";
}

public str prod2rascal(Production p) {
  switch (p) {
    case choice(s) : {
      <f,s> = takeOneFrom(s);
      return "<prod2rascal(f)><for (pr <- s){>| <prod2rascal(pr)><}>";
    }
    case first(s) : {
      <f,s> = takeOneFrom(s);
      return "<prod2rascal(f)>
<for (pr <- s){> <prod2rascal(pr)><}>";
    }
    case \assoc(a,s) : {
      <f,s> = takeOneFrom(s);
      return "(<attr2mod(\assoc(a))>:
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
    case prod(list[Symbol] lhs,Symbol rhs,Attributes attrs) : {
      return "<attrs2mods(attrs)><for(s <- lhs){><symbol2rascal(s)> <}>";
    }
    case regular(_,_) : return "";
    default: throw "missed a case <p>";
  }
}

public str attrs2mods(Attributes as) {
  switch (as) {
    case \no-attrs(): 
      return "";
    case \attrs([list[Attr] a,term(cons(c)),list[Attr] b]) : 
      return attrs2mods(\attrs([a,b])) + " <c>:";
    case \attrs([a,b*]) : 
      return "<attr2mod(a)> <attrs2mods(\attrs(b))>";
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
    case \bracket(): return "bracket";
    default: throw "missed a case <a>";
  }
}

public str symbol2rascal(Symbol sym) {
  switch (sym) {
    case label(str l, x) : return "<symbol2rascal(x)> <l>";  
    case sort(x) : return x;
    case lit(x) : return "\"<x>\"";
    case \char-class(x) : return cc2rascal(x);
    case opt(x) : return "<symbol2rascal(x)>?";
    case iter(x) : return "<symbol2rascal(x)>+";
    case \iter-star(x) : return "<symbol2rascal(x)>*";
    case \iter-sep(x,seps) : return "{<symbol2rascal(x)> <for(s <- seps){><symbol2rascal(s)> <}>}+";
    case \iter-star-sep(x,seps) : return "{<symbol2rascal(x)> <for(s <- seps){><symbol2rascal(s)> <}>}*";
    case \layout(): return "";
    case \start(x): return symbol2rascal(x);
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

public str range2rascal(CharRange r) {
  switch (r) {
    case range(c,c) : return char2rascal(c);
    case range(c,d) : return "<char2rascal(c)>-<char2rascal(d)>";
    default: throw "missed a case <range>";
  }
}

public str char2rascal(int ch) {
  if (ch < 256) {
    return "\\<ch % 256><ch % 64><ch % 8>";
  }
  else {
    return "\\u<ch % 65536><ch % 4096><ch % 256><ch % 16>";
  }
}