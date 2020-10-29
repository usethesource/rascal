module lang::rascalcore::compile::Examples::Tst1

value m() { //test bool rascalRuntimeExceptionsPlusOverloading(){
    str trace = "";

    void f(int _) {
        trace = trace + "Bad function f; ";
    }
    
    void g(0) {
        f(0);
        trace = trace + "finally; ";
    }
    
    default void g(int i) {
        trace = trace + "default void g(int);";
    }
    
    trace = "";
    g(0);
    return trace;// == "Bad function f; map key: 3 (not found); finally; default void g(int);";
}

value main() = 
m();