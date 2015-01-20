module experiments::Compiler::Examples::Tst2

import Node;
import ValueIO;
import IO;

/*TODO: clean up tmp */

private bool textWriteRead(type[&T] typ, str termString, value termValue){
   tmp = |file:///tmp/xxx|;
   writeFile(tmp, termString);
   try {
        if(readTextValueFile(typ, tmp) == termValue) return true;
   } catch:
        return false;
}


data FUN = f(int A, int B, int C);t

value main(list[value] args) = textWriteRead(#FUN, "f(1,2,3)",  f(1,2,3));

