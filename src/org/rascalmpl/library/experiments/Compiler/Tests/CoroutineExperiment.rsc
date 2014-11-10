module experiments::Compiler::Tests::CoroutineExperiment

import IO;

//coroutine Numbers(int low, int high, ref[int] result){
//   int current = low;
//   while(current <= high){
//        yield(current);
//        current += 1;
//   } 
//}

coroutine[int] Numbers1(int low, int high){
    int current = low;
    int state = 1;
    
    bool next(){
        switch(state){
            case 1: if(current <= high) { state = 2; return true;} else { state = 3; return false; }
            case 2: { current += 1; state = 1; }
            case 3: { state = 4; return false; }
        }
    }
    
    int myval() = current;

    return <mynext, myval>;
}

int() counter(){
    int i = 0;
    return int(){ i += 1; return i; };
}

value main2(list[value] args) {
    c = counter();
    for(i <- [0 .. 10]) println("<i>: <c()>");
    return true;
}

alias coroutine[&T] = tuple[bool() next, &T() val];
bool next(coroutine[&T] co) = co.next();
&T val(coroutine[&T] co) = co.val();


coroutine[int] Numbers2(int low, int high){
    int current = low;
    
    bool mynext() { 
        println("next, current = <current>");
        if(current <= high){
           current += 1;
           return true;
        } else {
           return false;
        }
    }
    
    int myval() = current;
    return <mynext, myval>;
}

value main2(list[value] args){
    gen = Numbers2(0, 10);
    int res = 0;
    while(next(gen)){
        println(val(gen));
    }
    return true;
}

alias coroutine3a[&T] = tuple[bool,&T]();
alias coroutine3b[&T,&U] = tuple[bool,&T](&U);

coroutine3a[int] Numbers3a(int low, int high){
    int current = low;
    
    tuple[bool,int] mynext() { 
        println("next, current = <current>");
        if(current <= high){
           current += 1;
           return <true, current>;
        } else {
           return <false, current>;
        }
    }
    
    return mynext;
}

value main3a(list[value] args){
    gen = Numbers3(0, 10);
    int res = 0;
    while(<true, val> := gen()){
        println(val);
    }
    return true;
}

coroutine3b[int,int] Numbers3b(int low, int high){
    int current = low;
    
    tuple[bool,int] mynext(int delta) { 
        println("next, current = <current>");
        if(current <= high){
           current += delta;
           return <true, current>;
        } else {
           return <false, current>;
        }
    }
    return mynext;
}

value main3b(list[value] args){
    gen = Numbers3b(0, 10);
    int res = 0;
    while(<true, val> := gen(3)){
        println(val);
    }
    return true;
}

alias coroutine4b[&T, &U] = &T(&U);

coroutine4b[int] Numbers4b(int low, int high){
    int current = low;
    
    int mynext(int delta) { 
        println("next, current = <current>");
        if(current <= high){
           current += delta;
           return current;
        } else {
           throw "no more values";
        }
    }
    
    return mynext;
}

value main4(list[value] args){
    gen = Numbers4b(0, 10);
    int res = 0;
    try {
        while(val := gen(3)){
            println(val);
        }
    }
    catch: {println("catch");   }
    return true;
}

data TREE = leaf(int) | nd(TREE left, int val, TREE right);

coroutine[int] ENUM(TREE tree, ref result){
    switch(tree){
        case leaf(int n):  
            { deref result = n; yield; }
        case nd(left, n, right): 
            { ENUM(left, result); deref result = n; yield; ENUM(right, result); } 
    }
}

//bool SAME_FRINGE(TREE tree1, TREE tree2){
//    int val1;
//    int val2;
//    enum1 = ENUM(tree1, ref val1);
//    enum2 = ENUM(tree2, ref val1);
//    
//    while(next(enum1)){
//        if(val2 := enum2()){
//           if(val1 != val2){
//              return false;
//           }
//        } else {
//            return false;
//        }
//    }
//    return val2 !:= enum2();
//}