module vis::web::examples::DisplayVariable

rel[int, int] rIntInt = {<1,2>, <1, 3>, <2, 3>};

rel[str, str] rStrStr = {<"a","b">, <"c", "d">, <"e", "f">};

rel[int, int, str] rIntIntKind = {<1,2, "a" >, <1, 3, "b" >, <2, 3, "a" >};

map[int, int] mIntInt = (1:3, 2:4, 3:1);

map[num, list[num]] mIntIntGroup = (1:[3, 5], 2:[4, 2], 3:[1, 6]);

map[str, int] mStrInt = ("aap":1 ,"noot": 4, "mies": 1);

map[str, list[int]] mStrGroup = ("aap":[3, 5] ,"noot":[4, 2] , "mies": [1, 6]);

rel[map[str, num] , str] r_MapXlabelRank_Tag = 
 {<("x" : 1), "y">, <("x" : 1), "z">, <("a": 4), "v" >};
 
rel[map[str, num] , str, str] r_MapXlabelRank_TagKind = 
 {<("x" : 1), "y","small">, <("x" : 1), "z", "big">, <("a": 4), "v", "small" >};
 
rel[map[str labelx, num rangx] , map[str labely, num rangy]] bubble = {<("a":10), ("z":1)>, <("b":5), ("y":2)>, <("c":1), ("x":3)>};
 
/* -------------------------------------------------------------------------------------------------- */

tuple[str, list[num], num(num)] parabole = <"parabole", [-1,-0.9..1.1], (num(num x){return x*x;})>;

tuple[str name, lrel[num, num] r] dots = <"dots", [<-1.5, -1.5>, <-1,-1>, <-1, 1>, <1, 1>, <1, -1>, <-1, -1>]>;













