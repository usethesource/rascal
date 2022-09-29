@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Atze van der Ploeg - <ploeg@cwi.nl>}
@synopsis{Experiments with the 8-queens puzzle in Rascal}
@description{
See: 
* <http://en.wikipedia.org/wiki/Eight_queens_puzzle>
* <http://en.wikipedia.org/wiki/Eight_queens_puzzle_solutions>
}
module demo::Queens


import List;
import util::Math;

alias Pos = tuple[int x,int y];

list[tuple[&T,&T]] pairs(list[&T] p) =
	[ <p[i],p[j]> | i <- [0..size(p)-1], j <- [i+1..size(p)]]; 

bool diagonalOverlap(Pos l, Pos r) = abs(l.x - r.x) == abs(l.y - r.y);

bool isSolution(list[Pos] queens) = all(<l,r> <- pairs(queens), !diagonalOverlap(l,r));

public list[list[Pos]] nQueens(int n) =
    [queens | cols <- permutations([0..n]),
              queens := [<i,cols[i]> | i <- [0..n]],
              isSolution(queens)];

