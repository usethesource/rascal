module lang::rascalcore::check::Test1

//import lang::rascal::\syntax::Rascal;

lexical Name = [a-z]+;

syntax Tag
    = @Folded @category="Comment" expression: "@" Name name "=" Expression expression !>> "@";

syntax Expression = "e" expression;

void getTags(){
  Tag tg;
 
  tg.expression;
 
}
