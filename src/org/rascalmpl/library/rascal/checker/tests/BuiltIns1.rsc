module rascal::checker::tests::BuiltIns1

public int testfun1() {
    list[int] l1 = [1..5];
    int n1 = 6;
    
    l1 + n1;
    l1 + n1;
    l1 + l1;
    n1 + n1;
    
    l1 * n1;
    n1 * l1;
    l1 * l1;
    
    l1 - n1;
    n1 - l1; // should be an error
}
