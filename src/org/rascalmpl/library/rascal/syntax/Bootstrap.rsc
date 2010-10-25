module rascal::syntax::Bootstrap

import zoo::sdf2::SDF2Grammar;
import zoo::sdf2::Load;
import rascal::syntax::Grammar2Rascal;
import rascal::syntax::Definition;
import rascal::syntax::Grammar;
import rascal::syntax::Generator;
import rascal::syntax::RascalForImportExtraction;
import IO;
import ValueIO;
import viz::Basic;

public void bootFromSDF() {
  rascaldef = loadSDF2Module("languages::rascal::syntax::Rascal", [|file:///Users/jurgenv/Sources/Rascal/rascal-grammar/spec|,|file:///Users/jurgenv/Sources/Rascal/sdf-library/library|]);
  gr = sdf2grammar(rascaldef);
  // println("grammar: <gr>");
  gr = removeIllegalPriorities(gr);
  gr = mergeSimilarProductions(gr);
  source = grammar2rascal(gr);
  writeFile(|project://RascalLibrary/src/rascal/syntax/RascalRascal.rsc|, "module rascal::syntax::RascalRascal\n\n" + source); 
}

public Grammar removeIllegalPriorities(Grammar g) {
  return visit (g) {
    case first(Symbol s, [list[Production] pre, Production p, list[Production] post]) : {
         if (p.rhs != s) {
            println("WARNING: removed <p> after <pre> and before <post>");
	        insert first(s, [pre, post]);
	     } else fail;
    }
  }
}

public Grammar mergeSimilarProductions(Grammar gr) {
  return visit(gr) {
    case choice(Symbol rhs, {prod(list[Symbol] lhs, rhs, Attributes attrs),
                             prod(lhs, rhs, Attributes attrs2), set[Production] rest}) =>
         choice(rhs, rest + {prod(lhs,rhs, add(attrs,attrs2))})
  }
}

public Attributes add(Attributes a1, Attributes a2) {
  switch (<a1,a2>) {
    case <\no-attrs(),\no-attrs()> : return \no-attrs();
    case <attrs(a),\no-attrs()> : return a1;
    case <\no-attrs(),attrs(a)> : return a2;
    case <attrs(a),attrs(b)> : return attrs(a+b);
  }
}
public void bootFromRascal() {
  println("parsing the rascal definition of rascal");
  Module \module = parse(#Module, |project://RascalLibrary/src/rascal/syntax/RascalRascal.rsc|);
  println("imploding the syntax definition and normalizing and desugaring it");
  Grammar gr = module2grammar(\module);
//  treeView(gr.rules[sort("Expression")]);
  println("dumping grammar");
  writeBinaryValueFile(|project://RascalLibrary/src/rascal/syntax/Rascal.grammar|, gr);
  println("generating Java source code");
  str source = generate("org.rascalmpl.library.rascal.syntax","RascalRascal", gr);
  println("writing a file");
  writeFile(|project://RascalLibrary/src/rascal/syntax/RascalRascal.java|, source);
  println("rascal parser has been generated");  
}