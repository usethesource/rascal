module experiments::Compiler::Examples::Tst6

data D = d1(int n, str s = "abc", bool b = true);

    //x = d1(3, s = "def");
    // veld n heeft waarde 3
    // keyword parameter s heeft waarde "def" en is opgeslagen in d1 instantie
    // keyword parameter b is niet gezet en ook niet opgeslagen in d1 instantie
    
    //x.s;
    // field selectie heeft extra argument: str s = "abc", bool b = true
    // x.s heeft waarde, deze wordt uit d1 instantie opgehaald en waarde is "def"
    // x.s? is true
    // x.b heeft waarde, maar deze wordt opgehaald uit extra argument, waarde is true
    // x.b? is false [ wat raar is want hij heeft wel een waarde, maar dit terzijde]
    
    
  value main() {
    x = d1(3);
    x.s ?= "pqr";
    return x;

   } 
    
           
