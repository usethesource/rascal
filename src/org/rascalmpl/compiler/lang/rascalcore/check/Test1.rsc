module lang::rascalcore::check::Test1 

 
//import lang::rascal::grammar::definition::Characters;
import lang::rascal::grammar::definition::Productions;
import ParseTree;

data CharRange = \empty-range();

CharRange intersect(CharRange r1, CharRange r2) { }

public bool lessThan(CharRange r1, CharRange r2) { }

public data Production = lookahead(Symbol def, set[Symbol] classes, Production production);

//list[CharRange] order(list[CharRange] x) {
//    return sort([ e | e <- x, e != \empty-range()], lessThan);
//}

 public Production optimizeLookaheads(Symbol rhs, set[Production] alts) {
  list[CharRange] l = [];
  
  // first we identify which unique ranges there are for all the different productions
  for (lookahead(_,set[Symbol] classes, Production p) <- alts) { 
    for (\char-class(rs) <- classes, r <- rs) {
      // find the first range that is not smaller than the pivot
      if ([pre*, post*] := l, all(z <- post, !lessThan(z,r)) || post == []) {
        if ([overlapping*, post2*] := post, all(\o <- overlapping, intersect(r,\o) != \empty-range())) {
          ;
        }

      }
    }
  }
}