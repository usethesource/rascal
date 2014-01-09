module experiments::Compiler::Examples::FailWithLabel1

value main(list[value] args) {
    int n = 0; 
    loop:for(int i <- [1,2,3,4], n <= 3) { 
        if(n == 3) {
            fail loop;
        } 
        n = n + 1; 
    } 
    return n == 3;
}