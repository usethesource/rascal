@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

public list[list[&T]] groupRangeByDomain(lrel[&U dom, &T ran] input)
    = dup([[r | <d,&T r> <- input] | &U d <- input.dom]);
    
public set[set[&T]] group(set[&T] input, bool (&T a, &T b) similar) {
  sinput = sort(input, bool (&T a, &T b) { return similar(a,b) ? false : a < b ; } );
  lres = while (!isEmpty(sinput)) {
    h = head(sinput);
    sim = h + takeWhile(tail(sinput), bool (&T a) { return similar(a,h); });
    append toSet(sim);
    sinput = drop(size(sim), sinput);
  }
  return toSet(lres); 
}
    