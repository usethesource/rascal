@bootstrapParser
module lang::rascal::\syntax::tests::PreBootstrap

import lang::rascal::\syntax::Rascal;
 
public test bool expr() = Expression _ := (Expression) `1`;
public test bool lit() = (Sym) `<CaseInsensitiveStringConstant _>` :=  (Sym) `'hello'`;
public test bool nont() = (Sym) `<Nonterminal _>` :=  (Sym) `X`;
public test bool paramnont() = (Sym) `<Nonterminal _>[<{Sym ","}+ _>]` :=  (Sym) `List[String,String]`;
public test bool string() =	(Sym) `<StringConstant _>` :=  (Sym) `"hello"`;
public test bool match() = (Expression) `<Expression _>` := (Expression) `1`;

 
 
