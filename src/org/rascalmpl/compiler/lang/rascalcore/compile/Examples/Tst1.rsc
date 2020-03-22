module lang::rascalcore::compile::Examples::Tst1

data Use;
data PathRole;

data ReferPath
    = referToDef(Use use, PathRole pathRole)
    | referToType(loc occ, loc scope, PathRole pathRole)
    ;
    
void f(){
  set[ReferPath] referPaths = {};
  set[str] messages = {};
  for(rp <- referPaths){
    switch(rp){
    case referToDef(_, _):
        messages += "a";
    case referToType(_, _, _):
        messages += "b";
    }
  }
}
        
//&L strange(&L <: num arg1, &R <: &L arg2){
//  return arg2;
//}
//
//value main() = strange(3, "abc");


//set[str] f(){
//    res = {};
//    res += 1;
//    return res;
//}
 
//str f(str n, bool c = true) = n;

//int f(int n, bool c = true) = n;
//
//void main(){
//    f(3, c=3,s =4);
//}
