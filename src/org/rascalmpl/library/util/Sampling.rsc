@license{
  Copyright (c) 2009-2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Utilities to randomly select smaller datasets from larger datasets}
@description{
Sampling is important when the analysis algorithms do not scale to the size of 
the original corpus, or when you need to train an analysis on a representative
set without overfitting on the entire corpus. These sampling functions all
assume that a uniformly random selection is required.
}
module util::Sampling

import util::Math;
import Map;
import List;
import Set;

@synopsis{Reduce the arity of a set by selecting a uniformly distributed sample.}
@description{
A uniform subset is computed by iterating over the set and skipping every element
with a probability of `1/(size(corpus) / target)`. This rapidly generates a new set of
expected `target` size, but most probably a little smaller or larger.
}
@examples{
```rascal-shell
import util::Sampling;
sample({"a","b","c","e","f","g","h","i","j","k"}, 4)
sample({"a","b","c","e","f","g","h","i","j","k"}, 4)
sample({"a","b","c","e","f","g","h","i","j","k"}, 4)
```
}
set[&T] sample(set[&T] corpus, int target) 
  = {e | int factor := size(corpus) / target, e <- corpus, factor == 0 || arbInt(factor) == 0};

@synopsis{Reduce the length of a list by selecting a uniformly distributed sample.}
@description{
The random selection of elements does not change their initial order in the list.
A uniform sublist is computed by iterating over the list and skipping every element
with a probability of `1/(size(corpus) / target)`. This rapidly generates a new list of
expected `target` size, but most probably a little smaller or larger.
}
@examples{
```rascal-shell
import util::Sampling;
sample([1..1000], 30)
sample([1..1000], 30)
sample([1..1000], 30)
```
}
list[&T] sample(list[&T] corpus, int target) 
  = [e | int factor := size(corpus) / target, e <- corpus, factor == 0 || arbInt(factor) == 0];

@synopsis{Reduce the size of a map by selecting a uniformly distributed sample.}
@description{
A uniform submap is computed by iterating over the map's keys and skipping every key
with a probability of `1/(size(corpus) / target)`. This rapidly generates a new map of
expected `target` size, but most probably a little smaller or larger.
}
map[&T,&U] sample(map[&T,&U] corpus, int target) 
  = (k : corpus[k] | int factor := size(corpus) / target, k <- corpus, factor == 0 || arbInt(factor) == 0); 