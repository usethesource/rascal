module rascal::syntax::ASTGen

import rascal::syntax::Grammar;
import rascal::syntax::Bootstrap;
import rascal::syntax::Parameters;
import ParseTree;

import IO;
import String;
import List;
import Set;

private str OUTDIR = "/Users/tvdstorm/GLT/rascal/build/rascal/src/org/rascalmpl/ast2"; 
private str PKG = "org.rascalmpl.ast2";


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

public void testIt() {
  g = rascalGrammar();
  g = expandParameterizedSymbols(g);
  g.rules -= (sort("Pattern"): {}, sort("RascalReservedKeywords"): {});

  g = visit (g) {
    case sort("Pattern") => sort("Expression")
  };

  grammarToJavaAPI(OUTDIR, PKG, g);
}


public void grammarToJavaAPI(str outdir, str pkg, Grammar g) {
  model = grammarToASTModel(pkg, g);
  grammarToVisitor(OUTDIR, PKG, model);
  grammarToASTFactory(OUTDIR, PKG, model);
  grammarToASTClasses(OUTDIR, PKG, model);
}

public void grammarToASTFactory(str outdir, str pkg, set[AST] asts) {
  fact =  "
package <pkg>;
import org.eclipse.imp.pdb.facts.INode;

public class ASTFactory {
<for (ast(sn, sigs) <- asts, sig(cn, args) <- sigs) {>
      public <sn>.<cn> make<sn><cn>(<signature(args)>) {
         return new <sn>.<cn>(<actuals(args)>);
      }
<}>

<for (leaf(sn) <- asts) { >
  public <sn>.Lexical make<sn>Lexical(INode node, String string) {
    return new <sn>.Lexical(node, string); 
  }
<}>

<for (a <- asts) { sn = a.name; >
  public <sn>.Ambiguity make<sn>Ambiguity(INode node, java.util.List\<<sn>\> alternatives) {
    return new <sn>.Ambiguity(node, alternatives);
  }
<}>
}
";
  loggedWriteFile(|file://<outdir>/ASTFactory.java|, fact);
}


public void grammarToVisitor(str outdir, str pkg, set[AST] asts) {
  ivisit = "
package <pkg>;

public interface IASTVisitor\<T\> {
  <for (ast(sn, sigs) <- asts, sig(cn, args) <- sigs) {>
      public T visit<sn><cn>(<sn>.<cn> x);
  <}>
  <for (leaf(sn) <- asts) {>
      public T visit<sn>Lexical(<sn>.Lexical x);
  <}>
  <for (a <- asts) { sn = a.name; >
      public T visit<sn>Ambiguity(<sn>.Ambiguity x);
  <}>
}
";

 loggedWriteFile(|file://<outdir>/IASTVisitor.java|, ivisit);

 nullVisit = "
package <pkg>;

public class NullASTVisitor\<T\> implements IASTVisitor\<T\> {
  <for (ast(sn, sigs) <- asts, sig(cn, args) <- sigs) {>
      public T visit<sn><cn>(<sn>.<cn> x) { return null; }
  <}>
  <for (leaf(sn) <- asts) {>
      public T visit<sn>Lexical(<sn>.Lexical x) { return null; }
  <}>
  <for (a <- asts) { sn = a.name; >
      public T visit<sn>Ambiguity(<sn>.Ambiguity x) { return null; }
  <}>
}
";

 loggedWriteFile(|file://<outdir>/NullASTVisitor.java|, nullVisit);
}


public void grammarToASTClasses(str outdir, str pkg, set[AST] asts) {
  for (a <- asts) {
     class = classForSort(pkg, ["org.eclipse.imp.pdb.facts.INode"], a); 
     loggedWriteFile(|file://<outdir>/<a.name>.java|, class); 
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

  public Ambiguity(INode node, java.util.List\<<pkg>.<name>\> alternatives) {
    this.alternatives = java.util.Collections.unmodifiableList(alternatives);
    this.node = node;
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
  public Lexical(INode node, java.lang.String string) {
    this.node = node;
    this.string = string;
  }
  public java.lang.String getString() {
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
       case \parameterized-sort(str s, _): a.typ = "<pkg>.<s>";
       case \iter(\parameterized-sort(str s, _)): a.typ = "<l>\<<pkg>.<s>\>";  
       case \iter-star(\parameterized-sort(str s, _)): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-seps(\parameterized-sort(str s, _), _): a.typ = "<l>\<<pkg>.<s>\>";
       case \iter-star-seps(\parameterized-sort(str s, _), _): a.typ = "<l>\<<pkg>.<s>\>";
     }
     append a;
   }
}

str signature(list[Arg] args) {
  if (args == []) {
     return "";
  }
  h = head(args);
  return ("<h.typ> <h.name>" | "<it>,  <t> <a>" | arg(t, a) <- tail(args) );
}

str actuals(list[Arg] args) {
  if (args == []) {
     return "";
  }
  h = head(args);
  return (h.name | "<it>, <a>" | arg(_, a) <- tail(args) );
}

public str construct(Sig sig) {
  return "
public <sig.name>(<signature(sig.args)>) {
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
  top-down-break visit (g) {
    case Production p: result += astProductions(p);
  }

  result = { p | p <- result,  (sort(_) := p.rhs || \parameterized-sort(_, _) := p.rhs),  
            p.attributes != \no-attrs() ==> term("literal"()) notin p.attributes.attrs }; 

  
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
  if (\parameterized-sort(str name, _) := p.rhs) {
     return name;
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
  return term("lex"()) in p.attributes.attrs;
}

private void loggedWriteFile(loc file, str src) {
 println("Writing <file>");
 writeFile(file, src);
}
