module lang::rascalcore::compile::Examples::Tst6



@synopsis{Check the AST node specification on a (large) set of ASTs and monitor the progress.}
//bool astNodeSpecification(set[node] toCheck, str language = "java", bool checkNameResolution=false, bool checkSourceLocation=true) 
//    = job("AST specification checker", bool (void (str, int) step) {
//       for (node ast <- toCheck) {
//          step(loc l := ast.src ? l.path : "AST without src location", 1);
//          if (!astNodeSpecification(ast, language=language, checkNameResolution=checkNameResolution, checkSourceLocation=checkSourceLocation)) {
//             return false;
//          }
//       }
//
//       return true;
//    }, totalWork=size(toCheck));


void f(int n) {}


void g(str s, void(int) p) {}

bool h(bool b = false){
    g("abc", void(int n){
        f(b ? 10 : 20);
    });
    return true;
}

