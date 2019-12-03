module lang::rascalcore::compile::Examples::Tst2

bool main() =  {*int _, int Y} := {1}; 


//bool main() = [1, *list[int] L, 6] := [1, 6];// && (L == [[2,3],[4,5]]));


//bool main() {int N = 1; return ([N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);}

//{ "f"(D), D } := {"f"(1), 1};

//{ int ONE = 1; set[int] S = {3}; return {*S, 2, ONE} := {1,2,3};}

//bool main1 = [*int _] := [1,2];
// 
//bool main() = [[1,2]] := [[1,2]];

//value main() =   [1] := [1];
//value main() {  if(([1, *int L] := [1, 2]) && (L == [2])) return 10; else return 20;}


//value main() = int i <- [1,4] && int k := i && k >= 10;

//value main() = [1, /int N, 3] := [1, [1,2,3,2], 3] && N == 2;

//test bool keywordParam92(){
//    bool f11(bool c = false){
//        bool g11(){
//            return c;
//        }
//        return g11();
//    }
//    return f11() == false;
//}