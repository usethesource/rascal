module experiments::Compiler::Examples::Tst

import Set;
import List;

public set[set[&T]] xgroup(set[&T] input, bool (&T a, &T b) similar) {
  sinput = sort(input, bool (&T a, &T b) { return similar(a,b) ? false : a < b ; } );
  lres = while (!isEmpty(sinput)) {
    h = head(sinput);
    sim = h + takeWhile(tail(sinput), bool (&T a) { return similar(a,h); });
	append toSet(sim);
	sinput = drop(size(sim), sinput);
  }
  return toSet(lres); 
}

value main(list[value] args) { 
	int nLegs(str animal){
    return legs[animal] ? 0;
	}
	bool similar(str a, str b) = nLegs(a) == nLegs(b);
	legs = ("bird": 2, "dog": 4, "human": 2, "snake": 0, "spider": 8, "millepede": 1000, "crab": 8, "cat": 4);
 	return xgroup({"bird", "dog", "human", "spider", "millepede", "zebra", "crab", "cat"}, similar);
}