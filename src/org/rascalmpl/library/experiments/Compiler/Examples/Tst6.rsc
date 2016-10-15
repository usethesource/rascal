module experiments::Compiler::Examples::Tst6
import ParseTree;
//import lang::pico::\syntax::Main;
import demo::lang::Pico::Syntax;
import IO;

test bool Pico1()  {t1 = (Program) `begin declare x: natural; x := 10 end`; return true;}

test bool Pico2() { return Program P := (Program) `begin declare x: natural; x := 10 end`;}

test bool Pico3() { return (Program) `<Program P>` := (Program) `begin declare x: natural; x := 10 end`;}
  
Tree t1 = (Program) `begin declare x: natural; x := 10 end`;
 
Tree t2 = (Declarations) `declare x : natural;`;
 
test bool PicoQuoted1() {
    bool match() { return (Program) `<Program program>` := t1; }

    return match();
}

test bool PicoQuoted2(){
    bool match() { return Program program := t1; }

    return match();
}

test bool PicoQuoted5(){
    bool match() { return (Program) `begin <Declarations decls> <{Statement ";"}* stats> end` := t1; }
    
    return match();
}