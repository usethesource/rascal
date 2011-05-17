@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::syntax::ASTGen

import Grammar;
import lang::rascal::syntax::Parameters;
import ParseTree;

import IO;
import String;
import List;
import Set;

data AST = ast(str name, set[Sig] sigs) | leaf(str name);
data Sig = sig(str name, list[Arg] args);
data Arg = arg(str typ, str name);

public set[AST] grammarToASTModel(str pkg, Grammar g) {
  map[str, set[Sig]] m = ();
  set[Sig] sigs = {};
  set[AST] asts = {};
  for (p <- astProductions(g)) {
    s = sortName(p);
    if (isLexical(p)) {
       asts += {leaf(s)};
    }
    else if (hasCons(p)) {
       args = productionArgs(pkg, p);
       m[s]?sigs += {sig(consName(p), args)};
    }
    else {
      ;//throw "<p> is not lexical but has no cons";
    }
  }
  for (sn <- m) {
    asts += ast(sn, m[sn]);
  }
  
  return asts;
}




public void grammarToJavaAPI(loc outdir, str pkg, Grammar g) {
  model = grammarToASTModel(pkg, g);
  grammarToVisitor(outdir, pkg, model);
  grammarToASTClasses(outdir, pkg, model);
}



public void grammarToVisitor(loc outdir, str pkg, set[AST] asts) {
  ivisit = "
package <pkg>;

public interface IASTVisitor\<T\> {
  <for (ast(sn, sigs) <- asts, sig(cn, args) <- sigs) {>
      public T visit<sn><cn>(<sn>.<cn> x);
  <}>
  <for (leaf(sn) <- asts) {>
      public T visit<sn>Lexical(<sn>.Lexical x);
  <}>
  <for (sn <- { a.name | a <- asts}) { >
      public T visit<sn>Ambiguity(<sn>.Ambiguity x);
  <}>
}
";

 loggedWriteFile(outdir + "/IASTVisitor.java", ivisit);

 nullVisit = "
package <pkg>;

public class NullASTVisitor\<T\> implements IASTVisitor\<T\> {
  <for (ast(sn, sigs) <- asts, sig(cn, args) <- sigs) {>
      public T visit<sn><cn>(<sn>.<cn> x) { return null; }
  <}>
  <for (leaf(sn) <- asts) {>
      public T visit<sn>Lexical(<sn>.Lexical x) { return null; }
  <}>
  <for (sn <- {a.name | a <- asts}) {>
      public T visit<sn>Ambiguity(<sn>.Ambiguity x) { return null; }
  <}>
}
";

 loggedWriteFile(outdir + "/NullASTVisitor.java", nullVisit);
}


public void grammarToASTClasses(loc outdir, str pkg, set[AST] asts) {
  for (a <- asts) {
     class = classForSort(pkg, ["org.eclipse.imp.pdb.facts.IConstructor","org.eclipse.imp.pdb.facts.IValue","org.rascalmpl.interpreter.Evaluator","org.rascalmpl.interpreter.asserts.Ambiguous","org.rascalmpl.interpreter.env.Environment","org.rascalmpl.interpreter.result.Result"], a); 
     loggedWriteFile(outdir + "/<a.name>.java", class); 
  }
}


public str classForSort(str pkg, list[str] imports, AST ast) {
  allArgs = { arg | /Arg arg <- ast };
return "
package <pkg>;

<for (i <- imports) {>
import <i>;
<}>

public abstract class <ast.name> extends AbstractAST {
  public <ast.name>(IConstructor node) {
    super(node);
  }
  
<for (arg(typ, lab) <- allArgs) { clabel = capitalize(lab); >
  public boolean has<clabel>() {
    return false;
  }

  public <typ> get<clabel>() {
    throw new UnsupportedOperationException();
  }
<}>

<ambiguityClass(pkg, ast.name)>

<if (leaf(_) := ast) {>
 <lexicalClass(ast.name)>
<}>

<for (/Sig sig <- ast) { >
  public boolean is<sig.name>() {
    return false;
  }
  <classForProduction(pkg, ast.name, sig)>
<}>

}
"; 
}


public str classForProduction(str pkg, str super, Sig sig) {
  return "
static public class <sig.name> extends <super> {
  // Production: <sig>

  <for (arg(typ, name) <- sig.args) {>
     private final <typ> <name>;
  <}>

  <construct(sig)>

  @Override
  public boolean is<sig.name>() { 
    return true; 
  }

  @Override
  public \<T\> T accept(IASTVisitor\<T\> visitor) {
    return visitor.visit<super><sig.name>(this);
  }
  
  <for (arg(typ, name) <- sig.args) { cname = capitalize(name); >
     @Override
     public <typ> get<cname>() {
        return this.<name>;
     }
     
     @Override
     public boolean has<cname>() {
        return true;
     }
  <}>	
}
";

}


public str ambiguityClass(str pkg, str name) {
return "static public class Ambiguity extends <name> {
  private final java.util.List\<<pkg>.<name>\> alternatives;

  public Ambiguity(IConstructor node, java.util.List\<<pkg>.<name>\> alternatives) {
    super(node);
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
  }

  @Override
  public Result\<IValue\> interpret(Evaluator __eval) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  @Override
  public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env) {
    throw new Ambiguous((IConstructor) this.getTree());
  }
  
  public java.util.List\<<pkg>.<name>\> getAlternatives() {
   return alternatives;
  }

  public \<T\> T accept(IASTVisitor\<T\> v) {
	return v.visit<name>Ambiguity(this);
  }
}
";
}


public str lexicalClass(str name) {
  return "
static public class Lexical extends <name> {
  private final java.lang.String string;
  public Lexical(IConstructor node, java.lang.String string) {
    super(node);
    this.string = string;
  }
  public java.lang.String getString() {
    return string;
  }
  public java.lang.String toString() {
    return string;
  }
  public \<T\> T accept(IASTVisitor\<T\> v) {
    return v.visit<name>Lexical(this);
  }
}
";
}


list[Arg] productionArgs(str pkg, Production p) {
   str l = "java.util.List";
   return for (label(str name, Symbol sym) <- p.lhs) {
     a = arg("", name);
     switch (sym) {
       case \sort(str s): a.typ = "<pkg>.<s>"; 
       case \iter(\sort(str s)): a.typ = "<l>\<<pkg>.<s>\>";  
       case \iter-star(\sort(str s)): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-seps(\sort(str s), _): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-star-seps(\sort(str s), _): a.typ = "<l>\<<pkg>.<s>\>";
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
  return "
public <sig.name>(IConstructor node <signature(sig.args)>) {
  super(node);
  <for (arg(_, name) <- sig.args) {>
    this.<name> = <name>;
  <}>
}
";
}


public str capitalize(str s) {
  return toUpperCase(substring(s, 0, 1)) + substring(s, 1);
}

public set[Production] astProductions(Grammar g) {
  set[Production] result = {};
  g = visit(g) {
    case action(_, Production p, _) => p
  }
  top-down-break visit (g) {
    case Production p: result += astProductions(p);
  }

  // TODO: inlining the following fun in the expression below results in a
  // NoSuchField("attrs") exception. Why?
  bool notLiteral(Production p) {
  	return p.attributes != \no-attrs() ==> \literal() notin p.attributes.attrs;
  }
  
  result = { p | p <- result,  (sort(_) := p.rhs || \parameterized-sort(_, _) := p.rhs),  
             notLiteral(p)}; 

  
  for (p <- result) {
    // sanity check
    if (!hasCons(p) && !isLexical(p)) {
      ;//println("WARNING: production <p> has no cons attr, but is not lexical either.");
    }
  }

  return result;
}

public set[Production] astProductions(Production p) {
  set[Production] result = {};
  top-down-break visit (p) {
    case diff(_, Production l, _): result += astProductions(l);
    case restrict(_, Production l, _): result += astProductions(l);
    case l:prod(_, _, _): result += {l};  
  }
  return result;
}


public str consName(Production p) {
  if (term("cons"(str name)) <- p.attributes.attrs) {
    return name;
  }
  throw "Production <p> has no cons name";
}


public str sortName(Production p) {
  if (\sort(str name) := p.rhs) {
     return name;
  }
  if (\parameterized-sort(str name, [\sort(str actual)]) := p.rhs) {
     return name + "_" + actual;
  }
  throw "Production <p> has no sort name";
}

public bool  hasCons(Production p) {
 if (p.attributes == \no-attrs()) {
    return false;
 }
 return term("cons"(_)) <- p.attributes.attrs;
}

public bool isLexical(Production p) {
  if (p.attributes == \no-attrs()) {
    return false;
  }
  return \lex() in p.attributes.attrs;
}

private void loggedWriteFile(loc file, str src) {
 println("Writing <file>");
 writeFile(file, src);
}
