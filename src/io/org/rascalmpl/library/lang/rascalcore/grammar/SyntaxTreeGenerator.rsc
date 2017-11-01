@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl - CWI}
@contributor{Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI}
module lang::rascalcore::grammar::SyntaxTreeGenerator

import Grammar;
import lang::rascalcore::grammar::definition::Parameters;
import ParseTree;

import IO;
import String;
import List;
import Set;
import util::Math;

private str header = "/*******************************************************************************
                     ' * Copyright (c) 2009-2015 CWI
                     ' * All rights reserved. This program and the accompanying materials
                     ' * are made available under the terms of the Eclipse Public License v1.0
                     ' * which accompanies this distribution, and is available at
                     ' * http://www.eclipse.org/legal/epl-v10.html
                     ' *
                     ' * Contributors:
                     ' *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
                     ' *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
                     ' *   * Paul Klint - Paul.Klint@cwi.nl - CWI
                     ' *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
                     ' *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
                     ' *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
                     ' *******************************************************************************/";
                     
data AST 
  = ast(str name, set[Sig] sigs) 
  | leaf(str name)
  ;
  
data Sig 
  = sig(str name, list[Arg] args, bool breakable = false)
  ;
  
data Arg 
  = arg(str typ, str name);

bool isBreakable(prod(_,_,{\tag("breakable"()), *_})) = true;
default bool isBreakable(Production p) = false;

public set[AST] grammarToASTModel(str pkg, Grammar g) {
  map[str, set[Sig]] m = ();
  set[Sig] sigs = {};
  set[AST] asts = {};
  
  g = visit(g) {
    case conditional(s,_) => s
  }
  
  for (/p:prod(label(c,sort(name)),_,_) := g) 
     m[name]?sigs += {sig(capitalize(c), productionArgs(pkg, p), breakable=isBreakable(p))};

  for (/p:prod(label(c,\parameterized-sort(name,[Symbol _:str _(str a)])),_,_) := g) 
     m[name + "_" + a]?sigs += {sig(capitalize(c), productionArgs(pkg, p), breakable=isBreakable(p))};

  for (sn <- m) 
    asts += ast(sn, m[sn]);
    
  for (/p:prod(\lex(s),_,_) := g) 
     asts += leaf(s);
     
  for (/p:prod(label(_,\lex(s)),_,_) := g) 
     asts += leaf(s);
 
  for (/p:prod(label(_,\parameterized-lex(s,[Symbol _:str _(str a)])),_,_) := g) 
     asts += leaf(s + "_" + a);
  
  return asts;
}

public void grammarToJavaAPI(loc outdir, str pkg, Grammar g) {
  arbSeed(42);
  model = grammarToASTModel(pkg, g);
  grammarToVisitor(outdir, pkg, model);
  grammarToASTClasses(outdir, pkg, model);
}

public void grammarToVisitor(loc outdir, str pkg, set[AST] asts) {
  ivisit = "package <pkg>;
           '
           'public interface IASTVisitor\<T\> {
           '<for (ast(sn, sigs) <- sort(asts), sig(cn, args) <- sort(sigs)) {>
           '  public T visit<sn><cn>(<sn>.<cn> x);
           '<}>
           '<for (leaf(sn) <- sort(asts)) {>
           '  public T visit<sn>Lexical(<sn>.Lexical x);
           '<}>
           '}";

  loggedWriteFile(outdir + "/IASTVisitor.java", ivisit);

  nullVisit = "package <pkg>;
              '
              'public class NullASTVisitor\<T\> implements IASTVisitor\<T\> {
              '<for (ast(sn, sigs) <- sort(asts), sig(cn, args) <- sort(sigs)) {>
              '  public T visit<sn><cn>(<sn>.<cn> x) { 
              '    return null; 
              '  }
              '<}>
              '<for (leaf(sn) <- sort(asts)) {>
              '  public T visit<sn>Lexical(<sn>.Lexical x) { 
              '    return null; 
              '  }
              '<}>
              '}";

   loggedWriteFile(outdir + "/NullASTVisitor.java", nullVisit);
}

public void grammarToASTClasses(loc outdir, str pkg, set[AST] asts) {
  for (a <- sort(asts)) {
     class = classForSort(pkg, ["io.usethesource.vallang.IConstructor", "io.usethesource.vallang.ISourceLocation"], a);
     loggedWriteFile(outdir + "/<a.name>.java", class); 
  }
}

public str classForSort(str pkg, list[str] imports, AST ast) {
  allArgs = { arg | /Arg arg <- ast };
  return "package <pkg>;
         '
         '<for (i <- sort(imports)) {>
         'import <i>;<}>
         '
         'public abstract class <ast.name> extends AbstractAST {
         '  public <ast.name>(ISourceLocation src, IConstructor node) {
         '    super(src /* we forget node on purpose */);
         '  }
         '
         '  <for (arg(typ, lab) <- sort(allArgs)) { clabel = capitalize(lab); >
         '  public boolean has<clabel>() {
         '    return false;
         '  }
         '
         '  public <typ> get<clabel>() {
         '    throw new UnsupportedOperationException();
         '  }<}>
         '
         '  <if (leaf(_) := ast) {><lexicalClass(ast.name)><}>
         '
         '  <for (ast is ast, Sig sig <- sort(ast.sigs)) { >
         '  public boolean is<sig.name>() {
         '    return false;
         '  }
         '
         '  <classForProduction(pkg, ast.name, sig)><}>
         '}"; 
}

public str classForProduction(str pkg, str super, Sig sig) {
  return "static public class <sig.name> extends <super> {
         '  // Production: <sig>
         '
         '  <for (arg(typ, name) <- sig.args) {>
         '  private final <typ> <name>;<}>
         '
         '  <construct(sig)>
         '
         '  @Override
         '  public boolean is<sig.name>() { 
         '    return true; 
         '  }
         '
         '  @Override
         '  public \<T\> T accept(IASTVisitor\<T\> visitor) {
         '    return visitor.visit<super><sig.name>(this);
         '  }
         '
         '  @Override
         '  protected void addForLineNumber(int $line, java.util.List\<AbstractAST\> $result) {
         '    if (getLocation().getBeginLine() == $line) {
         '      $result.add(this);
         '    }
         '    ISourceLocation $l;
         '    <for (arg(typ, name) <- sig.args) {><if (/java.util.List/ := typ) {>
         '    for (AbstractAST $elem : <name>) {
         '      $l = $elem.getLocation();
         '      if ($l.hasLineColumn() && $l.getBeginLine() \<= $line && $l.getEndLine() \>= $line) {
         '        $elem.addForLineNumber($line, $result);
         '      }
         '      if ($l.getBeginLine() \> $line) {
         '        return;
         '      }
         '
         '    }<} else {>
         '    $l = <name>.getLocation();
         '    if ($l.hasLineColumn() && $l.getBeginLine() \<= $line && $l.getEndLine() \>= $line) {
         '      <name>.addForLineNumber($line, $result);
         '    }
         '    if ($l.getBeginLine() \> $line) {
         '      return;
         '    }
         '    <}><}>
         '  }
         '
         '  @Override
         '  public boolean equals(Object o) {
         '    if (!(o instanceof <sig.name>)) {
         '      return false;
         '    }        
         '    <sig.name> tmp = (<sig.name>) o;
         '    return true <for (arg(_, name) <- sig.args) {>&& tmp.<name>.equals(this.<name>) <}>; 
         '  }
         ' 
         '  @Override
         '  public int hashCode() {
         '    return <arbPrime(1000)> <for (arg(_, name) <- sig.args) { >+ <arbPrime(1000)> * <name>.hashCode() <}>; 
         '  } 
         '
         '  <for (arg(typ, name) <- sig.args) { cname = capitalize(name); >
         '  @Override
         '  public <typ> get<cname>() {
         '    return this.<name>;
         '  }
         '
         '  @Override
         '  public boolean has<cname>() {
         '    return true;
         '  }<}>	
         '
         '  @Override
         '  public Object clone()  {
         '    return newInstance(getClass(), src, (IConstructor) null <cloneActuals(sig.args)>);
         '  }
         '  <if (sig.breakable) {>@Override
         '  public boolean isBreakable() {
         '    return true;
         '  }<}>        
         '}";
}

public str lexicalClass(str name) {
  return "static public class Lexical extends <name> {
         '  private final java.lang.String string;
         '  public Lexical(ISourceLocation src, IConstructor node, java.lang.String string) {
         '    super(src, node);
         '    this.string = string;
         '  }
         '  public java.lang.String getString() {
         '    return string;
         '  }
         '
         '  @Override
         '  public int hashCode() {
         '    return string.hashCode();
         '  }
         '
         '  @Override
         '  public boolean equals(Object o) {
         '    return o instanceof Lexical && ((Lexical) o).string.equals(string);  
         '  }
         '
         '  @Override
         '  public Object clone()  {
         '    return newInstance(getClass(), src, (IConstructor) null, string);
         '  }
         '
         '  @Override
         '  public AbstractAST findNode(int offset) {
         '    if (src.getOffset() \<= offset && offset \< src.getOffset() + src.getLength()) {
         '      return this;
         '    }
         '    return null;
         '  }
         '
         '  public java.lang.String toString() {
         '    return string;
         '  }
         '  public \<T\> T accept(IASTVisitor\<T\> v) {
         '    return v.visit<name>Lexical(this);
         '  }
         '}";
}


list[Arg] productionArgs(str pkg, Production p) {
   str l = "java.util.List";
   return for (label(str name, Symbol sym) <- p.symbols) {
     a = arg("", name);
     switch (sym) {
       case \sort(str s): a.typ = "<pkg>.<s>"; 
       case \lex(str s): a.typ = "<pkg>.<s>"; 
       case \iter(\sort(str s)): a.typ = "<l>\<<pkg>.<s>\>";  
       case \iter-star(\sort(str s)): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-seps(\sort(str s), _): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-star-seps(\sort(str s), _): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter(\lex(str s)): a.typ = "<l>\<<pkg>.<s>\>";  
       case \iter-star(\lex(str s)): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-seps(\lex(str s), _): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-star-seps(\lex(str s), _): a.typ = "<l>\<<pkg>.<s>\>";
       case \parameterized-sort(str s, [Symbol _:str _(str z)]): a.typ = "<pkg>.<s>_<z>";
       case \parameterized-lex(str s, [Symbol _:str _(str z)]): a.typ = "<pkg>.<s>_<z>";
       case \iter(\parameterized-sort(str s, [Symbol _:str _(str z)])): a.typ = "<l>\<<pkg>.<s>_<z>\>";  
       case \iter-star(\parameterized-sort(str s, [Symbol _:str _(str z)])): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter-seps(\parameterized-sort(str s, [Symbol _:str _(str z)]), _): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter-star-seps(\parameterized-sort(str s, [Symbol _:str _(str z)]), _): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter(\parameterized-lex(str s, [Symbol _:str _(str z)])): a.typ = "<l>\<<pkg>.<s>_<z>\>";  
       case \iter-star(\parameterized-lex(str s, [Symbol _:str _(str z)])): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter-seps(\parameterized-lex(str s, [Symbol _:str _(str z)]), _): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter-star-seps(\parameterized-lex(str s, [Symbol _:str _(str z)]), _): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       
     }
     append a;   
   }
}
 
str signature(list[Arg] args) {
  if (args == []) {
     return "";
  }
  h = head(args);
  return (", <h.typ> <h.name>" | "<it>,  <t> <a>" | arg(t, a) <- tail(args) );
}

str actuals(list[Arg] args) {
  if (args == []) {
     return "";
  }
  h = head(args);
  return (", <h.name>" | "<it>, <a>" | arg(_, a) <- tail(args) );
}

str cloneActuals(list[Arg] args) {
  if (args == []) {
     return "";
  }
  h = head(args);
  return (", clone(<h.name>)" | "<it>, clone(<a>)" | arg(_, a) <- tail(args) );
}

public str construct(Sig sig) {
  return "public <sig.name>(ISourceLocation src, IConstructor node <signature(sig.args)>) {
         '  super(src, node);
         '  <for (arg(_, name) <- sig.args) {>
         '  this.<name> = <name>;<}>
         '}";
}

private void loggedWriteFile(loc file, str src) {
  println("Writing <file>");
  writeFile(file, "<header>
                  '<src>");
}
