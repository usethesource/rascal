module Box
import List;
import String;
import IO;
import demo::AbstractPico::AbstractSyntax;
import demo::AbstractPico::Programs;
int maxWidth = 80;
int  lmargin = 5;

/*
 * Evaluator for Pico.
 */
 
 // Define the run-time representation of values
 
data PicoValue = intval(int i) | strval(str s);

// Define the global execution state as a map from identifiers to values

alias VEnv = map[PicoId, PicoValue];

// The global environment




// The actual evaluation functions

public Box evalProgram(PROGRAM P){
    list[Box] r=[];
    switch (P) {
      case program(list[DECL] Decls, list[STATEMENT] Series): {
           r+=evalDecls(Decls);
           r+=evalStatements(Series);
      }
   }
   return V(r);
}

Box evalDecls(list[DECL] Decls){
   /*  visit (Decls) {
      case decl(PicoId Id, string()): { 
           println("adding <Id> : string");
           Env[Id] = strval(""); 
      }
      case decl(PicoId Id, natural()): {
           println("adding <Id> : natural") ;
           Env[Id] = intval(0);  
      }
    }; */
    return H([]);
}

Box evalStatements(list[STATEMENT] Series){
    list[Box] r = [];
    for(STATEMENT Stat <- Series){
        r+=evalStatement(Stat);
    }
    return V(r);
}

Box evalStatement(STATEMENT Stat){
    switch (Stat) {
      case asgStat(PicoId Id, EXP Exp): {
         return H([L(Id), L(":="), evalExp(Exp)]);
      }
      case ifStat(EXP Exp, list[STATEMENT] Stats1,
                            list[STATEMENT] Stats2):{
           list[Box] rs =[L("if"),evalExp(Exp),L("then")];
           Box r = V([H(rs), I(evalStatements(Stats1)), L("else"), I(evalStatements(Stats2))]);
           return r;
        }
      case whileStat(EXP Exp, list[STATEMENT] Stats1): {
          list[Box] rs =[L("while"),evalExp(Exp),L("do")];
          Box r = V([H(rs), I(evalStatements(Stats1)), L("od")]);
          return r;
        }
     }
}

Box evalExp(EXP exp) {
    switch (exp) {
      case natCon(int N): 
           return L("<N>");

      case strCon(str S): 
           return L("\"<S>\"");

      case id(PicoId Id): 
           return L(Id);

      case add(EXP E1, EXP E2):
           return HV([evalExp(E1), L("+"), evalExp(E2)]);
      
      case sub(EXP E1, EXP E2):
          return HV([evalExp(E1), L("-"), evalExp(E2)]);  
 
      case conc(EXP E1, EXP E2):
          return HV([evalExp(E1), L("+"), evalExp(E2)]);
   } 
}

data Box
    =  H (list[Box] b)
    |  V(list[Box] b)
    |  HV (list[Box] b)
    |  I(Box bb)
    |  L(str)
    ;

anno int Box@hs;
anno int Box@vs;
anno int Box@is;
public Box a = H([L("aap"),L("noot"), L("mies")])[@hs=3];

public str expand(Box b, int lmargin) {      
               switch(b) {
               case L(str s):return s;
               case H(list[Box] bl):{
                      str r="";
                      int hmargin =b@hs?1;
                      for (Box bb <- bl){  
                         if (!isEmpty(r))  r+=right("", hmargin); 
                         r+=expand(bb, lmargin);
                         }
                      return r;
                      }  
               case V(list[Box] bl):{
                       str r="";
                       int vmargin =b@vs?1;
                       for (Box bb <- bl) {
                         if (!isEmpty(r))  for (int i <-[1, vmargin] ) {r+="\n";};
                         r+=right("", lmargin); 
                         r+=expand(bb, lmargin);
                         }
                       return r;
                      }
                case HV(list[Box] bl): {
                    int hmargin =b@hs?1;
                    str r = "";
                    for (Box bb <- bl) {  
                         // r+=right("", lmargin);                 
                         if (!isEmpty(r))  r+=right("", hmargin); 
                         r+=expand(bb, lmargin);
                         }
                     return r;
                     }
                 case I(Box bb): {
                      str r = "";
                      r+=expand(bb, lmargin+ b@is?5);
                     return r;
                 }
             }
     return "";
}

public void main() {
   Box b = evalProgram(small);
   println(b);
   println(expand(b, 0));
}