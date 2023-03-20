@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

anno loc Tree@\loc;

data Tree = appl(Production prod, list[Tree] args);

data Production;

public data TreeSearchResult[&T<:Tree] = treeFound(&T tree) | treeNotFound();


@doc{
#### Synopsis

Select the innermost Tree of a given type which is enclosed by a given location.

#### Description

}
public TreeSearchResult[&T<:Tree] treeAt(type[&T<:Tree] t, loc l, Tree a:appl(_, _)) {
    if ((a@\loc)?, al := a@\loc, al.offset <= l.offset, al.offset + al.length >= l.offset + l.length) {
        for (arg <- a.args, TreeSearchResult[&T<:Tree] r:treeFound(&T<:Tree _) := treeAt(t, l, arg)) {
            return r;
        }
        
        if (&T<:Tree tree := a) {
            return treeFound(tree);
        }
    }
    return treeNotFound();
}
//syntax N[&T,&U] = "\<\<" &T "," &U "\>\>";


//data Maybe[&T] = none() | some(&T t);
//
//data C = c(Maybe[int] i);


//lexical Name = "name";
//syntax Pattern = "pat" | call: KeywordArguments[Pattern];
//
//syntax Expression = "exp"
//                  | call: KeywordArguments[Expression] keywordArguments
//                  ;
//
//
//syntax KeywordArguments[&T]
//    = //\default: {KeywordArgument[&T] ","}+ keywordArgumentList
//      none: ()
//    ;
//    
////syntax KeywordArgument[&T] = \default: Name name "=" &T expression ;
//
//value main() = 42;
//syntax A = "a";
//syntax X[&T] = &T "x" &T;
//
//syntax XA = X[A];
//
//int f((X[A]) `axa`) = 1;
//
//value main() = f((X[A]) `axa`);

////data D[&T] = d(&T n);
//import lang::rascalcore::compile::Examples::Tst3;
//

//data D[&T] = d(&T n);
//
//int f(D[int] x) = 1;
//int f(D[str] x) = 3;
//
//
//value main() = f(d("abc")) + f(d(10));

//extend lang::rascalcore::check::ATypeInstantiation;
//import lang::rascal::\syntax::Rascal;
//
//
//void checkExpressionKwArgs(list[Keyword] kwFormals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, Bindings bindings){
//
//}