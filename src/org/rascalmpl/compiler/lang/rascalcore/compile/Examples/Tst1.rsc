module lang::rascalcore::compile::Examples::Tst1

data Tree //(loc src = |unknown:///|(0,0,<0,0>,<0,0>))
     = char(int character) // <4>
     ;

value main(){
 [A-Z] head = char(65);
 return head;
}
