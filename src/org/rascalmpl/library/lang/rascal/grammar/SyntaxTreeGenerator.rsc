@license{
  Copyright (c) 2009-2012 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl - CWI}
@contributor{Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI}
module lang::rascal::grammar::SyntaxTreeGenerator

import Grammar;
import lang::rascal::grammar::definition::Parameters;
import ParseTree;

import IO;
import String;
import List;
import Set;

private str header = "/*******************************************************************************
                     ' * Copyright (c) 2009-2012 CWI
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
  = sig(str name, list[Arg] args)
  ;
  
data Arg 
  = arg(str typ, str name);

public set[AST] grammarToASTModel(str pkg, Grammar g) {
  map[str, set[Sig]] m = ();
  set[Sig] sigs = {};
  set[AST] asts = {};
  
  g = visit(g) {
    case conditional(s,_) => s
  }
  
  for (/p:prod(label(c,sort(name)),_,_) := g) 
     m[name]?sigs += {sig(capitalize(c), productionArgs(pkg, p))};

  for (/p:prod(label(c,\parameterized-sort(name,[sort(a)])),_,_) := g) 
     m[name + "_" + a]?sigs += {sig(capitalize(c), productionArgs(pkg, p))};

  for (sn <- m) 
    asts += ast(sn, m[sn]);
    
  for (/p:prod(\lex(s),_,_) := g) 
     asts += leaf(s);
     
  for (/p:prod(label(_,\lex(s)),_,_) := g) 
     asts += leaf(s);
  
  return asts;
}

public void grammarToJavaAPI(loc outdir, str pkg, Grammar g) {
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
           '<for (sn <- { a.name | a <- sort(asts)}) { >
           '  public T visit<sn>Ambiguity(<sn>.Ambiguity x);<}>
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
              '<for (sn <- {a.name | a <- sort(asts)}) {>
              '  public T visit<sn>Ambiguity(<sn>.Ambiguity x) { 
              '    return null; 
              '  }
              '<}>
              '}";

   loggedWriteFile(outdir + "/NullASTVisitor.java", nullVisit);
}

public void grammarToASTClasses(loc outdir, str pkg, set[AST] asts) {
  for (a <- sort(asts)) {
     class = classForSort(pkg, ["org.eclipse.imp.pdb.facts.IConstructor", "org.rascalmpl.interpreter.asserts.Ambiguous","org.eclipse.imp.pdb.facts.IValue","org.rascalmpl.interpreter.IEvaluator","org.rascalmpl.interpreter.env.Environment","org.rascalmpl.interpreter.result.Result"], a); 
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
         '  public <ast.name>(IConstructor node) {
         '    super();
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
         '  <ambiguityClass(pkg, ast.name)>
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
         '}";
}

public str ambiguityClass(str pkg, str name) {
  return "static public class Ambiguity extends <name> {
         '  private final java.util.List\<<pkg>.<name>\> alternatives;
         '  private final IConstructor node;
         
         '  public Ambiguity(IConstructor node, java.util.List\<<pkg>.<name>\> alternatives) {
         '    super(node);
         '    this.node = node;
         '    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         '  }
         '  
         '  @Override
         '  public IConstructor getTree() {
         '    return node;
         '  }
         '
         '  @Override
         '  public AbstractAST findNode(int offset) {
         '    return null;
         '  }
         '
         '  @Override
         '  public Result\<IValue\> interpret(IEvaluator\<Result\<IValue\>\> __eval) {
         '    throw new Ambiguous(src);
         '  }
         '    
         '  @Override
         '  public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
         '    throw new Ambiguous(src);
         '  }
         '  
         '  public java.util.List\<<pkg>.<name>\> getAlternatives() {
         '    return alternatives;
         '  }
         '  
         '  public \<T\> T accept(IASTVisitor\<T\> v) {
         '  	return v.visit<name>Ambiguity(this);
         '  }
         '}";
}


public str lexicalClass(str name) {
  return "static public class Lexical extends <name> {
         '  private final java.lang.String string;
         '  public Lexical(IConstructor node, java.lang.String string) {
         '    super(node);
         '    this.string = string;
         '  }
         '  public java.lang.String getString() {
         '    return string;
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
       case \parameterized-sort(str s, [sort(str z)]): a.typ = "<pkg>.<s>_<z>";
       case \iter(\parameterized-sort(str s, [sort(str z)])): a.typ = "<l>\<<pkg>.<s>_<z>\>";  
       case \iter-star(\parameterized-sort(str s, [sort(str z)])): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter-seps(\parameterized-sort(str s, [sort(str z)]), _): a.typ = "<l>\<<pkg>.<s>_<z>\>";
       case \iter-star-seps(\parameterized-sort(str s, [sort(str z)]), _): a.typ = "<l>\<<pkg>.<s>_<z>\>";
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

public str construct(Sig sig) {
  return "public <sig.name>(IConstructor node <signature(sig.args)>) {
         '  super(node);
         '  <for (arg(_, name) <- sig.args) {>
         '  this.<name> = <name>;<}>
         '}";
}

private void loggedWriteFile(loc file, str src) {
  println("Writing <file>");
  writeFile(file, "<header>
                  '<src>");
}
