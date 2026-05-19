module lang::rascalcore::compile::Rascal2muRascal::SrcTestCase
   
import lang::rascal::\syntax::Rascal;
loc translateAddFunction(Expression e){
    return e.lhs.src;
}
