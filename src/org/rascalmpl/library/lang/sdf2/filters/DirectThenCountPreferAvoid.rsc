module lang::sdf2::filters::DirectThenCountPreferAvoid

import ParseTree;
import Set;
import List;

@doc{
Import his module if you want prefer/avoid filtering with counting enabled for your grammar. Use @prefer and @avoid to
label alternatives.
}
&T <:Tree directThenCountPreferAvoidFilter(amb(set[&T <:Tree] alternatives)) {
  if (size(alternatives) == 1) {
    fail amb;
  }
  // first check for direct prefers / avoids
  direct = { t | t:appl(prod(_,_,{\tag("prefer"()),*_}),_) <- alternatives};
  if ({oneTree} := direct) {
  	return oneTree;
  }
  if (size(direct) != 0) {
  	// some were filtered
 	alternatives = direct; 
  }
  
  avoids = { t | t:appl(prod(_,_,{\tag("avoid"()),*_}),_) <- alternatives};
  
  if (size(alternatives) == (size(avoids) + 1)) {
    return ParseTree::amb(alternatives - avoids);
  }
  
  alternatives -= avoids;
  
  
  // now filter the nested prefers
  
  alts = [*alternatives];
  indexes = index(alts);
  tags = getTags(alts);
  counts = [(0 | it + 1 | "prefer"() <- tags[i]) | i <- indexes];
  
  indexes = [i | i <- indexes, counts[i] == max(counts)];
  
  counts = [(0 | it + 1 | "avoid"() <- tags[i]) | i <- indexes];
  
  result = {alts[indexes[i]] | i <- index(indexes), counts[i] == min(counts)};
  
  if (result == alternatives) {
    fail amb;
  }
  else {
  	return amb(result);
  }
}

list[list[value]] getTags(list[Tree] ts) = [getTags(t) | t <- ts];

list[value] getTags(Tree t) {
	list[value] result = [];
	todo = [t];
	while (todo != []) {
		todoCopy = todo;
		todo = [];
		for (appl(prod(_,_,tags),args) <- todoCopy) {
			result += [tg | \tag(tg) <- tags];	
			todo += args;
		}
	}
	return result;
}
