@bootstrapParser
module lang::rascal::\syntax::tests::PreBootstrap

import ParseTree;
import lang::rascal::\syntax::Rascal;

public test bool expr() = Expression y := (Expression) `1`;
public test bool lit() = (Sym) `<CaseInsensitiveStringConstant l>` :=  (Sym) `'hello'`;
public test bool nont() = (Sym) `<Nonterminal l>` :=  (Sym) `X`;
public test bool paramnont() = (Sym) `<Nonterminal n>[<{Sym ","}+ syms>]` :=  (Sym) `List[String,String]`;
public test bool string() =	(Sym) `<StringConstant l>` :=  (Sym) `"hello"`;
public test bool match() = (Expression) `<Expression e>` := (Expression) `1`;


