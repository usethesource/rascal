@license{
 Copyright (c) 2009-2015 CWI
 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse License v1.0
 which accompanies this distribution, and is available at
 http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::rascal::tests::functionality::Reducer

test bool testCount() = ( 0 | it + 1 | _ <- [1,2,3] ) == 3;
	
test bool testMax() = ( 0 | x > it ? x : it | x <- [1,2,3] ) == 3;
	
test bool testSum() = ( 0 | it + x  | x <- [1,2,3] ) == 6;
	
test bool testFlatMap() = ( {} | it + x  | x <- {{1,2}, {2,3,4}} ) == {1,2,3,4};

int wordCount(list[str] lines) = (0 | it + (0 | it + 1 | /\w+/ := line) | str line <- lines);

test bool wordCount1() = wordCount(["Andra moi ennepe,", "Mousa, polutropon,", "hos mala polla "]) == 8;
