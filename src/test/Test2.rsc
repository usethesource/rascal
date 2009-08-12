module test::Test2
import IO;


public void test()
{
    println("case 1");
    cmds = [];
    for(c <- cmds) println(c);
    
    println("case 2");
    list[str] cmds1 = [];
    for(c1 <- cmds1) println(c1);
    
    println("case 3");
    for(str c2 <- cmds) println(c2);
    
    println("case 4");
    for(str c2 <- cmds1) println(c2);
}